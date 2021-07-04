package cn.com.kelaikewang.core.thymeleaf.processor;


import org.springframework.context.ApplicationContext;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.io.StringWriter;

public class PaginationTagProcessor extends AbstractElementTagProcessor  {
    private static final String TAG_NAME  = "pagination";//标签名
    private static final int PRECEDENCE = 10000;//优先级
    private ApplicationContext applicationContext;

    public PaginationTagProcessor(String dialectPrefix, ApplicationContext applicationContext){
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)
        this.applicationContext = applicationContext;
    }
    public PaginationTagProcessor(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext iTemplateContext,
                             IProcessableElementTag iProcessableElementTag,
                             IElementTagStructureHandler iElementTagStructureHandler) {
        //PageTagHandler pageTagHandler = ApplicationContextUtils.getBean(PageTagHandler.class);
        //pageTagHandler.doProcess(iTemplateContext,iProcessableElementTag,iElementTagStructureHandler);
        ITemplateEngine templateEngine = applicationContext.getBean(ITemplateEngine.class);
        StringWriter stringWriter = new StringWriter();
        String pageHtml = null;
        try {
            templateEngine.process("components/pagination",iTemplateContext,stringWriter);
            pageHtml = stringWriter.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stringWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final IModelFactory modelFactory = iTemplateContext.getModelFactory();

        final IModel model = modelFactory.createModel();

        model.add(modelFactory.createText(pageHtml));

        /*
         * 指示引擎用指定的模型替换整个元素。
         */
        iElementTagStructureHandler.replaceWith(model, false);

    }


}

