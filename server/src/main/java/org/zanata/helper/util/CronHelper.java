package org.zanata.helper.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alex Eng <a href="mailto:aeng@redhat.com">aeng@redhat.com</a>
 */
public final class CronHelper {
    public static CronType getTypeFromExpression(String expression) {
        if (expression.equals(CronType.THRITY_SECONDS.getExpression())) {
            return CronType.THRITY_SECONDS;
        } else if (expression.equals(CronType.ONE_MINUTE.getExpression())) {
            return CronType.ONE_MINUTE;
        } else if (expression.equals(CronType.FIVE_MINUTES.getExpression())) {
            return CronType.FIVE_MINUTES;
        } else if (expression.equals(CronType.TEN_MINUTES.getExpression())) {
            return CronType.TEN_MINUTES;
        } else if (expression
            .equals(CronType.FIFTEEN_MINUTES.getExpression())) {
            return CronType.FIFTEEN_MINUTES;
        }
        return CronType.FIVE_MINUTES;
    }

    public static String getExpressionFromDisplay(String display) {
        if (display.equals(CronType.THRITY_SECONDS.getDisplay())) {
            return CronType.THRITY_SECONDS.getExpression();
        } else if (display.equals(CronType.ONE_MINUTE.getDisplay())) {
            return CronType.ONE_MINUTE.getExpression();
        } else if (display.equals(CronType.FIVE_MINUTES.getDisplay())) {
            return CronType.FIVE_MINUTES.getExpression();
        } else if (display.equals(CronType.TEN_MINUTES.getDisplay())) {
            return CronType.TEN_MINUTES.getExpression();
        } else if (display.equals(CronType.FIFTEEN_MINUTES.getDisplay())) {
            return CronType.FIFTEEN_MINUTES.getExpression();
        }
        return CronType.FIVE_MINUTES.getExpression();
    }

    @Getter
    public enum CronType {
        THRITY_SECONDS("30 seconds", "0/30 * * * * ?"), ONE_MINUTE("1 minute",
            "1 * * * * ?"), FIVE_MINUTES("5 minutes", "5 * * * * ?"),
        TEN_MINUTES("10 minutes", "10 * * * * ?"), FIFTEEN_MINUTES(
            "15 minutes", "15 * * * * ?");

        private final String display;
        private final String expression;

        CronType(String display, String expression) {
            this.display = display;
            this.expression = expression;
        }
    }
}
