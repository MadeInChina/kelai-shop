package cn.com.kelaikewang.core.marketing.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.offer.domain.OfferImpl;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ZJCMS_OFFER_CODE_GROUP")
@AdminPresentationClass(friendlyName = "OfferCodeGroupImpl")
public class OfferCodeGroupImpl implements OfferCodeGroup{
    @Id
    @Column(name = "OFFER_CODE_GROUP_ID")
    @GeneratedValue(generator = "OfferCodeGroupId")
    @GenericGenerator(
            name="OfferCodeGroupId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="OfferCodeGroupImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="OfferCodeGroupImpl")
            }
    )
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    private Long id;

    @Column(name = "DESCRIPTION",nullable = false)
    @AdminPresentation(prominent = true,order = 1,gridOrder = 1,friendlyName = "OfferCodeGroupImpl_Description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = OfferImpl.class)
    @JoinColumn(name = "OFFER_ID",nullable = false)
    @AdminPresentation(friendlyName = "OfferCodeGroupImpl_Offer",order = 2,prominent = true,gridOrder = 2)
    @AdminPresentationToOneLookup(lookupDisplayProperty = "name")
    private Offer offer;

    @Column(name = "NUMBER_TO_GENERATE",nullable = false)
    @AdminPresentation(prominent = true,order = 3,gridOrder = 3,friendlyName = "OfferCodeGroupImpl_NumberToGenerate")
    private Integer numberToGenerate;

    @Column(name = "CODE_PREFIX")
    @AdminPresentation(order = 4,friendlyName = "OfferCodeGroupImpl_CodePrefix")
    private String codePrefix;

    @Column(name = "MAX_USES",nullable = false)
    @AdminPresentation(prominent = true,order = 5,gridOrder = 5,friendlyName = "OfferCodeGroupImpl_MaxUses")
    private Integer maxUses;

    @Column(name = "DEFAULT_ACTIVE_DATE",nullable = false)
    @AdminPresentation(prominent = true,order = 6,gridOrder = 6,friendlyName = "OfferCodeGroupImpl_DefaultActiveDate")
    private Date defaultActiveDate;

    @Column(name = "DEFAULT_END_DATE")
    @AdminPresentation(prominent = true,order = 7,gridOrder = 7,friendlyName = "OfferCodeGroupImpl_DefaultEndDate")
    private Date defaultEndDate;

    @Column(name = "STATE",nullable = false)
    @AdminPresentation(prominent = true,order = 8,gridOrder = 8,friendlyName = "OfferCodeGroupImpl_State",visibility = VisibilityEnum.FORM_HIDDEN)
    private String state;

    @Column(name = "NUMBER_OF_CODES_GENERATED",nullable = false)
    @AdminPresentation(prominent = true,order = 9,gridOrder = 9,friendlyName = "OfferCodeGroupImpl_NumberOfCodesGenerated",visibility = VisibilityEnum.FORM_HIDDEN)
    private Integer numberOfCodesGenerated;

    @Column(name = "CREATE_DATE")
    @AdminPresentation(prominent = true,order = 10,gridOrder = 10,friendlyName = "OfferCodeGroupImpl_CreateDate",readOnly = true,defaultValue = "today")
    private Date createDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Integer getNumberToGenerate() {
        return numberToGenerate;
    }

    public void setNumberToGenerate(Integer numberToGenerate) {
        this.numberToGenerate = numberToGenerate;
    }

    public String getCodePrefix() {
        return codePrefix;
    }

    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Date getDefaultActiveDate() {
        return defaultActiveDate;
    }

    public void setDefaultActiveDate(Date defaultActiveDate) {
        this.defaultActiveDate = defaultActiveDate;
    }

    public Date getDefaultEndDate() {
        return defaultEndDate;
    }

    public void setDefaultEndDate(Date defaultEndDate) {
        this.defaultEndDate = defaultEndDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getNumberOfCodesGenerated() {
        return numberOfCodesGenerated;
    }

    public void setNumberOfCodesGenerated(Integer numberOfCodesGenerated) {
        this.numberOfCodesGenerated = numberOfCodesGenerated;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
