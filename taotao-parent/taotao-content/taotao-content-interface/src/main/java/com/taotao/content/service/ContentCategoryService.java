package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {

    public List<EasyUITreeNode> getContentCategoryList(long parentId);

    public TaotaoResult createCategory(Long parentId, String name);

    public TaotaoResult updateCategory(Long id, String name);

    public TaotaoResult deleteCategory(Long id);
}
