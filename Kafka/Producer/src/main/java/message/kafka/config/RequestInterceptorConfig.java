package message.kafka.config;

import message.kafka.interceptor.RequestInterceptor;
import message.kafka.service.DataProducerService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RequestInterceptorConfig implements WebMvcConfigurer {

    private final DataProducerService dataProducerService;

    public RequestInterceptorConfig(DataProducerService dataProducerService) {
        this.dataProducerService = dataProducerService;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor(dataProducerService))
                .addPathPatterns("/data"); // Apply to specific controller's URL patterns

    }
}
