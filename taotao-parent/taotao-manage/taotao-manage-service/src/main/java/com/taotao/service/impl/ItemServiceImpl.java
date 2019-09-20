package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品管理service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;


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

    /**
     * 分页查询
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbItemExample example =new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result =new EasyUIDataGridResult();
        result.setRows(list);
        //取分页结果
        PageInfo<TbItem> pageInfo =new PageInfo<>(list);
        result.setTotal((int) pageInfo.getTotal());
        return result;
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @return
     */
    @Override
    public TaotaoResult addItem(TbItem tbItem, String desc) {
        //生成商品id
        long id = IDUtils.genItemId();
        //补全item的属性
        tbItem.setId(id);
        //1、正常  2、下架  3、删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(tbItem);
        //创建一个商品描述表对于的pojo，补全属性
        TbItemDesc tbItemDesc =new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setItemId(id);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        int insert = itemDescMapper.insert(tbItemDesc);
        //返回成功
        TaotaoResult ok = TaotaoResult.ok();
        return ok;
    }


    /**
     * 根据商品id获得商品描述
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult getItemDesc(Long itemId) {
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        TaotaoResult result =new TaotaoResult();
        result.setData(tbItemDesc);
        result.setStatus(200);
        return result;
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult deleteItems(String ids) {
        //判断ids不为空
        if (ids!=null&&!"".equals(ids)){
            String[] split = ids.split(",");
            for (String id:split) {
                itemMapper.deleteByPrimaryKey(Long.valueOf(id));
                itemDescMapper.deleteByPrimaryKey(Long.valueOf(id));
            }
            return TaotaoResult.ok();
        }
        return null;
    }

    /**
     * 商品上架
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult upItems(String ids) {
        //判断ids不为空
        if (ids!=null&&!"".equals(ids)){
            String[] split = ids.split(",");
            for (String id:split) {
                //获取当前商品
                TbItem tbItem = itemMapper.selectByPrimaryKey(Long.valueOf(id));
                //1、正常  2、下架  3、删除
                tbItem.setStatus((byte) 1);
                //更新商品
                itemMapper.updateByPrimaryKey(tbItem);
            }
            return TaotaoResult.ok();
        }
        return null;
    }

    /**
     * 商品下架
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult downItems(String ids) {
        //判断ids不为空
        if (ids!=null&&!"".equals(ids)){
            String[] split = ids.split(",");
            for (String id:split) {
                //获取当前商品
                TbItem tbItem = itemMapper.selectByPrimaryKey(Long.valueOf(id));
                //1、正常  2、下架  3、删除
                tbItem.setStatus((byte) 2);
                //更新商品
                itemMapper.updateByPrimaryKey(tbItem);
            }
            return TaotaoResult.ok();
        }
        return null;
    }

    /**
     * 编辑商品
     * @param tbItem
     * @param desc
     * @return
     */
    @Override
    public TaotaoResult editItem(TbItem tbItem, String desc) {
        //补全商品属性
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //更新商品表
        itemMapper.updateByPrimaryKey(tbItem);
        //创建商品描述的pojo，补全属性
        TbItemDesc itemDesc =new TbItemDesc();
        itemDesc.setItemId(tbItem.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        itemDesc.setCreated(new Date());
        itemDescMapper.updateByPrimaryKey(itemDesc);
        return TaotaoResult.ok();
    }


}
