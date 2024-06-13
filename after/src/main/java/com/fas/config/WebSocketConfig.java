package com.fas.config;

import com.fas.websocket.CustomHandshakeInterceptor;
import com.fas.websocket.WebSocketAPIHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;


import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
@Data
@NoArgsConstructor
public class WebSocketConfig  implements WebSocketConfigurer {

	@Autowired
	private CustomHandshakeInterceptor customHandshakeInterceptor;

	@Autowired
	private WebSocketAPIHandler webSocketAPIHandler;



	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(webSocketAPIHandler, "/api")
				.addInterceptors(customHandshakeInterceptor)
				.setAllowedOrigins("*")
		;
	}
}
