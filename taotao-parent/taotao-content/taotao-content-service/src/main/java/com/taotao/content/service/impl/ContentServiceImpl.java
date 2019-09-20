package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 分页查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //取分页结果
        PageInfo<TbContent> pageInfo =new PageInfo<>(list);
        result.setTotal((int) pageInfo.getTotal());
        return result;
    }

    /**
     * 添加
     * @param content
     * @return
     */
    @Override
    public TaotaoResult addContent(TbContent content) {
        //获得id
        long id = IDUtils.genItemId();
        //补全信息
        content.setId(id);
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入数据
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    /**
     * 编辑
     * @param content
     * @return
     */
    @Override
    public TaotaoResult editContent(TbContent content) {
        //1.补全属性
        //1.1获取创建日期
        TbContent content1 = contentMapper.selectByPrimaryKey(content.getId());
        Date created = content1.getCreated();
        content.setCreated(created);
        content.setUpdated(new Date());
        //2.更新数据
        contentMapper.updateByPrimaryKey(content);
        return TaotaoResult.ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult deleteContent(String ids) {
        //拆解ids
        String[] split = ids.split(",");
        if (split==null||split.length==0){
            return null;
        }else {
            for (String id:split) {
                //删除
                contentMapper.deleteByPrimaryKey(Long.valueOf(id));
            }
            return TaotaoResult.ok();
        }
    }

    /**
     * 根据内容分类id查询内容列表
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentListByCid(Long cid) {
        TbContentExample example  = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(example);
        return tbContents;
    }

}
