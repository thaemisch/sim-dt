package simulation;

import desmoj.core.simulator.*;

public class PickupExitEvent extends Event<CustomerEntity> {

    private DT_model myModel;

    public PickupExitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        if (!myModel.pickupQueue.isEmpty()) {
            CustomerEntity nextCustomer = myModel.pickupQueue.first();
            myModel.pickupQueue.remove(nextCustomer);

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(nextCustomer, new TimeSpan(myModel.getPickupTime()));
        } else {
            myModel.pickupWindowEmpty = true;
        }
    }
}