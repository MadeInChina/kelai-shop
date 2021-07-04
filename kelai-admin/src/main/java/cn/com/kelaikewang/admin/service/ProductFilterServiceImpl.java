package cn.com.kelaikewang.admin.service;

import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.catalog.dto.ProductDTO;
import cn.com.kelaikewang.core.catalog.dto.ProductFilterDTO;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service("zjcmsProductFilterService")
public class ProductFilterServiceImpl implements ProductFilterService {
    @Resource(name = "blCatalogService")
    private CatalogService catalogService;
    @PersistenceContext(unitName="blPU")
    protected EntityManager em;
    @Override
    public PageOfItems<ProductDTO> filter(ProductFilterDTO productFilterDTO) {
        Long categoryId = productFilterDTO.getCategoryId();
        String keyword = productFilterDTO.getKeyword();
        Integer pageIndex = productFilterDTO.getPageIndex();

        if (pageIndex == null){
            pageIndex = 0;
        }else {
            pageIndex = pageIndex - 1;
        }
        List<Product> products;
        BigInteger itemCount ;
        if (categoryId != null){
            //Long categoryId = Long.valueOf(categoryIdParameter);
            Category category = catalogService.findCategoryById(categoryId);
            products = catalogService.findActiveProductsByCategory(category,productFilterDTO.getPageSize(),pageIndex * productFilterDTO.getPageSize());
            itemCount = (BigInteger)em.createNativeQuery("select count(*) FROM BLC_CATEGORY_PRODUCT_XREF WHERE CATEGORY_ID = :categoryId")
                    .setParameter("categoryId",categoryId)
                    .getSingleResult();
        }else if (!StringUtils.isEmpty(keyword)){
            //products = //catalogService.findProductsByName(keyword,10,pageIndex * 10);
            String like = "%" + keyword.trim() + "%";
            TypedQuery<Product> query = this.em.createNamedQuery("BC_READ_PRODUCTS_BY_NAME", Product.class);
            query.setParameter("name", like);
            query.setFirstResult(pageIndex * productFilterDTO.getPageSize());
            query.setMaxResults(productFilterDTO.getPageSize());
            query.setHint("org.hibernate.cacheable", true);
            query.setHint("org.hibernate.cacheRegion", "query.Catalog");
            products = query.getResultList();

            //SELECT product FROM org.broadleafcommerce.core.catalog.domain.Product product
            //            WHERE product.defaultSku.name LIKE :name
            //            AND (product.archiveStatus.archived IS NULL OR product.archiveStatus.archived = 'N')
            //            ORDER BY product.id
            itemCount = (BigInteger)em.createNativeQuery("SELECT\n" +
                    "\tCOUNT( BLC_PRODUCT.PRODUCT_ID ) \n" +
                    "FROM\n" +
                    "\tBLC_PRODUCT\n" +
                    "\tINNER JOIN BLC_SKU ON BLC_SKU.DEFAULT_PRODUCT_ID = BLC_PRODUCT.PRODUCT_ID \n" +
                    "\tAND BLC_PRODUCT.DEFAULT_SKU_ID = BLC_SKU.SKU_ID \n" +
                    "WHERE\n" +
                    "\tBLC_SKU.`NAME` like :keyword")
                    .setParameter("keyword",like)
                    .getSingleResult();
        }else {
            products = catalogService.findAllProducts(productFilterDTO.getPageSize(),pageIndex * productFilterDTO.getPageSize());
            itemCount = (BigInteger)em.createNativeQuery("SELECT count(*) from BLC_PRODUCT")
                    .getSingleResult();
        }
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (products != null){
            for (Product product : products){
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(product.getId());
                productDTO.setImage(product.getMedia().get("primary").getUrl());
                if (product.getSalePrice() == null){
                    productDTO.setPrice(product.getRetailPrice().getAmount());
                }else {
                    productDTO.setPrice(product.getSalePrice().getAmount());
                }

                productDTO.setName(product.getName());
                productDTO.setUrl(product.getUrl());
                productDTOS.add(productDTO);
            }
        }
        PageOfItems<ProductDTO> pageOfItems = new PageOfItems<>();
        pageOfItems.setRecordCount(itemCount.longValue());
        pageOfItems.setPageSize(productFilterDTO.getPageSize());
        pageOfItems.setPageIndex(productFilterDTO.getPageIndex());
        pageOfItems.setItems(productDTOS);
        return pageOfItems;
    }
}
