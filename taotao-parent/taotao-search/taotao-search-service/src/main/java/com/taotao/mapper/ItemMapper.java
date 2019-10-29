package com.taotao.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {
    List<SearchItem> getItemList();

    SearchItem getItemById(long itemId);
}
