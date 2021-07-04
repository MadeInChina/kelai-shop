package cn.com.kelaikewang.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountIndexController {
    @RequestMapping("account/index")
    public String accountIndex(){
        return "account/index";
    }
}
