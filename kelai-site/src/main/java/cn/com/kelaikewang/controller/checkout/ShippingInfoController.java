/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.kelaikewang.controller.checkout;

import cn.com.kelaikewang.controller.checkout.stage.ZaoJiCMSCheckoutStageType;
import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.checkout.model.OrderMultishipOptionForm;
import org.broadleafcommerce.core.web.checkout.model.PaymentInfoForm;
import org.broadleafcommerce.core.web.checkout.model.ShippingInfoForm;
import org.broadleafcommerce.core.web.checkout.service.CheckoutFormService;
import org.broadleafcommerce.core.web.checkout.stage.CheckoutStageType;
import org.broadleafcommerce.core.web.controller.checkout.BroadleafShippingInfoController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerAddress;
import org.broadleafcommerce.profile.core.domain.Phone;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ShippingInfoController extends BroadleafShippingInfoController {

    @Resource(name = "blCheckoutFormService")
    protected CheckoutFormService checkoutFormService;

    //@Resource(name = "blShippingInfoFormValidator")
    //private ShippingInfoFormValidator shippingInfoFormValidator;

    @RequestMapping(value="/checkout/singleship", method = RequestMethod.GET)
    public String convertToSingleship(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        return super.convertToSingleship(request, response, model);
    }

    @RequestMapping(value="/checkout/singleship", method = RequestMethod.POST)
    public String saveSingleShip(HttpServletRequest request, HttpServletResponse response, Model model,
                                 @ModelAttribute("shippingInfoForm") ZaoJiCMSShippingInfoForm shippingInfoForm,
                                 BindingResult result)
            throws PricingException, ServiceException {
        //super.saveSingleShip(request, response, model, shippingInfoForm, result);
        processSaveSingleShip(request, response, model, shippingInfoForm, result);

        Order cart = CartState.getCart();
        PaymentInfoForm paymentInfoForm = new PaymentInfoForm();
        model.addAttribute("paymentInfoForm", paymentInfoForm);

        checkoutFormService.prePopulatePaymentInfoForm(paymentInfoForm, shippingInfoForm, cart);

        if (!result.hasErrors()) {
            checkoutFormService.prePopulateShippingInfoForm(shippingInfoForm, cart);
            checkoutFormService.determineIfSavedAddressIsSelected(model, shippingInfoForm, paymentInfoForm);
        }

        String nextActiveStage = ZaoJiCMSCheckoutStageType.SUBMIT_ORDER.getType();
        if (result.hasErrors()) {
            nextActiveStage = CheckoutStageType.SHIPPING_INFO.getType();
        } else if (cartStateService.cartHasThirdPartyPayment()) {
            nextActiveStage = CheckoutStageType.REVIEW.getType();
        }

        model.addAttribute(ACTIVE_STAGE, nextActiveStage);
        return getCheckoutStagesPartial();
    }


    public String processSaveSingleShip(HttpServletRequest request, HttpServletResponse response, Model model, ShippingInfoForm shippingForm, BindingResult result) throws PricingException, ServiceException {

        Order cart = CartState.getCart();
        if (shippingForm.shouldUseBillingAddress()) {
            this.copyBillingAddressToShippingAddress(cart, shippingForm);
        }

        this.addressService.populateAddressISOCountrySub(shippingForm.getAddress());
        this.shippingInfoFormValidator.validate(shippingForm, result);
        if (result.hasErrors()) {
            return this.getCheckoutView();
        } else {
            if (shippingForm.getAddress().getPhonePrimary() != null && StringUtils.isEmpty(shippingForm.getAddress().getPhonePrimary().getPhoneNumber())) {
                shippingForm.getAddress().setPhonePrimary((Phone)null);
            }

            if (shippingForm.getAddress().getPhoneSecondary() != null && StringUtils.isEmpty(shippingForm.getAddress().getPhoneSecondary().getPhoneNumber())) {
                shippingForm.getAddress().setPhoneSecondary((Phone)null);
            }

            if (shippingForm.getAddress().getPhoneFax() != null && StringUtils.isEmpty(shippingForm.getAddress().getPhoneFax().getPhoneNumber())) {
                shippingForm.getAddress().setPhoneFax((Phone)null);
            }

            Customer customer = CustomerState.getCustomer();
            if (!customer.isAnonymous() && shippingForm.isSaveAsDefault()){
                shippingForm.getAddress().setDefault(true);
            }
            Address address = null;
            if (shippingForm.getAddress().getId() == null || shippingForm.getAddress().getId() <=0) {
                address = this.addressService.saveAddress(shippingForm.getAddress());
                if (!customer.isAnonymous() ) {
                    CustomerAddress customerAddress = this.customerAddressService.create();
                    customerAddress.setAddress(address);
                    customerAddress.setAddressName(shippingForm.getAddressName());
                    customerAddress.setCustomer(customer);
                    this.customerAddressService.saveCustomerAddress(customerAddress);
                    //this.customerAddressService.makeCustomerAddressDefault(customerAddress.getId(), customer.getId());
                }
            }else {
                address = this.addressService.saveAddress(shippingForm.getAddress());
            }


            FulfillmentGroup shippableFulfillmentGroup = this.fulfillmentGroupService.getFirstShippableFulfillmentGroup(cart);
            if (shippableFulfillmentGroup != null) {
                shippableFulfillmentGroup.setAddress(address);
                shippableFulfillmentGroup.setPersonalMessage(shippingForm.getPersonalMessage());
                shippableFulfillmentGroup.setDeliveryInstruction(shippingForm.getDeliveryMessage());
                FulfillmentOption fulfillmentOption = this.fulfillmentOptionService.readFulfillmentOptionById(shippingForm.getFulfillmentOptionId());
                shippableFulfillmentGroup.setFulfillmentOption(fulfillmentOption);
                cart = this.orderService.save(cart, true);
            }

            (this.checkoutControllerExtensionManager.getProxy()).performAdditionalShippingAction();
            if (this.isAjaxRequest(request)) {
                (this.checkoutControllerExtensionManager.getProxy()).addAdditionalModelVariables(model);
                return this.getCheckoutView();
            } else {
                return this.getCheckoutPageRedirect();
            }
        }
    }


    @RequestMapping(value = "/checkout/multiship", method = RequestMethod.GET)
    public String showMultiship(HttpServletRequest request, HttpServletResponse response, Model model,
                                @ModelAttribute("orderMultishipOptionForm") OrderMultishipOptionForm orderMultishipOptionForm,
                                BindingResult result) throws PricingException {
        return super.showMultiship(request, response, model);
    }

    @RequestMapping(value = "/checkout/multiship", method = RequestMethod.POST)
    public String saveMultiship(HttpServletRequest request, HttpServletResponse response, Model model,
                                @ModelAttribute("orderMultishipOptionForm") OrderMultishipOptionForm orderMultishipOptionForm,
                                BindingResult result) throws PricingException, ServiceException {
        return super.saveMultiship(request, response, model, orderMultishipOptionForm, result);
    }

    @RequestMapping(value = "/checkout/add-address", method = RequestMethod.GET)
    public String showMultishipAddAddress(HttpServletRequest request, HttpServletResponse response, Model model,
                                          @ModelAttribute("addressForm") ZaoJiCMSShippingInfoForm addressForm, BindingResult result) {
        return super.showMultishipAddAddress(request, response, model);
    }

    @RequestMapping(value = "/checkout/add-address", method = RequestMethod.POST)
    public String saveMultishipAddAddress(HttpServletRequest request, HttpServletResponse response, Model model,
                                          @ModelAttribute("addressForm") ZaoJiCMSShippingInfoForm addressForm, BindingResult result) throws ServiceException {
        return super.saveMultishipAddAddress(request, response, model, addressForm, result);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }

}
