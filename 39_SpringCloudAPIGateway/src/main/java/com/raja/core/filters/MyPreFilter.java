package com.raja.core.filters;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MyPreFilter implements GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpResponse serverHttpResponse =exchange.getResponse();
		HttpHeaders  headersList=exchange.getRequest().getHeaders();
		Set<String> keySet = headersList.keySet();
		keySet.forEach(key -> {
			List<String> values = headersList.get(key);
			System.out.println(key +" :: "+values);
		});

		
		ArrayList<String> values =new ArrayList();
		values.addAll(keySet);
		for (String keyValue : values) {
			if(keyValue.equalsIgnoreCase("Postman-Token")) {
				serverHttpResponse.setStatusCode(HttpStatusCode.valueOf(404));
				 byte[] response = "{\"status\":\"error\",\"message\":\"request from postman occured\",\"errorcode\":\"410\"}".getBytes(StandardCharsets.UTF_8);
			        org.springframework.core.io.buffer.DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(response);
			      return exchange.getResponse().writeWith(Flux.just(buffer));
			        		
			}
			System.out.println("for each:" +" :: "+keyValue);
		}
		
	

		return chain.filter(exchange);
	}

}
