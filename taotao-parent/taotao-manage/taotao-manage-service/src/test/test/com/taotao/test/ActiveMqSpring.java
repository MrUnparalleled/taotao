package com.taotao.test;

import com.taotao.pojo.TbItem;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.print.attribute.standard.Destination;

public class ActiveMqSpring {

    @Test
    public void sendMessage(){
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //从容器中获得jmstemplate对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //从容器中获取一个destination对象
        Queue destination = (Queue) applicationContext.getBean("queueDestination");
        //发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("send activemq");
            }
        });
    }






    @Test
    public void test(){


        TbItem item = new TbItem();
        add(item);
        System.out.println(item.getId());
    }


    public void add(TbItem a){
        a.setId((long) 1);
    }
}
