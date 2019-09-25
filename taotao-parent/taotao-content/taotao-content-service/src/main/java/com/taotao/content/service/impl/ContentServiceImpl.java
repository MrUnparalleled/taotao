package com.taotao.content.service.impl;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.jedis.JedisClient;
import com.taotao.common.jedis.JedisClientCluster;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    /**
     * 分页查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
        List<TbContent> list;
        //查询缓存
        String json = jedisClient.hget("CONTENT_KEY", categoryId + "");
        //判断json是否为空
        if (StringUtils.isNotBlank(json)){
            //把json转换成list
            list = JsonUtils.jsonToList(json, TbContent.class);
        }else {
            //执行查询
            TbContentExample example = new TbContentExample();
            TbContentExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            list = contentMapper.selectByExample(example);
        }
        //设置分页信息
        PageHelper.startPage(page,rows);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //取分页结果
        PageInfo<TbContent> pageInfo =new PageInfo<>(list);
        result.setTotal((int) pageInfo.getTotal());
        //将信息存储到缓存当中
        jedisClient.hset("CONTENT_KEY",categoryId+"",JsonUtils.objectToJson(list));
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
        //缓存同步
        String json = jedisClient.hget("CONTENT_KEY", content.getCategoryId().toString());
        if (StringUtils.isNotBlank(json)){
            //把json转换成list
            List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
            list.add(content);
            jedisClient.hset("CONTENT_KEY",content.getCategoryId()+"",JsonUtils.objectToJson(list));
        }
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
        //同步缓存
        String json = jedisClient.hget("CONTENT_KEY", content.getCategoryId().toString());
        if (StringUtils.isNotBlank(json)){
            //把json转换成list
            List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
            for (TbContent tbContent:list) {
                if (tbContent.getId().equals(content.getId())){
                    list.remove(tbContent);
                    list.add(content);
                }
            }
        }
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
                //缓存同步
                //清除缓存
                jedisClient.hdel("CONTENT_KEY");
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
        //查询缓存
        String json = jedisClient.hget("CONTENT_KEY", cid + "");
        if (StringUtils.isNotBlank(json)){
            List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
            return list;
        }
        TbContentExample example  = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(example);
        //缓存同步
        jedisClient.hset("CONTENT_KEY",cid+"",JsonUtils.objectToJson(tbContents));
        return tbContents;
    }

}
