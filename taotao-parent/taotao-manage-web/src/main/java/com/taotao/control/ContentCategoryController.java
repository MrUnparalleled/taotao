package com.taotao.control;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(
            @RequestParam(value="id", defaultValue="0") Long parentId) {

        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }

    /**
     * 新增节点
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult createCateogry(Long parentId,String name){
        TaotaoResult result = contentCategoryService.createCategory(parentId,name);
        return result;
    }

    /**
     * 重命名节点
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public TaotaoResult updateCategory(Long id,String name){
        TaotaoResult result = contentCategoryService.updateCategory(id,name);
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteCategory(Long id){
        TaotaoResult result = contentCategoryService.deleteCategory(id);
        return result;
    }

}
