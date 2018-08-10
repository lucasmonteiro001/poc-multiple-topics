package hello;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
public class CreateOrderController {

    public static String secretKeyInBase64 = "YXNkZg==";
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private ValidityTokenPolicy validityToken = new ValidityTokenPolicy(1);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @RequestMapping(value = "/orders/addNewMessage", method = RequestMethod.POST)
    public void addMessage(@RequestParam String orderId, @RequestBody String origin) throws Exception {
        String token = WebTokenManager.tokenOrderRelation.get(orderId);
        if (token == null || !Tokens.validaToken(token)) {
            simpMessagingTemplate.convertAndSend("/topic/order/" + orderId, "Autenticacao Expirada");
        } else {

            simpMessagingTemplate.convertAndSend("/topic/order/" + orderId, origin);
        }


    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public String createOrderHandler(@RequestBody String origin) throws Exception {
        String orderId = new Integer(atomicInteger.getAndIncrement()).toString();
        String token = createToken(orderId);

        WebTokenManager.tokenOrderRelation.put(token, orderId);
        WebTokenManager.tokenOrderRelation.put(orderId, token);

        return origin + ">><<" + orderId + ">><<" + token;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public String refreshToken(@RequestParam  String token) throws Exception {

        String orderId = WebTokenManager.tokenOrderRelation.get(token);
        String newtoken =  "";
        if ( orderId != null && !orderId.isEmpty()){
            newtoken = createToken(orderId);

            WebTokenManager.tokenOrderRelation.remove(token);
            WebTokenManager.tokenOrderRelation.put(newtoken, orderId);
            WebTokenManager.tokenOrderRelation.put(orderId, newtoken);
        }
        return newtoken;
    }

    private String createToken(String orderId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("orderId", orderId);

        return Jwts.builder().setSubject("ws authentication").setClaims(parameters)
                .signWith(SignatureAlgorithm.HS256, this.secretKeyInBase64)
                .setIssuedAt(this.validityToken.getIssuerDate()).setExpiration(this.validityToken.getExpirationDate())
                .compact();

    }

}
