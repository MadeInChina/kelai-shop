package cn.com.kelaikewang.core.rating.dao;

import org.broadleafcommerce.core.rating.dao.ReviewDetailDaoImpl;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;

public class ZaoJiCMSReviewDetailDaoImpl extends ReviewDetailDaoImpl implements ZaoJiCMSReviewDetailDao {
    @Override
    public void create(ReviewDetail reviewDetail) {
        em.persist(reviewDetail);
    }

    @Override
    public void flush() {
        em.flush();
    }
}
