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
        data.nameLogger("pe", "Customer" + customer.getName());
        data.chronoLogger("pe", myModel.presentTime().getTimeAsDouble());

        data.chronoLogger("sv", myModel.getSalesVolumePerCustomer());
        if (!myModel.pickupQueue.isEmpty()) {
            CustomerEntity nextCustomer = myModel.pickupQueue.first();
            myModel.pickupQueue.remove(nextCustomer);
            nextCustomer.setPickupQueueExit(myModel.presentTime().getTimeAsDouble());

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Queue: Customer" + nextCustomer.getName() + " left");


            CustomerEntity stuckCustomer = myModel.stuckInOrder.first();
            if (stuckCustomer != null && myModel.pickupQueue.length() != myModel.getPickupQueueLimit()) {
                myModel.stuckInOrder.remove(stuckCustomer);
                System.out.println("DEBUG2 " + stuckCustomer.getName() +" | "+ myModel.presentTime().getTimeAsDouble());
                OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
                orderExit.schedule(stuckCustomer, new TimeInstant(myModel.presentTime().getTimeAsDouble()+0.0000001));
            }

            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            Double newPickupTime = myModel.getPickupTime()- nextCustomer.getPickupQueueTime();
            if (newPickupTime < 0.083) {
                newPickupTime = 0.083;
            }

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(nextCustomer, new TimeSpan(newPickupTime));

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + nextCustomer.getName() + " arrived");
            data.nameLogger("pw", "Customer" + nextCustomer.getName());
            data.chronoLogger("pw", myModel.presentTime().getTimeAsDouble());
        }

    }
}