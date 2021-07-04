package cn.com.kelaikewang.core.rating.service;

import cn.com.kelaikewang.core.rating.dto.OrderRatingDTO;

public interface ZaoJiCMSRatingService {
    void submitOrderRating(OrderRatingDTO orderRating);
    OrderRatingDTO getOrderRating(long orderId);
    void updateOrderRating(OrderRatingDTO orderRating);
    //ReviewDetail reviewOrderItem(String orderItemId,String productId, RatingType type, Customer customer, Double rating, String reviewText);
    //ReviewDetail readReviewByOrderItemId(long orderItemId);
}
