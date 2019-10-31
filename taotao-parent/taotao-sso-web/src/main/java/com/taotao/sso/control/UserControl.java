package com.taotao.sso.control;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

@Controller
public class UserControl {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type){
        TaotaoResult result = userService.checkData(param, type);
        return result;
    }

    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){
        TaotaoResult result = userService.getUserByToken(token);
        //是否为jsonp请求
        if (StringUtils.isBlank(callback)){
            String strResult = callback+"("+ JsonUtils.objectToJson(result) +")";
            return strResult;
        }
        return JsonUtils.objectToJson(result);
    }

}
