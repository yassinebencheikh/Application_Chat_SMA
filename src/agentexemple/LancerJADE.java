package agentexemple;


import jade.core.ProfileImpl;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class LancerJADE {
    
	public static AgentContainer contPrincipal;
	public ServiceDescription sd;

	public void lancerPersonAgent(String username,String person) {
		AgentController controle1;
		try {
			Object object[]=new Object[1];
			
			if (person.equals("Professeur")) {
				 object[0]="Professeur";
			}else if (person.equals("Etudiant")) {
			     object[0]="Etudiant";
			}else {
				System.err.println("nom incorrect");
			}
			
			
			controle1 = contPrincipal.createNewAgent(username,"com.midvi.startChat.PersonAgent",object);
			controle1.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String []args) {
		try {
			//Lancer la plate frome JADE
			jade.core.Runtime run= jade.core.Runtime.instance();
			Properties prop=new ExtendedProperties();
			//prop.setProperty("gui", "true");
			ProfileImpl prof=new ProfileImpl(prop);
			//Creer le conteneur principal
		    contPrincipal =run.createMainContainer(prof);
			contPrincipal.start();
			//Céer et lancer un agent
//			AgentController controle2 = contPrincipal.createNewAgent("Description","com.midvi.startChat.DescriptionAgent",null);
//			controle2.start();
			home();
	      }catch(ControllerException ex) {
		      System.err.println(ex.getMessage());
	      }
}

	private static void home() {
       HomeGUI home = new HomeGUI();
        home.frame.setVisible(true);
        home.frame.setLocationRelativeTo(null);
        //String name = getAID().getName().split("@")[0];
        home.frame.setTitle("Home");
		
	}
}