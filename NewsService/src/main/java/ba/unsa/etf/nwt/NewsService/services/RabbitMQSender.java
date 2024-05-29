package ba.unsa.etf.nwt.NewsService.services;

import ba.unsa.etf.nwt.NewsService.model.Notification;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${NewsService.rabbitmq.exchange}")
    private String exchange;

    @Value("${NewsService.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Notification company) {
        rabbitTemplate.convertAndSend(exchange, routingkey, company);
        System.out.println("Send msg = " + company);

    }
}