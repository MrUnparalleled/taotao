package com.taotao.control;

import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理Controller
 */
@Controller
public class ItemControl {

    @Autowired
    private ItemService itemService;


    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        TbItem tbItem =itemService.getItemById(itemId);
        System.out.println(tbItem.toString());
        return tbItem;
    }

}
