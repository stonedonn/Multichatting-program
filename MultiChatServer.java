package chatting;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;
import java.util.logging.*;

public class MultiChatServer {

	// ���� ���� �� Ŭ���̾�Ʈ ���� ����
	private ServerSocket ss = null;
	private Socket s = null;

	// ����� Ŭ���̾�Ʈ �����带 �����ϴ� ArrayList
	ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();

	// �ΰ� ��ü
	Logger logger;

	// ��Ƽ ä�� ���� ���α׷� �κ�
	public void start() {
		logger = Logger.getLogger(this.getClass().getName());

		try {
			// ���� ���� ����
			ss = new ServerSocket(8888);
			logger.info("MultiCharServer start");

			// ���� ������ ���鼭 Ŭ���̾�Ʈ ������ ��ٸ���
			while (true) {
				s = ss.accept();
				// ����� Ŭ���̾�Ʈ�� ���� ������ Ŭ���� ����
				ChatThread chat = new ChatThread();
				// Ŭ���̾�Ʈ ����Ʈ �߰�
				chatThreads.add(chat);
				// ������ ����
				chat.start();
			}
		} catch (Exception e) {
			logger.info("[MultiChatServer]start() Exception �߻�!!");
			e.printStackTrace();

		}
	}
	void msgSendAll(String msg) {
        for(ChatThread ct : chatThreads) {
           ct.outMsg.println(msg);
        }
     }
	class ChatThread extends Thread{
      //���� �޼��� �� �Ľ� �޼��� ó���� ���� ���� ����
      String msg;
      //�޼��� ��ü ����
      Message m = new Message();
      //JSON �ļ� �ʱ�ȭ
      Gson gson = new Gson();
      //����� ��Ʈ��
      private BufferedReader inMsg = null;
      private PrintWriter outMsg = null;

  	void msgSendSelect(String msg1) {
  		String[] temp2 = msg1.split(" ");
  		System.out.println(temp2[0].substring(1));
  		for(ChatThread ct1 : chatThreads) {
  	         	if(ct1.m.getId().equals(temp2[0].substring(1))) {
  	         		StringBuffer buffer = new StringBuffer();
  	         		for(int i = 1 ; i< temp2.length;i++ ) {
  	         			buffer.append(temp2[i]);
  	         			buffer.append(" ");
  	         		}
  	         		ct1.outMsg.println(gson.toJson(new Message(m.getId(),"",buffer.toString(),"secret")));
  	         		// ct1.outMsg.println(temp3);
  	         	}
  	        }
  	}
      public void run() {   
         boolean status = true;
         logger.info("ChatThread start!");
         
               try {
                  inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
                  outMsg = new PrintWriter(s.getOutputStream(),true);
                  while(status) {
                      //���ŵ� �޽����� msg������ ����                  
                   msg = inMsg.readLine();                    
                   //JSON �޽����� Message ��ü�� ����
                   m = gson.fromJson(msg,Message.class);
                   //�Ľ̵� ���ڿ� �迭�� �ι�° ��Ұ��� ���� ó��
                   //�α׾ƿ� �޽����϶�
                   if(m.getType().equals("logout")) {
                      chatThreads.remove(this);
                      msgSendAll(gson.toJson(new Message(m.getId(), "", "���� �����߽��ϴ�.", "server")));
                      //�ش� Ŭ���̾�Ʈ ������ ����� status�� false�� ����
                      status = false;
                   }
                   //�α��� �޽��� �϶�
                   else if(m.getType().equals("login")) {
                      msgSendAll(gson.toJson(new Message(m.getId(), "", "���� �α��� �߽��ϴ�.","server")));
                   }
                   else if(m.getType().equals("secret")) {
                       msgSendSelect(m.getMsg());
                    }
                   //�� ���� ���, �� �Ϲ� �޽��� �϶�
                   else {
                   	msgSendAll(msg);
                   }
                   //������ ����� Ŭ���̾�Ʈ ������ ����ǹǷ� ������ ���ͷ�Ʈ
                
                  }
                  this.interrupt();
                  logger.info(this.getName()+" �����!");
               } catch (IOException e) {
                  
                  e.printStackTrace();
               }     
   }      
      //����� ��� Ŭ���̾�Ʈ�� �޽��� �߰�
      
   }
	public static void main(String[] args) {
		MultiChatServer server = new MultiChatServer();
		server.start();
	}
}