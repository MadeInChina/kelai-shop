package cn.com.kelaikewang.admin.controller.cms;

import org.broadleafcommerce.cms.admin.web.controller.AdminPageController;
import org.broadleafcommerce.openadmin.web.form.component.ListGrid;
import org.broadleafcommerce.openadmin.web.form.component.ListGridRecord;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.broadleafcommerce.openadmin.web.form.entity.EntityFormAction;
import org.broadleafcommerce.openadmin.web.form.entity.Field;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeSet;

public class ZaoJiCMSAdminPageController extends AdminPageController {

    @Override
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @RequestParam MultiValueMap<String, String> requestParams) throws Exception {

        String view = super.viewEntityList(request,response,model,pathVars,requestParams);
        ListGrid listGrid = (ListGrid)model.asMap().get("listGrid");
        TreeSet<Field> headerFields = (TreeSet<Field>)listGrid.getHeaderFields();
        Field field = new Field();
        field.setName("id");
        field.setFriendlyName("可视化设计页面");
        field.setMainEntityLink(false);
        field.setCanLinkToExternalEntity(false);
        field.setOrder(Integer.MAX_VALUE);
        field.setGridFieldComponentRenderer("contentbox");
        field.setFieldType("STRING");
        field.setOwningEntityClass("org.broadleafcommerce.cms.page.domain.PageImpl");
        headerFields.add(field);
        for (ListGridRecord record : listGrid.getRecords()){
            record.getFields().add(field);
        }
        return view;
    }

    @Override
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id) throws Exception {

        String view = super.viewEntityForm(request, response, model, pathVars, id);

        EntityForm ef = (EntityForm)model.asMap().get("entityForm");
        EntityFormAction entityFormAction = new EntityFormAction("EDIT_PAGE_IN_CONTENT_BOX");
        entityFormAction.setDisplayText("可视化设计页面");
        entityFormAction.setUrlOverride("/admin/nio-contentbuilder/contentbox.html?id=" + id);

        ef.addAction(entityFormAction);

        return view;
    }
}
