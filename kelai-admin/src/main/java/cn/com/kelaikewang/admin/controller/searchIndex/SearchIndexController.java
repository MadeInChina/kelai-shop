package cn.com.kelaikewang.admin.controller.searchIndex;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.search.service.solr.index.SolrIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("searchIndex")
public class SearchIndexController {
    @Autowired
    protected SolrIndexService sis;

    @RequestMapping(value = "rebuildIndex",method = RequestMethod.GET)
    public String viewRebuildIndex(Model model){
        model.addAttribute("templateName", "views/rebuildIndex");
        return "layout/fullPageLayout";
    }
    @RequestMapping(value = "rebuildIndex",method = RequestMethod.POST)
    public String rebuildIndex(Model model)  {
        try {
            sis.rebuildIndex();
            model.addAttribute("rebuildIndexStatus","SUCCESS");
        } catch (ServiceException e) {
            e.printStackTrace();
            model.addAttribute("rebuildIndexStatus","FAILED");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("rebuildIndexStatus","FAILED");
        }
        model.addAttribute("templateName", "views/rebuildIndex");
        return "layout/fullPageLayout";
    }
}
