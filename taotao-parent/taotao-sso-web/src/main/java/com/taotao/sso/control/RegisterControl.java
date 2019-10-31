package com.taotao.sso.control;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 注册功能controller
 */
@Controller
public class RegisterControl {

    @Autowired
    private UserService userService;

    /**
     * 显示注册页面
     * @return
     */
    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user){
        TaotaoResult result = userService.createUser(user);
        return result;
    }
}
