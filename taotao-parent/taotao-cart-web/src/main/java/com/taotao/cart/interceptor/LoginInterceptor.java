package com.taotao.cart.interceptor;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
       //前处理，执行handle之前执行此方法
        //返回true，放行。  false  拦截

        //从cookie中获取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "TT_TOKEN");
        //如果没有token未登录，直接放行
        if (StringUtils.isBlank(token)){
            return true;
        }
        //有token，调用sso的服务，根据token获取用户详细信息
        TaotaoResult result = userService.getUserByToken(token);
        //没有取到用户信息，登录已过期
        if (result.getStatus()!=200){
            return true;
        }
        //取到用户信息，登录状态
        TbUser user = (TbUser) result.getData();
        //把用户信息放到request中，只需要在controller中判断request中是否含有user信息
        httpServletRequest.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //Handler执行之后，返回modelandview之前


    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //完成处理，返回modelandview之后
        //可以在此处理异常

    }
}
