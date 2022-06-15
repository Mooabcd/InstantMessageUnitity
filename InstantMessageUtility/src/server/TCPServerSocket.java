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

//������

public class TCPServerSocket{

	String file = "msg.txt";
	
	public static final int PORT = 12345;// �����Ķ˿ں�
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("����������...\n");
		TCPServerSocket server = new TCPServerSocket();
		server.init();
	}
	
	public void init() throws IOException {
	
		//����־�ļ������ڣ��򴴽�
		File f = new File(file);
		if (!f.exists())
			f.createNewFile();
		
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				
				// һ������,���ʾ��������ͻ��˻��������
				Socket client = serverSocket.accept();
				
				// �����������
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
				
				// ��ȡ�ͻ�������
				DataInputStream input = new DataInputStream(socket.getInputStream());
				String clientInputStr = input.readUTF();
				System.out.println("�� �ͻ��� ��: " + clientInputStr);
				
				// ��ͻ��˻ظ���Ϣ
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				System.out.print("�� ������ ��: ");
				
				// ������Ϣ
				String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeUTF(s);
				
				//-��¼������Ϣ���ı��ļ���---
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
																	
				FileOutputStream fos = new FileOutputStream(file,true);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				String t = formatter.format(date);
//				bos.write(t.getBytes());
				
				String m = "--------------------------"+t+"--------------------------\r\n";
				bos.write(m.getBytes());
				m = "�� �ͻ��� ��: " + clientInputStr + "\r\n";
				bos.write(m.getBytes());
				m = "�� ������ ��: " + s + "\r\n\n";
				bos.write(m.getBytes());
				if(s.equals("��ֹ�Ի�")) {
					m = "***************************************************************************\r\n\n";
					bos.write(m.getBytes());
				}
				
				
				//�ر��ͷ���Դ
				out.close();
				input.close();
				bos.close();
				fos.close();
			
			} catch (Exception e) {
			
				System.out.println("�������쳣: \n " + e.getMessage());
			
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("�����������쳣: \n" + e.getMessage());
					}
				}
			}
		}
	}
}

