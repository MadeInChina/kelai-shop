package cn.com.kelaikewang.admin.service;

import cn.com.kelaikewang.contentbuilder.binder.ContentBoxDTOBinder;
import cn.com.kelaikewang.contentbuilder.binder.ContentDTOBinder;
import cn.com.kelaikewang.contentbuilder.binder.DTOBinder;
import cn.com.kelaikewang.contentbuilder.service.ContentBoxService;
import cn.com.kelaikewang.contentbuilder.service.ContentBuilderService;
import cn.com.kelaikewang.contentbuilder.service.DefaultContentBeanFactoryImpl;
import cn.com.kelaikewang.infrastructure.spring.ApplicationContextUtils;


public class ZaoJiCMSContentBeanFactory extends DefaultContentBeanFactoryImpl {
    private static final ContentDTOBinder CONTENT_DTO_BINDER = new ContentDTOBinder();
    private static final ContentBoxDTOBinder CONTENT_BOX_DTO_BINDER = new ContentBoxDTOBinder();
    @Override
    public ContentBoxService buildContentBoxService() {
        return ApplicationContextUtils.getBean("blContentBoxService");
    }

    @Override
    public ContentBuilderService buildContentBuilderService() {
        return ApplicationContextUtils.getBean("blContentBuilderService");
    }

    @Override
    public DTOBinder buildContentBoxDTOBinder() {
        return CONTENT_BOX_DTO_BINDER;
    }

    @Override
    public DTOBinder buildContentDTOBinder() {
        return CONTENT_DTO_BINDER;
    }
}
