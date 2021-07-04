package cn.com.kelaikewang.core.order.dao;

import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderImpl;
import cn.com.kelaikewang.core.order.service.type.OrderListStatus;
import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import org.broadleafcommerce.common.util.dao.TQOrder;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.broadleafcommerce.core.order.dao.OrderDaoImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.profile.core.domain.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ZaoJiCMSOrderDaoImpl extends OrderDaoImpl implements ZaoJiCMSOrderDao {
    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;
    @Override
    public List<ZaoJiCMSOrder> findOrderPageForCustomer(Customer customer, OrderListStatus orderListStatus, Integer pageIndex, Integer pageSize) {
        TypedQueryBuilder<ZaoJiCMSOrder> typedQueryBuilder = new TypedQueryBuilder<>(ZaoJiCMSOrder.class, "ord")
                .addRestriction("ord.customer.id", "=", customer.getId());
        if (orderListStatus != null){
            if (orderListStatus.getType().equals(OrderListStatus.AFTER_SALES_SERVICE.getType())){
                typedQueryBuilder.addRestriction("ord.status", "in", Arrays.asList(
                        ZaoJiCMSOrderStatus.REFUND_PROCESSING.getType(),
                        ZaoJiCMSOrderStatus.REFUND_REJECTED.getType(),
                        ZaoJiCMSOrderStatus.REFUNDED.getType()
                ));
            }else {
                if (orderListStatus.getType().equals(OrderListStatus.RECEIVED.getType())){
                    typedQueryBuilder.addRestriction("ord.submittedRate", "!=", true);
                }else {
                    typedQueryBuilder.addRestriction("ord.status", "=", orderListStatus.getType());
                }
            }
        }else {
            typedQueryBuilder.addRestriction("ord.status","<>",OrderStatus.IN_PROCESS.getType());
        }
        typedQueryBuilder.addOrder(new TQOrder("id",false));
        TypedQuery<ZaoJiCMSOrder>  typedQuery = typedQueryBuilder.toQuery(em).setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    @Override
    public long readCount(Customer customer, OrderListStatus orderListStatus) {
        TypedQueryBuilder<Order> countQueryBuilder =  new TypedQueryBuilder<Order>(Order.class, "ord")
                .addRestriction("ord.customer.id", "=", customer.getId());
        if (orderListStatus != null) {
            if (orderListStatus.getType().equals(OrderListStatus.AFTER_SALES_SERVICE.getType())){
                countQueryBuilder.addRestriction("ord.status", "in", Arrays.asList(
                        ZaoJiCMSOrderStatus.REFUND_PROCESSING.getType(),
                        ZaoJiCMSOrderStatus.REFUND_REJECTED.getType(),
                        ZaoJiCMSOrderStatus.REFUNDED.getType()
                ));
            }else {
                if (orderListStatus.getType().equals(OrderListStatus.RECEIVED.getType())){
                    countQueryBuilder.addRestriction("ord.submittedRate", "!=", true);
                }else {
                    countQueryBuilder.addRestriction("ord.status", "=", orderListStatus.getType());
                }
            }
        }else {
            countQueryBuilder.addRestriction("ord.status","<>",OrderStatus.IN_PROCESS.getType());
        }

        return countQueryBuilder.toCountQuery(em).getSingleResult();
    }

    @Override
    public ZaoJiCMSOrder getById(long orderId) {
        return em.find(ZaoJiCMSOrderImpl.class,orderId);
    }

    @Override
    public BigDecimal readOrderTransactionAmountByDateRange(Date start, Date end) {
        return em.createNamedQuery("BC_READ_ORDER_TRANSACTION_AMOUNT_BY_DATE_RANGE",BigDecimal.class)
                .setParameter("start",start)
                .setParameter("end",end).getSingleResult();
    }

    @Override
    public Long readNumberOfOrderByStatus(OrderStatus orderStatus) {
        TypedQuery<Long> query = new TypedQueryBuilder<Order>(Order.class, "ord")
                .addRestriction("ord.status", "=", orderStatus.getType())
                .toCountQuery(em);
        return query.getSingleResult();
    }

    @Override
    public BigDecimal readTransactionAmountOfSubmitedOrder() {
        return em.createNamedQuery("BC_READ_TRAN_AMOUNT_OF_SUBMITTED_ORDER",BigDecimal.class).getSingleResult();
    }

    @Override
    public Long readCountOfOrderByStatus(OrderStatus orderStatus) {
        return em.createNamedQuery("BC_READ_COUNT_OF_ORDER_BY_STATUS",Long.class)
                .setParameter("status",orderStatus.getType())
                .getSingleResult();
    }

    @Override
    public ZaoJiCMSOrder getByWriteOffCode(long writeOffCode) {
        List<ZaoJiCMSOrder> orders = em.createNamedQuery("BC_READ_ORDER_BY_WRITE_OFF_CODE",ZaoJiCMSOrder.class)
                .setParameter("writeOffCode",writeOffCode).getResultList();
        if (orders == null || orders.size() == 0){
            return null;
        }
        return orders.get(0);
    }

    @Override
    public List<ZaoJiCMSOrder> getPageOfOrderWriteOff(long userId, int pageIndex, int pageSize) {
         TypedQuery<ZaoJiCMSOrder> typedQueryBuilder = new TypedQueryBuilder<>(ZaoJiCMSOrder.class, "ord")
                .addRestriction("ord.writeOffByUserId", "=", userId)
                 .addOrder(new TQOrder("ord.writeOffDate",false))
                .toQuery(em)
                .setFirstResult((pageIndex-1)*pageSize)
                .setMaxResults(pageSize);
         return typedQueryBuilder.getResultList();
    }

    @Override
    public long getCountOfOrderWriteOff(long userId) {
        return new TypedQueryBuilder<>(ZaoJiCMSOrder.class, "ord")
                .addRestriction("ord.writeOffByUserId", "=", userId)
                .toCountQuery(em).getSingleResult();
    }
}
