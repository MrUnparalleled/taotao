package com.taotao.control;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(@RequestParam(value="categoryId", defaultValue="0")Long categoryId, Integer page, Integer rows){
        EasyUIDataGridResult list = contentService.getContentList(categoryId, page, rows);
        return list;
    }

    /**
     * 添加
     * @param content
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content){
        TaotaoResult result = contentService.addContent(content);
        return result;
    }

    /**
     * 编辑
     * @param content
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public TaotaoResult editContent(TbContent content){
        TaotaoResult result = contentService.editContent(content);
        return result;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteContent(String ids){
        TaotaoResult result = contentService.deleteContent(ids);
        return result;
    }
}
