package cn.com.kelaikewang.core.order.dao;

import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.service.type.OrderListStatus;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.profile.core.domain.Customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ZaoJiCMSOrderDao extends org.broadleafcommerce.core.order.dao.OrderDao {
    List<ZaoJiCMSOrder> findOrderPageForCustomer(Customer customer, OrderListStatus orderListStatus, Integer pageIndex, Integer pageSize);
    long readCount(Customer customer,OrderListStatus orderListStatus);
    ZaoJiCMSOrder getById(long orderId);
    BigDecimal readOrderTransactionAmountByDateRange(Date start,Date end);
    Long readNumberOfOrderByStatus(OrderStatus orderStatus);
    BigDecimal readTransactionAmountOfSubmitedOrder();
    Long readCountOfOrderByStatus(OrderStatus orderStatus);
    ZaoJiCMSOrder getByWriteOffCode(long writeOffCode);

    List<ZaoJiCMSOrder> getPageOfOrderWriteOff(long userId,int pageIndex,int pageSize);
    long getCountOfOrderWriteOff(long userId);
}
