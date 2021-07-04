package cn.com.kelaikewang.admin.controller.store;

import org.broadleafcommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller("zjcmsStoreController")
@RequestMapping("/" + StoreController.SECTION_KEY)
public class StoreController extends AdminBasicEntityController {
    public static final String SECTION_KEY = "store";

    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        String sectionKey = super.getSectionKey(pathVars);
        if (sectionKey != null) {
            return sectionKey;
        }
        return SECTION_KEY;
    }

    @Override
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        model.addAttribute("customScriptView","views/store/customScript/selectAdministrativeRegion");
        return super.viewEntityList(request, response, model, pathVars, requestParams);
    }

    @Override
    public String viewAddEntityForm(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @RequestParam(defaultValue = "") String entityType) throws Exception {
       // model.addAttribute("customScriptView","views/customScript/selectRegion");
        return super.viewAddEntityForm(request, response, model, pathVars, entityType);
    }

    @Override
    public String addEntity(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @ModelAttribute("entityForm") EntityForm entityForm, BindingResult result) throws Exception {

        String view = super.addEntity(request, response, model, pathVars, entityForm, result);
       /* if (result.hasErrors()){
            model.addAttribute("customScriptView","views/customScript/selectRegion");
        }*/
        return view;
    }

    @Override
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id) throws Exception {
        model.addAttribute("customScriptView","views/store/customScript/editStore");
        return super.viewEntityForm(request, response, model, pathVars, id);
    }

    @Override
    public String saveEntity(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @ModelAttribute("entityForm") EntityForm entityForm, BindingResult result, RedirectAttributes ra) throws Exception {
        return super.saveEntity(request, response, model, pathVars, id, entityForm, result, ra);
    }
}
