package cn.com.kelaikewang.controller.checkout;

import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddress;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddressImpl;
import org.broadleafcommerce.core.web.checkout.model.ShippingInfoForm;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.PhoneImpl;

public class ZaoJiCMSShippingInfoForm extends ShippingInfoForm {
    protected ZaoJiCMSAddress address = new ZaoJiCMSAddressImpl();
    public ZaoJiCMSShippingInfoForm(){
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
        this.address = (ZaoJiCMSAddress) address;
    }
}
