package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomer;
import org.broadleafcommerce.cms.file.domain.StaticAsset;
import org.broadleafcommerce.cms.file.service.StaticAssetService;
import org.broadleafcommerce.cms.file.service.StaticAssetStorageService;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.web.controller.account.BroadleafUpdateAccountController;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ZaoJiCMSUpdateAccountController extends BroadleafUpdateAccountController {
    @Resource(name = "blUserDetailsService")
    private UserDetailsService userDetailsService;

    @Resource(name = "blStaticAssetStorageService")
    protected StaticAssetStorageService staticAssetStorageService;

    @Resource(name = "blStaticAssetService")
    protected StaticAssetService staticAssetService;

    protected String UpdateAccountProfilePhotoView = "account/updateAccountProfilePhoto";
    protected String UpdateAccountProfilePhotoMessage = "头像已经更新成功";

    @RequestMapping(method = RequestMethod.GET)
    public String viewUpdateAccount(HttpServletRequest request, Model model, @ModelAttribute("updateAccountForm") ZaoJiCMSUpdateAccountForm form) {
        return this.zaojiCMSViewUpdateAccount(request, model, form);
    }

    public String zaojiCMSViewUpdateAccount(HttpServletRequest request, Model model, ZaoJiCMSUpdateAccountForm form) {
        ZaoJiCMSCustomer customer = (ZaoJiCMSCustomer)CustomerState.getCustomer();
        form.setEmailAddress(customer.getEmailAddress());
        form.setFirstName(customer.getFirstName());
        form.setLastName(customer.getLastName());
        form.setNickname(customer.getNickname());
        model.addAttribute("profilePhoto",customer.getProfilePhoto());
        return this.getUpdateAccountView();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processUpdateAccount(HttpServletRequest request, Model model, @ModelAttribute("updateAccountForm") ZaoJiCMSUpdateAccountForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        //return super.processUpdateAccount(request, model, form, result, redirectAttributes);
        model.addAttribute("mobile", CustomerState.getCustomer().getUsername());
        this.updateAccountValidator.validate(form, result);
        if (result.hasErrors()) {
            return this.getUpdateAccountView();
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                ZaoJiCMSCustomer customer = (ZaoJiCMSCustomer)CustomerState.getCustomer();
                //customer.setEmailAddress(form.getEmailAddress());
                customer.setFirstName(form.getFirstName());
                customer.setLastName(form.getLastName());
                customer.setNickname(form.getNickname());
                if (this.useEmailForLogin) {
                    customer.setUsername(form.getEmailAddress());
                }

                customer = (ZaoJiCMSCustomer)this.customerService.saveCustomer(customer);
                if (this.useEmailForLogin) {
                    UserDetails principal = this.userDetailsService.loadUserByUsername(customer.getUsername());
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), auth.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                }

                redirectAttributes.addFlashAttribute("successMessage", this.getAccountUpdatedMessage());
                return this.getAccountRedirectView();
            } else {
                throw new AuthenticationCredentialsNotFoundException("Authentication was null, not authenticated, or not logged in.");
            }
        }
    }
    @RequestMapping(value = "updateAccountProfilePhoto",method = RequestMethod.GET)
    public String viewUpdateAccountProfilePhoto(Model model){
        ZaoJiCMSCustomer cmsCustomer = (ZaoJiCMSCustomer)CustomerState.getCustomer();
        model.addAttribute("profilePhoto",cmsCustomer.getProfilePhoto());
        return getUpdateAccountProfilePhotoView();
    }
    @RequestMapping(value = "updateAccountProfilePhoto",method = RequestMethod.POST)
    public String processUpdateAccountProfilePhoto(Model model,
                                                   HttpServletRequest request,
                                                   @RequestParam("avatar") MultipartFile file) throws IOException, ServletException {
        //Part file = request.getPart("avatar");
        //InputStream fileStream = file.getInputStream();

        StaticAsset staticAsset = staticAssetService.createStaticAssetFromFile(file,null);
        staticAssetStorageService.createStaticAssetStorageFromFile(file, staticAsset);

        String staticAssetUrlPrefix = staticAssetService.getStaticAssetUrlPrefix();
        if (staticAssetUrlPrefix != null && !staticAssetUrlPrefix.startsWith("/")) {
            staticAssetUrlPrefix = "/" + staticAssetUrlPrefix;
        }
        String assetUrl =  staticAssetUrlPrefix + staticAsset.getFullUrl();

        ZaoJiCMSCustomer cmsCustomer = (ZaoJiCMSCustomer)CustomerState.getCustomer();
        cmsCustomer.setProfilePhoto(assetUrl);
       customerService.saveCustomer(cmsCustomer);

        model.addAttribute("updateAccountProfilePhotoMessage",getUpdateAccountProfilePhotoMessage());
        return getUpdateAccountProfilePhotoView();
    }

    @Override
    public String getAccountUpdatedMessage() {
        return "更新成功";
    }
    protected String getUpdateAccountProfilePhotoView(){
        return UpdateAccountProfilePhotoView;
    }
    protected String getUpdateAccountProfilePhotoMessage(){
        return UpdateAccountProfilePhotoMessage;
    }
}
