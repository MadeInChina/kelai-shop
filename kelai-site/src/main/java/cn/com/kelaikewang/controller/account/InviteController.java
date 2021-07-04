package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.commons.zxing.ZxingUtils;
import cn.com.kelaikewang.infrastructure.encryption.AesEncryptUtils;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
@RequestMapping("account")
public class InviteController {
    @RequestMapping(value = "invite",method = RequestMethod.GET)
    public String invite(HttpServletRequest request,Model model) throws Exception {

        model.addAttribute("inviteUrl",getInviteUrl(request));
        return "account/invite";
    }
    @RequestMapping(value = "inviteQRCode",method = RequestMethod.GET)
    public void inviteQRCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        byte[] qrcodeBytes = ZxingUtils.generateQRCodeImage(getInviteUrl(request),300,300);
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrcodeBytes,0,qrcodeBytes.length);
        outputStream.flush();
        outputStream.close();
    }
    private String getInviteUrl(HttpServletRequest request) throws Exception {
        Customer customer = CustomerState.getCustomer();
        //model.addAttribute("customer", customer);

        StringBuffer url = request.getRequestURL();
        String inviteUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        if (!inviteUrl.endsWith("/")){
            inviteUrl += "/";
        }
        inviteUrl += "register?sid=" + AesEncryptUtils.encrypt(customer.getId().toString());
        return inviteUrl;
    }
}
