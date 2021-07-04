package cn.com.kelaikewang.core.search.domain;

import org.broadleafcommerce.core.search.domain.FieldEntity;

public class ZaoJiCMSFieldEntity extends FieldEntity {

    public static final FieldEntity ARTICLE = new FieldEntity("ARTICLE", "文章");

    public ZaoJiCMSFieldEntity() {
        //do nothing
        super();
    }

    public ZaoJiCMSFieldEntity(final String type, final String friendlyType) {
        super(type,friendlyType);
    }
}
