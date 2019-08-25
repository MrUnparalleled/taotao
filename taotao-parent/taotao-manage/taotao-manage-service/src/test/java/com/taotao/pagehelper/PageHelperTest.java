package com.taotao.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class PageHelperTest {

    @Test
    public void testPageHelper() throws Exception{
        //初始化sprig容器
        ApplicationContext applicationContext =new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        //从容器中获得Mapper代理对象
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
        //执行sql预计之前设置分页信息，使用pageHelper的startPage方法
        PageHelper.startPage(1,30);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //去分页信息，PageInfo 1、总记录数 2、总页数 3、当前页码
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
        System.out.println(pageInfo.getPageNum());
        System.out.println(pageInfo.getPageSize());
    }
}
