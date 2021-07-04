package cn.com.kelaikewang.service;


import cn.com.kelaikewang.dto.ZaoJiCMSMenuItemDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryXref;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.dto.MenuItemDTO;
import org.broadleafcommerce.menu.service.MenuServiceImpl;
import org.broadleafcommerce.menu.type.MenuItemType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("zjcmsMenuService")
public class ZaoJiCMSMenuServiceImpl extends MenuServiceImpl {
    @Override
    public List<MenuItemDTO> constructMenuItemDTOsForMenu(Menu menu) {
        List<MenuItemDTO> dtos = new ArrayList();
        if (CollectionUtils.isNotEmpty(menu.getMenuItems())) {
            Iterator var3 = menu.getMenuItems().iterator();

            while(var3.hasNext()) {
                MenuItem menuItem = (MenuItem)var3.next();
                MenuItemDTO menuItemDTO = this.convertMenuItemToDTO(menuItem);
                if (menuItemDTO != null) {
                    dtos.add(menuItemDTO);
                }
            }
        }

        return dtos;
    }
    @Override
    protected MenuItemDTO convertMenuItemToDTO(MenuItem menuItem) {
        ZaoJiCMSMenuItemDTO dto;
        if (MenuItemType.SUBMENU.equals(menuItem.getMenuItemType()) && menuItem.getLinkedMenu() != null) {
            dto = new ZaoJiCMSMenuItemDTO();
            dto.setId(menuItem.getId());
            dto.setUrl(menuItem.getDerivedUrl());
            dto.setLabel(menuItem.getDerivedLabel());
            List<MenuItemDTO> submenu = new ArrayList();
            List<MenuItem> items = menuItem.getLinkedMenu().getMenuItems();
            if (CollectionUtils.isNotEmpty(items)) {
                Iterator var5 = items.iterator();

                while(var5.hasNext()) {
                    MenuItem item = (MenuItem)var5.next();
                    submenu.add(this.convertMenuItemToDTO(item));
                }
            }

            dto.setSubmenu(submenu);
            return dto;
        } else if (MenuItemType.CATEGORY.equals(menuItem.getMenuItemType())) {
            Category category = this.catalogService.findCategoryByURI(menuItem.getActionUrl());
            return category != null ? this.convertCategoryToMenuItemDTO(category,menuItem) : null;
        } else {
            dto = new ZaoJiCMSMenuItemDTO();
            dto.setId(menuItem.getId());
            dto.setUrl(menuItem.getDerivedUrl());
            dto.setLabel(menuItem.getDerivedLabel());
            if (menuItem.getImageUrl() != null) {
                dto.setImageUrl(menuItem.getImageUrl());
                dto.setAltText(menuItem.getAltText());
            }

            return dto;
        }
    }

    protected MenuItemDTO convertCategoryToMenuItemDTO(Category category,MenuItem menuItem) {
        Set<Category> convertedCategories = new HashSet();
        return this.convertCategoryToMenuItemDTO(category, convertedCategories,menuItem);
    }

    protected MenuItemDTO convertCategoryToMenuItemDTO(Category category, Set<Category> convertedCategories,MenuItem menuItem) {
        MenuItemDTO dto = this.createDto(category,menuItem);
        List<CategoryXref> childXrefs = ListUtils.emptyIfNull(category.getChildCategoryXrefs());
        List<MenuItemDTO> submenu = new ArrayList();
        convertedCategories.add(category);
        Iterator var6 = childXrefs.iterator();

        while(var6.hasNext()) {
            CategoryXref childXref = (CategoryXref)var6.next();
            Category childCategory = childXref.getSubCategory();
            if (!convertedCategories.contains(childCategory)) {
                submenu.add(this.convertCategoryToMenuItemDTO(childCategory, convertedCategories,menuItem));
            }
        }

        dto.setSubmenu(submenu);
        return dto;
    }

    protected MenuItemDTO createDto(Category category,MenuItem menuItem) {
        ZaoJiCMSMenuItemDTO dto = new ZaoJiCMSMenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setLabel(category.getName());
        dto.setUrl(category.getUrl());
        dto.setCategoryId(category.getId());
        return dto;
    }


}
