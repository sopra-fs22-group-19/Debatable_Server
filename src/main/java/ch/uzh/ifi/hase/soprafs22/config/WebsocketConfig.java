package ch.uzh.ifi.hase.soprafs22.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
    public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Set prefix for the endpoint that the client listens for our messages from
        registry.enableSimpleBroker("/debates");

        // Set prefix for endpoints the client will send messages to
        registry.setApplicationDestinationPrefixes("/ws");

    }


    //register STOMP endpoint
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the endpoint where the connection will take place
        registry.addEndpoint("/ws-endpoint")
                .setAllowedOrigins("http://localhost:3000", "https://sopra-fs22-group19-client.herokuapp.com").withSockJS();
    }
}
