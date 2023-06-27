package simulation;

import desmoj.core.simulator.*;

public class PickupExitEvent extends Event<CustomerEntity> {

    private DT_model myModel;

    public PickupExitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        PickupEntity pickup = myModel.busyPickupWindow.first();
        myModel.busyPickupWindow.remove(pickup);
        myModel.freePickupWindow.insert(pickup);

        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + customer.getName() + " left");
        data.chronoLogger("pe", myModel.presentTime().getTimeAsDouble());

        data.chronoLogger("sv", myModel.getSalesVolumePerCustomer());
        if (!myModel.pickupQueue.isEmpty()) {
            CustomerEntity nextCustomer = myModel.pickupQueue.first();
            myModel.pickupQueue.remove(nextCustomer);
            nextCustomer.setPickupQueueExit(myModel.presentTime().getTimeAsDouble());

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Queue: Customer" + nextCustomer.getName() + " left");

            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(nextCustomer, new TimeSpan(myModel.getPickupTime()- nextCustomer.getPickupQueueTime()));

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + nextCustomer.getName() + " arrived");
            data.chronoLogger("pw", myModel.presentTime().getTimeAsDouble());
        }
    }
}