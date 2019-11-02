package com.taotao.sso.control;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginControl {

    @Autowired
    private UserService userService;

    /**
     * 显示登陆页面
     * @return
     */
    @RequestMapping("/page/login")
    public String showLogin(){
        return "login";
    }

    /**
     * 用户登陆
     * @param
     * @return
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result = userService.login(username,password);
        //从返回结果中取token，将token放入cookie中
        String token = result.getData().toString();
        CookieUtils.setCookie(request,response,"TT_TOKEN",token);
        return result;
    }
}
