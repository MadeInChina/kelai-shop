package cn.com.kelaikewang.core.logistics.service;

import cn.com.kelaikewang.core.logistics.dao.ExpressCompanyDao;
import cn.com.kelaikewang.core.logistics.domain.ExpressCompany;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsExpressCompanyService")
public class ExpressCompanyServiceImpl implements ExpressCompanyService {
    @Resource(name = "zjcmsExpressCompanyDao")
    private ExpressCompanyDao expressCompanyDao;
    @Override
    public List<ExpressCompany> listAll() {
        return expressCompanyDao.listAll();
    }
}
