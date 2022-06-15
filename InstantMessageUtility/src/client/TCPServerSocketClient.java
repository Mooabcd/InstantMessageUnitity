package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//客户端

public class TCPServerSocketClient{

	public static final String IP_ADDR = "localhost";//服务器地址 127.0.0.1
	public static final int PORT = 12345;//服务器端口号
	
	public static void main(String[] args) {
	
		System.out.println("客户端启动...");
		System.out.println("当收到服务器端的信息内容为“终止对话”时，将自动终止客户端程序。");
		System.out.println("可以开始聊天啦~\n");
		
		while (true) {
			Socket socket = null;
			try {
				
				// 创建套接字并将其连接到指定主机上的指定端口号
				socket = new Socket(IP_ADDR, PORT);
				
				// 读取服务器端传递的数据
				DataInputStream input = new DataInputStream(socket.getInputStream());
				
				// 向服务器端发送数据
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				System.out.print("【 客户端 】: ");
				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeUTF(str);
				String ret = input.readUTF();
				System.out.println("【 服务器 】: " + ret);
				 
				// 收到 StopTalk时断开连接
				if ("终止对话".equalsIgnoreCase(ret)) {
					System.out.println("客户端关闭连接。");
					Thread.sleep(500);
					break;	
				}
				
				out.close();
				input.close();
				
			} catch (Exception e) {
				
				System.out.println("客户端异常: " + e.getMessage());
				
			} finally {
				
				if (socket != null) {
					
					try {
						socket.close();
					} catch (IOException e) {
						socket = null;
						System.out.println("客户端运行异常: " + e.getMessage());
					}
				}
			}
		}
	}
}