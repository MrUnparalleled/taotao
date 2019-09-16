package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

    public TbItem getItemById(Long id);

    public EasyUIDataGridResult getItemList(Integer page,Integer rows);

    public TaotaoResult addItem(TbItem tbItem,String desc);


    public TaotaoResult getItemDesc(Long itemId);

    public TaotaoResult deleteItems(String ids);

    public TaotaoResult upItems(String ids);

    public TaotaoResult downItems(String ids);

    public TaotaoResult editItem(TbItem tbItem, String desc);
}
