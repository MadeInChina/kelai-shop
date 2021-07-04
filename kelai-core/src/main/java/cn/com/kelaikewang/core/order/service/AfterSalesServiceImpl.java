package cn.com.kelaikewang.core.order.service;

import cn.com.kelaikewang.core.order.service.type.AfterSalesAuditStatus;
import cn.com.kelaikewang.core.order.service.type.GoodsStatus;
import cn.com.kelaikewang.core.order.service.type.RefundStatus;
import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import cn.com.kelaikewang.core.order.dao.AfterSalesDao;
import cn.com.kelaikewang.core.order.domain.AfterSales;
import cn.com.kelaikewang.core.order.domain.AfterSalesImpl;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.dto.CreateAfterSalesDTO;
import cn.com.kelaikewang.core.order.dto.UpdateAfterSalesDTO;
import cn.com.kelaikewang.core.order.service.type.*;
import cn.com.kelaikewang.core.payment.domain.ZaoJiCMSOrderPayment;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.payment.domain.OrderPayment;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("zjcmsAfterSalesService")
public class AfterSalesServiceImpl implements AfterSalesService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AfterSalesServiceImpl.class);

    @Resource(name = "zjcmsAfterSalesDao")
    protected AfterSalesDao afterSalesDao;
    @Resource(name = "blCustomerService")
    protected CustomerService customerService;
    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService orderService;

    @Resource(name = "wxPayService")
    protected WxPayService wxPayService;

    @Value("${gateway.wxpay.refundNotifyUrl}")
    protected String wxPayRefundNotifyUrl;

    @Resource(name = "blAlipayClient")
    protected DefaultAlipayClient alipayClient;

    @Transactional("blTransactionManager")
    @Override
    public ResponseDTO create(CreateAfterSalesDTO afterSales) {

        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(afterSales.getOrderId());
        if (ZaoJiCMSOrderStatus.PAID.getType().equals(order.getStatus().getType()) ||
                ZaoJiCMSOrderStatus.SHIPPED.getType().equals(order.getStatus().getType())){
            afterSales.setGoodsStatus(GoodsStatus.NOT_RECEIVED);
        }else if (ZaoJiCMSOrderStatus.RECEIVED.getType().equals(order.getStatus().getType())){
            afterSales.setGoodsStatus(GoodsStatus.RECEIVED);
        }else {
            if (ZaoJiCMSOrderStatus.REFUND_PROCESSING.getType().equals(order.getStatus().getType())){
                return ResponseDTO.fail("退款处理中，请不要重复提交退款申请");
            }
            if (ZaoJiCMSOrderStatus.REFUND_REJECTED.getType().equals(order.getStatus().getType())){
                return ResponseDTO.fail("商家已拒绝退款");
            }
            if (ZaoJiCMSOrderStatus.REFUNDED.getType().equals(order.getStatus().getType())){
                return ResponseDTO.fail("商家已退款");
            }else {
                return ResponseDTO.fail("该订单不支持该操作");
            }
        }


        if (afterSales.getApplyRefundAmount().compareTo(order.getTotal().getAmount()) > 0){
            return ResponseDTO.fail("退款金额不能大于订单总额");
        }

        AfterSales model = new AfterSalesImpl();
        model.setApplyRefundAmount(afterSales.getApplyRefundAmount());
        model.setCreateTime(new Date());
        model.setCustomer(customerService.readCustomerById(afterSales.getCustomerId()));
        model.setEvidence(afterSales.getEvidence());
        model.setGoodsStatus(afterSales.getGoodsStatus());
        model.setOrder(order);
        model.setReason(afterSales.getReason());
        model.setAuditStatus(AfterSalesAuditStatus.AUDITING);
        model.setType(afterSales.getType());
        afterSalesDao.create(model);

        orderService.refundProcessing(afterSales.getOrderId());

        return ResponseDTO.success("提交成功");
    }

    @Transactional("blTransactionManager")
    @Override
    public ResponseDTO update(UpdateAfterSalesDTO afterSales) {
        AfterSales model = afterSalesDao.getById(afterSales.getId());

        if (!AfterSalesAuditStatus.AUDITING.getType().equals(model.getAuditStatus())){
            return ResponseDTO.fail("不能更新退款申请");
        }
        Order order = model.getOrder();
        if (afterSales.getApplyRefundAmount().compareTo(order.getTotal().getAmount()) > 0){
            return ResponseDTO.fail("退款金额不能大于订单总额");
        }

        model.setApplyRefundAmount(afterSales.getApplyRefundAmount());
        model.setCreateTime(new Date());
        model.setEvidence(afterSales.getEvidence());
        model.setGoodsStatus(afterSales.getGoodsStatus());
        model.setReason(afterSales.getReason());
        model.setType(afterSales.getType().getType());
        model.setLastModified(new Date());

        afterSalesDao.update(model);

        return ResponseDTO.success("更新成功");
    }

    @Override
    public void updateRefundStatus(long id, RefundStatus refundStatus) {
        AfterSales model = afterSalesDao.getById(id);
        model.setRefundStatus(refundStatus);
        afterSalesDao.update(model);
    }

    @Transactional("blTransactionManager")
    @Override
    public ResponseDTO agree(long id, String reply) throws WxPayException, AlipayApiException {
        AfterSales model = afterSalesDao.getById(id);
        /**
         * 执行退款
         */
        ResponseDTO result = refund(model);

        model.setAuditStatus(AfterSalesAuditStatus.AGREE);
        model.setAuditTime(new Date());
        model.setReply(reply);
        afterSalesDao.update(model);

        return result;

    }
    private ResponseDTO refund(AfterSales afterSales) throws WxPayException, AlipayApiException {
        Order order = afterSales.getOrder();
        List<OrderPayment> orderPayments = order.getPayments();
        ZaoJiCMSOrderPayment zaoJiCMSOrderPayment = (ZaoJiCMSOrderPayment)orderPayments.get(0);

        String paymentGatewayType = zaoJiCMSOrderPayment.getGatewayType().getType();

        switch (paymentGatewayType){
            case "WxPay":
                return wxPayRefund(afterSales,zaoJiCMSOrderPayment,order);
            case "Alipay":
                return alipayRefund(afterSales,zaoJiCMSOrderPayment,order);
        }
        return ResponseDTO.fail("退款失败：未知的支付方式");

    }
    private ResponseDTO wxPayRefund(AfterSales afterSales,ZaoJiCMSOrderPayment zaoJiCMSOrderPayment,Order order) throws WxPayException {
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        refundRequest.setOutTradeNo(zaoJiCMSOrderPayment.getUnifiedOrderId().toString());
        refundRequest.setOutRefundNo(afterSales.getId().toString());
        BigDecimal fen = new BigDecimal(100);
        refundRequest.setRefundFee(afterSales.getApplyRefundAmount().multiply(fen).intValue());
        refundRequest.setTotalFee(order.getTotal().getAmount().multiply(fen).intValue());
        refundRequest.setTransactionId(zaoJiCMSOrderPayment.getPaymentGatewayTradeNO());
        refundRequest.setNotifyUrl(wxPayRefundNotifyUrl);
        WxPayRefundResult refundResult = wxPayService.refund(refundRequest);

        if ("SUCCESS".equals(refundResult.getReturnCode()) && "SUCCESS".equals(refundResult.getResultCode())){
            afterSales.setRefundStatus(RefundStatus.PROCESSING);
            afterSales.setRefundId(refundResult.getRefundId());
            afterSales.setRefundTransactionId(refundResult.getTransactionId());
            LOGGER.info("微信退款请求提交成功");
            return ResponseDTO.success("退款请求提交成功");
        }else {
            LOGGER.info("微信退款请求提交失败,returnCode={},resultCode={},err_code={},err_code_des={}" ,
                    refundResult.getReturnCode(),
                    refundResult.getResultCode(),
                    refundResult.getErrCode(),
                    refundResult.getErrCodeDes());
            afterSales.setRefundStatus(RefundStatus.FAIL);
            //afterSales.setRawRefundResponse(JSON.toJSONString(refundResult));
            return ResponseDTO.fail("微信退款请求提交失败：" + refundResult.getErrCodeDes());
        }
    }
    private ResponseDTO alipayRefund(AfterSales afterSales,ZaoJiCMSOrderPayment zaoJiCMSOrderPayment,Order order) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + zaoJiCMSOrderPayment.getUnifiedOrderId() + "\"," +
                "\"trade_no\":\"" + zaoJiCMSOrderPayment.getPaymentGatewayTradeNO() + "\"," +
                "\"refund_amount\":" + afterSales.getApplyRefundAmount() + "," +
                "\"out_request_no\":\"" + afterSales.getId() + "\"," +
                "  }");
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            LOGGER.info("alipay退款成功,orderId={}",order.getId());
            afterSales.setRefundStatus(RefundStatus.SUCCESS);
            afterSales.setRefundTransactionId(response.getTradeNo());
            //afterSales.setRawRefundResponse(JSON.toJSONString(response));
            return ResponseDTO.success("退款成功");
        } else {
            LOGGER.info("alipay退款失败,orderId={},code={},msg={},sub_code={},sub_msg={}",
                    order.getId(),
                    response.getCode(),
                    response.getMsg(),
                    response.getSubCode(),
                    response.getSubMsg());
            afterSales.setRefundStatus(RefundStatus.FAIL);
            //afterSales.setRawRefundResponse(JSON.toJSONString(response));
            return ResponseDTO.fail("退款失败：" + response.getSubMsg());
        }
    }
    @Transactional("blTransactionManager")
    @Override
    public void disagree(long id, String reply) {

        AfterSales model = afterSalesDao.getById(id);
        model.setAuditStatus(AfterSalesAuditStatus.DISAGREE);
        model.setAuditTime(new Date());
        model.setReply(reply);

        afterSalesDao.update(model);
    }

    @Override
    public List<AfterSales> listByOrderId(long orderId) {
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        return order.getAfterSales();
    }

    @Override
    public AfterSales getById(long id) {
        return afterSalesDao.getById(id);
    }
}
