package cn.com.kelaikewang.admin.service;

import cn.com.kelaikewang.contentbuilder.dto.ContentDTO;
import cn.com.kelaikewang.contentbuilder.service.ContentBuilderService;
import org.broadleafcommerce.cms.structure.domain.*;
import org.broadleafcommerce.cms.structure.service.StructuredContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Service("blContentBuilderService")
public class ContentBuilderServiceImpl implements ContentBuilderService<ContentDTO> {
    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;
    @Resource(name = "blStructuredContentService")
    private StructuredContentService structuredContentService;

    @Transactional("blTransactionManager")
    @Override
    public ContentDTO getContent(Long aLong) {
        StructuredContent structuredContent = structuredContentService.findStructuredContentById(aLong);
        if (structuredContent == null){
            return null;
        }
        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setId(structuredContent.getId());
        StructuredContentFieldXref fieldXref ;
        Map<String, StructuredContentFieldXref> stringStructuredContentFieldXrefMap = structuredContent.getStructuredContentFieldXrefs();
        if(stringStructuredContentFieldXrefMap.size() >0){
            fieldXref = stringStructuredContentFieldXrefMap.get("htmlContent");
            if (fieldXref != null) {
                StructuredContentField structuredContentField = fieldXref.getStructuredContentField();
                if (structuredContentField != null) {
                    contentDTO.setHtml(structuredContentField.getValue());
                }
            }
        }else {
            contentDTO.setHtml("<p>Quick start</p>");
        }

        return contentDTO;
    }

    @Override
    public Long createContent(ContentDTO contentDTO) {
        return null;
    }

    @Override
    public void deleteContent(Long aLong) {

    }
    @Transactional("blTransactionManager")
    @Override
    public void updateContent(ContentDTO contentDTO) {
        StructuredContent structuredContent = structuredContentService.findStructuredContentById(contentDTO.getId());
        if (structuredContent == null){
            throw new RuntimeException("不存在id为" + contentDTO.getId() + "的Content");
        }
        //StructuredContentField field = structuredContent.getStructuredContentFieldXrefs().get("htmlContent").getStructuredContentField();
        //field.setValue(contentDTO.getHtml());
        StructuredContentFieldXref fieldXref ;
        Map<String, StructuredContentFieldXref> structuredContentFieldXrefMap = structuredContent.getStructuredContentFieldXrefs();
        if(structuredContentFieldXrefMap.size() >0){
            fieldXref = structuredContentFieldXrefMap.get("htmlContent");
            if (fieldXref != null) {
                StructuredContentField structuredContentField = fieldXref.getStructuredContentField();
                if (structuredContentField != null) {
                    structuredContentField.setValue(contentDTO.getHtml());
                    em.merge(structuredContentField);
                }
            }
        }else {

            StructuredContentFieldXref structuredContentFieldXref = new StructuredContentFieldXrefImpl();
            structuredContentFieldXref.setKey("htmlContent");
            structuredContentFieldXref.setStructuredContent(structuredContent);

            StructuredContentField structuredContentField = new StructuredContentFieldImpl();
            structuredContentField.setValue(contentDTO.getHtml());
            structuredContentField.setFieldKey("htmlContent");
            structuredContentFieldXref.setStrucuturedContentField(structuredContentField);


            em.merge(structuredContentFieldXref);
        }

    }
}
