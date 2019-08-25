package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

    public TbItem getItemById(Long id);

    public EasyUIDataGridResult getItemList(Integer page,Integer rows);


}
