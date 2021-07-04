package cn.com.kelaikewang.infrastructure.id.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class IdFactoryImpl implements IdFactory {

    @Value("${id-factory.defaultAlgorithm}")
    private String defaultAlgorithm;

    @Autowired
    private Map<String,IdWorker> idWorkerMap;

    @Override
    public <T  extends Serializable> T getNextId(){
        IdWorker<T> idWorker = idWorkerMap.get(defaultAlgorithm);
        return idWorker.nextId();
    }

    @Override
    public <T  extends Serializable> T getNextId(String algorithm) {
        IdWorker<T> idWorker = idWorkerMap.get(algorithm);
        return idWorker.nextId();
    }


}
