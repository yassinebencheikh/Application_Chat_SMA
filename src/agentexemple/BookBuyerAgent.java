package agentexemple;
/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A.
GNU Lesser General Public License
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation,
version 2.1 of the License.
This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookBuyerAgent extends Agent {
    // The title of the book to buy
    private String targetBookTitle;
    // The list of known seller agents
    private AID[] sellerAgents;

    // Put agent initializations here
    protected void setup() {
        // Printout a welcome message
        System.out.println("Hallo! Buyer-agent "+getAID().getName()+" is ready.");



            // Add a TickerBehaviour that schedules a request to seller agents every minute
            addBehaviour(new TickerBehaviour(this, 10000) {
                protected void onTick() {
                    System.out.println("Trying to buy "+targetBookTitle);
                    // Update the list of seller agents
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Found the following active populer :");
                        sellerAgents = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            sellerAgents[i] = result[i].getName();
                            System.out.println(sellerAgents[i].getName());
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    // Perform the request
                    myAgent.addBehaviour(new RequestPerformer());
                }
            } );
        
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
    }

    /**
       Inner class RequestPerformer.
       This is the behaviour used by Book-buyer agents to request seller
       agents the target book.
     */
    public class RequestPerformer extends Behaviour {
        private AID bestSeller; // The agent who provides the best offer
        private int repliesCnt = 0; // The counter of replies from seller agents
        private MessageTemplate mt; // The template to receive replies
        private int step = 0;

        public void action() {
           
                // Send the cfp to all sellers
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < sellerAgents.length; ++i) {
                    cfp.addReceiver(sellerAgents[i]);
                }
                cfp.setContent(targetBookTitle);
                cfp.setConversationId("book-trade");
                cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
                myAgent.send(cfp);
                // Prepare the template to get proposals
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                                         MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));

            
        }

        public boolean done() {
            if (step == 2 && bestSeller == null) {
                System.out.println("Attempt failed: "+targetBookTitle+" not available for sale");
            }
            return ((step == 2 && bestSeller == null) || step == 4);
        }
    }  // End of inner class RequestPerformer
}