package chatting;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatUI extends JFrame{
	
	private JPanel loginPanel;
	protected JButton loginButton;
	private JLabel inLabel;
	protected JLabel outLabel;
	protected JTextField idInput;
	
	private JPanel logoutPanel;
	protected JButton logoutButton;
	
	private JPanel msgPanel;
	protected JTextField msgInput;
	protected JButton exitButton;
	
	protected JTextArea textArea;
	protected JTextArea msgOut;
	protected Container tab;
	protected CardLayout cardLayout;
	protected String id;
		MultiChatUI(){
			super("::멀티챗::");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			msgPanel = new JPanel();
			loginPanel = new JPanel();
			idInput = new JTextField("",15);
			loginButton = new JButton("로그인");
			inLabel = new JLabel("대화명 ");
			textArea = new JTextArea();
			msgInput = new JTextField();
			exitButton = new JButton("종료");
			logoutPanel = new JPanel();
			logoutButton = new JButton("로그아웃");
			outLabel = new JLabel("대화명:uesr1");
			msgOut = new JTextArea("",10,30);
			
			JScrollPane jsp = new JScrollPane(msgOut,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			loginPanel.setLayout(new BorderLayout());
			loginPanel.add(inLabel,BorderLayout.WEST);
			loginPanel.add(idInput,BorderLayout.CENTER);
			loginPanel.add(loginButton,BorderLayout.EAST);
			
			msgPanel.setLayout(new BorderLayout());
			msgPanel.add(msgInput,BorderLayout.CENTER);
			msgPanel.add(exitButton,BorderLayout.EAST);
			
			logoutPanel.setLayout(new BorderLayout());
			logoutPanel.add(outLabel,BorderLayout.CENTER);
			logoutPanel.add(logoutButton,BorderLayout.EAST);
			
			msgOut.setEditable(false);
			
			tab = new JPanel();
			cardLayout = new CardLayout();
			tab.setLayout(cardLayout);
			tab.add(loginPanel,"login");
			tab.add(logoutPanel,"logout");
			cardLayout.show(tab, "login");
			//cardLayout.show(tab,"logout");
			
			add(tab,BorderLayout.NORTH);
			add(jsp,BorderLayout.CENTER);
			add(msgPanel,BorderLayout.SOUTH);
	
			setSize(400,300);
			setVisible(true);
		}		
		public void addButtonActionListener(ActionListener actionListener) {
			loginButton.addActionListener(actionListener);
			logoutButton.addActionListener(actionListener);
			exitButton.addActionListener(actionListener);
			msgInput.addActionListener(actionListener);
		}
}