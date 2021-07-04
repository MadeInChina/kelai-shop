package cn.com.kelaikewang.infrastructure.model.attribute;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.core.ResolvableType;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseAttributeConverter<T> implements AttributeConverter<Object, String> {


    /**
     * 构造方法
     */
    public BaseAttributeConverter() {
    }

    /**
     * 转换属性为数据库值
     *
     * @param attribute
     *            属性
     * @return 数据库值
     */
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.toJSONString(attribute);
    }

    /**
     * 转换数据库值为属性
     *
     * @param dbData
     *            数据库值
     * @return 属性
     */
    public abstract Object convertToEntityAttribute(String dbData);
}
