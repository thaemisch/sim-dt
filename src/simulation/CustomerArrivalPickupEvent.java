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
        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Queue: Customer" + customer.getName() + " arrived");
        data.chronoLogger("pq", myModel.presentTime().getTimeAsDouble());
        if (!myModel.freePickupWindow.isEmpty()) {
            myModel.pickupQueue.remove(customer);

            PickupEntity pickup = myModel.freePickupWindow.first();
            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + customer.getName() + " arrived");
            data.chronoLogger("pw", myModel.presentTime().getTimeAsDouble());

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(customer, new TimeSpan(myModel.getPickupTime()));
        }
    }
}
