/*
 * #%L
 * BroadleafCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
/*!
 * jQuery QueryBuilder 2.2.0
 * Locale: English (en)
 * Author: Damien "Mistic" Sorel, http://www.strangeplanet.fr
 * Licensed under MIT (http://opensource.org/licenses/MIT)
 */

(function(root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery', 'query-builder'], factory);
    }
    else {
        factory(root.jQuery);
    }
}(this, function($) {
"use strict";

var QueryBuilder = $.fn.queryBuilder;

QueryBuilder.regional['zh_CN'] = {
  "__locale": "简体中文 (zh_CN)",
  "__author": "Damien \"Mistic\" Sorel, http://www.strangeplanet.fr",
  "add_rule": "添加规则",
  "add_group": "添加组",
  "delete_rule": "删除",
  "delete_group": "删除",
  "conditions": {
    "AND": "和",
    "OR": "或"
  },
  "operators": {
    "EQUALS": "等于",
    "IEQUALS": "等于 (忽略大小写)",
    "NOT_EQUAL": "不等于",
    "INOT_EQUAL": "不等于 (忽略大小写)",
    "IN_SET": "在...之內",
    "NOT_IN_SET": "不在...之內",
    "LESS_THAN": "小于",
    "LESS_OR_EQUAL": "小于或等于",
    "GREATER_THAN": "大于",
    "GREATER_OR_EQUAL": "大于或等于",
    "BETWEEN": "在...之间",
    "BETWEEN_INCLUSIVE": "在...之间 (包含)",
    "ISTARTS_WITH": "以...开始",
    "INOT_STARTS_WITH": "不以...开始",
    "ICONTAINS": "包含",
    "CONTAINS": "包含",
    "INOT_CONTAINS": "不包含",
    "NOT_CONTAINS": "不包含",
    "IENDS_WITH": "以...结尾",
    "INOT_ENDS_WITH": "不以...结尾",
    "COUNT_GREATER_THAN": "总数大于",
    "COUNT_GREATER_OR_EQUAL": "总数大于或等于",
    "COUNT_LESS_THAN": "总数小于",
    "COUNT_LESS_OR_EQUAL": "总数小于或等于",
    "COUNT_EQUALS": "总数等于",
    "COLLECTION_IN": "在...里面",
    "COLLECTION_NOT_IN": "不在...里面",
    "IS_NULL": "为空"
  },
  "errors": {
    "no_filter": "未选择筛选条件",
    "empty_group": "该组为空",
    "radio_empty": "未选择",
    "checkbox_empty": "未选择",
    "select_empty": "未选择",
    "string_empty": "值为空",
    "string_exceed_min_length": "至少包含 {0} 个字符",
    "string_exceed_max_length": "必须包含不超过{0}字符",
    "string_invalid_format": "无效格式 ({0})",
    "number_nan": "不是数字",
    "number_not_integer": "不是整数",
    "number_not_double": "不是实数",
    "number_exceed_min": "必须大于 {0}",
    "number_exceed_max": "必须小于 {0}",
    "number_wrong_step": "必须是 {0} 的倍数",
    "datetime_empty": "值为空",
    "datetime_invalid": "无效的日期格式 ({0})",
    "datetime_exceed_min": "必须在 {0} 之后",
    "datetime_exceed_max": "必须在 {0} 之前",
    "boolean_not_valid": "不是布尔值",
    "operator_not_multiple": "操作符 {0} 无法接受多个值"
  }
};

QueryBuilder.defaults({ lang_code: 'zh_CN' });
}));
