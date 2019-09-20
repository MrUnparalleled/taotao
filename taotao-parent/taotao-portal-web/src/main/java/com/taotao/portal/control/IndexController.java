package com.taotao.portal.control;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;

    @Autowired
    public ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(Model model){
        //查询内容列表
        List<TbContent> ad1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
        model.addAttribute("ad1List",ad1List);
        return "index";
    }

}
