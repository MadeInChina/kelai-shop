package cn.com.kelaikewang.core.order.domain;

import cn.com.kelaikewang.core.logistics.domain.ExpressCompany;
import cn.com.kelaikewang.core.logistics.domain.ExpressCompanyImpl;
import cn.com.kelaikewang.core.logistics.service.type.ConfirmReceiptType;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationCollection;
import org.broadleafcommerce.core.order.domain.OrderImpl;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ZJCMS_ORDER")
@PrimaryKeyJoinColumn(name = "ORDER_ID")
public class ZaoJiCMSOrderImpl extends OrderImpl implements ZaoJiCMSOrder {

    @Column(name = "PROVINCE")
    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_Province", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 3 )
    protected String province;

    @Column(name = "PROVINCE_CODE")
    protected String provinceCode;
    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_City", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 4)
    @Column(name = "CITY")
    protected String city;

    @Column(name = "CITY_CODE")
    protected String cityCode;
    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_Region", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 5)
    @Column(name = "REGION")
    protected String region;

    @Column(name = "REGION_CODE")
    protected String regionCode;
    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_Street", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 6)
    @Column(name = "STREET")
    protected String street;

    @Column(name = "STREET_CODE")
    protected String streetCode;
    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_AddressDetails", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 7)
    @Column(name = "ADDRESS")
    protected String address;

    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_FullName", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 1)
    @Column(name = "FULL_NAME")
    protected String fullName;

    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_Mobile", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 2)
    @Column(name = "MOBILE")
    protected String mobile;

    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_PostalCode", group = "OrderImpl_Shipping_Address",
            tab = "OrderImpl_Fulfillment_Groups_Tab",
            order = 8)
    @Column(name = "POSTAL_CODE")
    protected String postalCode;

    @AdminPresentation(friendlyName = "OrderImpl_Shipping_Address_Remarks", group =  GroupName.General,
            order = Integer.MAX_VALUE)
    @Column(name = "REMARKS")
    protected String remarks;

    @Column(name = "IS_SUBMITTED_RATE")
    protected Boolean submittedRate;

    /**
     * 快递单号
     */
    @Column(name = "LOGISTICS_TRACKING_NUMBER")
    protected String logisticsTrackingNumber;
    /**
     * 发货时间
     */
    @Column(name = "DELIVERY_TIME")
    protected Date deliveryTime;
    /**
     * 快递公司
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ExpressCompanyImpl.class)
    @JoinColumn(name = "EXPRESS_COMPANY_ID")
    @AdminPresentation(excluded = true)
    protected ExpressCompany expressCompany;
    /**
     * 确认收货方式（手动/自动）
     */
    @Column(name = "CONFIRM_RECEIPT_TYPE")
    protected String confirmReceiptType;

    @Column(name = "CONFIRM_RECEIPT_TIME")
    protected Date confirmReceiptTime;

    @AdminPresentationCollection(tab = "OrderImpl_AfterSales",friendlyName = "AfterSalesImpl",readOnly = true)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "order",targetEntity = AfterSalesImpl.class)
    protected List<AfterSales> afterSales;

    @Column(name = "IS_PICKED_UP_IN_STORE")
    protected Boolean pickedUpInStore;
    @Column(name = "WRITE_OFF_CODE",unique = true)
    protected Long writeOffCode;
    @Column(name = "WRITE_OFF_STATUS")
    protected String writeOffStatus;
    @Column(name = "WRITE_OFF_BY_USER_ID")
    protected Long writeOffByUserId;
    @Column(name = "WRITE_OFF_DATE")
    protected Date writeOffDate;
    /**
     * 支付方式：货到付款/在线支付
     */
    @Column(name = "PAYMENT_METHOD")
    protected String paymentMethod;

    @Override
    public String getProvince() {
        return province;
    }

    @Override
    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getMobile() {
        return mobile;
    }

    @Override
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postCode) {
        this.postalCode = postCode;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getLogisticsTrackingNumber() {
        return logisticsTrackingNumber;
    }

    public void setLogisticsTrackingNumber(String logisticsTrackingNumber) {
        this.logisticsTrackingNumber = logisticsTrackingNumber;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public ExpressCompany getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(ExpressCompany expressCompany) {
        this.expressCompany = expressCompany;
    }

    public ConfirmReceiptType getConfirmReceiptType() {
        return ConfirmReceiptType.getInstance(confirmReceiptType);
    }

    public void setConfirmReceiptType(ConfirmReceiptType confirmReceiptType) {
        this.confirmReceiptType = confirmReceiptType.getType();
    }

    @Override
    public Date getConfirmReceiptTime() {
        return confirmReceiptTime;
    }

    @Override
    public void setConfirmReceiptTime(Date confirmReceiptTime) {
        this.confirmReceiptTime = confirmReceiptTime;
    }

    public List<AfterSales> getAfterSales() {
        return afterSales;
    }

    public void setAfterSales(List<AfterSales> afterSales) {
        this.afterSales = afterSales;
    }

    public Boolean getSubmittedRate() {
        return submittedRate;
    }

    public void setSubmittedRate(Boolean submittedRate) {
        this.submittedRate = submittedRate;
    }

    public Boolean getPickedUpInStore() {
        return pickedUpInStore;
    }

    public void setPickedUpInStore(Boolean pickedUpInStore) {
        this.pickedUpInStore = pickedUpInStore;
    }

    public Long getWriteOffCode() {
        return writeOffCode;
    }

    public void setWriteOffCode(Long writeOffCode) {
        this.writeOffCode = writeOffCode;
    }

    public String getWriteOffStatus() {
        return writeOffStatus;
    }

    public void setWriteOffStatus(String writeOffStatus) {
        this.writeOffStatus = writeOffStatus;
    }

    public Long getWriteOffByUserId() {
        return writeOffByUserId;
    }

    public void setWriteOffByUserId(Long writeOffByUserId) {
        this.writeOffByUserId = writeOffByUserId;
    }

    public Date getWriteOffDate() {
        return writeOffDate;
    }

    public void setWriteOffDate(Date writeOffDate) {
        this.writeOffDate = writeOffDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
