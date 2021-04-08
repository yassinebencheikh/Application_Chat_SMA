package serverceAgents;

import jade.core.ProfileImpl;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class LancerJaDE {
    
	public static AgentContainer contPrincipal;
	public ServiceDescription sd;

	
		
	public static void main(String []args) {
		try {
			//Lancer la plate frome JADE
			jade.core.Runtime run= jade.core.Runtime.instance();
			Properties prop=new ExtendedProperties();
			prop.setProperty("gui", "true");
			ProfileImpl prof=new ProfileImpl(prop);
			//Creer le conteneur principal
		    contPrincipal =run.createMainContainer(prof);
			contPrincipal.start();
			
			AgentController controle1 = contPrincipal.createNewAgent("Etudiant","serverceAgents.EtudiantAgent",null);
			controle1.start();
			AgentController controle2 = contPrincipal.createNewAgent("Professeur","serverceAgents.ProfesseurAgent",null);
			controle2.start();
	      }catch(ControllerException ex) {
		      System.err.println(ex.getMessage());
	      }
}
}