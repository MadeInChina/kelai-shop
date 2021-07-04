package cn.com.kelaikewang.core.marketing.domain;

import org.broadleafcommerce.core.offer.domain.Offer;

import java.io.Serializable;
import java.util.Date;

public interface OfferCodeGroup extends Serializable {
    Long getId();

    void setId(Long id);

    String getDescription();

    void setDescription(String description);

    Offer getOffer();

    void setOffer(Offer offer);

    Integer getNumberToGenerate();

    void setNumberToGenerate(Integer numberToGenerate);

    String getCodePrefix();

    void setCodePrefix(String codePrefix);

    Integer getMaxUses();

    void setMaxUses(Integer maxUses);

    Date getDefaultActiveDate();

    void setDefaultActiveDate(Date defaultActiveDate);

    Date getDefaultEndDate();

    void setDefaultEndDate(Date defaultEndDate);

    String getState();

    void setState(String state);

    Integer getNumberOfCodesGenerated();

    void setNumberOfCodesGenerated(Integer numberOfCodesGenerated);

    Date getCreateDate();

    void setCreateDate(Date createDate);
}
