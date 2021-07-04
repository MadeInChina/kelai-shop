package cn.com.kelaikewang.core.order.dto;

import cn.com.kelaikewang.core.order.service.type.GoodsStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CreateAfterSalesDTO implements Serializable {
    private Long orderId;
    private Long customerId;
    private String type;
    private GoodsStatus goodsStatus;
    private String reason;
    private BigDecimal applyRefundAmount;
    private List<String> evidence;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GoodsStatus getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(GoodsStatus goodsStatus) {
        this.goodsStatus = goodsStatus;
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


    public List<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<String> evidence) {
        this.evidence = evidence;
    }
}
