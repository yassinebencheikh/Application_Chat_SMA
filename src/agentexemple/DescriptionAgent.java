package agentexemple;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.*;

public class DescriptionAgent extends Agent {

	private static final long serialVersionUID = 1L;
	// The listOfMessageNoSent of books for sale (maps the title of a book to its price)
    private Hashtable<String, String> listOfMessageNoSent;
    // The GUI by means of which the user can add books in the listOfMessageNoSent
//    private AutentificationGUI autentif;
//    public HomeGUI home;
    // The list of known seller agents
    private AID[] PersonAgents;
    public DFAgentDescription dfd;
    public ServiceDescription sd;

    // Put agent initializations here
    protected void setup() {
        // Create the listOfMessageNoSent
        listOfMessageNoSent = new Hashtable<String, String>();

        // Create and show the GUI


//        // Register the Message service in the yellow pages
//        dfd = new DFAgentDescription();
//        dfd.setName(getAID());
//        sd = new ServiceDescription();
//        sd.setType("MIDVI");
//        sd.setName(getLocalName());
//        dfd.addServices(sd);
//        
        
//        home = new HomeGUI();
//        home.frame.setVisible(true);
//        home.frame.setLocationRelativeTo(null);
//        String name = getAID().getName().split("@")[0];
//        home.frame.setTitle(name);
        
        
        
        
//        try {
//            DFService.register(this, dfd);
//        } catch (FIPAException fe) {
//            fe.printStackTrace();
//        }

        // Add the behaviour serving queries from Person agents
        addBehaviour(new messageServer());

        // Add the behaviour serving purchase orders from Student agents
        addBehaviour(new deleteMessageServer());
        
        
//        // Add a TickerBehaviour that schedules a request to seller agents every minute
//        addBehaviour(new CyclicBehaviour() {
//			private static final long serialVersionUID = 1L;
//
//		@Override
//		public void action() {
//			//sendToAllPerson();
//			//System.out.println("Found the following active populer :");
//            PersonAgents=searchDF("MIDVI");
//            for (int i = 0; i < PersonAgents.length; ++i) {
//
//                System.out.println("le service a été lancer "+PersonAgents[i].getLocalName());
//            }
//
//            //myAgent.addBehaviour(new RequestP());
//           }
//            	
//           
//        });
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Printout a dismissal message
        System.out.println("Description-agent "+getAID().getName()+" terminating.");
    }

    /**
       This is invoked by the GUI when the user adds a new book for sale
     */
    public void updatelistOfMessageNoSent(final String agentName, final int msg) {
        addBehaviour(new OneShotBehaviour() {
            
			private static final long serialVersionUID = 1L;

			public void action() {
                listOfMessageNoSent.put(agentName,agentName );
                System.out.println(msg+" is send to person call "+agentName);
            }
        });
    }

 
    private class messageServer extends CyclicBehaviour {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String agentName = msg.getContent();
                ACLMessage reply = msg.createReply();
                //Returns the value to which the specified key is mapped,or null if this map contains no mapping for the key. 
                String messageValue =  (String) listOfMessageNoSent.get(agentName);
                if (messageValue != null) {
                    // The requested book is available for sale. Reply with the price
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(messageValue);
                } else {
                    // The requested book is NOT available for sale.
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent("no message has been recieve");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }  // End of inner class messageServer

    /**
         Inner class PurchaseOrdersServer.
         This is the behaviour used by Book-seller agents to serve incoming
         offer acceptances (i.e. purchase orders) from buyer agents.
         The seller agent removes the purchased book from its listOfMessageNoSent
         and replies with an INFORM message to notify the buyer that the
         purchase has been successfully completed.
    */
    private class deleteMessageServer extends CyclicBehaviour {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String agentName = msg.getContent();
                ACLMessage reply = msg.createReply();

                String nbr =  listOfMessageNoSent.remove(agentName);
                if (nbr != null) {
                    reply.setPerformative(ACLMessage.INFORM);
                    System.out.println(agentName+" sold to agent "+msg.getSender().getName());
                } else {
                    // The requested book has been sold to another buyer in the meanwhile .
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }  // End of inner class OfferRequestsServer
    
    private class RequestPerformer extends Behaviour {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String content ;
        public RequestPerformer(String content) {
			this.content=content;
		}

		public void action() {
           
                // Send the cfp to all sellers
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < PersonAgents.length; ++i) {
                    cfp.addReceiver(PersonAgents[i]);
                }
                cfp.setContent(content);
                cfp.setConversationId("conversation");
                cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
                myAgent.send(cfp);
        }

        public boolean done() {
           return true;
        }
    }  // End of inner class RequestPerformer
    
    
    AID [] searchDF( String service ){

        
        SearchConstraints ALL = new SearchConstraints();
        ALL.setMaxResults(new Long(-1));

        try
        {
            DFAgentDescription[] result = DFService.search(this, dfd, ALL);
            AID[] agents = new AID[result.length];
            for (int i=0; i<result.length; i++) 
                agents[i] = result[i].getName() ;
            return agents;

        }
        catch (FIPAException fe) { fe.printStackTrace(); }
        
        return null;
    }

    
    

} // end of the agent class