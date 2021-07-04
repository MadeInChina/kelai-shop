package cn.com.kelaikewang.core.order.domain;

import cn.com.kelaikewang.core.order.service.type.AfterSalesAuditStatus;
import cn.com.kelaikewang.core.order.service.type.GoodsStatus;
import cn.com.kelaikewang.core.order.service.type.RefundStatus;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.profile.core.domain.Customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AfterSales extends Serializable {
    Long getId();

    void setId(Long id);

    Order getOrder();

    void setOrder(Order order);

    Customer getCustomer();

    void setCustomer(Customer customer);

    String getType();

    void setType(String type);

    GoodsStatus getGoodsStatus();

    void setGoodsStatus(GoodsStatus goodsStatus);

    String getReason();

    void setReason(String reason);

    BigDecimal getApplyRefundAmount();

    void setApplyRefundAmount(BigDecimal applyRefundAmount);



    List<String> getEvidence();

    void setEvidence(List<String> evidence);

    Date getCreateTime();

    void setCreateTime(Date created);

    Date getAuditTime();

    void setAuditTime(Date auditTime);

    String getReply();

    void setReply(String reply);

    AfterSalesAuditStatus getAuditStatus();

    void setAuditStatus(AfterSalesAuditStatus auditStatus);

    Date getLastModified();

    void setLastModified(Date lastModified);

    RefundStatus getRefundStatus();
    void setRefundStatus(RefundStatus refundStatus);

    String getRefundTransactionId();

    void setRefundTransactionId(String refundTransactionId);

    String getRefundId();

    void setRefundId(String refundId);

}
