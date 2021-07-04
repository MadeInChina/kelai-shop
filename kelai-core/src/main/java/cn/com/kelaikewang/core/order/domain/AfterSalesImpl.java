package cn.com.kelaikewang.core.order.domain;

import cn.com.kelaikewang.core.order.service.type.AfterSalesAuditStatus;
import cn.com.kelaikewang.core.order.service.type.GoodsStatus;
import cn.com.kelaikewang.core.order.service.type.RefundStatus;
import com.alibaba.fastjson.JSON;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomerImpl;
import cn.com.kelaikewang.infrastructure.model.attribute.BaseAttributeConverter;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "ZJCMS_AFTER_SALES")
@Entity
@AdminPresentationClass(friendlyName = "AfterSalesImpl")
public class AfterSalesImpl implements AfterSales, Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "AfterSalesServiceId")
    @GenericGenerator(
            name="AfterSalesServiceId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="AfterSalesServiceImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="AfterSalesServiceImpl")
            }
    )
    protected Long id;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ZaoJiCMSOrderImpl.class)
    @JoinColumn(name = "ORDER_ID")
    protected Order order;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ZaoJiCMSCustomerImpl.class)
    @JoinColumn(name = "CUSTOMER_ID")
    @AdminPresentation(friendlyName = "AfterSalesImpl_Customer",prominent = true,order = 1)
    protected Customer customer;
    /**
     * 退款方式
     */
    @Column(name = "TYPE")
    @AdminPresentation(friendlyName = "AfterSalesImpl_Type",
            prominent = true,
            order = 2,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.com.kelaikewang.core.order.service.type.AfterSalesServiceType")
    protected String type;
    /**
     * 货物状态
     */
    @Column(name = "GOODS_STATUS")
    @AdminPresentation(friendlyName = "AfterSalesImpl_GoodsStatus",
            prominent = true,
            order = 3,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.com.kelaikewang.core.order.service.type.GoodsStatus")
    protected String goodsStatus;
    /**
     * 退款原因
     */
    @Column(name = "REASON")
    @AdminPresentation(friendlyName = "AfterSalesImpl_Reason",prominent = true,order = 4)
    protected String reason;
    /**
     * 退款金额
     */
    @Column(name = "APPLY_REFUND_AMOUNT")
    @AdminPresentation(friendlyName = "AfterSalesImpl_ApplyRefundAmount",prominent = true,order = 5)
    protected BigDecimal applyRefundAmount;

    /**
     * 证据
     */
    @Column(name = "EVIDENCE")
    @Convert(converter = EvidenceConverter.class)
    protected List<String> evidence;
    @Column(name = "CREATE_TIME")
    @AdminPresentation(friendlyName = "AfterSalesImpl_CreateTime",prominent = true,order = 6)
    protected Date createTime;

    @Column(name = "AUDIT_TIME")
    @AdminPresentation(friendlyName = "AfterSalesImpl_AuditTime",prominent = true,order = 7)
    protected Date auditTime;
    @Column(name = "REPLY")
    protected String reply;
    @Column(name = "AUDIT_STATUS")
    @AdminPresentation(friendlyName = "AfterSalesImpl_AuditStatus",
            prominent = true,
            order = 8,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.com.kelaikewang.core.order.service.type.AfterSalesAuditStatus")
    protected String auditStatus;
    /**
     * 退款状态
     */
    @Column(name = "REFUND_STATUS")
    @AdminPresentation(friendlyName = "AfterSalesImpl_RefundStatus",
            prominent = true,
            order = 9,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "cn.com.kelaikewang.core.order.service.type.RefundStatus")
    protected String refundStatus;
    /**
     * 第三方支付的订单号
     */
    @Column(name = "REFUND_TRANSACTION_ID")
    protected String refundTransactionId;
    /**
     * 第三方支付的退款单号
     */
    @Column(name = "REFUND_ID")
    protected String refundId;


    @Column(name = "LAST_MODIFIED")
    @AdminPresentation(friendlyName = "AfterSalesImpl_LastModified",prominent = true,order = 10)
    protected Date lastModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GoodsStatus getGoodsStatus() {
        return GoodsStatus.getInstance(goodsStatus);
    }

    public void setGoodsStatus(GoodsStatus goodsStatus) {
        this.goodsStatus = goodsStatus.getType();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getApplyRefundAmount() {
        return applyRefundAmount;
    }

    public void setApplyRefundAmount(BigDecimal applyRefundAmount) {
        this.applyRefundAmount = applyRefundAmount;
    }

    @Override
    public List<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<String> evidence) {
        this.evidence = evidence;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getAuditTime() {
        return auditTime;
    }

    @Override
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public AfterSalesAuditStatus getAuditStatus() {
        return AfterSalesAuditStatus.getInstance(auditStatus);
    }

    @Override
    public void setAuditStatus(AfterSalesAuditStatus auditStatus) {
        this.auditStatus = auditStatus.getType();
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public RefundStatus getRefundStatus() {
        return RefundStatus.getInstance(refundStatus);
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus.getType();
    }

    public String getRefundTransactionId() {
        return refundTransactionId;
    }

    public void setRefundTransactionId(String refundTransactionId) {
        this.refundTransactionId = refundTransactionId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    @Converter
    public static class EvidenceConverter extends BaseAttributeConverter<List<String>>{

        @Override
        public Object convertToEntityAttribute(String dbData) {
            return JSON.parseArray(dbData,String.class);
        }
    }

}
