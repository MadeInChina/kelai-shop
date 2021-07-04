package cn.com.kelaikewang.core.rating.dao;


import org.broadleafcommerce.core.rating.dao.RatingSummaryDaoImpl;
import org.broadleafcommerce.core.rating.domain.RatingSummary;

public class ZaoJiCMSRatingSummaryDaoImpl extends RatingSummaryDaoImpl implements ZaoJiCMSRatingSummaryDao{

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void create(RatingSummary ratingSummary) {
        em.persist(ratingSummary);
    }

}
