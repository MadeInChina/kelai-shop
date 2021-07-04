package cn.com.kelaikewang.core.profile.dao;

import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomer;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomerImpl;
import org.broadleafcommerce.common.util.dao.TQRestriction;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.broadleafcommerce.profile.core.dao.CustomerDaoImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*@Repository("zjcmsCustomerDao")*/
public class ZaoJiCMSCustomerDaoImpl extends CustomerDaoImpl implements ZaoJiCMSCustomerDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

   /* @Resource(name = "blCustomerDao")
    protected CustomerDao customerDao;*/

    @Override
    public synchronized ZaoJiCMSCustomer saveCustomerWeChatInfo(String weChatOpenId, Long customerId) {

        ZaoJiCMSCustomerImpl youZanPuZiCustomer = (ZaoJiCMSCustomerImpl)this.readCustomerById(customerId);
        youZanPuZiCustomer.setWeChatOpenId(weChatOpenId);
        //youZanPuZiCustomer.setCustomerId(customerId);
        ZaoJiCMSCustomer response = em.merge(youZanPuZiCustomer);
        em.flush();
        return response;

    }

    @Override
    public ZaoJiCMSCustomer readCustomerWeChatInfoByWeChatOpenId(String weChatOpenId) {
        TypedQuery<ZaoJiCMSCustomer> query = new TypedQueryBuilder<>(ZaoJiCMSCustomer.class, "c")
                .addRestriction("c.weChatOpenId", "=", weChatOpenId)
                .toQuery(em);
        List<ZaoJiCMSCustomer> list = query.getResultList();
        if (list == null || list.size() == 0){
            return null;
        }else {
            return list.get(0);
        }
    }

    @Override
    public ZaoJiCMSCustomer readCustomerByNickname(String nickname) {
        TypedQuery<ZaoJiCMSCustomer> query = new TypedQueryBuilder<>(ZaoJiCMSCustomer.class, "c")
                .addRestriction("c.nickname", "=", nickname)
                .toQuery(em);
        List<ZaoJiCMSCustomer> list = query.getResultList();
        if (list == null || list.size() == 0){
            return null;
        }else {
            return list.get(0);
        }
    }

    @Override
    public Long readCountOfRegCustomerByDateRange(Date start, Date end) {
        return em.createNamedQuery("BC_READ_COUNT_OF_REG_CUSTOMER_BY_DATE_RANGE",Long.class)
                .setParameter("start",start)
                .setParameter("end",end)
                .getSingleResult();
    }

    @Override
    public Long readCountOfRegCustomer() {
        return em.createNamedQuery("BC_READ_COUNT_OF_REG_CUSTOMER",Long.class).getSingleResult();
    }

    @Override
    public List<ZaoJiCMSCustomer> listCustomerByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0){
            return new ArrayList<>();
        }
        return new TypedQueryBuilder<>(ZaoJiCMSCustomer.class, "c")
                .addRestriction(new TQRestriction("c.id","in",ids))
                .toQuery(em)
                .getResultList();
    }
}
