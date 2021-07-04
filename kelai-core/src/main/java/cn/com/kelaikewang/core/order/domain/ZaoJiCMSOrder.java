package cn.com.kelaikewang.core.order.domain;

import cn.com.kelaikewang.core.logistics.domain.ExpressCompany;
import cn.com.kelaikewang.core.logistics.service.type.ConfirmReceiptType;
import org.broadleafcommerce.core.order.domain.Order;

import java.util.Date;
import java.util.List;

public interface ZaoJiCMSOrder extends Order {

    String getProvince();
    void setProvince(String province);
    String getCity();
    void setCity(String city);
    String getRegion();
    void setRegion(String region);
    String getStreet();
    void setStreet(String street);
    String getAddress();
    void setAddress(String address);
    String getFullName();
    void setFullName(String fullName);
    String getMobile();
    void setMobile(String mobile);
    String getPostalCode();
    void setPostalCode(String postalCode);
    String getRemarks();
    void setRemarks(String remarks);

    String getProvinceCode();
    void setProvinceCode(String provinceCode);
    String getCityCode();
    void setCityCode(String cityCode);
    String getRegionCode();
    void setRegionCode(String regionCode);
    String getStreetCode();
    void setStreetCode(String streetCode);

    String getLogisticsTrackingNumber();

    void setLogisticsTrackingNumber(String logisticsTrackingNumber);

    Date getDeliveryTime();

    void setDeliveryTime(Date deliveryTime);

    ExpressCompany getExpressCompany();

    void setExpressCompany(ExpressCompany expressCompany);

    ConfirmReceiptType getConfirmReceiptType();

    void setConfirmReceiptType(ConfirmReceiptType confirmReceiptType);

    Date getConfirmReceiptTime();
    void setConfirmReceiptTime(Date confirmReceiptTime);

    List<AfterSales> getAfterSales();

    void setAfterSales(List<AfterSales> afterSales);


    Boolean getSubmittedRate();

    void setSubmittedRate(Boolean submittedRate);

    Boolean getPickedUpInStore();

    void setPickedUpInStore(Boolean pickedUpInStore);

    Long getWriteOffCode();

    void setWriteOffCode(Long writeOffCode);

    String getWriteOffStatus();

    void setWriteOffStatus(String writeOffStatus);

    Long getWriteOffByUserId();

    void setWriteOffByUserId(Long writeOffByUserId);

    Date getWriteOffDate();

    void setWriteOffDate(Date writeOffDate);

    String getPaymentMethod();
    void setPaymentMethod(String paymentMethod);
}
