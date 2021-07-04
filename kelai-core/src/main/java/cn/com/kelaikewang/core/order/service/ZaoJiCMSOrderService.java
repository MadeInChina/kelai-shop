package cn.com.kelaikewang.core.order.service;

import cn.com.kelaikewang.core.order.service.type.OrderListStatus;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.profile.core.domain.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface ZaoJiCMSOrderService extends OrderService {
    PageOfItems<ZaoJiCMSOrder> findOrderPageForCustomer(Customer customer, OrderListStatus orderListStatus, Integer pageIndex, Integer pageSize);
    void saveOrderShippingAddress(long addressId,long orderId);
    List<StatisticItemDTO<BigDecimal>> getLast12MonthsOrderTransactionAmount();
    List<StatisticItemDTO<BigDecimal>> getOrderTransactionAmountByDateRange();
    Long readCountOfOrderByStatus(OrderStatus orderStatus);
    void refundProcessing(long orderId);
    void save(ZaoJiCMSOrder order);
    ResponseDTO orderWriteOff(long writeOffCode, long userId);
    ZaoJiCMSOrder getByWriteOffCode(long code);
    PageOfItems<ZaoJiCMSOrder> getPageOfOrderWriteOff(long userId,int pageIndex,int pageSize);
    long getCountOfOrderWriteOff(long userId);
}
