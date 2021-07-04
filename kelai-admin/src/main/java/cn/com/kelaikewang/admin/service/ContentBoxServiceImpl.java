package cn.com.kelaikewang.admin.service;

import cn.com.kelaikewang.contentbuilder.dto.ContentBoxDTO;
import cn.com.kelaikewang.contentbuilder.service.ContentBoxService;
import cn.com.kelaikewang.infrastructure.spring.ApplicationContextUtils;
import org.broadleafcommerce.cms.page.domain.Page;
import org.broadleafcommerce.cms.page.domain.PageField;
import org.broadleafcommerce.cms.page.service.PageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service("blContentBoxService")
public class ContentBoxServiceImpl implements ContentBoxService<ContentBoxDTO> {
    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;
    @Resource(name = "blPageService")
    private PageService pageService;

    @Transactional("blTransactionManager")
    @Override
    public ContentBoxDTO getContent(Long aLong) {
        //PageService pageService = ApplicationContextUtils.getBean("blPageService");
        Page page = pageService.findPageById(aLong);
        if (page == null){
            return null;
        }
        ContentBoxDTO contentBoxDTO = new ContentBoxDTO();
        contentBoxDTO.setId(page.getId());
        PageField pageField = page.getPageFields().get("body");
        contentBoxDTO.setHtml(pageField.getValue());
        return contentBoxDTO;
    }

    @Override
    public Long createContent(ContentBoxDTO contentBoxDTO) {
        return null;
    }

    @Override
    public void deleteContent(Long aLong) {

    }

    @Transactional("blTransactionManager")
    @Override
    public void updateContent(ContentBoxDTO contentBoxDTO) {
        PageService pageService = ApplicationContextUtils.getBean("blPageService");
        Page page = pageService.findPageById(contentBoxDTO.getId());
        if (page == null){
            throw new RuntimeException("不存在id为" + contentBoxDTO.getId() + "的页面");
        }
        PageField pageField = page.getPageFields().get("body");
        pageField.setValue(contentBoxDTO.getHtml());
        em.merge(pageField);

    }
}
