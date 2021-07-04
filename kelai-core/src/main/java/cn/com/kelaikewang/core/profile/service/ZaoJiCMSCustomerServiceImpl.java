package cn.com.kelaikewang.core.profile.service;

import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;
import cn.com.kelaikewang.commons.lang.DateUtils;
import cn.com.kelaikewang.core.profile.dao.ZaoJiCMSCustomerDao;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomer;
import cn.com.kelaikewang.core.profile.form.UpdateMobileForm;
import org.broadleafcommerce.common.security.util.PasswordUtils;
import org.broadleafcommerce.common.service.GenericResponse;
import org.broadleafcommerce.common.time.SystemTime;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerForgotPasswordSecurityToken;
import org.broadleafcommerce.profile.core.domain.CustomerForgotPasswordSecurityTokenImpl;
import org.broadleafcommerce.profile.core.service.CustomerServiceImpl;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/*@Service("zjcmsCustomerService")*/
public class ZaoJiCMSCustomerServiceImpl extends CustomerServiceImpl implements ZaoJiCMSCustomerService {

    /*@Resource(name = "blCustomerDao")
    protected CustomerDao customerDao;*/

    @Value("${distribution.server.secret}")
    private String secret;

    @Resource(name = "blCustomerDao")
    private ZaoJiCMSCustomerDao nextShopCustomerDao;

    //@Autowired
    //protected DistributionService distributionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZaoJiCMSCustomerServiceImpl.class);

    @Transactional("blTransactionManager")
    @Override
    public void updateMobile(UpdateMobileForm updateMobileForm) {
        Customer customer = CustomerState.getCustomer();
        customer.setUsername(updateMobileForm.getNewMobile());
        customerDao.save(customer);

        //loginService.loginCustomer(updateMobileForm.getNewMobile(),updateMobileForm.getPassword());
    }

    @Transactional("blTransactionManager")
    @Override
    public String generateForgotPasswordToken(String mobile) {
        Customer customer = this.readCustomerByUsername(mobile);
        if (customer == null){
            return null;
        }
        String token = PasswordUtils.generateSecurePassword(this.getPasswordTokenLength());
        token = token.toLowerCase();
        CustomerForgotPasswordSecurityToken fpst = new CustomerForgotPasswordSecurityTokenImpl();
        fpst.setCustomerId(customer.getId());
        fpst.setToken(this.encodePassword(token));
        fpst.setCreateDate(SystemTime.asDate());
        this.customerForgotPasswordSecurityTokenDao.saveToken(fpst);
        return token;
    }
    @Transactional("blTransactionManager")
    @Override
    public Customer readCustomerByWeChatOpenId(String weChatOpenId) {
        ZaoJiCMSCustomer youZanPuZiCustomer = nextShopCustomerDao.readCustomerWeChatInfoByWeChatOpenId(weChatOpenId);

        return youZanPuZiCustomer;
    }

    @Override
    public Customer readCustomerByNickname(String nickname) {
        return nextShopCustomerDao.readCustomerByNickname(nickname);
    }

    @Transactional("blTransactionManager")
    @Override
    public void saveCustomerWeChatInfo(String weChatOpenId, String username) {
        /*YouZanPuZiCustomer youZanPuZiCustomer = new YouZanPuZiCustomerImpl();
        youZanPuZiCustomer.setWeChatOpenId(weChatOpenId);
        youZanPuZiCustomer.setId(customerId);*/
        if (nextShopCustomerDao.readCustomerWeChatInfoByWeChatOpenId(weChatOpenId) == null) {
            Customer customer = customerDao.readCustomerByUsername(username);
            if (customer != null) {
                nextShopCustomerDao.saveCustomerWeChatInfo(weChatOpenId, customer.getId());
            }
        }
    }


    @Override
    public List<StatisticItemDTO<Long>> getCountOfLast12MonthsUserRegistration() {
        List<StatisticItemDTO<Long>> itemDTOS = new ArrayList<>();
        for (int i=-11,j=1;i<1;i++,j++) {
            Date now = new Date();
            Date ago = DateUtils.addMonths(now, i);
            Date start = DateUtils.getMonthBegin(ago);
            Date end = DateUtils.getMonthEnd(ago);

            Long count = nextShopCustomerDao.readCountOfRegCustomerByDateRange(start,end);
            if (count == null){
                count = 0L;
            }
            itemDTOS.add(new StatisticItemDTO<>("th" + j + "MonthCount",DateUtils.formatDate(start,"yy年MM月"),count));
        }
        return itemDTOS;
    }

