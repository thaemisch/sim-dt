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
        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Queue: Customer arrived");

        if (!myModel.freePickupWindow.isEmpty()) {
            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer arrived");
            PickupEntity pickup = myModel.freePickupWindow.first();
            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            myModel.pickupQueue.remove(customer);

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(customer, new TimeSpan(myModel.getPickupTime()));
        }
    }
}
