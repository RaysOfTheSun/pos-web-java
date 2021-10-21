package com.raysofthesun.poswebjava.events;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;


public class SimpleHandler implements WebSocketHandler {

	private final Flux<String> events;

	public SimpleHandler(Flux<String> events) {
		this.events = events;
	}

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		return webSocketSession.send(events
				.filterWhen((message) ->
						Mono.just(isMessageForSocket(message, webSocketSession.getHandshakeInfo().getUri())))
				.map(webSocketSession::textMessage));

	}

	private boolean isMessageForSocket(String message, URI uri) {
		String agentIdFromUri = uri.getPath().split("/")[3];
		String agentIdFromMessage = message.split(":")[0];

		return agentIdFromUri.equals(agentIdFromMessage);
	}
}
