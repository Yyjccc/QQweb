package com.fas;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketTest {

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connected to server");
		// 在这里可以添加一些初始化代码或者发送一些初始消息
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Received message: " + message);
		// 在这里处理收到的消息
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Connection closed");
	}

	public static void main(String[] args) {
		try {
			// WebSocket 服务器的地址
			String serverUri = "ws://127.0.0.1:8080/chat";
			// 创建 WebSocket 客户端实例
			WebSocketTest client = new WebSocketTest();

			// 连接到服务器
			Session session = ContainerProvider.getWebSocketContainer().connectToServer(client, URI.create(serverUri));
			// 这里可以添加一些额外的代码，例如发送消息
			// Session session = client.getSession();
			 session.getBasicRemote().sendText("Hello, Server!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

