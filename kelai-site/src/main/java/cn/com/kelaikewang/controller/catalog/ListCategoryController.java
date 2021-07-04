package cn.com.kelaikewang.controller.catalog;


import cn.com.kelaikewang.dto.ZaoJiCMSMenuItemDTO;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.dto.MenuItemDTO;
import org.broadleafcommerce.menu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ListCategoryController {

    @Resource(name = "zjcmsMenuService")
    private MenuService menuService;

    private static final List<MenuItemDTO> EMPTY_MENU_ITEMS = new ArrayList<>();

    @RequestMapping("/catalog/listCategory")
    public String listCategory(HttpServletRequest request, Model model){

        List<MenuItemDTO> menuItems = null;
        Menu menu = menuService.findMenuByName("Category Nav");
        if (menu == null){
            menuItems = EMPTY_MENU_ITEMS;
        }else {
            menuItems = menuService.constructMenuItemDTOsForMenu(menu);
        }

        model.addAttribute("menuItems",menuItems);
        Map<Long,List<MenuItemDTO>> submenuMap = new HashMap<>();

        for (MenuItemDTO menuItem : menuItems){
            ZaoJiCMSMenuItemDTO youZanPuZiMenuItemDTO = (ZaoJiCMSMenuItemDTO)menuItem;
            submenuMap.put(youZanPuZiMenuItemDTO.getId(),menuItem.getSubmenu());
        }
        model.addAttribute("submenuMap",submenuMap);
        return "catalog/listCategory";
    }
}
