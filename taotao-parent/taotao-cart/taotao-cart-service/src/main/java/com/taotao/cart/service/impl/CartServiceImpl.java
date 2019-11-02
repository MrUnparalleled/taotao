package com.taotao.cart.service.impl;

import com.taotao.cart.service.CartService;
import com.taotao.common.jedis.JedisClient;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    /**
     * 购物车添加商品
     * @param userId
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult addCart(Long userId, long itemId,int num) {
        //向redis中添加购物车，
        // 数据类型为hash  key:用户id field:商品id value:商品信息
        //判断商品是否存在
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (hexists){
            //如果存在---数量相加
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            //转换为pojo
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            item.setNum(item.getNum()+num);
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
            return TaotaoResult.ok();
        }
        //如果不存在，根据商品id去商品信息
        TbItem tbItem = itemService.getItemById(itemId);
        tbItem.setNum(num);
        //取一张图片
        String image = tbItem.getImage();
        if (StringUtils.isNoneBlank(image)){
            tbItem.setImage(image.split(",")[0]);
        }
        //存入购物车当中
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
        return TaotaoResult.ok();
    }

    /**
     * 合并购物车
     * @param userId
     * @param itemList
     * @return
     */
    @Override
    public TaotaoResult mergeCart(Long userId, List<TbItem> itemList) {
        //查询redis的购物车
        List<TbItem> list = showCartList(userId);
        //将cookie中的购物车加到redis的购物车当中
        if (itemList!=null&&itemList.size()!=0){
            for (TbItem item : itemList) {
                addCart(userId,item.getId(),item.getNum());
            }
        }
        //返回
        return TaotaoResult.ok();
    }

    /**
     * 展示购物车
     * @param userId
     * @return
     */
    @Override
    public List<TbItem> showCartList(Long userId) {
        //从redis取数据
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> tbItemList = new ArrayList<>();
        for (String string : jsonList) {
            TbItem item = JsonUtils.jsonToPojo(string,TbItem.class);
            tbItemList.add(item);
        }
        return tbItemList;
    }

    /**
     * 更改购物车里的商品数量
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public TaotaoResult updateNum(Long userId, Long itemId, Integer num) {
        //获取redis购物车的商品列表
        List<TbItem> list = showCartList(userId);
        //更改数量
        for (TbItem item : list) {
            if (item.getId()==itemId.longValue()){
                item.setNum(num);
                //写回redis
                jedisClient.hset(REDIS_CART_PRE+":"+userId,item.getId()+"",JsonUtils.objectToJson(item));
            }
        }

        return TaotaoResult.ok();
    }

    /**
     * 删除商品
     * @param userId
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult deleteCartItem(Long userId, Long itemId) {
        //获取购物车列表
        List<TbItem> list = showCartList(userId);
        Iterator<TbItem> iterator = list.iterator();
        while (iterator.hasNext()){
            TbItem next = iterator.next();
            if (next.getId()==itemId.longValue()){
                iterator.remove();
            }else {
                //存入购物车当中
                jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(next));
            }
        }

/*        for (TbItem item : list) {
            if (item.getId()==itemId.longValue()){
                list.remove(item);
            }else {
                //存入购物车当中
                jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
            }
        }*/
        return TaotaoResult.ok();
    }


}
