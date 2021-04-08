package agentexemple;



//Agent Vendeur


import java.sql.Connection;

import com.mysql.jdbc.Driver;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
public class AgentVendeur extends Agent {
	 
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		protected void setup(){
			Connection connection;
			String prod =null;
			//----------------------------------start& registre AV------------------------------------
			final SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
			System.out.println(getLocalName()+" STARTED");
			
			try {
				
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.setName(getAID());
				//Enregistrement de la description de l'agent dans DF (Directory Facilitator)
				DFService.register(this, dfd);
				System.out.println(getLocalName()+" REGISTERED WITH THE DF");
				
			} catch (FIPAException e) {
			e.printStackTrace();
		}
//-------------------------------------waite  msg from de AM  ----------------------------------
			comportementSequentiel.addSubBehaviour(new OneShotBehaviour(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void action() {
				
						 System.out.println(" Agent Vendeur en Attente  Agent Mobile  ");
						 ACLMessage msg2Recu =null ;
						 while (msg2Recu == null){
						 msg2Recu= receive() ;
						}
						 System.out.println(" Agent Vendeur :message recu de Agent Mobile = "+ msg2Recu.getContent());}
					});
       
		 
//----------------------------------------------------------------------------------------------------
		      comportementSequentiel.addSubBehaviour(new OneShotBehaviour(){
		      	/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				@Override
		      	public void action() {
		      	 ACLMessage msg3 = new ACLMessage(ACLMessage.INFORM);
		     	     msg3.addReceiver(new AID("AgentService", AID.ISLOCALNAME));
		     	     msg3.setContent("public key");
		     	     send(msg3);
		     	     System.out.println("L'agent Vendeur  vas interoger l'Agent Service pour  demandé la clé pub "+msg3.getContent());  
		      	}
		      	});
		      addBehaviour(comportementSequentiel);
		//-----------------------------------------------------------------------------------------------------
		      addBehaviour(new CyclicBehaviour(this) {
					/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
					public void action() {
				    System.out.println("L'agent Vendeur  en attente l'Agent Service  ");  
					
					ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					if (msg != null) {		
						try {
						System.out.println(" Agent Vendeur : message recu de Agent Service = "+msg.getContentObject() );
					    } catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					  }	
					    } 
					
					else block();
					
					
					}});
	      
	//-------------------------------------------------------------------------------------------------
					
	    /*  comportementSequentiel.addSubBehaviour(new OneShotBehaviour(){
	      	@Override
	      	public void action() {
	      		ACLMessage msg11 = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	      		System.out.println("L'agent Vendeur  attente l'Agent Service  ");  
			         while (msg11== null){	msg11 = receive() ;}
			  
			         try {
						System.out.println("Agent Vendeur : message recu de l'Agent Service   = "+ msg11.getContentObject());
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	      	}
	      	});*/
	     	
	//-------------------------------------------------------------------------------------------------------
	     
	      comportementSequentiel.addSubBehaviour(new OneShotBehaviour(){
	      	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
	
			@Override
	      	public void action() {
	      		
	      	    // --- cryptage --- 
	      	}
	      	});
					
					
	//---------------------------------------------------------------------------------------------------
	      
	      comportementSequentiel.addSubBehaviour(new OneShotBehaviour(){
	      	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
	
			@Override
	      	public void action() {
	      		
	      	    // --- Terminisation --- 
	      	}
	      	});
					
	      
	}

}