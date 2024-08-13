package message.kafka.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import message.kafka.service.DataProducerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private Logger logger = LogManager.getLogger("ProducerInterceptorLogger");

    private final DataProducerService dataProducerService;

    public RequestInterceptor(DataProducerService dataProducerService) {
        this.dataProducerService = dataProducerService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        HttpServletRequest req = request;

        Map<String, Object> map = new HashMap<>();

        map.put("message","message");
        map.put("topic","topic");
        map.put("principal",req.getUserPrincipal().getName());
        map.put("uri",req.getRequestURI());

        logger.info("Sending the message to the DataProducerService");

        dataProducerService.sendMessage((Serializable) map);

        logger.info("Message sent");

    }
}
