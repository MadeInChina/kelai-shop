package cn.com.kelaikewang.infrastructure.id.factory;

import java.io.Serializable;

public interface IdWorker<T extends Serializable> {
    T nextId();
}
