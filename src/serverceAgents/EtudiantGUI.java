package serverceAgents;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class EtudiantGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public EtudiantAgent clientAgent;
	public List listOfPerson,list_Message;
	public boolean drapo = true;
	JTextPane textPane;
	
	public void fermer(){
		dispose();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EtudiantGUI frame = new EtudiantGUI(null);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setTitle("Client GUI");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param EtudiantAgent 
	 */
	public EtudiantGUI(EtudiantAgent clientAgent) {
		this.clientAgent=clientAgent;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300,100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblClient = new JLabel("Etudiant");
		lblClient.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblClient.setHorizontalAlignment(SwingConstants.CENTER);
		lblClient.setBounds(74, 0, 117, 26);
		contentPane.add(lblClient);
		
		list_Message = new List();
		list_Message.setBounds(10, 32, 237, 216);
		contentPane.add(list_Message);
		
		textPane = new JTextPane();
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEv) {
				if(keyEv.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = textPane.getText();
					list_Message.add("Client: "+ msg);
					textPane.setText("");
					
					if(true) {
						drapo=false;
						String[] listofRecieve = listOfPerson.getSelectedItems();
						for (String string : listofRecieve) {
							clientAgent.sendTo(string.split(" ")[0],msg);
						}
					}
					else
					  clientAgent.reply(msg);
				}
			}
		});
		textPane.setBounds(10, 260, 237, 42);
		contentPane.add(textPane);
		
		listOfPerson = new List();
		listOfPerson.setBounds(474, 32, 110, 244);
		contentPane.add(listOfPerson);
		
		JLabel lblLesPersons = new JLabel("les persons ");
		lblLesPersons.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblLesPersons.setHorizontalAlignment(SwingConstants.CENTER);
		lblLesPersons.setBounds(467, 0, 117, 26);
		contentPane.add(lblLesPersons);
		
		
		
	}

	
	public void addPerson(String person) {
		listOfPerson.add(person);
	}
	
	public void setResult(String message) {
		
		list_Message.add(message);
		
		
	}
	public void clearListOfPerson() {
		listOfPerson.clear();
	}
	
}
