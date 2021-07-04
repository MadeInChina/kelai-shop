package cn.com.kelaikewang.admin.controller.catalog;

import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProductImpl;
import org.broadleafcommerce.admin.web.controller.entity.AdminProductController;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.ClassTree;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ZaoJiCMSAdminProductController extends AdminProductController {


    @Override
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        return super.viewEntityList(request, response, model, pathVars, requestParams);
    }

    @Override
    public String updateCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @PathVariable("collectionField") String collectionField, @PathVariable("collectionItemId") String collectionItemId, @ModelAttribute("entityForm") EntityForm entityForm, BindingResult result) throws Exception {
        return super.updateCollectionItem(request, response, model, pathVars, id, collectionField, collectionItemId, entityForm, result);
    }

    @Override
    public String updateCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @PathVariable("collectionField") String collectionField, @PathVariable("collectionItemId") String collectionItemId, @ModelAttribute("entityForm") EntityForm entityForm, @PathVariable("alternateId") String alternateId, BindingResult result) throws Exception {
        return super.updateCollectionItem(request,response,model,pathVars,id,collectionField,collectionItemId,entityForm,alternateId,result);
    }

    @Override
    public String saveEntityJson(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @ModelAttribute("entityForm") EntityForm entityForm, BindingResult result, RedirectAttributes ra) throws Exception {
        return super.saveEntityJson(request, response, model, pathVars, id, entityForm, result, ra);
    }

    @Override
    public String saveEntity(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @ModelAttribute("entityForm") EntityForm entityForm, BindingResult result, RedirectAttributes ra) throws Exception {
        return super.saveEntity(request, response, model, pathVars, id, entityForm, result, ra);
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