    @Override
    public List<StatisticItemDTO<Long>> getCountOfUserRegistrationByDateRange() {
        Date today = new Date();
        Date todayBegin = DateUtils.getDayBegin(today);
        Date todayEnd = DateUtils.getDayEnd(today);
        Long todayCount = nextShopCustomerDao.readCountOfRegCustomerByDateRange(todayBegin,todayEnd);

        Date currentWeekBegin = DateUtils.getFirstDayOfWeekStartTime();
        Date currentWeekEnd = DateUtils.getLastDayOfWeekEndTime();
        Long currentWeekCount = nextShopCustomerDao.readCountOfRegCustomerByDateRange(currentWeekBegin,currentWeekEnd);

        Date currentMonthBegin = DateUtils.getMonthBegin(today);
        Date currentMonthEnd = DateUtils.getMonthEnd(today);
        Long currentMonthCount = nextShopCustomerDao.readCountOfRegCustomerByDateRange(currentMonthBegin,currentMonthEnd);

        Date currentQuarterBegin = DateUtils.getQuarterStartTime();
        Date currentQuarterEnd = DateUtils.getQuarterEndTime();
        Long currentQuarterCount = nextShopCustomerDao.readCountOfRegCustomerByDateRange(currentQuarterBegin,currentQuarterEnd);

        Date currentYearBegin = DateUtils.getCurrentYearStartTime();
        Date currentYearEnd = DateUtils.getCurrentYearEndTime();
        Long currentYearCount = nextShopCustomerDao.readCountOfRegCustomerByDateRange(currentYearBegin,currentYearEnd);

        Long count = nextShopCustomerDao.readCountOfRegCustomer();

        return Arrays.asList(
                new StatisticItemDTO<>("todayCount","今日注册量",todayCount),
                new StatisticItemDTO<>("currentWeekCount","本周注册量",currentWeekCount),
                new StatisticItemDTO<>("currentMonthCount","本月注册量",currentMonthCount),
                new StatisticItemDTO<>("currentQuarterCount","本季度注册量",currentQuarterCount),
                new StatisticItemDTO<>("currentYearCount","本年度注册量",currentYearCount),
                new StatisticItemDTO<>("count","总注册量",count)
        );
    }

    @Override
    protected void checkCustomer(Customer customer, GenericResponse response) {
        //super.checkCustomer(customer, response);
        if (customer == null) {
            response.addErrorCode("invalidCustomer");
        }  else if (customer.isDeactivated()) {
            response.addErrorCode("inactiveUser");
        }
    }

    @Transactional("blTransactionManager")
    @Override
    public Customer registerCustomer(Customer customer, String password, String passwordConfirm) {
        //return super.registerCustomer(customer, password, passwordConfirm);
        ZaoJiCMSCustomer nextShopCustomer = (ZaoJiCMSCustomer)customer;
        nextShopCustomer.setWeChatOpenId(null);
        nextShopCustomer.setRegistered(true);
        if (nextShopCustomer.getId() == null) {
            nextShopCustomer.setId(this.findNextCustomerId());
        }

        nextShopCustomer.setUnencodedPassword(password);
        //记录分销上下级，不要抛异常
     /*   boolean isInviteReg = false;
        if (BLCSystemProperty.resolveBooleanSystemProperty("enableDistribution")) {
            try {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();
                Cookie[] cookies = request.getCookies();
                if (cookies != null && cookies.length > 0) {
                    for (Cookie cookie : cookies) {
                        if (CookieConstants.SID.equals(cookie.getName())) {
                            String sid = AesEncryptUtils.decrypt(cookie.getValue().replace("@","+"));
                            nextShopCustomer.setSuperiorId(Long.valueOf(sid));
                            isInviteReg = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("用户注册发生错误", e);
            }
        }*/
        Customer retCustomer = this.saveCustomer(nextShopCustomer);
        this.createRegisteredCustomerRoles(retCustomer);
        HashMap<String, Object> vars = new HashMap();
        vars.put("customer", retCustomer);
        //this.sendEmail(customer.getEmailAddress(), this.getRegistrationEmailInfo(), vars);

        this.notifyPostRegisterListeners(retCustomer);

       /* if (BLCSystemProperty.resolveBooleanSystemProperty("enableDistribution")) {
            if (isInviteReg) {
                inviteUser(retCustomer.getUsername(), readCustomerById(nextShopCustomer.getSuperiorId()).getUsername());
            } else {
                //如果不是邀请注册，上级是admin
                inviteUser(retCustomer.getUsername(), "admin");
            }
        }*/

        return retCustomer;
    }
}
