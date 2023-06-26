package simulation;

import desmoj.core.simulator.*;

public class PickupExitEvent extends Event<CustomerEntity> {

    private DT_model myModel;

    public PickupExitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + customer.getName() + " left");
        data.chronoLogger("pe", myModel.presentTime().getTimeAsDouble());

        data.silentScreamer(myModel.getSalesVolumePerCustomer() + " | Pickup Exit: Customer" + customer.getName() + " left");
        data.chronoLogger("sv", myModel.getSalesVolumePerCustomer());


        PickupEntity pickup = myModel.busyPickupWindow.first();
        myModel.busyPickupWindow.remove(pickup);
        myModel.freePickupWindow.insert(pickup);
        if (!myModel.pickupQueue.isEmpty()) {
            CustomerEntity nextCustomer = myModel.pickupQueue.first();
            myModel.pickupQueue.remove(nextCustomer);

            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + customer.getName() + " arrived");
            data.chronoLogger("pw", myModel.presentTime().getTimeAsDouble());

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(nextCustomer, new TimeSpan(myModel.getPickupTime()));
        }
    }
}