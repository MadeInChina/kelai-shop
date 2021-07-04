package cn.com.kelaikewang.core.config;

import cn.com.kelaikewang.core.marketing.service.OfferCodeGenerator;
import cn.com.kelaikewang.core.marketing.service.handler.OfferCodeGroupCustomPersistenceHandler;
import cn.com.kelaikewang.infrastructure.id.uid.UidGenerator;
import cn.com.kelaikewang.infrastructure.id.uid.worker.ReusableWorkerIdAssigner;
import cn.com.kelaikewang.infrastructure.id.uid.worker.WorkerIdAssigner;
import org.broadleafcommerce.common.extensibility.context.merge.Merge;
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff Fischer
 */
@Configuration
@ComponentScan({"cn.com.kelaikewang"})
@ImportResource({"classpath:core-applicationContext.xml"})
public class CoreConfig {
    @Merge("blCustomPersistenceHandlers")
    public List<CustomPersistenceHandler> customPersistenceHandlers(OfferCodeGroupCustomPersistenceHandler offerCodeGroupCustomPersistenceHandler) {
        List<CustomPersistenceHandler> customPersistenceHandlers = new ArrayList<>();
        customPersistenceHandlers.add(offerCodeGroupCustomPersistenceHandler);
        return customPersistenceHandlers;
    }
    @Bean
    public WorkerIdAssigner workerIdAssigner() {
        return new ReusableWorkerIdAssigner();
    }
    @Bean("offerCodeGenerator")
    public UidGenerator offerCodeGenerator(WorkerIdAssigner workerIdAssigner){
        OfferCodeGenerator offerCodeGenerator = new OfferCodeGenerator();
        offerCodeGenerator.setWorkerIdAssigner(workerIdAssigner);

        offerCodeGenerator.setTimeBits(28);
        offerCodeGenerator.setWorkerBits(8);
        offerCodeGenerator.setSeqBits(13);
        offerCodeGenerator.setRandomBits(14);
        offerCodeGenerator.setEpochStr("2019-03-21");
        return offerCodeGenerator;
    }
}
