package cn.com.kelaikewang.admin.controller.cms;

import org.broadleafcommerce.cms.admin.web.controller.AdminAssetController;

import java.util.Map;

public class ZaoJiCMSAdminAssetController extends AdminAssetController {
    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        return "assets";
    }
}
