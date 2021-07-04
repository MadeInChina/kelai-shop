package cn.com.kelaikewang.core.order.dao;

import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderItem;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderItemImpl;
import org.broadleafcommerce.core.order.dao.OrderItemDaoImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;


public class ZaoJiCMSOrderItemDaoImpl extends OrderItemDaoImpl implements ZaoJiCMSOrderItemDao {
    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Override
    public void createOrderItemShippingPricingRule(long orderItemId, Long ruleId) {
        em.createNativeQuery("INSERT INTO ZJCMS_ORDER_ITEM(ORDER_ITEM_ID,SHIPPING_PRICING_RULE_ID) VALUE (:orderItemId,:ruleId)")
                .setParameter("orderItemId",orderItemId)
                .setParameter("ruleId",ruleId)
                .executeUpdate();
    }

    @Override
    public void updateOrderItemShippingPricingRule(long orderItemId, Long ruleId) {
        em.createNativeQuery("UPDATE ZJCMS_ORDER_ITEM SET SHIPPING_PRICING_RULE_ID = :ruleId  WHERE ORDER_ITEM_ID = :orderItemId")
                .setParameter("orderItemId",orderItemId)
                .setParameter("ruleId",ruleId)
                .executeUpdate();
        em.flush();
    }

    @Override
    public boolean isOrderItemShippingPricingRuleExists(long orderItemId) {
        BigInteger count = (BigInteger)em.createNativeQuery("SELECT COUNT(*) FROM ZJCMS_ORDER_ITEM WHERE ORDER_ITEM_ID = :orderItemId")
                .setParameter("orderItemId",orderItemId)
                .getSingleResult();
        return count != null && count.longValue() > 0;
    }

    @Override
    public boolean isOrderItemShippingPricingRule(long orderItemId,Long ruleId) {
        BigInteger count = null;
        if (ruleId == null){
            count = (BigInteger)em.createNativeQuery("SELECT COUNT(*) FROM ZJCMS_ORDER_ITEM WHERE ORDER_ITEM_ID = :orderItemId AND SHIPPING_PRICING_RULE_ID is null")
                    .setParameter("orderItemId",orderItemId)
                    .getSingleResult();
        }else {
            count = (BigInteger)em.createNativeQuery("SELECT COUNT(*) FROM ZJCMS_ORDER_ITEM WHERE ORDER_ITEM_ID = :orderItemId AND SHIPPING_PRICING_RULE_ID = :ruleId")
                    .setParameter("orderItemId",orderItemId)
                    .setParameter("ruleId",ruleId)
                    .getSingleResult();
        }

        return count != null && count.longValue() > 0;
    }

    @Override
    public ZaoJiCMSOrderItem getOrderItem(long orderItemId) {
        return em.find(ZaoJiCMSOrderItemImpl.class,orderItemId);
    }


}
