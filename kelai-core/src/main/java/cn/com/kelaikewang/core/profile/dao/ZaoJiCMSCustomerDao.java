package cn.com.kelaikewang.core.profile.dao;

import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomer;
import org.broadleafcommerce.profile.core.dao.CustomerDao;

import java.util.Date;
import java.util.List;

public interface ZaoJiCMSCustomerDao extends CustomerDao {
    ZaoJiCMSCustomer saveCustomerWeChatInfo(String weChatOpenId, Long customerId);
    ZaoJiCMSCustomer readCustomerWeChatInfoByWeChatOpenId(String weChatOpenId);
    ZaoJiCMSCustomer readCustomerByNickname(String nickname);
    Long readCountOfRegCustomerByDateRange(Date start,Date end);
    Long readCountOfRegCustomer();

    List<ZaoJiCMSCustomer> listCustomerByIds(List<Long> ids);
}
