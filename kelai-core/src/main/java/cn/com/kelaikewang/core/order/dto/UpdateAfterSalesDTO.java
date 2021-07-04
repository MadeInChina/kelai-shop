package cn.com.kelaikewang.core.order.dto;

import cn.com.kelaikewang.core.order.service.type.AfterSalesServiceType;
import cn.com.kelaikewang.core.order.service.type.GoodsStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class UpdateAfterSalesDTO implements Serializable {
    private Long id;
    private AfterSalesServiceType type;
    private GoodsStatus goodsStatus;
    private String reason;
    private BigDecimal applyRefundAmount;
    private String remark;
    private List<String> evidence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AfterSalesServiceType getType() {
        return type;
    }

    public void setType(AfterSalesServiceType type) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<String> evidence) {
        this.evidence = evidence;
    }
}
