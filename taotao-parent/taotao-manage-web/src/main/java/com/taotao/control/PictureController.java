package com.taotao.control;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.json.JSONObject;
import com.taotao.common.utils.FastDFSClient;
import com.taotao.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;


    @RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE+";charset-utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile){
        //把图片上传到图片服务器
        try {
            FastDFSClient fastDFSClient =new FastDFSClient("classpath:conf/client.conf");
            //取扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //得到一个图片的地址和文件名
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //补充为完整的url
            url=IMAGE_SERVER_URL+url;
            //封装到map中返回
            Map result = new HashMap();
            result.put("error",0);
            result.put("url",url);
            //转换成String
            String s = JsonUtils.objectToJson(result);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            Map result = new HashMap();
            result.put("error",0);
            result.put("message","图片上传失败");
            //转换成String
            String s = JsonUtils.objectToJson(result);
            return s;
        }

    }
}
