package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//�ͻ���

public class TCPServerSocketClient{

	public static final String IP_ADDR = "localhost";//��������ַ 127.0.0.1
	public static final int PORT = 12345;//�������˿ں�
	
	public static void main(String[] args) {
	
		System.out.println("�ͻ�������...");
		System.out.println("���յ��������˵���Ϣ����Ϊ����ֹ�Ի���ʱ�����Զ���ֹ�ͻ��˳���");
		System.out.println("���Կ�ʼ������~\n");
		
		while (true) {
			Socket socket = null;
			try {
				
				// �����׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ں�
				socket = new Socket(IP_ADDR, PORT);
				
				// ��ȡ�������˴��ݵ�����
				DataInputStream input = new DataInputStream(socket.getInputStream());
				
				// ��������˷�������
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				System.out.print("�� �ͻ��� ��: ");
				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeUTF(str);
				String ret = input.readUTF();
				System.out.println("�� ������ ��: " + ret);
				 
				// �յ� StopTalkʱ�Ͽ�����
				if ("��ֹ�Ի�".equalsIgnoreCase(ret)) {
					System.out.println("�ͻ��˹ر����ӡ�");
					Thread.sleep(500);
					break;	
				}
				
				out.close();
				input.close();
				
			} catch (Exception e) {
				
				System.out.println("�ͻ����쳣: " + e.getMessage());
				
			} finally {
				
				if (socket != null) {
					
					try {
						socket.close();
					} catch (IOException e) {
						socket = null;
						System.out.println("�ͻ��������쳣: " + e.getMessage());
					}
				}
			}
		}
	}
}