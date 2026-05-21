package com.cts.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// FeignClientConfig — Header Propagation
//
// When this service calls another service via Feign, this interceptor
// automatically copies X-User-Email and X-User-Role from the current
// incoming HTTP request and adds them to the outgoing Feign request.
//
// Why this matters:
//   Client → Gateway (validates JWT, injects X-User-Email, X-User-Role)
//        → ServiceA (reads role, calls ServiceB via Feign)
//             → ServiceB (also needs the role header for its own checks)
//
// Without this config, ServiceB would receive a Feign call with no role
// header and would reject it as Forbidden.
@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor roleHeaderPropagationInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                String role  = attributes.getRequest().getHeader("X-User-Role");
                String email = attributes.getRequest().getHeader("X-User-Email");

                if (role  != null) requestTemplate.header("X-User-Role",  role);
                if (email != null) requestTemplate.header("X-User-Email", email);
            }
        };
    }
}
