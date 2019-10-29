package com.taotao.item.control;

import com.taotao.common.jedis.JedisClient;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model){
        //根据商品id查询商品信息
        TbItem tbItem = itemService.getItemById(itemId);
        //转换为item
        Item item = new Item(tbItem);
        //根据商品id查询商品详细信息
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        //把数据传递给页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);
        return "item";
    }


}
