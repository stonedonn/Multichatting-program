package chatting;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;
import java.util.logging.*;

public class MultiChatServer {

	// 서버 소켓 및 클라이언트 연결 소켓
	private ServerSocket ss = null;
	private Socket s = null;

	// 연결된 클라이언트 스레드를 관리하는 ArrayList
	ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();

	// 로거 객체
	Logger logger;

	// 멀티 채팅 메인 프로그램 부분
	public void start() {
		logger = Logger.getLogger(this.getClass().getName());

		try {
			// 서버 소켓 생성
			ss = new ServerSocket(8888);
			logger.info("MultiCharServer start");

			// 무한 루프를 돌면서 클라이언트 연결을 기다린다
			while (true) {
				s = ss.accept();
				// 연결된 클라이언트에 대해 스레드 클래스 생성
				ChatThread chat = new ChatThread();
				// 클라이언트 리스트 추가
				chatThreads.add(chat);
				// 스레드 시작
				chat.start();
			}
		} catch (Exception e) {
			logger.info("[MultiChatServer]start() Exception 발생!!");
			e.printStackTrace();

		}
	}
	void msgSendAll(String msg) {
        for(ChatThread ct : chatThreads) {
           ct.outMsg.println(msg);
        }
     }
	class ChatThread extends Thread{
      //수신 메세지 및 파싱 메세지 처리를 위한 변수 선언
      String msg;
      //메세지 객체 생성
      Message m = new Message();
      //JSON 파서 초기화
      Gson gson = new Gson();
      //입출력 스트림
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
                      //수신된 메시지를 msg변수에 저장                  
                   msg = inMsg.readLine();                    
                   //JSON 메시지를 Message 객체로 매핑
                   m = gson.fromJson(msg,Message.class);
                   //파싱된 문자열 배열의 두번째 요소값에 따라 처리
                   //로그아웃 메시지일때
                   if(m.getType().equals("logout")) {
                      chatThreads.remove(this);
                      msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 종료했습니다.", "server")));
                      //해당 클라이언트 스레드 종료로 status를 false로 설정
                      status = false;
                   }
                   //로그인 메시지 일때
                   else if(m.getType().equals("login")) {
                      msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 로그인 했습니다.","server")));
                   }
                   else if(m.getType().equals("secret")) {
                       msgSendSelect(m.getMsg());
                    }
                   //그 밖의 경우, 즉 일반 메시지 일때
                   else {
                   	msgSendAll(msg);
                   }
                   //로프를 벗어나면 클라이언트 연결이 종료되므로 스레드 인터럽트
                
                  }
                  this.interrupt();
                  logger.info(this.getName()+" 종료됨!");
               } catch (IOException e) {
                  
                  e.printStackTrace();
               }     
   }      
      //연결된 모든 클라이언트에 메시지 중계
      
   }
	public static void main(String[] args) {
		MultiChatServer server = new MultiChatServer();
		server.start();
	}
}