package cn.com.kelaikewang.core.logistics.service;

import cn.com.kelaikewang.core.logistics.service.type.ConfirmReceiptType;
import cn.com.kelaikewang.core.order.dao.ZaoJiCMSOrderDao;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import com.alibaba.fastjson.JSON;
import cn.com.kelaikewang.core.logistics.dao.ExpressCompanyDao;
import cn.com.kelaikewang.core.logistics.domain.ExpressCompany;
import cn.com.kelaikewang.core.logistics.dto.LogisticTrackingInfoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service("zjcmsShippingService")
public class ShippingServiceImpl implements ShippingService {

    @Resource(name = "blOrderDao")
    protected ZaoJiCMSOrderDao zaoJiCMSOrderDao;
    @Resource(name = "zjcmsExpressCompanyDao")
    protected ExpressCompanyDao expressCompanyDao;
    @Resource(name = "kdniaoTrackQueryAPI")
    protected  KdniaoTrackQueryAPI kdniaoTrackQueryAPI;

    @Transactional("blTransactionManager")
    @Override
    public void sendOut(long orderId, long expressCompanyId,String  logisticsTrackingNumber) {
        ZaoJiCMSOrder order = zaoJiCMSOrderDao.getById(orderId);
        if (!ZaoJiCMSOrderStatus.PAID.getType().equals(order.getStatus().getType())){
            throw new RuntimeException("非待发货状态订单不支持发货操作");
        }
        ExpressCompany expressCompany = expressCompanyDao.getById(expressCompanyId);
        order.setLogisticsTrackingNumber(logisticsTrackingNumber);
        order.setExpressCompany(expressCompany);
        order.setStatus(ZaoJiCMSOrderStatus.SHIPPED);
        order.setDeliveryTime(new Date());
        zaoJiCMSOrderDao.save(order);
    }
    @Transactional("blTransactionManager")
    @Override
    public void confirmReceipt(long orderId, ConfirmReceiptType confirmReceiptType) {
        ZaoJiCMSOrder order = zaoJiCMSOrderDao.getById(orderId);
        if (!ZaoJiCMSOrderStatus.PAID.getType().equals(order.getStatus().getType()) &&
                !ZaoJiCMSOrderStatus.SHIPPED.getType().equals(order.getStatus().getType())){
            //虚拟商品、自提商品支付后就可以确认收货
            throw new RuntimeException("非已发货状态或非待发货状态不支持该操作");
        }

        order.setStatus(ZaoJiCMSOrderStatus.RECEIVED);
        order.setConfirmReceiptType(ConfirmReceiptType.MANUAL);
        order.setConfirmReceiptTime(new Date());
        zaoJiCMSOrderDao.save(order);
    }

    @Override
    public LogisticTrackingInfoDTO getLogisticTrackingInfo(long orderId) throws Exception {
        ZaoJiCMSOrder order = zaoJiCMSOrderDao.getById(orderId);
        ExpressCompany expressCompany = order.getExpressCompany();
        String jsonResult = kdniaoTrackQueryAPI.getOrderTracesByJson(expressCompany.getCode(),order.getLogisticsTrackingNumber());
        return JSON.parseObject(jsonResult,LogisticTrackingInfoDTO.class);
    }
}
