package cn.com.kelaikewang.core.search.service;

import org.broadleafcommerce.common.exception.ServiceException;

import java.io.IOException;

public interface SolrIndexCleanupService {

    public void rebuildIndexAtStartupIfNecessary() throws ServiceException, IOException;

}