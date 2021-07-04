package cn.com.kelaikewang.core.marketing.service;

import cn.com.kelaikewang.core.marketing.dao.OfferCodeGroupDao;
import cn.com.kelaikewang.core.marketing.domain.OfferCodeGroup;
import cn.com.kelaikewang.infrastructure.id.uid.UidGenerator;
import org.broadleafcommerce.core.offer.dao.OfferCodeDao;
import org.broadleafcommerce.core.offer.domain.OfferCode;
import org.broadleafcommerce.core.offer.domain.OfferCodeImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("zjcmsOfferCodeGroupService")
public class OfferCodeGroupServiceImpl implements OfferCodeGroupService {

    @Resource(name = "zjcmsOfferCodeGroupDao")
    private OfferCodeGroupDao offerCodeGroupDao;

    @Resource(name = "offerCodeGenerator")
    private UidGenerator uidGenerator;

    @Resource(name = "blOfferCodeDao")
    private OfferCodeDao offerCodeDao;

    @Override
    public OfferCodeGroup generateOfferCode(OfferCodeGroup offerCodeGroup) {
        OfferCodeGroup response = offerCodeGroupDao.create(offerCodeGroup);
        for (int i=0;i<offerCodeGroup.getNumberOfCodesGenerated();i++){
            OfferCode offerCode = new OfferCodeImpl();
            offerCode.setEndDate(offerCodeGroup.getDefaultEndDate());
            offerCode.setMaxUses(offerCodeGroup.getMaxUses());
            offerCode.setOffer(offerCodeGroup.getOffer());
            offerCode.setOfferCode(String.valueOf(uidGenerator.getUID()));
            offerCode.setStartDate(offerCodeGroup.getDefaultActiveDate());
            offerCode.setUses(0);

            offerCodeDao.save(offerCode);

        }
        return response;
    }
}
