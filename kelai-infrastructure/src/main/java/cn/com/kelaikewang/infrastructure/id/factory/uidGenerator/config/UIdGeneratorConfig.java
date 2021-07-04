package cn.com.kelaikewang.infrastructure.id.factory.uidGenerator.config;

import cn.com.kelaikewang.infrastructure.id.uid.UidGenerator;
import cn.com.kelaikewang.infrastructure.id.uid.impl.CachedUidGenerator;
import cn.com.kelaikewang.infrastructure.id.uid.impl.DefaultUidGenerator;
import cn.com.kelaikewang.infrastructure.id.uid.worker.ReusableWorkerIdAssigner;
import cn.com.kelaikewang.infrastructure.id.uid.worker.WorkerIdAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UIdGeneratorConfig {
    @Autowired
    private UIdGeneratorConfigProperties configProperties;

    @Bean
    public WorkerIdAssigner workerIdAssigner(){
        return new ReusableWorkerIdAssigner();
    }
  /*  @Bean("defaultUidGenerator")
    public DefaultUidGenerator defaultUidGenerator(WorkerIdAssigner workerIdAssigner){
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
        config(defaultUidGenerator,workerIdAssigner);
        return defaultUidGenerator;
    }*/
    @Bean("cachedUidGenerator")
    public UidGenerator cachedUidGenerator(WorkerIdAssigner workerIdAssigner){
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setBoostPower(configProperties.getBoostPower());
        cachedUidGenerator.setScheduleInterval(configProperties.getScheduleInterval());
        config(cachedUidGenerator,workerIdAssigner);
        return cachedUidGenerator;
    }
    private void config(DefaultUidGenerator uidGenerator, WorkerIdAssigner workerIdAssigner){
        uidGenerator.setEpochStr(configProperties.getEpochStr());
        uidGenerator.setSeqBits(configProperties.getSeqBits());
        uidGenerator.setTimeBits(configProperties.getTimeBits());
        uidGenerator.setWorkerBits(configProperties.getWorkerBits());
        uidGenerator.setWorkerIdAssigner(workerIdAssigner);
    }
}
