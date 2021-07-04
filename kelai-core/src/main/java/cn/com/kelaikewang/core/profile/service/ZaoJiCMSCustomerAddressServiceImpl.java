package cn.com.kelaikewang.core.profile.service;

import cn.com.kelaikewang.core.profile.dao.ZaoJiCMSAddressDao;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.broadleafcommerce.profile.core.service.CustomerAddressServiceImpl;

import javax.annotation.Resource;

public class ZaoJiCMSCustomerAddressServiceImpl extends CustomerAddressServiceImpl {
    @Resource(name = "zjcmsAddressDao")
    private ZaoJiCMSAddressDao nextShopAddressDao;
    @Override
    public CustomerAddress readCustomerAddressById(Long customerAddressId) {
        CustomerAddress  customerAddress = super.readCustomerAddressById(customerAddressId);
        customerAddress.setAddress(nextShopAddressDao.getById(customerAddress.getAddress().getId()));
        return customerAddress;
    }
}
