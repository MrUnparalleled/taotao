package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

    public TbItem getItemById(Long id);

    public EasyUIDataGridResult getItemList(Integer page,Integer rows);

    public TaotaoResult addItem(TbItem tbItem,String desc);


}
