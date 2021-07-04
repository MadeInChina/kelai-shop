package cn.com.kelaikewang.admin;

import com.alibaba.fastjson.JSON;
import cn.com.kelaikewang.core.logistics.dto.BigRegionDTO;
import cn.com.kelaikewang.core.logistics.dto.CityDTO;
import cn.com.kelaikewang.core.logistics.dto.ProvinceDTO;
import org.broadleafcommerce.common.config.EnableBroadleafAdminAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@ServletComponentScan(basePackages = {"cn.com.kelaikewang"})
public class AdminApplication {

    @Configuration
    @EnableBroadleafAdminAutoConfiguration
    public static class BroadleafFrameworkConfiguration {}

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}

