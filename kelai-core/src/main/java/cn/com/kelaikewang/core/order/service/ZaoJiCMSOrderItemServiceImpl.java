package cn.com.kelaikewang.core.order.service;

import cn.com.kelaikewang.core.logistics.dao.ShippingPricingRuleDao;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import cn.com.kelaikewang.core.order.dao.ZaoJiCMSOrderItemDao;
import cn.com.kelaikewang.core.order.dto.OrderItemShippingPricingRuleDTO;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.OrderItemServiceImpl;
import org.broadleafcommerce.core.order.service.call.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class ZaoJiCMSOrderItemServiceImpl extends OrderItemServiceImpl implements ZaoJiCMSOrderItemService{
    protected Logger LOGGER = LoggerFactory.getLogger(ZaoJiCMSOrderItemServiceImpl.class);

    @Resource(name = "blOrderItemDao")
    private ZaoJiCMSOrderItemDao nextShopOrderItemDao;
    @Resource(name = "zjcmsShippingPricingRuleDao")
    private ShippingPricingRuleDao shippingPricingRuleDao;

    @Override
    public OrderItem buildOrderItemFromDTO(Order order, OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem item = super.buildOrderItemFromDTO(order,orderItemRequestDTO);
        //允许折扣
        item.setDiscountingAllowed(true);
        return item;
    }
    @Transactional("blTransactionManager")
    @Override
    public void saveOrderItemsShippingPricingRule(List<OrderItemShippingPricingRuleDTO> orderItemShippingPricingRuleDTOS) {
        for (OrderItemShippingPricingRuleDTO dto : orderItemShippingPricingRuleDTOS){
            if(nextShopOrderItemDao.isOrderItemShippingPricingRuleExists(dto.getOrderItemId())){
                nextShopOrderItemDao.updateOrderItemShippingPricingRule(dto.getOrderItemId(),dto.getShippingPricingRuleId());
            }else {
                nextShopOrderItemDao.createOrderItemShippingPricingRule(dto.getOrderItemId(),dto.getShippingPricingRuleId());
            }
        }
    }
    @Transactional("blTransactionManager")
    @Override
    public void saveOrderItemShippingPricingRule(OrderItemShippingPricingRuleDTO dto) {
        if(nextShopOrderItemDao.isOrderItemShippingPricingRuleExists(dto.getOrderItemId())){
            nextShopOrderItemDao.updateOrderItemShippingPricingRule(dto.getOrderItemId(),dto.getShippingPricingRuleId());
        }else {
            nextShopOrderItemDao.createOrderItemShippingPricingRule(dto.getOrderItemId(),dto.getShippingPricingRuleId());
        }
    }

    @Override
    public boolean isOrderItemShippingPricingRule(long orderItemId, long ruleId) {
        return nextShopOrderItemDao.isOrderItemShippingPricingRule(orderItemId,ruleId);
    }

    @Override
    public ShippingPricingRule getOrderItemShippingPricingRule(long orderItemId) {
        return shippingPricingRuleDao.getOrderItemShippingPricingRule(orderItemId);
    }

}
