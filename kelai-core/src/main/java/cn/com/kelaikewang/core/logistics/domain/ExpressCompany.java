package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.admin.domain.AdminMainEntity;

import java.io.Serializable;

public interface ExpressCompany extends Serializable, AdminMainEntity {
    public Long getId();

    public void setId(Long id);

    public String getName();

    public void setName(String name);

    public String getCode();

    public void setCode(String code);
}
