package com.taotao.control;

import com.alibaba.dubbo.config.support.Parameter;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理Controller
 * @author ucmed
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        System.out.println(tbItem.toString());
        return tbItem;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //调用服务查询商品列表
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;

    }


    /**
     * 商品添加功能
     * @param tbItem
     * @param desc
     * @return
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(TbItem tbItem,String desc){
        TaotaoResult result = itemService.addItem(tbItem, desc);
        return result;
    }

    /**
     * 根据商品id查询商品描述
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/rest/item/query/item/desc/{itemId}")
    @ResponseBody
    public TaotaoResult selectItemDesc(@PathVariable Long itemId){
        TaotaoResult result = itemService.getItemDesc(itemId);
        return result;
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @RequestMapping(value = "/rest/item/delete")
    @ResponseBody
    public TaotaoResult deleteItem(String ids){
        TaotaoResult result = itemService.deleteItems(ids);
        return result;
    }

    /**
     * 批量上架
     * @param ids
     * @return
     */
    @RequestMapping(value = "/rest/item/reshelf")
    @ResponseBody
    public TaotaoResult upItem(String ids){
        TaotaoResult result = itemService.upItems(ids);
        return result;
    }


    /**
     * 批量下架
     * @param ids
     * @return
     */
    @RequestMapping(value = "/rest/item/instock")
    @ResponseBody
    public TaotaoResult downItem(String ids){
        TaotaoResult result = itemService.downItems(ids);
        return result;
    }

    /**
     * 编辑商品
     * @param
     * @return
     */
    @RequestMapping(value = "/rest/item/update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult editItem(TbItem tbItem,String desc){
        TaotaoResult result = itemService.editItem(tbItem,desc);
        return result;
    }

}
