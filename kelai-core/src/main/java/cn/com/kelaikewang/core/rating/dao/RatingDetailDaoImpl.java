package cn.com.kelaikewang.core.rating.dao;

import org.broadleafcommerce.core.rating.domain.RatingDetail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("ratingDetailDao")
public class RatingDetailDaoImpl implements RatingDetailDao{
    @PersistenceContext(
            unitName = "blPU"
    )
    protected EntityManager em;
    @Override
    public void create(RatingDetail ratingDetail) {
        em.persist(ratingDetail);
    }

    @Override
    public void flush() {
        em.flush();
    }
}
