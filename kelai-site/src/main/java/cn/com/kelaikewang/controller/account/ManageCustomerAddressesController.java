/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.kelaikewang.controller.account;

import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.web.controller.account.BroadleafManageCustomerAddressesController;
import org.broadleafcommerce.profile.core.domain.Country;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.broadleafcommerce.profile.core.domain.Phone;
import org.broadleafcommerce.profile.core.domain.State;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/account/addresses")
public class ManageCustomerAddressesController extends BroadleafManageCustomerAddressesController {

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }
    
    @ModelAttribute("states")
    protected List<State> populateStates() {
        return super.populateStates();
    }
    
    @ModelAttribute("countries")
    protected List<Country> populateCountries() {
        return super.populateCountries();
    }
    
    @ModelAttribute("customerAddresses")
    protected List<CustomerAddress> populateCustomerAddresses() {
        return super.populateCustomerAddresses();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String viewCustomerAddresses(HttpServletRequest request, Model model) {

        ZaoJiCMSCustomerAddressForm customerAddressForm = new ZaoJiCMSCustomerAddressForm();
        model.addAttribute("customerAddressForm", customerAddressForm);
        return this.getCustomerAddressesView();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addCustomerAddress(HttpServletRequest request, Model model, @ModelAttribute("customerAddressForm") ZaoJiCMSCustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        return super.addCustomerAddress(request, model, form, result, redirectAttributes);
    }
    
    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.GET)
    public String viewCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId) {
        return super.viewCustomerAddress(request, model, customerAddressId);
    }

    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST)
    public String updateCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId, @ModelAttribute("customerAddressForm") ZaoJiCMSCustomerAddressForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        //return super.updateCustomerAddress(request, model, customerAddressId, form, result, redirectAttributes);
        this.customerAddressValidator.validate(form, result);
        if (result.hasErrors()) {
            return this.getCustomerAddressesView();
        } else {
            if (form.getAddress().getPhonePrimary() != null && StringUtils.isEmpty(form.getAddress().getPhonePrimary().getPhoneNumber())) {
                form.getAddress().setPhonePrimary((Phone)null);
            }

            if (form.getAddress().getPhoneSecondary() != null && StringUtils.isEmpty(form.getAddress().getPhoneSecondary().getPhoneNumber())) {
                form.getAddress().setPhoneSecondary((Phone)null);
            }

            if (form.getAddress().getPhoneFax() != null && StringUtils.isEmpty(form.getAddress().getPhoneFax().getPhoneNumber())) {
                form.getAddress().setPhoneFax((Phone)null);
            }

            CustomerAddress customerAddress = this.customerAddressService.readCustomerAddressById(customerAddressId);
            if (customerAddress == null) {
                throw new IllegalArgumentException("Customer Address not found with the specified customerAddressId");
            } else {
                this.validateCustomerOwnedData(customerAddress);
                //customerAddress.setAddress(form.getAddress());
                customerAddress.setAddressName(form.getAddressName());
                customerAddress = this.customerAddressService.saveCustomerAddress(customerAddress);
                if (form.getAddress().isDefault()) {
                    this.customerAddressService.makeCustomerAddressDefault(customerAddress.getId(), customerAddress.getCustomer().getId());
                }
                addressService.saveAddress(form.getAddress());
                redirectAttributes.addFlashAttribute("successMessage", this.getAddressUpdatedMessage());
                return this.getCustomerAddressesRedirect();
            }
        }
    }

    @RequestMapping(value = "/{customerAddressId}", method = RequestMethod.POST, params="removeAddress=Remove")
    public String removeCustomerAddress(HttpServletRequest request, Model model, @PathVariable("customerAddressId") Long customerAddressId, RedirectAttributes redirectAttributes) {
        return super.removeCustomerAddress(request, model, customerAddressId, redirectAttributes);
    }

    @Override
    public String getAddressAddedMessage() {
        return "收货地址新增成功";
    }

    @Override
    public String getAddressUpdatedMessage() {
        return "收货地址更新成功";
    }

    @Override
    public String getAddressRemovedMessage() {
        return "收货地址删除成功";
    }

    @Override
    public String getAddressRemovedErrorMessage() {
        //Address could not be removed as it is in use
        return "被使用的地址不能删除";
    }
}
