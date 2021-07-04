package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import org.broadleafcommerce.cms.file.domain.StaticAsset;
import org.broadleafcommerce.cms.file.service.StaticAssetService;
import org.broadleafcommerce.cms.file.service.StaticAssetStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("account")
public class UploadController {
    @Resource(name = "blStaticAssetStorageService")
    protected StaticAssetStorageService staticAssetStorageService;

    @Resource(name = "blStaticAssetService")
    protected StaticAssetService staticAssetService;

    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO upload(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam("files[]") MultipartFile file) throws IOException {
        StaticAsset staticAsset = staticAssetService.createStaticAssetFromFile(file,null);
        staticAssetStorageService.createStaticAssetStorageFromFile(file, staticAsset);

        String staticAssetUrlPrefix = staticAssetService.getStaticAssetUrlPrefix();
        if (staticAssetUrlPrefix != null && !staticAssetUrlPrefix.startsWith("/")) {
            staticAssetUrlPrefix = "/" + staticAssetUrlPrefix;
        }
        String assetUrl =  staticAssetUrlPrefix + staticAsset.getFullUrl();
        return ResponseDTO.success("上传成功",assetUrl);
    }
}
