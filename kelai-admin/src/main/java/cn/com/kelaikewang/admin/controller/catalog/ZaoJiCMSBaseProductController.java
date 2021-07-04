package cn.com.kelaikewang.admin.controller.catalog;

import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProductImpl;
import cn.com.kelaikewang.core.catalog.service.ZaoJiCMSProductService;
import org.apache.commons.collections.CollectionUtils;
import org.broadleafcommerce.admin.web.controller.entity.AdminBaseProductController;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.broadleafcommerce.openadmin.dto.*;
import org.broadleafcommerce.openadmin.server.domain.PersistencePackageRequest;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.RestrictionType;
import org.broadleafcommerce.openadmin.web.form.component.ListGrid;
import org.broadleafcommerce.openadmin.web.form.entity.Field;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZaoJiCMSBaseProductController extends AdminBaseProductController {

    @Resource(name = "zjcmsProductService")
    protected ZaoJiCMSProductService nextShopProductService;

    @Override
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        String sectionKey = this.getSectionKey(pathVars);
        String sectionClassName = this.getClassNameForSection(sectionKey);
        List<SectionCrumb> crumbs = this.getSectionCrumbs(request, null, null);
        PersistencePackageRequest ppr = this.getSectionPersistencePackageRequest(sectionClassName, requestParams, crumbs, pathVars);

        ClassMetadata cmd = this.service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        DynamicResultSet drs = this.service.getRecords(ppr).getDynamicResultSet();
        ListGrid listGrid = this.formService.buildMainListGrid(drs, cmd, sectionKey, crumbs);
        listGrid.setSelectType(ListGrid.SelectType.NONE);
        Set<Field> headerFields = listGrid.getHeaderFields();
        if (CollectionUtils.isNotEmpty(headerFields)) {
            Field firstField = headerFields.iterator().next();
            if (requestParams.containsKey(firstField.getName())) {
                model.addAttribute("mainSearchTerm", ((List)requestParams.get(firstField.getName())).get(0));
            }
        }

        model.addAttribute("viewType", "entityList");
        this.setupViewEntityListBasicModel(request, cmd, sectionKey, sectionClassName, model, requestParams);
        model.addAttribute("listGrid", listGrid);
        return "modules/defaultContainer";
    }

    @Override
    protected void modifyCriteria(Map<String, FilterAndSortCriteria> fasMap) {
        super.modifyCriteria(fasMap);
    }

    @Override
    protected PersistencePackageRequest getSectionPersistencePackageRequest(String sectionClassName, List<SectionCrumb> sectionCrumbs, Map<String, String> pathVars) {
        return super.getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars);
    }

    @Override
    protected PersistencePackageRequest getSectionPersistencePackageRequest(String sectionClassName, MultiValueMap<String, String> requestParams, List<SectionCrumb> sectionCrumbs, Map<String, String> pathVars) {
        //return super.getSectionPersistencePackageRequest(sectionClassName, requestParams, sectionCrumbs, pathVars);
        FilterAndSortCriteria[] fascs = this.getCriteria(requestParams);
        FilterAndSortCriteria productBunbleIdFilterAndSortCriteria = new FilterAndSortCriteria("id",1);
        productBunbleIdFilterAndSortCriteria.setRestrictionType(RestrictionType.LONG_NOT_EQUAL);

        List<Long> allProductBunbleId = nextShopProductService.readAllProductBundleId();
        List<String> productBunbleFilterValues = new ArrayList<>();

        for (Long id : allProductBunbleId){
            productBunbleFilterValues.add(id.toString());
        }
        productBunbleIdFilterAndSortCriteria.setFilterValues(productBunbleFilterValues);

        FilterAndSortCriteria[] newfascs = new FilterAndSortCriteria[fascs.length + 1];
        int newfascsLastIndex = newfascs.length - 1;
        for (int i=0;i<newfascs.length;i++){
            if (i != newfascsLastIndex){
                newfascs[i] = fascs[i];
            }else {
                newfascs[i] = productBunbleIdFilterAndSortCriteria;
            }
        }
        String[] sectionCriteria = this.customCriteriaService.mergeSectionCustomCriteria(sectionClassName, this.getSectionCustomCriteria());
        PersistencePackageRequest ppr = PersistencePackageRequest.standard()
                .withCeilingEntityClassname(sectionClassName)
                .withCustomCriteria(sectionCriteria)
                .withFilterAndSortCriteria(newfascs)
                .withStartIndex(this.getStartIndex(requestParams))
                .withMaxIndex(this.getMaxIndex(requestParams))
                .withSectionCrumbs(sectionCrumbs)
                .withLastId(this.getLastId(requestParams))
                .withFirstId(this.getFirstId(requestParams))
                .withUpperCount(this.getUpperCount(requestParams))
                .withLowerCount(this.getLowerCount(requestParams))
                .withPageSize(this.getPageSize(requestParams))
                .withPresentationFetch(true);
        this.attachSectionSpecificInfo(ppr, pathVars);
        return ppr;
    }
    @Override
    protected void setupViewEntityListBasicModel(HttpServletRequest request, ClassMetadata cmd, String sectionKey, String sectionClassName, Model model, MultiValueMap<String, String> requestParams) {
        super.setupViewEntityListBasicModel(request, cmd, sectionKey, sectionClassName, model, requestParams);
        List<ClassTree> classTrees = (List<ClassTree>)model.asMap().get("entityTypes");
        ClassTree removeClassTree = null;
        for (ClassTree classTree : classTrees){
            if (classTree.getFullyQualifiedClassname().equals(ProductImpl.class.getName())){
                removeClassTree = classTree;
                break;
            }
        }
        classTrees.remove(removeClassTree);
        classTrees.sort((o1, o2) -> {
            if (o1.getFullyQualifiedClassname().equals(ZaoJiCMSProductImpl.class.getName())){
                return -1;
            }
            return 1;
        });
        model.addAttribute("entityTypes", classTrees);
    }
}
