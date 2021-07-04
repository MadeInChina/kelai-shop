package cn.com.kelaikewang.core.order.service;

import cn.com.kelaikewang.core.order.service.type.OrderListStatus;
import cn.com.kelaikewang.core.order.service.type.OrderWriteOffStatus;
import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import cn.com.kelaikewang.core.logistics.service.type.ConfirmReceiptType;
import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;
import cn.com.kelaikewang.commons.lang.DateUtils;
import cn.com.kelaikewang.core.order.dao.ZaoJiCMSOrderDao;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.profile.dao.ZaoJiCMSAddressDao;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddress;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import org.broadleafcommerce.common.time.SystemTime;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderServiceImpl;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ZaoJiCMSOrderServiceImpl extends OrderServiceImpl implements ZaoJiCMSOrderService {

    @Resource(name = "blOrderDao")
    private ZaoJiCMSOrderDao zaoJiCMSOrderDao;

    @Resource(name = "zjcmsAddressDao")
    private ZaoJiCMSAddressDao addressDao;

    @Transactional("blTransactionManager")
    @Override
    public PageOfItems<ZaoJiCMSOrder> findOrderPageForCustomer(Customer customer, OrderListStatus orderListStatus, Integer pageIndex, Integer pageSize) {
        List<ZaoJiCMSOrder> orders = zaoJiCMSOrderDao.findOrderPageForCustomer(customer,orderListStatus,pageIndex,pageSize);
        long recordCount = zaoJiCMSOrderDao.readCount(customer,orderListStatus);

        PageOfItems<ZaoJiCMSOrder> pageOfItems = new PageOfItems<>();
        pageOfItems.setItems(orders);
        pageOfItems.setPageIndex(pageIndex);
        pageOfItems.setPageSize(10);
        pageOfItems.setRecordCount(recordCount);

        return pageOfItems;
    }

    @Transactional("blTransactionManager")
    @Override
    public void saveOrderShippingAddress(long addressId, long orderId) {

        ZaoJiCMSOrder order = zaoJiCMSOrderDao.getById(orderId);
        ZaoJiCMSAddress address = addressDao.getById(addressId);
        order.setAddress(address.getAddressLine1());
        order.setProvince(address.getStateProvinceRegion());
        order.setProvinceCode(address.getProvinceCode());
        order.setCity(address.getCity());
        order.setCityCode(address.getCityCode());
        order.setRegion(address.getRegion());
        order.setRegionCode(address.getRegionCode());
        order.setStreet(address.getStreetCommunity());
        order.setStreetCode(address.getStreetCode());
        order.setFullName(address.getFullName());
        order.setMobile(address.getPhonePrimary().getPhoneNumber());
        order.setPostalCode(address.getPostalCode());

        zaoJiCMSOrderDao.save(order);
    }

    @Override
    public List<StatisticItemDTO<BigDecimal>> getLast12MonthsOrderTransactionAmount() {
        List<StatisticItemDTO<BigDecimal>> itemDTOS = new ArrayList<>();
        for (int i=-11,j=1;i<1;i++,j++) {
            Date now = new Date();
            Date ago = DateUtils.addMonths(now, i);
            Date start = DateUtils.getMonthBegin(ago);
            Date end = DateUtils.getMonthEnd(ago);

            BigDecimal totalAmount = zaoJiCMSOrderDao.readOrderTransactionAmountByDateRange(start,end);
            if (totalAmount == null){
                totalAmount = BigDecimal.ZERO;
            }
            itemDTOS.add(new StatisticItemDTO<>("th" + j + "MonthTotalAmount",DateUtils.formatDate(start,"yy年MM月"),totalAmount));
        }
        return itemDTOS;
    }

    @Override
    public List<StatisticItemDTO<BigDecimal>> getOrderTransactionAmountByDateRange() {
        Date today = new Date();
        Date todayBegin = DateUtils.getDayBegin(today);
        Date todayEnd = DateUtils.getDayEnd(today);
        BigDecimal todayAmount = zaoJiCMSOrderDao.readOrderTransactionAmountByDateRange(todayBegin,todayEnd);
        if(todayAmount == null){
            todayAmount = BigDecimal.ZERO;
        }

        Date currentWeekBegin = DateUtils.getFirstDayOfWeekStartTime();
        Date currentWeekEnd = DateUtils.getLastDayOfWeekEndTime();
        BigDecimal currentWeekAmount = zaoJiCMSOrderDao.readOrderTransactionAmountByDateRange(currentWeekBegin,currentWeekEnd);
        if (currentWeekAmount == null){
            currentWeekAmount = BigDecimal.ZERO;
        }

        Date currentMonthBegin = DateUtils.getMonthBegin(today);
        Date currentMonthEnd = DateUtils.getMonthEnd(today);
        BigDecimal currentMonthAmount = zaoJiCMSOrderDao.readOrderTransactionAmountByDateRange(currentMonthBegin,currentMonthEnd);
        if (currentMonthAmount == null){
            currentMonthAmount = BigDecimal.ZERO;
        }

        Date currentQuarterBegin = DateUtils.getQuarterStartTime();
        Date currentQuarterEnd = DateUtils.getQuarterEndTime();
        BigDecimal currentQuarterAmount = zaoJiCMSOrderDao.readOrderTransactionAmountByDateRange(currentQuarterBegin,currentQuarterEnd);
        if (currentQuarterAmount == null){
            currentQuarterAmount = BigDecimal.ZERO;
        }

        Date currentYearBegin = DateUtils.getCurrentYearStartTime();
        Date currentYearEnd = DateUtils.getCurrentYearEndTime();
        BigDecimal currentYearAmount = zaoJiCMSOrderDao.readOrderTransactionAmountByDateRange(currentYearBegin,currentYearEnd);
        if (currentYearAmount == null){
            currentYearAmount = BigDecimal.ZERO;
        }

        BigDecimal totalAmount = zaoJiCMSOrderDao.readTransactionAmountOfSubmitedOrder();
        if (totalAmount == null){
            totalAmount = BigDecimal.ZERO;
        }

        return Arrays.asList(
                new StatisticItemDTO<>("todayAmount","今日成交",todayAmount),
                new StatisticItemDTO<>("currentWeekAmount","本周成交",currentWeekAmount),
                new StatisticItemDTO<>("currentMonthAmount","本月成交",currentMonthAmount),
                new StatisticItemDTO<>("currentQuarterAmount","本季度成交",currentQuarterAmount),
                new StatisticItemDTO<>("currentYearAmount","本年度成交",currentYearAmount),
                new StatisticItemDTO<>("totalAmount","总成交额",totalAmount)
        );
    }

    @Override
    public Long readCountOfOrderByStatus(OrderStatus orderStatus) {
        Long result = zaoJiCMSOrderDao.readCountOfOrderByStatus(orderStatus);
        if (result == null){
            return 0L;
        }
        return result;
    }

    @Override
    public void refundProcessing(long orderId) {
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderDao.readOrderById(orderId);
        order.setStatus(ZaoJiCMSOrderStatus.REFUND_PROCESSING);
        orderDao.save(order);
    }

    @Override
    public void save(ZaoJiCMSOrder order) {
        orderDao.save(order);
    }

    @Transactional("blTransactionManager")
    @Override
    public ResponseDTO orderWriteOff(long writeOffCode, long userId) {

        ZaoJiCMSOrder order = zaoJiCMSOrderDao.getByWriteOffCode(writeOffCode);
        if (order == null){
            return ResponseDTO.fail("核销码不存在");
        }
        if (order.getPickedUpInStore() == null || !order.getPickedUpInStore()){
            return ResponseDTO.fail("不是自提订单");
        }
        if (OrderWriteOffStatus.WRITE_OFF.getType().equals(order.getWriteOffStatus())){
            return ResponseDTO.fail("该订单已经核销");
        }
        order.setWriteOffStatus(OrderWriteOffStatus.WRITE_OFF.getType());
        order.setWriteOffDate(new Date());
        order.setWriteOffByUserId(userId);
        order.setStatus(ZaoJiCMSOrderStatus.RECEIVED);
        order.setConfirmReceiptType(ConfirmReceiptType.IN_STORE);
        order.setConfirmReceiptTime(new Date());
        zaoJiCMSOrderDao.save(order);

        return ResponseDTO.success("核销成功");

    }

    @Override
    public ZaoJiCMSOrder getByWriteOffCode(long code) {
        return zaoJiCMSOrderDao.getByWriteOffCode(code);
    }

    @Override
    public PageOfItems<ZaoJiCMSOrder> getPageOfOrderWriteOff(long userId, int pageIndex, int pageSize) {
        List<ZaoJiCMSOrder> orders = zaoJiCMSOrderDao.getPageOfOrderWriteOff(userId,pageIndex,pageSize);
        PageOfItems<ZaoJiCMSOrder> pageOfItems = new PageOfItems<>();
        pageOfItems.setItems(orders);
        pageOfItems.setPageIndex(pageIndex);
        pageOfItems.setPageSize(pageSize);
        pageOfItems.setRecordCount(zaoJiCMSOrderDao.getCountOfOrderWriteOff(userId));
        return pageOfItems;
    }

    @Override
    public long getCountOfOrderWriteOff(long userId) {
        return zaoJiCMSOrderDao.getCountOfOrderWriteOff(userId);
    }

    @Override
    public Order confirmOrder(Order order) {
        order.setOrderNumber(determineOrderNumber(order));
        order.setSubmitDate(new Date());
        return super.confirmOrder(order);
    }
    protected String determineOrderNumber(Order order) {
        return (new SimpleDateFormat("yyyyMMddHHmmssS")).format(SystemTime.asDate()) + order.getId();
    }
}
