package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 查询分类
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        // 1、取查询参数id，parentId
        // 2、根据parentId查询tb_content_category，查询子节点列表。
        TbContentCategoryExample example = new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        // 3、得到List<TbContentCategory>
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        // 4、把列表转换成List<EasyUITreeNode>ub
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到列表
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 新增节点
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public TaotaoResult createCategory(Long parentId, String name) {
        //1.接收参数
        //1.1创建对象
        TbContentCategory contentCategory =new TbContentCategory();
        //1.2补全属性
        contentCategory.setIsParent(false);
        contentCategory.setName(name);
        contentCategory.setParentId(parentId);
        //排列序号,表示同级类目的展现次序,如次序相同则按名称次序排序。取值范围：大于0的整数
        contentCategory.setSortOrder(1);
        //状态  1 正常   2 删除
        contentCategory.setStatus(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //2.插入数据
        contentCategoryMapper.insert(contentCategory);
        //3.判断父节点的isparent是否为true
        TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parentCategory.getIsParent()){
            parentCategory.setIsParent(true);
            //更新父节点
            contentCategoryMapper.updateByPrimaryKey(parentCategory);
        }
        //4.需要主键返回
        //5.返回taotaoresult 其中包装ContentCategory对象
        return TaotaoResult.ok(contentCategory);
    }

    /**
     * 重命名节点
     * @param id
     * @param name
     * @return
     */
    @Override
    public TaotaoResult updateCategory(Long id, String name) {
        //1.接收参数
        //1.1根据id查询
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //1.2更改name
        contentCategory.setName(name);
        //1.2设置更新时间
        contentCategory.setUpdated(new Date());
        //更新数据
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return TaotaoResult.ok();
    }

    /**
     * 删除节点
     * @param id
     * @return
     */
    @Override
    public TaotaoResult deleteCategory(Long id) {
        //根据id获取
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //判断是否为父节点
        if (contentCategory.getIsParent()){
            //是父节点---删除该节点以及其子节点
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            //删除该节点以及其子节点
            contentCategoryMapper.deleteByPrimaryKey(id);
            contentCategoryMapper.deleteByExample(example);
        }else {
            //不是父节点
            //根据id删除
            contentCategoryMapper.deleteByPrimaryKey(id);
            Long parentId = contentCategory.getParentId();
            //查询父节点是否有子节点
            TbContentCategoryExample example =new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
            if (list==null||list.size()==0){
                //无子节点--将父节点的isparent改为false
                TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
                parentNode.setIsParent(false);
                //更新
                contentCategoryMapper.updateByPrimaryKey(parentNode);
            }else {
                //有子节点
            }
        }
        return TaotaoResult.ok();
    }

}
