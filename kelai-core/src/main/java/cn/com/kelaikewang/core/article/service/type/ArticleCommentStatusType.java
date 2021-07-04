package cn.com.kelaikewang.core.article.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ArticleCommentStatusType  implements Serializable, BroadleafEnumerationType {
    private String type;
    private String friendlyType;

    private static final LinkedHashMap<String, ArticleCommentStatusType> TYPES = new LinkedHashMap<>();

    public static final ArticleCommentStatusType OPEN = new ArticleCommentStatusType("OPEN", "开启");
    public static final ArticleCommentStatusType CLOSE = new ArticleCommentStatusType("CLOSE", "关闭");


    public static ArticleCommentStatusType getInstance(final String type) {
        return TYPES.get(type);
    }

    public ArticleCommentStatusType() {

    }

    public ArticleCommentStatusType(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        this.setType(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    public void setType(String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    public void setFriendlyType(String friendlyType) {
        this.friendlyType = friendlyType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        ArticleCommentStatusType other = (ArticleCommentStatusType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
