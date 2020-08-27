package chatting;

import javax.swing.*;

public class MultiChatData {

    JTextArea msgOut;
    public void addObj(JComponent comp) {
    	this.msgOut = (JTextArea)comp;
    }
    public void refreshData(String msg) {
        // JTextArea 에 수신된 메시지 추가하기
        msgOut.append(msg);
    }
}