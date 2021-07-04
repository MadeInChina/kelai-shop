package cn.com.kelaikewang.infrastructure.id.factory;

import java.io.Serializable;


public interface IdFactory {
    <T extends Serializable> T  getNextId();
    <T extends Serializable> T  getNextId(String algorithm);
}
