package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * @author ucmed
 */
public interface ItemCatService {

    public List<EasyUITreeNode> getCatList(Long parentId);

}
