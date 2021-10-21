package com.raysofthesun.poswebjava.events;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Component
public class WebSocketsConfig {

	@Bean
	Sinks.Many<String> messageSink() {
		return Sinks.many().multicast().directBestEffort();
	}

	@Bean
	Flux<String> messageEvents(Sinks.Many<String> messageSink) {
		return messageSink.asFlux();
	}

	@Bean
	public WebSocketHandler simpleHandler(Flux<String> messageEvents) {
		return new SimpleHandler(messageEvents);
	}

	@Bean
	public HandlerMapping handlerMapping(WebSocketHandler simpleLoggingHandler) {
		Map<String, WebSocketHandler> handlerMap =
				Map.of("/v1/apply/{agentId}/events", simpleLoggingHandler);

		return new SimpleUrlHandlerMapping(handlerMap, Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter(WebSocketService socketService) {
		return new WebSocketHandlerAdapter(socketService);
	}

	@Bean
	WebSocketService webSocketService() {
		return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
	}
}
