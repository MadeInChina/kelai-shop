package cn.com.kelaikewang.core.rating.dao;

import org.broadleafcommerce.core.rating.domain.RatingDetail;

public interface RatingDetailDao {
    void create(RatingDetail ratingDetail);
    void flush();
}
