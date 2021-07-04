package cn.com.kelaikewang.core.marketing.service;


import cn.com.kelaikewang.infrastructure.id.uid.BitsAllocator;
import cn.com.kelaikewang.infrastructure.id.uid.UidGenerator;
import cn.com.kelaikewang.infrastructure.id.uid.exception.UidGenerateException;
import cn.com.kelaikewang.infrastructure.id.uid.utils.DateUtils;
import cn.com.kelaikewang.infrastructure.id.uid.worker.WorkerIdAssigner;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OfferCodeGenerator implements UidGenerator, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferCodeGenerator.class);


    protected int timeBits = 28;
    protected int workerBits = 8;
    protected int seqBits = 13;
    protected int randomBits = 14;


    protected String epochStr = "2016-05-20";
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);


    protected OfferCodeBitsAllocator bitsAllocator;
    protected long workerId;


    protected long sequence = 0L;
    protected long lastSecond = -1L;

    private int maxRandom;


    @Resource
    protected WorkerIdAssigner workerIdAssigner;

    @Override
    public void afterPropertiesSet() throws Exception {

        bitsAllocator = new OfferCodeBitsAllocator(timeBits, workerBits,randomBits, seqBits);
        maxRandom = 1 << randomBits;

        workerId = workerIdAssigner.assignWorkerId();
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new RuntimeException("Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
        }

        LOGGER.info("Initialized bits(1, {}, {}, {}) for workerID:{}", timeBits, workerBits, seqBits, workerId);
    }

    @Override
    public long getUID() throws UidGenerateException {
        try {
            return nextId();
        } catch (Exception e) {
            LOGGER.error("Generate unique id exception. ", e);
            throw new UidGenerateException(e);
        }
    }

    @Override
    public String parseUID(long uid) {
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID
        //先左移，高位丢弃，再右移就可以得到原值
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long random = (uid << (timestampBits + workerIdBits)) >>> (totalBits - randomBits);
        long workerId = (uid << timestampBits + signBits) >>> (totalBits - workerIdBits);
        long deltaSeconds = uid >>> (workerIdBits + randomBits + sequenceBits);


        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));
        String thatTimeStr = DateUtils.formatByDateTimePattern(thatTime);


        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"random\":\"%d\",\"sequence\":\"%d\"}",
                uid,thatTimeStr, workerId,random, sequence);
    }


    protected synchronized long nextId() {
        long currentSecond = getCurrentSecond();


        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new UidGenerateException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
        }


        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();

            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }


        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;


        return bitsAllocator.allocate(currentSecond - epochSeconds, workerId,new Random().nextInt(maxRandom)+1,sequence);
    }


    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }


    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }


    public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
        this.workerIdAssigner = workerIdAssigner;
    }

    public void setTimeBits(int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern(epochStr).getTime());
        }
    }

    public int getRandomBits() {
        return randomBits;
    }

    public void setRandomBits(int randomBits) {
        this.randomBits = randomBits;
    }
}
