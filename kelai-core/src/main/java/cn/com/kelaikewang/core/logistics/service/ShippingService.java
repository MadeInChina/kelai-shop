package cn.com.kelaikewang.core.logistics.service;

import cn.com.kelaikewang.core.logistics.service.type.ConfirmReceiptType;
import cn.com.kelaikewang.core.logistics.dto.LogisticTrackingInfoDTO;

public interface ShippingService {
    /**
     * 发货
     * @param orderId
     * @param expressCompanyId
     * @param logisticsTrackingNumber
     */
    void sendOut(long orderId,long expressCompanyId,String  logisticsTrackingNumber);

    /**
     * 确认收货
     * @param orderId
     * @param confirmReceiptType
     */
    void confirmReceipt(long orderId, ConfirmReceiptType confirmReceiptType);

    /**
     * 获取物流信息
     * @param orderId
     * @return
     */
    LogisticTrackingInfoDTO getLogisticTrackingInfo(long orderId) throws Exception;
}
