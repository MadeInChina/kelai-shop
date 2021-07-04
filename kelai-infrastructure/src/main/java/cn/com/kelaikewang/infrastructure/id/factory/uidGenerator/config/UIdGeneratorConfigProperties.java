package cn.com.kelaikewang.infrastructure.id.factory.uidGenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class UIdGeneratorConfigProperties {
    /**
     * defaultUidGenerator or cachedUidGenerator
     */
    @Value("${uid-generator.generator}")
    private String generator;
    /**
     * 当前时间，相对于时间基点"2016-05-20"的增量值，单位：秒
     */
    @Value("${uid-generator.timeBits}")
    private Integer timeBits;
    /**
     * 机器id，最多可支持约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
     */
    @Value("${uid-generator.workerBits}")
    private Integer workerBits;
    /**
     * 每秒下的并发序列，13 bits可支持每秒8192个并发。
     */
    @Value("${uid-generator.seqBits}")
    private Integer seqBits;
    /**
     *
     */
    @Value("${uid-generator.epochStr}")
    private String epochStr;
    //以下三个属性只有启用CachedUidGenerator时才需要填写
    /**
     *  RingBuffer size扩容参数, 可提高UID生成的吞吐量
     *  默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536
     */
    @Value("${uid-generator.boostPower}")
    private Integer boostPower;
    /**
     * 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
     * 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
     * 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全
     */
    @Value("${uid-generator.paddingFactor}")
    private Integer paddingFactor;
    /**
     * 另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充
     *  默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
     */
    @Value("${uid-generator.scheduleInterval}")
    private Integer scheduleInterval;


    public Integer getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(Integer timeBits) {
        this.timeBits = timeBits;
    }

    public Integer getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(Integer workerBits) {
        this.workerBits = workerBits;
    }

    public Integer getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(Integer seqBits) {
        this.seqBits = seqBits;
    }

    public String getEpochStr() {
        return epochStr;
    }

    public void setEpochStr(String epochStr) {
        this.epochStr = epochStr;
    }

    public Integer getBoostPower() {
        return boostPower;
    }

    public void setBoostPower(Integer boostPower) {
        this.boostPower = boostPower;
    }

    public Integer getPaddingFactor() {
        return paddingFactor;
    }

    public void setPaddingFactor(Integer paddingFactor) {
        this.paddingFactor = paddingFactor;
    }

    public Integer getScheduleInterval() {
        return scheduleInterval;
    }

    public void setScheduleInterval(Integer scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }
}
