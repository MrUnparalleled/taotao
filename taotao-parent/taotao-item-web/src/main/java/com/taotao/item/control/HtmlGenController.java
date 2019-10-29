package com.taotao.item.control;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成静态页面测试controller
 */
@Controller
public class HtmlGenController {

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws IOException, TemplateException {
        //从spring容器中获取freemaekerconfigurer对象
        //从freemarkerConfigurer对象中获取configurer对象
        Configuration configuration = freeMarkerConfig.getConfiguration();
        //获取template对象
        Template template = configuration.getTemplate("hello.ftl");
        //创建数据集
        Map dateModel = new HashMap();
        dateModel.put("hello","1000");
        //创建输出文件的writer对象
        Writer out = new FileWriter(new File("D:/temp/out/spring-freemarker.html"));
        //调用模板的process方法，生成文件
        template.process(dateModel,out);
        //关闭流
        out.close();
        return "OK";
    }
}
