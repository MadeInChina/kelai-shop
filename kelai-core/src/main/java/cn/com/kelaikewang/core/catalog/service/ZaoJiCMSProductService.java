package cn.com.kelaikewang.core.catalog.service;

import cn.com.kelaikewang.core.report.dto.StatisticItemDTO;

import java.util.List;

public interface ZaoJiCMSProductService{
    List<Long> readAllProductBundleId();
    List<StatisticItemDTO<Long>> getTopSaleProducts(int top);
    Integer generateColorOptionValueMedia(Long productId);
}