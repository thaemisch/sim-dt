package simulation;

import desmoj.core.simulator.*;

public class PickupQueueRejectedEvent extends Event<CustomerEntity>{

    private DT_model myModel;

    public PickupQueueRejectedEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void  eventRoutine(CustomerEntity customer) {
        System.out.println("Customer" + customer.getName()+" Rejected");
        Boolean rejected = true;
        do {
            if (myModel.pickupQueue.length() < myModel.getPickupQueueLimit()) {
                rejected = false;
                System.out.println("Customer" + customer.getName()+" Accepted");
            }
        } while (rejected);
        if (!rejected){
            OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
            orderExit.schedule(customer, new TimeInstant(myModel.presentTime().getTimeAsDouble()+0.0000001));
        }
    }
}
