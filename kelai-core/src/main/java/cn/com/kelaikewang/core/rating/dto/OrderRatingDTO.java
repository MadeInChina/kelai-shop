package cn.com.kelaikewang.core.rating.dto;

import java.io.Serializable;
import java.util.List;

public class OrderRatingDTO implements Serializable {
    private Long orderId;
    private List<OrderItemRatingDTO> orderItemRates;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemRatingDTO> getOrderItemRates() {
        return orderItemRates;
    }

    public void setOrderItemRates(List<OrderItemRatingDTO> orderItemRates) {
        this.orderItemRates = orderItemRates;
    }
    public  OrderItemRatingDTO getOrderItemProductRating(long productId){
        return orderItemRates.stream().filter(item-> item.getProductId().equals(String.valueOf(productId))).findFirst().get();
    }
}
