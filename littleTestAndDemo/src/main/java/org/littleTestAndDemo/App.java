package org.littleTestAndDemo;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;


public class App {
    public static final String queue_name="my_queue";
    public static final boolean durable=true; //消息队列持久化
    public static void main(String[] args)
    throws java.io.IOException, TimeoutException{
    	
    	String host = "10.2.21.96";
    	
        ConnectionFactory factory=new ConnectionFactory(); //创建连接工厂
        factory.setHost(host);
        //factory.setVirtualHost("my_mq");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection=factory.newConnection(); //创建连接
        Channel channel=connection.createChannel();//创建信道
        
        channel.exchangeDeclare(queue_name,"direct",true);
        channel.queueDeclare(queue_name, durable, false, false, null); //声明消息队列，且为可持久化的
        channel.queueBind(queue_name, queue_name, queue_name);
        
        String message="Hello world"+Math.random();
        //将队列设置为持久化之后，还需要将消息也设为可持久化的，MessageProperties.PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", queue_name, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println("Send message:"+message);
        channel.close();
        connection.close();
    }

}