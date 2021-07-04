package cn.com.kelaikewang.core.rating.dao;

import org.broadleafcommerce.core.rating.dao.ReviewDetailDao;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;

public interface ZaoJiCMSReviewDetailDao extends ReviewDetailDao {
    void create(ReviewDetail reviewDetail);
    void flush();
}
