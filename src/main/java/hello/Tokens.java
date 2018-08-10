package hello;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class Tokens {


    public static boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(CreateOrderController.secretKeyInBase64).parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException
                | IllegalArgumentException e) {
            throw new RuntimeException("erro");
        }
    }

    public static boolean validaToken(String token) {
        try {
            Jwts.parser().setSigningKey(CreateOrderController.secretKeyInBase64).parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
          return false;
        }
    }

}
