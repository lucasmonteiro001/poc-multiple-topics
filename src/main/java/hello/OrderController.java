package hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;

@Controller
public class OrderController {


    @MessageMapping("/update/{name}")
    @SendTo("/topic/order/{name}")
    public OrderEventResponse orderEventHandler(OrderMessage message, @DestinationVariable String name) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new OrderEventResponse("Event = " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}
