

package serverceAgents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class EtudiantAgent extends Agent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The GUI by means of which the user can add books in the catalogue
    private EtudiantGUI myGui;
    public ACLMessage msgRecieve;
    public AID []PersonAgents;
    // Put agent initializations here
    protected void setup() {
        // Create and show the GUI
        myGui = new EtudiantGUI(this);
        myGui.show();

     // Register the MIDVI service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("MIDVI");
        sd.setName( getLocalName() );
        dfd.addServices(sd);
        
       
        
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Add the behaviour serving queries from buyer agents
        addBehaviour(new OfferRequestsServer());
      //add all person to list avtice 
        addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
    		public void action() {
    			//update list of each person
    			updateListOfAllPerson();
               }
                	
               
            });
      
    }

    /**
         Inner class OfferRequestsServer.
         This is the behaviour used by Book-seller agents to serve incoming requests
         for offer from buyer agents.
         If the requested book is in the local catalogue the seller agent replies
         with a PROPOSE message specifying the price. Otherwise a REFUSE message is
         sent back.
    */
    private class OfferRequestsServer extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
        
		public void action() {
			
			msgRecieve = myAgent.receive();
            if (msgRecieve != null) {
            	// Reply received
            	// INFORM_IF Message received. Process it
                String content = msgRecieve.getContent();
                if (msgRecieve.getPerformative() == ACLMessage.INFORM_IF) {
	            	
                	//Add all agents to the list of active people
	                addAllAgent();

    				
                }else {
                	// put message 
                	myGui.setResult(msgRecieve.getSender().getLocalName()+ ":"+content);
            	}
            } else {
                block();
            }

        }
    }  // End of inner class OfferResultServer
	
 // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
          //update list of each person
			updateListOfAllPerson();
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Close the GUI
        myGui.dispose();
        // Printout a dismissal message
        System.out.println("Etudiant-agent "+getAID().getName()+" terminating.");
    }

    
    public void sendTo(String agentname,String content) {
		ACLMessage msg =new ACLMessage(ACLMessage.PROPOSE);
 		msg.addReceiver (new AID(agentname,AID.ISLOCALNAME));
 		msg.setContent("User: "+content);
 		this.send(msg);
		
	}
	
	public void reply(String content) {
    	//Envoyerla reponse 
		ACLMessage m2=msgRecieve.createReply();
		m2.setContent(content);
		this.send(m2);
		
	}
	
	// send message to groups 
		public void sendMessagegToGroup(String [] groups,String message) {
			    
			    ACLMessage msg =new ACLMessage(ACLMessage.CFP);		    
			    for (String person : groups) {
					msg.addReceiver (new AID(person.split(" ")[0],AID.ISLOCALNAME));
				}
				msg.setContent(message);
			    this.send(msg);	
			}
	
	
	// function search of popular activate
		public AID [] searchDF( String service ){
	        DFAgentDescription dfd = new DFAgentDescription();
	        ServiceDescription sd = new ServiceDescription();
	        sd.setType( service );
	        dfd.addServices(sd);
	        
	        SearchConstraints ALL = new SearchConstraints();
	        ALL.setMaxResults(new Long(-1));

	        try
	        {
	            DFAgentDescription[] result = DFService.search(this, dfd, ALL);
	            PersonAgents = new AID[result.length];
	            for (int i=0; i<result.length; i++) 
	            	PersonAgents[i] = result[i].getName() ;
	            return PersonAgents;

	        }
	        catch (FIPAException fe) { fe.printStackTrace(); }
	        
	        return null;
	    }
		public void updateListOfAllPerson() {
			
			PersonAgents=searchDF("MIDVI");
		    ACLMessage msg =new ACLMessage(ACLMessage.INFORM_IF);
		    
			for (int i=0; i<PersonAgents.length;i++){
			        String agentName = PersonAgents[i].getLocalName();
			        msg.addReceiver (new AID(agentName,AID.ISLOCALNAME));
			}
			msg.setContent("__addAllPerson__");
		    this.send(msg);
			
		}
		// add all person active to list of activate popular 
				public void addAllAgent() {
					PersonAgents=searchDF("MIDVI");
				      //step 2 : showing the result of the request :
					myGui.clearListOfPerson();
					for (int i=0; i<PersonAgents.length;i++){
					     String agentName = PersonAgents[i].getLocalName();
					     if(!agentName.equals(agentName))
					    	 myGui.addPerson(agentName+" (active)");
					     
					}
				}
				

} // end of the agent class