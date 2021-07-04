package cn.com.kelaikewang.core.inventory.service;

import cn.com.kelaikewang.core.config.SiteConfigProperties;
import cn.com.kelaikewang.core.inventory.dao.InventoryNotificationDao;
import cn.com.kelaikewang.core.inventory.domain.InventoryNotification;
import cn.com.kelaikewang.core.inventory.domain.InventoryNotificationImpl;
import cn.com.kelaikewang.core.inventory.domain.InventoryNotificationStatus;
import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("zjcmsInventoryNotificationService")
@Transactional("blTransactionManager")
public class InventoryNotificationServiceImpl implements InventoryNotificationService {

    @Resource(name = "zjcmsInventoryNotificationDao")
    private InventoryNotificationDao inventoryNotificationDao;

    @Resource(name = "blEmailService")
    private EmailService emailService;

    @Resource(name = "blInventoryNotificationEmailInfo")
    private EmailInfo emailInfo;

    @Resource(name = "blCustomerService")
    private CustomerService customerService;

    @Autowired
    private SiteConfigProperties siteConfigProperties;

    @Resource(name = "blCatalogService")
    private CatalogService catalogService;

    @Async("asyncServiceExecutor")
    @Override
    public void notification(long productId) {
        List<InventoryNotification> inventoryNotifications = inventoryNotificationDao.readAllUnprocessedByProductId(productId);
        notificationInternal(inventoryNotifications,productId);
    }
    @Async("asyncServiceExecutor")
    @Override
    public void retryNotification(long productId) {

        List<InventoryNotification> inventoryNotifications = inventoryNotificationDao.readAllFailByProductIdAndTimesLessThan(productId,3);
        notificationInternal(inventoryNotifications,productId);

    }
    private void notificationInternal(List<InventoryNotification> inventoryNotifications,Long productId){
        Product product = catalogService.findProductById(productId);
        for (InventoryNotification inventoryNotification : inventoryNotifications) {
            inventoryNotification.setNotificationDateTime(new Date());
            inventoryNotification.setNotificationTimes(inventoryNotification.getNotificationTimes() + 1);
            try {
                Customer customer = customerService.readCustomerById(inventoryNotification.getCustomerId());
                Map<String, Object> data = new HashMap<>();
                data.put("siteDomain", siteConfigProperties.getSiteDomain());
                data.put("siteName", siteConfigProperties.getSiteName());
                data.put("dateTime", new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date()));
                data.put("product", product);
                if (emailService.sendTemplateEmail(customer.getEmailAddress(), emailInfo, data)) {
                    inventoryNotification.setNotificationStatus(InventoryNotificationStatus.SUCCESS);
                } else {
                    inventoryNotification.setNotificationStatus(InventoryNotificationStatus.FAIL);
                }
            } catch (Exception e) {
                inventoryNotification.setNotificationStatus(InventoryNotificationStatus.FAIL);
            }
            inventoryNotificationDao.update(inventoryNotification);
        }
    }

    @Override
    public void create(long productId, long customerId) {
        InventoryNotification dbInventoryNotification = inventoryNotificationDao.readByProductIdAndCustomerIdAndStatus(productId,customerId,InventoryNotificationStatus.UNPROCESSED.getType());
        if(dbInventoryNotification == null) {
            InventoryNotification inventoryNotification = new InventoryNotificationImpl();
            inventoryNotification.setNotificationTimes(0);
            inventoryNotification.setNotificationStatus(InventoryNotificationStatus.UNPROCESSED);
            inventoryNotification.setCreated(new Date());
            inventoryNotification.setCustomerId(customerId);
            inventoryNotification.setProductId(productId);

            inventoryNotificationDao.create(inventoryNotification);
        }
    }
}
