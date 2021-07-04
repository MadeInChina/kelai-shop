package cn.com.kelaikewang.core.marketing.service.handler;

import cn.com.kelaikewang.core.marketing.domain.OfferCodeGroup;
import cn.com.kelaikewang.core.marketing.domain.OfferCodeGroupImpl;
import cn.com.kelaikewang.core.marketing.service.OfferCodeGroupService;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.offer.dao.OfferDao;
import org.broadleafcommerce.openadmin.dto.*;
import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

@Component("zjcmsOfferCodeGroupCustomPersistenceHandler")
public class OfferCodeGroupCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    @Resource(name = "blOfferDao")
    private OfferDao offerDao;
    @Resource(name = "zjcmsOfferCodeGroupService")
    private OfferCodeGroupService offerCodeGroupService;

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[1-9]\\d*$");
    private static final Pattern CODE_PREFIX_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return OfferCodeGroup.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        //return super.add(persistencePackage, dynamicEntityDao, helper);
        Entity entity = persistencePackage.getEntity();
        OfferCodeGroup offerCodeGroup = new OfferCodeGroupImpl();
        Map<String, Property> propertyMap = entity.getPMap();

        String description = propertyMap.get("description").getValue();
        String offerId = propertyMap.get("offer").getValue();
        String numberToGenerate = propertyMap.get("numberToGenerate").getValue();
        String codePrefix = propertyMap.get("codePrefix").getValue();
        String maxUses = propertyMap.get("maxUses").getValue();
        String defaultActiveDate = propertyMap.get("defaultActiveDate").getValue();
        String defaultEndDate = propertyMap.get("defaultEndDate").getValue();

        if (StringUtils.isEmpty(description) ||
                StringUtils.isEmpty(offerId) ||
                !NUMBER_PATTERN.matcher(offerId).matches() ||
                StringUtils.isEmpty(numberToGenerate) ||
                !NUMBER_PATTERN.matcher(numberToGenerate).matches() ||
                (!StringUtils.isEmpty(codePrefix) && !CODE_PREFIX_PATTERN.matcher(codePrefix).matches()) ||
                StringUtils.isEmpty(maxUses) ||
                !NUMBER_PATTERN.matcher(maxUses).matches() ||
                StringUtils.isEmpty(defaultActiveDate)){
            throw new IllegalArgumentException("保存失败，请检查提交的数据是否正确");
        }



        offerCodeGroup.setCodePrefix(codePrefix);
        offerCodeGroup.setCreateDate(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        try {
            offerCodeGroup.setDefaultActiveDate(simpleDateFormat.parse(defaultActiveDate));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("生效开始日期无效");
        }
        if (!StringUtils.isEmpty(defaultEndDate)){
            try {
                offerCodeGroup.setDefaultEndDate(simpleDateFormat.parse(defaultEndDate));
            } catch (ParseException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("截止日期无效");
            }
        }

        int generated = Integer.valueOf(numberToGenerate);

        offerCodeGroup.setDescription(description);
        offerCodeGroup.setMaxUses(Integer.valueOf(maxUses));
        offerCodeGroup.setNumberOfCodesGenerated(111);
        offerCodeGroup.setNumberToGenerate(generated);
        offerCodeGroup.setOffer(offerDao.readOfferById(Long.valueOf(offerId)));
        offerCodeGroup.setNumberOfCodesGenerated(generated);
        offerCodeGroup.setState("成功");

        OfferCodeGroup response = offerCodeGroupService.generateOfferCode(offerCodeGroup);

        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();

        Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(OfferCodeGroup.class.getName(), persistencePerspective);

        return helper.getRecord(adminProperties, response, null, null);

    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return OfferCodeGroup.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return super.update(persistencePackage, dynamicEntityDao, helper);
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return OfferCodeGroup.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        super.remove(persistencePackage, dynamicEntityDao, helper);
    }
}
