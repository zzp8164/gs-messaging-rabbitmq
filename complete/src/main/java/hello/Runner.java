package hello;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static hello.Application.QUEUE_NAME;
import static hello.Application.SPRING_BOOT_EXCHANGE_WITH_FANOUT;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final ConfigurableApplicationContext context;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate,
                  ConfigurableApplicationContext context) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.setExchange(SPRING_BOOT_EXCHANGE_WITH_FANOUT);
        rabbitTemplate.convertAndSend(QUEUE_NAME, "Hello from RabbitMQ!" + QUEUE_NAME);
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        context.close();
    }

}
