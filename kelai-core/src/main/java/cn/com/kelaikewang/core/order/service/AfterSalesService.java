package cn.com.kelaikewang.core.order.service;

import cn.com.kelaikewang.core.order.service.type.RefundStatus;
import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import cn.com.kelaikewang.core.order.domain.AfterSales;
import cn.com.kelaikewang.core.order.dto.CreateAfterSalesDTO;
import cn.com.kelaikewang.core.order.dto.UpdateAfterSalesDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;

import java.util.List;

public interface AfterSalesService {
    ResponseDTO create(CreateAfterSalesDTO afterSales);
    ResponseDTO update(UpdateAfterSalesDTO afterSales);
    void updateRefundStatus(long id, RefundStatus refundStatus);
    ResponseDTO agree(long id,String reply) throws WxPayException, AlipayApiException;
    void disagree(long id,String reply);
    List<AfterSales> listByOrderId(long orderId);
    AfterSales getById(long id);
}
