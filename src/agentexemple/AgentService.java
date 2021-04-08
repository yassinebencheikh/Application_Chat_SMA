package agentexemple;




///Agent Service 


import java.io.IOException;
import java.io.Serializable;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.lang.acl.MessageTemplate;

public class AgentService  extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  Serializable Pubkey=null;
	ACLMessage msg1Recu =null ;
	 
	
	protected void setup(){
	
		 try {
			 
			    System.out.println(getLocalName()+" STARTED");
				// Création de desciprion de l'agent [Agent Service]
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.setName(getAID());
				// Enregistrement de la description de l'agent dans DF (Directory Facilitator)
				DFService.register(this, dfd);
				System.out.println(getLocalName()+" REGISTERED WITH THE DF");
				
			} catch (FIPAException e) {
			e.printStackTrace();
		}
//-------------------------------------------------------------------------
	     SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
	     comportementSequentiel.addSubBehaviour(new OneShotBehaviour(){
	    	 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	    	 public void action() {
	    		 System.out.println(" AgentService:en Attente AgentLanceur  ");
	    		
	    		 while (msg1Recu == null){
	    		 msg1Recu= receive() ;
	    		}
	    		
	    		 try {
					Pubkey = msg1Recu.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		try {
					System.out.println("message recu de Agent Lanceur et registre sous pubkey= "+msg1Recu.getContentObject() );
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		
				}
	    	 }); 
	     addBehaviour(comportementSequentiel);
//-------------------------------------------------------------------------------------------------------
	     addBehaviour(new CyclicBehaviour(this) {
				
				/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				public void action() {
					
				// Attente de message (de l'agent vendeur)
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {				
						if (msg.getContent().equalsIgnoreCase("public key")){
																			
							   ACLMessage reply = msg.createReply();
								
							   try {
									reply.setContentObject(Pubkey);
				
									System.out.println(" Agent Service :message envoyé de Agent vendeur = "+reply.getContentObject());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (UnreadableException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				         }
				}
				else {
					//Pendant que le message n'est pas encore arrivé le comportement est bloqué
					block();
				}
				
				}});
	    
	
	}
}