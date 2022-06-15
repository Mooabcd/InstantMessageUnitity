package server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

//服务器

public class TCPServerSocket{

	String file = "msg.txt";
	
	public static final int PORT = 12345;// 监听的端口号
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("服务器启动...\n");
		TCPServerSocket server = new TCPServerSocket();
		server.init();
	}
	
	public void init() throws IOException {
	
		//如日志文件不存在，则创建
		File f = new File(file);
		if (!f.exists())
			f.createNewFile();
		
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				
				// 一旦阻塞,则表示服务器与客户端获得了连接
				Socket client = serverSocket.accept();
				
				// 处理这次连接
				new HandlerThread(client);
			}
		
		} catch (Exception e) {
			System.out.println("Server Exception: " + e.getMessage());
		}
		
	}
	
	private class HandlerThread implements Runnable {
	
		private Socket socket;
		
		public HandlerThread(Socket client) {
			socket = client;
			new Thread(this).start();
		}
		
		public void run() {
			
			try {
				
				// 读取客户端数据
				DataInputStream input = new DataInputStream(socket.getInputStream());
				String clientInputStr = input.readUTF();
				System.out.println("【 客户端 】: " + clientInputStr);
				
				// 向客户端回复信息
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				System.out.print("【 服务器 】: ");
				
				// 发送信息
				String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeUTF(s);
				
				//-记录聊天信息到文本文件中---
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
																	
				FileOutputStream fos = new FileOutputStream(file,true);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				String t = formatter.format(date);
//				bos.write(t.getBytes());
				
				String m = "--------------------------"+t+"--------------------------\r\n";
				bos.write(m.getBytes());
				m = "【 客户端 】: " + clientInputStr + "\r\n";
				bos.write(m.getBytes());
				m = "【 服务器 】: " + s + "\r\n\n";
				bos.write(m.getBytes());
				if(s.equals("终止对话")) {
					m = "***************************************************************************\r\n\n";
					bos.write(m.getBytes());
				}
				
				
				//关闭释放资源
				out.close();
				input.close();
				bos.close();
				fos.close();
			
			} catch (Exception e) {
			
				System.out.println("服务器异常: \n " + e.getMessage());
			
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("服务器运行异常: \n" + e.getMessage());
					}
				}
			}
		}
	}
}

