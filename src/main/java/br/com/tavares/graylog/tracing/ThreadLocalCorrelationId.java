package br.com.tavares.graylog.tracing;

import org.slf4j.MDC;
import org.zalando.logbook.CorrelationId;
import org.zalando.logbook.HttpRequest;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalCorrelationId implements CorrelationId {

    @Override
    public String generate(HttpRequest request) {
        if ("org.zalando.logbook.spring.LocalRequest".equals(request.getClass().getName())) {
            return generateCorrelationId();
        }
        updateLoggerMDC();
        return MDC.get("correlationId");
    }

    public static void updateLoggerMDC() {
        MDC.put("correlationId", generateCorrelationId());
    }

    public static String generateCorrelationId() {
        final Random random = ThreadLocalRandom.current();
        // set most significant bit to produce fixed length string
        return Long.toHexString(random.nextLong() | Long.MIN_VALUE);
    }


}
