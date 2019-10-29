package com.taotao.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;


public class ActiveMQTest {

    /**
     * 点对点形式发送
     * @throws JMSException
     */
    @Test
    public void testQueueProducer() throws JMSException {
        //1.创建一个工厂对象，需要制定ip以及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        //2.使用工厂对象创建一个connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接，调用connection对象的start方法
        connection.start();
        //4.创建一个session对象----
            // 第一个参数：是都开启事务。如果true开启事务，第二个参数无意义。一般不开启事务
            // 第二个参数：应答模式。自动应答或者是手动应答。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用session对象创建一个destination对象。两种形式queue、topic，现在使用queue
        Queue queue = session.createQueue("test-queue");
        //6.使用session对象创建一个produce对象
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个message对象，可以使用txtMessage
       /* TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("hello activeMQ");*/
        TextMessage textMessage = session.createTextMessage("hello,activeMQ");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testQueueConsumer() throws JMSException, IOException {
        //创建一个ConnectionFactory对象链接到MQ服务器
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        //创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用connection创建一个session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个destination 对象。queue对象
        Queue queue = session.createQueue("test-queue");
        //创建一个消费者对象
        MessageConsumer consumer = session.createConsumer(queue);
        //接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //打印结果
                TextMessage textMessage = (TextMessage) message;
                String text = null;
                try {
                    text = textMessage.getText();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                System.out.println(text);
            }
        });
        //等待接收消息
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();

    }


    /**
     * 发布订阅
     * @throws JMSException
     */
    @Test
    public void testQueueProducer2() throws JMSException {
        //1.创建一个工厂对象，需要制定ip以及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        //2.使用工厂对象创建一个connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接，调用connection对象的start方法
        connection.start();
        //4.创建一个session对象----
        // 第一个参数：是都开启事务。如果true开启事务，第二个参数无意义。一般不开启事务
        // 第二个参数：应答模式。自动应答或者是手动应答。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用session对象创建一个destination对象。两种形式queue、topic，现在使用topic
        Topic topic = session.createTopic("test-topic");
        //6.使用session对象创建一个produce对象
        MessageProducer producer = session.createProducer(topic);
        //7.创建一个message对象，可以使用txtMessage
       /* TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("hello activeMQ");*/
        TextMessage textMessage = session.createTextMessage("hello,activeMQ");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testQueueConsumer2() throws JMSException, IOException {
        //创建一个ConnectionFactory对象链接到MQ服务器
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        //创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用connection创建一个session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个destination 对象。queue对象
        Topic topic = session.createTopic("test-topic");
        //创建一个消费者对象
        MessageConsumer consumer = session.createConsumer(topic);
        //接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //打印结果
                TextMessage textMessage = (TextMessage) message;
                String text = null;
                try {
                    text = textMessage.getText();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                System.out.println(text);
            }
        });
        //等待接收消息
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
