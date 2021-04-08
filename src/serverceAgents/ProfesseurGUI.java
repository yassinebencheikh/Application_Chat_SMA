package serverceAgents;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



public class ProfesseurGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public boolean drapo = true;
	public List listOfPerson,list_Message;
	JTextPane textPane;
	public void fermer(){
		dispose();
	}

	public ProfesseurGUI(ProfesseurAgent userServiceAgent) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300,100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUser = new JLabel("User");
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(77, 11, 46, 14);
		contentPane.add(lblUser);
		
		JLabel lblListOfAll = new JLabel("list of all person");
		lblListOfAll.setHorizontalAlignment(SwingConstants.CENTER);
		lblListOfAll.setBounds(467, 11, 117, 14);
		contentPane.add(lblListOfAll);
		
		listOfPerson = new List();
		listOfPerson.setBounds(474, 31, 110, 244);
		contentPane.add(listOfPerson);
		
		list_Message = new List();
		list_Message.setBounds(31, 36, 237, 216);
		contentPane.add(list_Message);
		
		textPane = new JTextPane();
		
		
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyE) {
				
				if(keyE.getKeyCode() == KeyEvent.VK_ENTER) {
					
					String msg = textPane.getText();
					list_Message.add("User:"+ msg);
					textPane.setText("");
					if(true) {
						drapo=false;
						String[] listofRecieve = listOfPerson.getSelectedItems();
						for (String string : listofRecieve) {
							userServiceAgent.sendTo(string.split(" ")[0],msg);
						}
						
					}
					else
						userServiceAgent.reply(msg);
				
				}
			}
		});
		textPane.setBounds(31, 264, 237, 42);
		contentPane.add(textPane);
		
		
		
	}

	public void addPerson(String person) {
		listOfPerson.add(person);
	}
	
	public void clearListOfPerson() {
		listOfPerson.clear();
	}
	
	public void setResult(String message) {
		
		list_Message.addItem(message);
		
		
	}
}
