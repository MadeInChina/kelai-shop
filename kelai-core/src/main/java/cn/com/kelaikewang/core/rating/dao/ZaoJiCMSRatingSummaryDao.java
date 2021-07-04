package cn.com.kelaikewang.core.rating.dao;

import org.broadleafcommerce.core.rating.dao.RatingSummaryDao;
import org.broadleafcommerce.core.rating.domain.RatingSummary;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;
import org.broadleafcommerce.profile.core.domain.Customer;

public interface ZaoJiCMSRatingSummaryDao extends RatingSummaryDao {
    void flush();
    void create(RatingSummary ratingSummary);
}
