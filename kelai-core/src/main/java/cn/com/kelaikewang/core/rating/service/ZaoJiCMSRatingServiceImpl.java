package cn.com.kelaikewang.core.rating.service;

import cn.com.kelaikewang.core.order.dao.ZaoJiCMSOrderDao;
import cn.com.kelaikewang.core.order.dao.ZaoJiCMSOrderItemDao;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import cn.com.kelaikewang.core.rating.dao.RatingDetailDao;
import cn.com.kelaikewang.core.rating.dao.ZaoJiCMSRatingSummaryDao;
import cn.com.kelaikewang.core.rating.dao.ZaoJiCMSReviewDetailDao;
import cn.com.kelaikewang.core.rating.dto.OrderItemRatingDTO;
import cn.com.kelaikewang.core.rating.dto.OrderRatingDTO;
import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.rating.domain.ReviewDetail;
import org.broadleafcommerce.core.rating.service.RatingServiceImpl;
import org.broadleafcommerce.core.rating.service.type.RatingType;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class ZaoJiCMSRatingServiceImpl extends RatingServiceImpl implements ZaoJiCMSRatingService {
    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService orderService;
    @Resource(name = "blOrderDao")
    protected ZaoJiCMSOrderDao zaoJiCMSOrderDao;
    @Resource(name = "blOrderItemDao")
    protected ZaoJiCMSOrderItemDao orderItemDao;
    @Resource(name = "blRatingSummaryDao")
    protected ZaoJiCMSRatingSummaryDao zaoJiCMSRatingSummaryDao;
    @Resource(name = "blReviewDetailDao")
    protected ZaoJiCMSReviewDetailDao reviewDetailDao;
    @Resource(name = "ratingDetailDao")
    protected RatingDetailDao ratingDetailDao;

    @Transactional("blTransactionManager")
    @Override
    public void submitOrderRating(OrderRatingDTO orderRating) {
        ZaoJiCMSOrder order = zaoJiCMSOrderDao.getById(orderRating.getOrderId());
        if (order.getConfirmReceiptTime() == null){
            throw new RuntimeException("未确认收货的订单不允许评论");
        }
        for (OrderItemRatingDTO itemRatingDTO : orderRating.getOrderItemRates()){
            reviewItem(itemRatingDTO.getProductId(),
                    RatingType.PRODUCT,
                    CustomerState.getCustomer(),
                    itemRatingDTO.getRating(),
                    itemRatingDTO.getReviewText());
        }
        order.setSubmittedRate(true);
        zaoJiCMSOrderDao.save(order);
    }

    @Override
    public OrderRatingDTO getOrderRating(long orderId) {
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        if (order.getSubmittedRate() != null && order.getSubmittedRate()){
            OrderRatingDTO orderRatingDTO = new OrderRatingDTO();
            orderRatingDTO.setOrderId(orderId);
            List<OrderItem> orderItems = order.getOrderItems();
            List<OrderItemRatingDTO> orderItemRatingDTOS = new ArrayList<>();
            for (OrderItem orderItem : orderItems){
                DiscreteOrderItem discreteOrderItem = (DiscreteOrderItem)orderItem;
                Long orderItemId = orderItem.getId();
                ReviewDetail reviewDetail = readReviewByCustomerAndItem(CustomerState.getCustomer(),discreteOrderItem.getProduct().getId().toString());
                OrderItemRatingDTO orderItemRatingDTO = new OrderItemRatingDTO();
                //orderItemRatingDTO.setOrderItemId(orderItemId.toString());
                orderItemRatingDTO.setProductId(discreteOrderItem.getProduct().getId().toString());
                orderItemRatingDTO.setRating(reviewDetail.getRatingDetail().getRating());
                orderItemRatingDTO.setReviewText(reviewDetail.getReviewText());
                orderItemRatingDTOS.add(orderItemRatingDTO);

            }
            orderRatingDTO.setOrderItemRates(orderItemRatingDTOS);
            return orderRatingDTO;
        }
        return null;
    }

    @Transactional("blTransactionManager")
    @Override
    public void updateOrderRating(OrderRatingDTO orderRating) {
        for (OrderItemRatingDTO itemRatingDTO : orderRating.getOrderItemRates()){
            ReviewDetail reviewDetail = readReviewByCustomerAndItem(CustomerState.getCustomer(),itemRatingDTO.getProductId());
            reviewDetail.setReviewText(itemRatingDTO.getReviewText());
            reviewDetail.getRatingDetail().setRating(itemRatingDTO.getRating());
            reviewDetailDao.saveReviewDetail(reviewDetail);
        }
    }
/*    @Transactional("blTransactionManager")
    @Override
    public ReviewDetail reviewOrderItem(String orderItemId, String productId, RatingType type, Customer customer, Double rating, String reviewText) {
        RatingSummary ratingSummary = this.zaoJiCMSRatingSummaryDao.readRatingSummary(productId,type);
        if (ratingSummary == null) {
            ratingSummary = zaoJiCMSRatingSummaryDao.createSummary(productId, type);
            zaoJiCMSRatingSummaryDao.create(ratingSummary);
            zaoJiCMSRatingSummaryDao.flush();
        }

        RatingDetail ratingDetail =  zaoJiCMSRatingSummaryDao.createDetail(ratingSummary, rating, SystemTime.asDate(), customer);
        //ratingSummary.getRatings().add(ratingDetail);
        ratingDetailDao.create(ratingDetail);
        ratingDetailDao.flush();

        ReviewDetail reviewDetail  = new ReviewDetailImpl(customer, SystemTime.asDate(), ratingDetail, reviewText, ratingSummary);
        reviewDetailDao.create(reviewDetail);
        reviewDetailDao.flush();
        //ratingSummary.getReviews().add(reviewDetail);


        // load reviews
        //ratingSummary.getReviews().size();


        //zaoJiCMSRatingSummaryDao.refresh(ratingSummary);

        ZaoJiCMSOrderItem orderItem = orderItemDao.getOrderItem(Long.parseLong(orderItemId));
        orderItem.setReviewDetail(reviewDetail);
        orderItemDao.save(orderItem);

        return reviewDetail;
    }*/
/*    @Transactional("blTransactionManager")
    @Override
    public ReviewDetail readReviewByOrderItemId(long orderItemId) {
        ZaoJiCMSOrderItem orderItem = orderItemDao.getOrderItem(orderItemId);
        return orderItem.getReviewDetail();
    }*/

/*    @Override
    public ReviewDetail readReviewByCustomerAndItemAndAdditionalId(Customer customer, String itemId, String additionalId) {
        return zaoJiCMSRatingSummaryDao.readReviewByCustomerAndItemAndAdditionalId(customer,itemId,additionalId);
    }*/
}
