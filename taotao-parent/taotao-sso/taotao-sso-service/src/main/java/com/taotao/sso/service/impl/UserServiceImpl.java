package com.taotao.sso.service.impl;

import com.taotao.common.jedis.JedisClient;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    /**
     * 数据校验
     * @param param
     * @param type
     * @return
     */
    @Override
    public TaotaoResult checkData(String param, int type) {
        //从tb_user表中查询数据
        TbUserExample example =new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //查询条件根据参数动态生成
        //type 1、2、3分别代表username、phone、email
        if (type==1){
            criteria.andUsernameEqualTo(param);
        }else if (type==2){
            criteria.andPhoneEqualTo(param);
        }else if (type==3){
            criteria.andEmailEqualTo(param);
        }else {
            return TaotaoResult.build(400,"非法参数");
        }
        //查询
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        //判断查询结果
        if (tbUsers!=null&&tbUsers.size()!=0){
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public TaotaoResult createUser(TbUser user) {
        if (StringUtils.isBlank(user.getUsername())){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())){
            return TaotaoResult.build(400,"密码不能为空");
        }
        //校验数据是否可用
        TaotaoResult checkData = checkData(user.getUsername(), 1);
        if (!(boolean)checkData.getData()){
            return TaotaoResult.build(400,"用户名已被使用");
        }
        //校验手机号是否可用
        TaotaoResult checkData2 = checkData(user.getUsername(), 2);
        if (!(boolean)checkData.getData()){
            return TaotaoResult.build(400,"手机号已被使用");
        }
        //校验邮箱是否可用
        TaotaoResult checkData3 = checkData(user.getUsername(), 3);
        if (!(boolean)checkData.getData()){
            return TaotaoResult.build(400,"邮箱地址已被使用");
        }
        //补全属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        //把用户数据插入数据库当中
        userMapper.insert(user);
        return TaotaoResult.ok();
    }


    /**
     * 用户登陆
     * @param
     * @return
     */
    @Override
    public TaotaoResult login(String username,String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //校验用户名
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (tbUsers==null||tbUsers.size()==0){
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        TbUser tbUser = tbUsers.get(0);
        //校验密码
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        //获取token
        String token = UUID.randomUUID().toString();
        //清除user的password
        tbUser.setPassword(null);
        //登陆成功，将token放到redis当中
        jedisClient.set("USER_INFO" + ":" + token, JsonUtils.objectToJson(tbUser));
        jedisClient.expire("USER_INFO" + ":" + token,SESSION_EXPIRE);
        return TaotaoResult.ok(token);
    }

    /**
     * 根据token获取user信息
     * @param token
     * @return
     */
    @Override
    public TaotaoResult getUserByToken(String token) {
        //根据token查询redis
        String json = jedisClient.get("USER_INFO" + ":" + token);
        if (StringUtils.isBlank(json)){
            //查询不到数据，通知用户过期
            return TaotaoResult.build(400,"用户登录已经过期，请重新登录");
        }
        //如果查询到数据，则用户已登录---重新设置过期时间
        jedisClient.expire("USER_INFO" + ":" + token,SESSION_EXPIRE);
        //把json转换为tbuser对象，使用taotaoresult包装返回。
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(user);
    }
}
