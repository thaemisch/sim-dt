package simulation;

import desmoj.core.simulator.*;

public class CustomerArrivalPickupEvent extends Event<CustomerEntity>{

    private DT_model myModel;

    public CustomerArrivalPickupEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        myModel.pickupQueue.insert(customer);

        if (!myModel.freePickupWindow.isEmpty()) {
            PickupEntity pickup = myModel.freePickupWindow.first();
            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            myModel.pickupQueue.remove(customer);

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(customer, new TimeSpan(myModel.getPickupTime()));
        }
    }
}
