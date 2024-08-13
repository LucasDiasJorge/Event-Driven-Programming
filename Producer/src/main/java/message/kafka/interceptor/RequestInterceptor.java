package message.kafka.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import message.kafka.util.CachedBodyHttpServletRequest;
import message.kafka.service.DataProducerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private Logger logger = LogManager.getLogger("ProducerInterceptorLogger");

    private final DataProducerService dataProducerService;

    public RequestInterceptor(DataProducerService dataProducerService) {
        this.dataProducerService = dataProducerService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        HttpServletRequest req = request;

        CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(req);

        // Reading the request body
        String body = new String(cachedBodyRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        logger.info("Sending the message to the DataProducerService");

        dataProducerService.sendMessage((Serializable) body);

        logger.info("Message sent");

    }
}
