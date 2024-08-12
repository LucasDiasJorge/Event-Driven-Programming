package message.kafka.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import message.kafka.filter.util.CachedBodyHttpServletRequest;
import message.kafka.service.DataProducerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Component
@Order(1)
public class ProducerFilter implements Filter {

    private Logger logger = LogManager.getLogger("ProducerFilterLogger");

    private final DataProducerService dataProducerService;

    public ProducerFilter(DataProducerService dataProducerService) {
        this.dataProducerService = dataProducerService;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        logger.info("Starting a transaction for req : {}", req.getRequestURI());

        // Wrap the request to cache the body
        CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(req);

        // Reading the request body
        String body = new String(cachedBodyRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // Sending the message to the DataProducerService
        dataProducerService.sendMessage((Serializable) body);

        // Continue with the filter chain
        chain.doFilter(cachedBodyRequest, response);

        logger.info("Committing a transaction for req : {}", req.getRequestURI());
    }
}
