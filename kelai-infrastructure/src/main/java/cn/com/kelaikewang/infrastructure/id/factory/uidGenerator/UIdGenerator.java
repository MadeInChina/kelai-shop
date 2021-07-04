package cn.com.kelaikewang.infrastructure.id.factory.uidGenerator;

import cn.com.kelaikewang.infrastructure.id.factory.uidGenerator.config.UIdGeneratorConfigProperties;
import cn.com.kelaikewang.infrastructure.id.factory.IdWorker;
import cn.com.kelaikewang.infrastructure.id.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("uidGenerator")
public class UIdGenerator implements IdWorker<Long> {
    @Autowired
    private Map<String, UidGenerator> uidGeneratorMap;
    @Autowired
    private UIdGeneratorConfigProperties uIdGeneratorConfigProperties;
    @Override
    public Long nextId() {
        return uidGeneratorMap.get(uIdGeneratorConfigProperties.getGenerator()).getUID();
    }
}
