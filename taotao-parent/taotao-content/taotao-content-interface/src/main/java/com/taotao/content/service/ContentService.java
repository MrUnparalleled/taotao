package com.taotao.content.service;


import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);

    public TaotaoResult addContent(TbContent content);

    public TaotaoResult editContent(TbContent content);

    public TaotaoResult deleteContent(String ids);

    public List<TbContent> getContentListByCid(Long cid);
}
