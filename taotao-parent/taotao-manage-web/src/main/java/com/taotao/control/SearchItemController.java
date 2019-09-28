package com.taotao.control;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public TaotaoResult importItemIndex(){
        TaotaoResult result = searchItemService.importAllItems();
        return result;
    }
}
