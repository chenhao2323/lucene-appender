package test;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MDCSpringConfig: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-1 15:02
 */
@Configuration
public class MDCSpringConfig {

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean<MDCInsertingServletFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MDCInsertingServletFilter());
        registration.addUrlPatterns("/*");
        registration.setName("LogMDCRequstFilter");
        registration.setOrder(1);
        return registration;
    }
}
