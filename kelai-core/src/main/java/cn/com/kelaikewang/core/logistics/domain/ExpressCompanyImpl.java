package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "ZJCMS_EXPRESS_COMPANY")
@Entity
@AdminPresentationClass(friendlyName = "ExpressCompanyImpl")
public class ExpressCompanyImpl implements ExpressCompany{
    @Id
    @GeneratedValue(generator = "ExpressCompanyId")
    @GenericGenerator(
            name="ExpressCompanyId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ExpressCompanyImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ExpressCompanyImpl")
            }
    )
    @Column(name = "ID")
    protected Long id;
    @AdminPresentation(friendlyName = "ExpressCompanyImpl_Name",prominent = true,gridOrder = 1)
    @Column(name = "NAME")
    protected String name;
    @AdminPresentation(friendlyName = "ExpressCompanyImpl_Code",prominent = true,gridOrder = 2)
    @Column(name = "CODE")
    protected String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }
}
