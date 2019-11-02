package com.taotao.cart.control;

import com.taotao.cart.service.CartService;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartControl {

    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Value("${TT_CART}")
    private String TT_CART;
    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;

    /**
     * 页面显示
     * @return
     */
    @RequestMapping(value = "/cart")
    public String cartShow(){
        return "cart";
    }

    /**
     * 添加商品到购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCartItem(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        //判断是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //登录--写入redis
        if (user !=null){
            //保存到服务器端
            cartService.addCart(user.getId(),itemId,num);
            //返回逻辑视图
            return "cartSuccess";
        }
        //未登录
        //从cookie中查询商品列表
        List<TbItem> cartList = getCartList(request);
        boolean hasItem = false;
        //判断商品是否存在购物车
        for (TbItem item : cartList) {
            //对象比较的是地址，应该是值的比较
            if (item.getId()==itemId.longValue()){
                //如果存在，值相加
                item.setNum(item.getNum()+num);
                hasItem = true;
                break;
            }
        }
        if (!hasItem){
            //不存在，根据商品id查询商品信息
            TbItem item = itemService.getItemById(itemId);
            //取一张图片
            String image = item.getImage();
            if (StringUtils.isNoneBlank(image)){
                String[] images = image.split(",");
                item.setImage(images[0]);
            }
            //这是购买数量
            item.setNum(num);
            //把商品添加到购物车
            cartList.add(item);
            //把购物车商品列表写入cookie
            CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(cartList),CART_EXPIRE,true);
        }
        return "cartSuccess";
    }


    /**
     * 根据cookie获得购物车列表
     * @param request
     * @return
     */
    public List<TbItem> getCartList(HttpServletRequest request){
        //取购物车列表
        String json = CookieUtils.getCookieValue(request, TT_CART, true);
        //判断json是否为空
        if (!StringUtils.isBlank(json)){
            //把json转换为商品列表返回
            List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
            return list;
        }
        List<TbItem> list = new ArrayList<>();
        return list;
    }

    /**
     * 显示购物车列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response, Model model) {
        //判断是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //取cookie中的购物车
        List<TbItem> cartList = getCartList(request);
        //登录
        if (user!=null){
                if (cartList!=null&&cartList.size()!=0){
                //购物车不为空，把cookie中的购物车和服务端的购物车合并
                cartService.mergeCart(user.getId(),cartList);
                //把cookie中的购物车清空
                CookieUtils.deleteCookie(request,response,TT_CART);
            }
            //从服务端取购物车
            cartList = cartService.showCartList(user.getId());
        }
        //取购物车商品列表
        //传递给页面
        model.addAttribute("cartList", cartList);
        return "cart";
    }


    /**
     * 更改购物车里的商品数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
        //是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user!=null){
            //登录--服务器端操作
            TaotaoResult result = cartService.updateNum(user.getId(), itemId, num);
            //return
            return result;
        }
        //从cookie中获取列表
        List<TbItem> list = getCartList(request);
        for (TbItem item : list) {
            if (item.getId()==itemId){
                //对应的商品，更改商品数量
                item.setNum(num);
            }
        }
        //将购物车列表写入cookie
        String cart = JsonUtils.objectToJson(list);
        CookieUtils.setCookie(request,response,TT_CART,cart,CART_EXPIRE,true);
        return TaotaoResult.ok();
    }


    /**
     * 删除商品
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        //是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user!=null){
            //登录--服务器端操作
            cartService.deleteCartItem(user.getId(),itemId);
            //return
            return "redirect:/cart/cart.html";
        }
        //从url中获取id
        //从cookie中获取购物车
        List<TbItem> list = getCartList(request);
        //遍历购物车
        for (TbItem item : list) {
            if (item.getId()==itemId.longValue()){
                //删除商品
                list.remove(item);
                break;
            }
        }
        //将商品列表写入cookie
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(list),CART_EXPIRE,true);
        //返回逻辑视图，在逻辑视图中做redirt跳转
        return "redirect:/cart/cart.html";
    }
}
