package chatting;

import javax.swing.*;

public class MultiChatData {

    JTextArea msgOut;
    public void addObj(JComponent comp) {
    	this.msgOut = (JTextArea)comp;
    }
    public void refreshData(String msg) {
        // JTextArea �� ���ŵ� �޽��� �߰��ϱ�
        msgOut.append(msg);
    }
}