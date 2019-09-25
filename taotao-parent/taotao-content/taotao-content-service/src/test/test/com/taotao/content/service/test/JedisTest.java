package com.taotao.content.service.test;

import com.taotao.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {


    /**
     * 连接单击版
     * @throws Exception
     */
    @Test
    public void testJedis() throws Exception{
        //创建一个Jedis对象，参数：host、port
        Jedis jedis = new Jedis("192.168.25.128",6379);
        //直接使用jedis操作redis。所有jedis的命令对应一个方法
        jedis.set("test123","jedis test");
        String string = jedis.get("test123");
        System.out.println(string);
        //关闭链接
        jedis.close();
    }

    /**
     * 连接池单击版
     * @throws Exception
     */
    @Test
    public void testJedisPool() throws Exception{
        //创建一个连接池
        JedisPool pool = new JedisPool("192.168.25.128",6379);
        //获取一个jedis
        Jedis jedis = pool.getResource();
        //使用jedis操作数据库
        jedis.set("first","1");
        //查询
        String s = jedis.get("test123");
        System.out.println(s);
        String s1 = jedis.get("first");
        System.out.println(s1);
        //关闭连接池
        pool.close();
    }


    /**
     * 连接集群版
     * @throws Exception
     */
    @Test
    public void testJedisCluster() throws Exception{
        //使用jedisCluster对象，需在一个Set<HostAndPort>参数。reids节点的列表。
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.128",7001));
        nodes.add(new HostAndPort("192.168.25.128",7002));
        nodes.add(new HostAndPort("192.168.25.128",7003));
        nodes.add(new HostAndPort("192.168.25.128",7004));
        nodes.add(new HostAndPort("192.168.25.128",7005));
        nodes.add(new HostAndPort("192.168.25.128",7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用jedisCluster对象操作redis，在系统中单例存在
        jedisCluster.set("jedisCluster","jedisClusterTest");
        //打印结果
        String jedisCluster1 = jedisCluster.get("jedisCluster");
        System.out.println(jedisCluster1);
        //系统关闭之前,关闭jedisCluster
        jedisCluster.close();
    }

    @Test
    public void testJedisClient() throws Exception{
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //从容器中获取jedisClient对象
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("first","100");
        String first = jedisClient.get("first");
        System.out.println(first);
    }
}
