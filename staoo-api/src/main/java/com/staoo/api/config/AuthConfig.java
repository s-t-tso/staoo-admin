package com.staoo.api.config;

import com.staoo.api.config.ApiPrefixConfig;
import com.staoo.framework.auth.filter.JwtAuthenticationFilter;
import com.staoo.framework.interceptor.TenantInterceptor;
import com.staoo.framework.interceptor.UserContextInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * 认证配置类
 * 配置Spring Security和JWT认证相关的设置
 */
@Configuration
@EnableWebSecurity
public class AuthConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(AuthConfig.class);

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private UserContextInterceptor userContextInterceptor;
    
    @Autowired
    private ApiPrefixConfig apiPrefixConfig;

    public AuthConfig() {
        logger.info("AuthConfig initialized");
    }

    /**
     * 配置Spring Security过滤链
     */
    @Bean
    @Order(2)  // 设置低于SpringDocSecurityConfig的优先级
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring main SecurityFilterChain for application");

        http
                // 关闭CSRF保护，因为我们使用JWT进行认证
                .csrf(csrf -> csrf.disable())
                // 设置会话管理为无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用Basic认证
                .httpBasic(httpBasic -> httpBasic.disable())
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 首先明确允许API文档相关路径
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        // 允许公开访问的认证端点，使用动态API前缀
                        .requestMatchers("/" + apiPrefixConfig.getPrefix() + "/auth/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/" + apiPrefixConfig.getPrefix() + "/public/**").permitAll()
                        .requestMatchers("/").permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("Main SecurityFilterChain configured successfully");
        return http.build();
    }

    /**
     * 创建BCrypt密码编码器
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置CORS
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的来源 - 明确指定，因为allowCredentials为true时不能使用通配符*
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:8080", 
            "http://localhost:5173", 
            "http://127.0.0.1:8080", 
            "http://127.0.0.1:5173",
            "http://localhost:3000"
        ));
        // 允许的方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 允许凭证
        configuration.setAllowCredentials(true);
        // 最大缓存时间
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 应用到所有路径
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 创建CORS过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
    
    /**
     * 注册拦截器
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册租户拦截器，拦截所有请求
        registry.addInterceptor(new TenantInterceptor())
                .addPathPatterns("/**")
                // 排除静态资源和特定接口
                .excludePathPatterns(
                        "/error",
                        // 接口文档相关路径
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/webjars/**"
                );
                
        // 注册用户上下文拦截器，在请求完成后清理ThreadLocal中的用户信息
        registry.addInterceptor(userContextInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/" + apiPrefixConfig.getPrefix() + "/auth/login", "/" + apiPrefixConfig.getPrefix() + "/auth/logout", "/" + apiPrefixConfig.getPrefix() + "/auth/refresh", "/" + apiPrefixConfig.getPrefix() + "/public/") // 排除认证相关接口
                .excludePathPatterns("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs", "/webjars/**"); // 排除Swagger相关接口
    }
}
