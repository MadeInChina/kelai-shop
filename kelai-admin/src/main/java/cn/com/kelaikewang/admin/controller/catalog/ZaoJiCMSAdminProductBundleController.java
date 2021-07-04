package cn.com.kelaikewang.admin.controller.catalog;

import org.broadleafcommerce.admin.web.controller.entity.AdminBundleProductController;
import org.broadleafcommerce.common.extension.ExtensionResultHolder;
import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.core.catalog.domain.ProductBundle;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.SectionCrumb;
import org.broadleafcommerce.openadmin.server.domain.PersistencePackageRequest;
import org.broadleafcommerce.openadmin.web.controller.AdminAbstractControllerExtensionHandler;
import org.broadleafcommerce.openadmin.web.controller.modal.ModalHeaderType;
import org.broadleafcommerce.openadmin.web.form.component.ListGrid;
import org.broadleafcommerce.openadmin.web.form.component.ListGridAction;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class ZaoJiCMSAdminProductBundleController extends AdminBundleProductController {

    @Override
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id) throws Exception {
        String view = viewEntityFormInternal(request, response, model, pathVars, id);
        EntityForm form = (EntityForm)model.asMap().get("entityForm");
        ListGridAction generateSkusAction = (new ListGridAction("GEN_SKUS")).withDisplayText("Generate_Skus").withIconClass("icon-fighter-jet").withButtonClass("generate-skus").withUrlPostfix("/generate-skus").withActionUrlOverride("/product/" + id + "/additionalSkus/generate-skus");
        ListGrid skusGrid = form.findListGrid("additionalSkus");
        if (skusGrid != null) {
            skusGrid.setCanFilterAndSort(false);
        }

        ListGrid productOptionsGrid = form.findListGrid("productOptions");
        if (productOptionsGrid != null) {
            productOptionsGrid.addToolbarAction(generateSkusAction);
        }

        if (ProductBundle.class.isAssignableFrom(Class.forName(form.getEntityType()))) {
            form.removeListGrid("additionalSkus");
            form.removeListGrid("productOptions");
            form.removeField("canSellWithoutOptions");
        }

        form.removeListGrid("defaultSku.skuAttributes");
        return view;
    }
    private String viewEntityFormInternal(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id) throws Exception {
        String sectionKey = this.getSectionKey(pathVars);
        String sectionClassName = "org.broadleafcommerce.core.catalog.domain.Product";
        List<SectionCrumb> crumbs = this.getSectionCrumbs(request, sectionKey, id);
        PersistencePackageRequest ppr = this.getSectionPersistencePackageRequest(sectionClassName, crumbs, pathVars);
        ClassMetadata cmd = this.service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        Entity entity = this.service.getRecord(ppr, id, cmd, false).getDynamicResultSet().getRecords()[0];
        Map<String, DynamicResultSet> subRecordsMap = this.getViewSubRecords(request, pathVars, cmd, entity, crumbs);
        EntityForm entityForm = this.formService.createEntityForm(cmd, entity, subRecordsMap, crumbs);
        if (this.isAddRequest(entity)) {
            this.modifyAddEntityForm(entityForm, pathVars);
        } else {
            this.modifyEntityForm(entityForm, pathVars);
        }

        if (request.getParameter("headerFlash") != null) {
            model.addAttribute("headerFlash", request.getParameter("headerFlash"));
        }

        entityForm.setSectionKey(sectionKey);
        String originatingUri = (new UrlPathHelper()).getOriginatingRequestUri(request);
        int startIndex = request.getContextPath().length();
        String currentUrl = originatingUri.substring(startIndex);
        model.addAttribute("entity", entity);
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("currentUrl", currentUrl);
        this.setModelAttributes(model, sectionKey);
        this.addAuditableDisplayFields(entityForm);
        if (this.isAjaxRequest(request)) {
            entityForm.setReadOnly();
            model.addAttribute("viewType", "modal/entityView");
            model.addAttribute("modalHeaderType", ModalHeaderType.VIEW_ENTITY.getType());
            return "modules/modalContainer";
        } else {
            model.addAttribute("useAjaxUpdate", true);
            model.addAttribute("viewType", "entityEdit");
            return "modules/defaultContainer";
        }
    }
    private boolean isAddRequest(Entity entity) {
        ExtensionResultHolder<Boolean> resultHolder = new ExtensionResultHolder();
        ExtensionResultStatusType result = ((AdminAbstractControllerExtensionHandler)this.extensionManager.getProxy()).isAddRequest(entity, resultHolder);
        return result.equals(ExtensionResultStatusType.NOT_HANDLED) ? false : (Boolean)resultHolder.getResult();
    }
}
