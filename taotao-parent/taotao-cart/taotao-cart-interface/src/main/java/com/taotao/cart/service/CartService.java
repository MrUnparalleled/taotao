package com.taotao.cart.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.List;

public interface CartService {

    TaotaoResult addCart(Long userId,long itemId,int num);

    TaotaoResult mergeCart(Long userId, List<TbItem> itemList);

    List<TbItem> showCartList(Long userId);

    TaotaoResult updateNum(Long userId,Long itemId,Integer num);

    TaotaoResult deleteCartItem(Long userId,Long itemId);
}
