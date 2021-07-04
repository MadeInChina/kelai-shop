package cn.com.kelaikewang.core.marketing.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

public  class OfferCodeBitsAllocator {

    public static final int TOTAL_BITS = 1 << 6;



    private int signBits = 1;
    private final int timestampBits;
    private final int workerIdBits;
    private final int sequenceBits;
    private final int randomBits;


    private final long maxDeltaSeconds;
    private final long maxWorkerId;
    private final long maxSequence;


    private final int timestampShift;
    private final int workerIdShift;
    private final int randomShift;


    public OfferCodeBitsAllocator(int timestampBits, int workerIdBits, int randomBits, int sequenceBits) {

        int allocateTotalBits = signBits  + randomBits + timestampBits + workerIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");


        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
        this.sequenceBits = sequenceBits;
        this.randomBits = randomBits;


        this.maxDeltaSeconds = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxSequence = ~(-1L << sequenceBits);


        this.timestampShift = workerIdBits + sequenceBits + randomBits;
        this.workerIdShift = sequenceBits + randomBits;
        this.randomShift = sequenceBits;
    }


    public long allocate(long deltaSeconds, long workerId,long random, long sequence) {
        return  (deltaSeconds << timestampShift) | (workerId << workerIdShift) | (random << randomShift) | sequence;
    }


    public int getSignBits() {
        return signBits;
    }

    public int getTimestampBits() {
        return timestampBits;
    }

    public int getWorkerIdBits() {
        return workerIdBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public long getMaxDeltaSeconds() {
        return maxDeltaSeconds;
    }

    public long getMaxWorkerId() {
        return maxWorkerId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public int getTimestampShift() {
        return timestampShift;
    }

    public int getWorkerIdShift() {
        return workerIdShift;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
