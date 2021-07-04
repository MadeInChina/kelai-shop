package cn.com.kelaikewang.core.catalog.service;

import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;
import cn.com.kelaikewang.core.catalog.dao.ZaoJiCMSProductDao;
import cn.com.kelaikewang.core.catalog.dao.ProductColorOptionValueMediaDao;
import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProduct;
import cn.com.kelaikewang.core.catalog.domain.ProductColorOptionValueMedia;
import cn.com.kelaikewang.core.catalog.domain.ProductColorOptionValueMediaImpl;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.core.catalog.dao.ProductDao;
import org.broadleafcommerce.core.catalog.domain.ProductOption;
import org.broadleafcommerce.core.catalog.domain.ProductOptionValue;
import org.broadleafcommerce.core.catalog.domain.ProductOptionXref;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("zjcmsProductService")
public class ZaoJiCMSProductServiceImpl implements ZaoJiCMSProductService {

    @Resource(name = "zjcmsProductDao")
    private ZaoJiCMSProductDao nextShopProductDao;

    @Resource(name = "blProductDao")
    private ProductDao productDao;

    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "zjcmsProductColorOptionValueMediaDao")
    protected ProductColorOptionValueMediaDao productColorOptionValueMediaDao;

    @Override
    public List<Long> readAllProductBundleId() {
        return nextShopProductDao.readAllProductBundleId();
    }

    @Override
    public List<StatisticItemDTO<Long>> getTopSaleProducts(int top) {
        List<Object[]> topSaleProducts = nextShopProductDao.readTopSaleProduct(top);
        List<StatisticItemDTO<Long>> statisticItemDTOS = new ArrayList<>();
        for (Object[] objects : topSaleProducts){
            Long productId = (Long)objects[0];
            Long quantity = (Long)objects[1];
            String productName = (String)objects[2];
            if (StringUtils.isEmpty(productName)){
                productName = productDao.readProductById(productId).getName();
            }
            StatisticItemDTO<Long> dto = new StatisticItemDTO<>("productId_" + productId,productName,quantity);
            statisticItemDTOS.add(dto);
        }
        return statisticItemDTOS;
    }
    @Transactional("blTransactionManager")
    @Override
    public Integer generateColorOptionValueMedia(Long productId) {
        ZaoJiCMSProduct product = (ZaoJiCMSProduct)catalogService.findProductById(productId);
        int count = 0;
        if (product != null){
            List<ProductOptionXref> productOptions = product.getProductOptionXrefs();
            for (ProductOptionXref option : productOptions){
                ProductOption productOption = option.getProductOption();
                if(productOption.getType().getType().equals("COLOR")){
                    List<ProductOptionValue> optionValues = productOption.getAllowedValues();
                    for (ProductOptionValue optionValue : optionValues){
                        if (!productColorOptionValueMediaDao.isColorOptionValueMediaExists(productId,optionValue.getId())){
                            ProductColorOptionValueMedia optionValueMedia = new ProductColorOptionValueMediaImpl();
                            optionValueMedia.setProduct(product);
                            optionValueMedia.setProductOptionValue(optionValue);
                            productColorOptionValueMediaDao.create(optionValueMedia);
                            count++;
                        }
                    }
                    break;
                }
            }
        }
        return count;
    }
}
