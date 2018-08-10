package hello;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ValidityTokenPolicy {

    private int validityTokenInMinutes;

    public ValidityTokenPolicy(int validityTokenInMinutes) {
        if (validityTokenInMinutes < 0) {
            throw new RuntimeException("Validity token in minutes must be bigger than 0 ");
        }

        if (validityTokenInMinutes > 60) {
            throw new RuntimeException("Validity token in minutes can`t be bigger than 60 minutes ");
        }
        this.validityTokenInMinutes = validityTokenInMinutes;
    }

    public Date getExpirationDate() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = now.plus(this.validityTokenInMinutes, ChronoUnit.MINUTES);
        Date expirationDate = Date.from(expirationDateTime.toInstant());
        return expirationDate;
    }

    public Date getIssuerDate() {
        ZonedDateTime now = ZonedDateTime.now();
        Date issueDate = Date.from(now.toInstant());
        return issueDate;
    }
}
