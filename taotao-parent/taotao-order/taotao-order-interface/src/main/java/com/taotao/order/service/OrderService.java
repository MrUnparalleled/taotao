package com.taotao.order.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

public interface OrderService {

    TaotaoResult createOrder(OrderInfo orderInfo);
}
