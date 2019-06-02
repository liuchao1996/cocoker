package com.cocoker;

import com.cocoker.utils.Mywebebsocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class CocokerApplication {

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(CocokerApplication.class);
		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		// 解决WebSocket不能注入的问题
		Mywebebsocket.setApplicationContext(configurableApplicationContext);
	}

}

