package cn.com.kelaikewang.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/account/actionMsg")
public class ActionMessageController {
    @RequestMapping("success")
    public String success(Model model, HttpServletRequest request){
        model.addAttribute("successMessage",request.getSession().getAttribute("successMessage"));
        return "account/actionMsg/success";
    }
}
