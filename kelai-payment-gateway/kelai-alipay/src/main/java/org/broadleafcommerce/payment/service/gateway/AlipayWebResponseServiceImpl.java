package org.broadleafcommerce.payment.service.gateway;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.dto.GatewayCustomerDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayWebResponseService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayReportingService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.payment.service.PaymentUnifiedOrderService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.alipay.service.payment.AlipayPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service("blAlipayWebResponseService")
public class AlipayWebResponseServiceImpl extends AbstractPaymentGatewayWebResponseService implements PaymentGatewayWebResponseService {

    @Resource(name = "blAlipayConfiguration")
    private AlipayConfiguration alipayConfiguration;


    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    protected static final Log LOG = LogFactory.getLog(AlipayWebResponseServiceImpl.class);

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, AlipayPaymentGatewayType.ALIPAY);
        paymentResponseDTO.paymentTransactionType(PaymentTransactionType.AUTHORIZE);
        //paymentResponseDTO.rawResponse(URLDecoder.decode(response.getRawResponse(), "UTF-8"))
        //获取支付宝POST过来反馈信息
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name =  iter.next();
                String[] values =  requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
                paymentResponseDTO.responseMap(name,valueStr);
            }

            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfiguration.getAlipayPublicKey(), "UTF-8", "RSA2"); //调用SDK验证签名

            //——请在这里编写您的程序（以下代码仅作参考）——

            /* 实际验证过程建议商户务必添加以下校验：
            1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
            2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
            3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
            4、验证app_id是否为该商户本身。*/

            if (signVerified) {//验证成功
                LOG.info("验签成功");
                paymentResponseDTO.valid(true);
                GatewayCustomerDTO<PaymentResponseDTO> customerDTO = paymentResponseDTO.customer();
                customerDTO.customerId(request.getParameter("buyer_id"));
                //商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getById(Long.parseLong(out_trade_no));
                paymentResponseDTO.orderId(paymentUnifiedOrder.getOrderId().toString());
                paymentResponseDTO.amount(new Money(request.getParameter("total_amount"), Currency.getInstance("CNY")));
                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                paymentResponseDTO.responseMap("PAYMENT_GATEWAY_TRADE_NO",trade_no);
                paymentResponseDTO.responseMap("UNIFIED_ORDER_ID",out_trade_no);
                //交易状态：
                // WAIT_BUYER_PAY（交易创建，等待买家付款）、
                // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
                // TRADE_SUCCESS（交易支付成功）、
                // TRADE_FINISHED（交易结束，不可退款）
                String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

                if (trade_status.equals("TRADE_FINISHED")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    paymentResponseDTO.successful(true);
                    LOG.info("交易完成");
                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    paymentResponseDTO.successful(true);
                    LOG.info("交易成功");
                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                }else {
                    paymentResponseDTO.successful(false);
                    LOG.error("交易处理中或者交易失败");
                }

                //out.println("success");


            } else {//验证失败
                //out.println("fail");
                paymentResponseDTO.valid(false);
                paymentResponseDTO.successful(false);
                LOG.error("验签失败");
            }

            //——请在这里编写您的程序（以上代码仅作参考）——
        } catch (AlipayApiException e) {
            e.printStackTrace();
            LOG.error("接收支付宝通知出错",e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOG.error("接收支付宝通知出错",e);
        }
        return paymentResponseDTO;
    }
}
