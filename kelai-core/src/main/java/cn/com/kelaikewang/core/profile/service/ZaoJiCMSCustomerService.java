package cn.com.kelaikewang.core.profile.service;


import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;
import cn.com.kelaikewang.core.profile.form.UpdateMobileForm;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;

import java.util.List;

public interface ZaoJiCMSCustomerService extends CustomerService {
    void updateMobile(UpdateMobileForm updateMobileForm);
    String generateForgotPasswordToken(String mobile);
    Customer readCustomerByWeChatOpenId(String weChatOpenId);
    Customer readCustomerByNickname(String nickname);
    void saveCustomerWeChatInfo(String weChatOpenId,String username);
    List<StatisticItemDTO<Long>> getCountOfLast12MonthsUserRegistration();
    List<StatisticItemDTO<Long>> getCountOfUserRegistrationByDateRange();
}
