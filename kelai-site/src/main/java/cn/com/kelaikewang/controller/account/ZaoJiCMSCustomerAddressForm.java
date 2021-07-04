package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddress;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddressImpl;
import org.broadleafcommerce.core.web.controller.account.CustomerAddressForm;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.PhoneImpl;

public class ZaoJiCMSCustomerAddressForm extends CustomerAddressForm {
    protected ZaoJiCMSAddress address = new ZaoJiCMSAddressImpl();
    public ZaoJiCMSCustomerAddressForm() {
        this.address.setPhonePrimary(new PhoneImpl());
        this.address.setPhoneSecondary(new PhoneImpl());
        this.address.setPhoneFax(new PhoneImpl());
    }
    @Override
    public Address getAddress() {
        return this.address;
    }
    @Override
    public void setAddress(Address address) {
        if (address.getPhonePrimary() == null) {
            address.setPhonePrimary(new PhoneImpl());
        }

        if (address.getPhoneSecondary() == null) {
            address.setPhoneSecondary(new PhoneImpl());
        }

        if (address.getPhoneFax() == null) {
            address.setPhoneFax(new PhoneImpl());
        }

        this.address = (ZaoJiCMSAddress)address;
    }
}
