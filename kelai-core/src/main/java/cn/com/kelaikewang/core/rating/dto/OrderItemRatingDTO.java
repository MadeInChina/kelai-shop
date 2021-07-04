package cn.com.kelaikewang.core.rating.dto;

import java.io.Serializable;

public class OrderItemRatingDTO implements Serializable {
    //private String orderItemId;
    private String productId;
    protected Double rating;
    protected String reviewText;

/*    public String getOrderItemId() {
        return orderItemId;
    }*/

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

 /*   public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }*/

}
