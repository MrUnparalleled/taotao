package com.taotao.service.impl;

import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品管理service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;


    @Override
    public TbItem getItemById(Long id) {
//        根据主键查询
        TbItem item = itemMapper.selectByPrimaryKey(id);
        return item;
//        TbItemExample example =new TbItemExample();
//        TbItemExample.Criteria criteria = example.createCriteria();
////        设置条件查询
//        criteria.andIdEqualTo(id);
////        执行查询
//        List<TbItem> list = itemMapper.selectByExample(example);
//        if (list!=null && list.size()>0){
//            return list.get(0);
//        }
//        return null;
    }
}
