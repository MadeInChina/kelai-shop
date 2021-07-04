package cn.com.kelaikewang.core.inventory.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ZJCMS_INVENTORY_NOTIFICATION")
public class InventoryNotificationImpl implements InventoryNotification {

    @Id
    @GeneratedValue(generator = "InventoryNotificationId")
    @GenericGenerator(
            name="InventoryNotificationId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="InventoryNotificationIdImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="InventoryNotificationImpl")
            }
    )
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "NOTIFICATION_STATUS")
    private String notificationStatus;

    @Column(name = "NOTIFICATION_DATE_TIME")
    private Date notificationDateTime;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "NITIFICATION_TIMES",nullable=false,columnDefinition="INT default 0")
    private Integer notificationTimes;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getCustomerId() {
        return customerId;
    }

    @Override
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public Long getProductId() {
        return productId;
    }

    @Override
    public void setProductId(Long id) {
        this.productId = id;
    }

    @Override
    public InventoryNotificationStatus getNotificationStatus() {
        return InventoryNotificationStatus.getInstance(notificationStatus);
    }

    @Override
    public void setNotificationStatus(InventoryNotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus.getType();
    }

    @Override
    public Date getNotificationDateTime() {
        return notificationDateTime;
    }

    @Override
    public void setNotificationDateTime(Date notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created =created;
    }

    @Override
    public Integer getNotificationTimes() {
        return notificationTimes;
    }

    @Override
    public void setNotificationTimes(Integer notificationTimes) {
        this.notificationTimes = notificationTimes;
    }
}
