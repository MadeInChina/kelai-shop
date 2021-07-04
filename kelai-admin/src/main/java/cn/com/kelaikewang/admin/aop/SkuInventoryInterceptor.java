package cn.com.kelaikewang.admin.aop;

import cn.com.kelaikewang.core.inventory.service.InventoryNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.broadleafcommerce.openadmin.web.form.entity.Field;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
@Aspect
public class SkuInventoryInterceptor {

    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "zjcmsInventoryNotificationService")
    private InventoryNotificationService inventoryNotificationService;

    @Pointcut("execution(* cn.com.kelaikewang.admin.controller.catalog.ZaoJiCMSAdminProductController.updateCollectionItem(..)) || execution(* cn.com.kelaikewang.admin.controller.catalog.ZaoJiCMSAdminProductController.saveEntityJson(..))")
    private void adjustInventoryPointcut(){

    }
    @Before(value="adjustInventoryPointcut()")
    public void beforeAdjustInventory(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(joinPoint.getSignature().getName().equals("updateCollectionItem") && args[5].equals("additionalSkus")) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();

            Long collectionItemId = Long.valueOf(args[6].toString());
            Sku sku = catalogService.findSkuById(collectionItemId);
            request.setAttribute("original_quantity_available", sku.getQuantityAvailable());
            System.out.println("sku 调整前....");
        }else if(joinPoint.getSignature().getName().equals("saveEntityJson")){
            System.out.println("default sku 调整前....");
            long productId = Long.parseLong(args[4].toString());
            Product product = catalogService.findProductById(productId);
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            request.setAttribute("original_quantity_available",product.getDefaultSku().getQuantityAvailable());
        }
    }
    @After(value="adjustInventoryPointcut()")
    public void afterAdjustInventory(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(joinPoint.getSignature().getName().equals("updateCollectionItem") && args[5].equals("additionalSkus")) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Integer originalQuantityAvailable = (Integer) request.getAttribute("original_quantity_available");

            EntityForm entityForm = (EntityForm) args[7];
            Field field = entityForm.getFields().get("quantityAvailable");
            long productId = Long.parseLong(args[4].toString());

            notification(field,productId,originalQuantityAvailable);
            System.out.println("sku 调整后....");
        }else if(joinPoint.getSignature().getName().equals("saveEntityJson")){
            System.out.println("default sku 调整后....");
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Integer originalQuantityAvailable = (Integer) request.getAttribute("original_quantity_available");
            EntityForm entityForm = (EntityForm) args[5];
            Field field = entityForm.getFields().get("defaultSku.quantityAvailable");
            long productId = Long.parseLong(args[4].toString());
            notification(field,productId,originalQuantityAvailable);
        }
    }
    private void notification(Field quantityAvailableField,long productId,Integer originalQuantityAvailable){
        Integer newQuantityAvailable = null;
        if (quantityAvailableField != null && !StringUtils.isEmpty(quantityAvailableField.getValue())) {
            newQuantityAvailable = Integer.valueOf(quantityAvailableField.getValue());
        }

        if (originalQuantityAvailable == null || originalQuantityAvailable <= 0 && newQuantityAvailable != null && newQuantityAvailable > 0) {
            Product product = catalogService.findProductById(productId);

            List<Sku> skus = product.getAdditionalSkus();
            boolean skuAvailable = false;

            for (Sku sku : skus){
                if (sku.getQuantityAvailable() != null && sku.getQuantityAvailable() >0){
                    skuAvailable = true;
                    break;
                }
            }
            if (!skuAvailable && skus.size() == 0){
                Sku defaultSku = product.getDefaultSku();
                if (defaultSku != null){
                    if (defaultSku.getQuantityAvailable() != null && defaultSku.getQuantityAvailable() > 0){
                        skuAvailable = true;
                    }
                }
            }
            if (skuAvailable) {
                inventoryNotificationService.notification(productId);
            }

        }
    }
}
