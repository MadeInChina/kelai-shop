package cn.com.kelaikewang.admin.configuration;

import org.broadleafcommerce.common.demo.AutoImportPersistenceUnit;
import org.broadleafcommerce.common.demo.AutoImportSql;
import org.broadleafcommerce.common.demo.AutoImportStage;
import org.broadleafcommerce.common.demo.ImportCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration("zjcmsDataConfig")
@Conditional(ImportCondition.class)
public class ImportSQLConfig {
    @Bean
    public AutoImportSql blAdminMenuAndPermissionsData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU,"config/bc/sql/load_admin_menu_and_permissions.sql", AutoImportStage.PRIMARY_LATE + 100);
    }
    @Bean
    public AutoImportSql blCustomFieldData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU,"config/bc/sql/load_custom_field_admin_security.sql", AutoImportStage.PRIMARY_LATE + 200);
    }
    @Bean
    public AutoImportSql blCustomHomepageContentData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_system_default_data.sql", AutoImportStage.PRIMARY_LATE + 300);
    }
    @Bean
    public AutoImportSql blProvinceData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_province.sql", AutoImportStage.PRIMARY_LATE + 400);
    }
    @Bean
    public AutoImportSql blCityData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_city.sql", AutoImportStage.PRIMARY_LATE + 500);
    }
    @Bean
    public AutoImportSql blRegionData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_region.sql", AutoImportStage.PRIMARY_LATE + 600);
    }
    @Bean
    public AutoImportSql blStreetData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_street.sql", AutoImportStage.PRIMARY_LATE + 700);
    }
    @Bean
    public AutoImportSql blLogisticsData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_logistics_data.sql", AutoImportStage.PRIMARY_LATE + 800);
    }
    @Bean
    public AutoImportSql blFieldData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_field_data.sql", AutoImportStage.PRIMARY_LATE + 900);
    }
    //marketing
    @Bean
    public AutoImportSql blMarketingData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_marketing_data.sql", AutoImportStage.PRIMARY_LATE + 1000);
    }
    @Bean
    public AutoImportSql blArticleData() {
        return new AutoImportSql(AutoImportPersistenceUnit.BL_PU, "config/bc/sql/load_article_data.sql", AutoImportStage.PRIMARY_LATE + 1001);
    }
}
