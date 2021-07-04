package cn.com.kelaikewang.admin.service;

import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.catalog.dto.ProductDTO;
import cn.com.kelaikewang.core.catalog.dto.ProductFilterDTO;

public interface ProductFilterService {
    PageOfItems<ProductDTO> filter(ProductFilterDTO productFilterDTO);
}
