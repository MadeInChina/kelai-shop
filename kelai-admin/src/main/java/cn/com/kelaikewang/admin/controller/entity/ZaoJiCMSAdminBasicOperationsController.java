package cn.com.kelaikewang.admin.controller.entity;

import org.apache.commons.lang3.StringEscapeUtils;
import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.openadmin.dto.*;
import org.broadleafcommerce.openadmin.server.domain.PersistencePackageRequest;
import org.broadleafcommerce.openadmin.web.controller.AdminBasicOperationsControllerExtensionHandler;
import org.broadleafcommerce.openadmin.web.controller.entity.AdminBasicOperationsController;
import org.broadleafcommerce.openadmin.web.controller.modal.ModalHeaderType;
import org.broadleafcommerce.openadmin.web.form.component.ListGrid;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ZaoJiCMSAdminBasicOperationsController extends AdminBasicOperationsController {
    @Override
    public String showSelectCollectionItem(HttpServletRequest request,
                                           HttpServletResponse response,
                                           Model model,
                                           @PathVariable Map<String, String> pathVars,
                                           @PathVariable("owningClass") String owningClass,
                                           @PathVariable("collectionField") String collectionField,
                                           @RequestParam(required = false) String requestingEntityId,
                                           @RequestParam(defaultValue = "false") boolean dynamicField,
                                           @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        List<SectionCrumb> sectionCrumbs = this.getSectionCrumbs(request, (String)null, (String)null);
        String validatedClass = this.getClassNameForSection(owningClass);
        PersistencePackageRequest ppr = this.getSectionPersistencePackageRequest(validatedClass, requestParams, sectionCrumbs, pathVars);
        ppr.addCustomCriteria("requestingEntityId=" + requestingEntityId);
        ppr.addCustomCriteria("owningClass=" + owningClass);
        ppr.addCustomCriteria("requestingField=" + collectionField);
        ClassMetadata mainMetadata = this.service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = null;
        FieldMetadata md = null;
        if (!collectionField.contains("|") && !dynamicField) {
            collectionProperty = (Property)mainMetadata.getPMap().get(collectionField);
            md = collectionProperty.getMetadata();
            ppr = PersistencePackageRequest.fromMetadata((FieldMetadata)md, sectionCrumbs);
        } else {
            md = new BasicFieldMetadata();
            ((FieldMetadata)md).setFriendlyName(mainMetadata.getPolymorphicEntities().getFriendlyName());
            collectionProperty = new Property();
            collectionProperty.setMetadata((FieldMetadata)md);
        }

        ppr.addFilterAndSortCriteria(this.getCriteria(requestParams));
        ppr.setStartIndex(this.getStartIndex(requestParams));
        ppr.setMaxIndex(this.getMaxIndex(requestParams));
        ppr.removeFilterAndSortCriteria("requestingEntityId");
        ppr.addCustomCriteria("requestingEntityId=" + requestingEntityId);
        ppr.addCustomCriteria("owningClass=" + owningClass);
        ppr.addCustomCriteria("requestingField=" + collectionField);
        this.modifyFetchPersistencePackageRequest(ppr, pathVars);
        ClassMetadata targetClassMetadata = this.service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        ExtensionResultStatusType extensionResultStatusType = ((AdminBasicOperationsControllerExtensionHandler)this.extensionManager.getProxy()).buildLookupListGrid(ppr, targetClassMetadata, ppr.getCeilingEntityClassname(), sectionCrumbs, model, requestParams);
        if (extensionResultStatusType.equals(ExtensionResultStatusType.NOT_HANDLED)) {
            DynamicResultSet drs = this.service.getRecords(ppr).getDynamicResultSet();
            ListGrid listGrid = null;
            if (!collectionField.contains("|") && !dynamicField) {
                if (md instanceof BasicFieldMetadata) {
                    listGrid = this.formService.buildCollectionListGrid((String)null, drs, collectionProperty, owningClass, sectionCrumbs);
                    listGrid.removeAllRowActions();
                }
            } else {
                listGrid = this.formService.buildMainListGrid(drs, mainMetadata, "/" + owningClass, sectionCrumbs);
                listGrid.setListGridType(ListGrid.Type.TO_ONE);
                listGrid.setSubCollectionFieldName(collectionField);
                listGrid.setPathOverride("/" + owningClass + "/" + collectionField + "/select");
            }

            listGrid.setFriendlyName(StringEscapeUtils.escapeEcmaScript(listGrid.getFriendlyName()));
            listGrid.setJsonFieldName(StringEscapeUtils.escapeEcmaScript(listGrid.getJsonFieldName()));
            model.addAttribute("listGrid", listGrid);
        }

        model.addAttribute("viewType", "modal/simpleSelectEntity");
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", ModalHeaderType.SELECT_COLLECTION_ITEM.getType());
        model.addAttribute("collectionProperty", collectionProperty);
        model.addAttribute("sectionCrumbs", request.getParameter("sectionCrumbs"));
        this.setModelAttributes(model, owningClass);

        if (targetClassMetadata.getCeilingType().equals("org.broadleafcommerce.core.catalog.domain.CategoryImpl")){
            model.addAttribute("viewType", "category/modal/simpleSelectCategory");
            return  "modules/selectCategoryItem";
        }else {
            return "modules/modalContainer";
        }
    }
}
