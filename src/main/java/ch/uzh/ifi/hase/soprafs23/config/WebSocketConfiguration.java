package ch.uzh.ifi.hase.soprafs23.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    
    private static final String ORIGIN_LOCALHOST = "http://localhost:3000";
    private static final String ORIGIN_PROD = "http://sopra-fs23-group-30-client-new.oa.r.appspot.com";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/inventories", "/applications")
                .setHeartbeatValue(new long[] {1000, 1000})
                .setTaskScheduler(heartBeatScheduler())
                .setHeartbeatValue(new long[] {60000, 120000});
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
        .setAllowedOrigins(ORIGIN_LOCALHOST, ORIGIN_PROD)
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .addInterceptors(new HttpSessionHandshakeInterceptor());

        registry.addEndpoint("/wss")
        .setAllowedOrigins(ORIGIN_LOCALHOST, ORIGIN_PROD)
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxSessionIdleTimeout(-1L);
        return container;
    }

    @Bean
    public TaskScheduler heartBeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        return scheduler;
    }
}
