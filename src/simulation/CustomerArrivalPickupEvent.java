package simulation;

import desmoj.core.simulator.*;

public class CustomerArrivalPickupEvent extends Event<CustomerEntity>{

    private DT_model myModel;
    private Boolean insertedCustomer = false;

    public CustomerArrivalPickupEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        if (myModel.getPickupQueueLimit() == 0){
            myModel.pickupQueue.insert(customer);
            insertedCustomer = true;
        } else if (myModel.getPickupQueueLimit() > 0 && myModel.pickupQueue.length() < myModel.getPickupQueueLimit()) {
            myModel.pickupQueue.insert(customer);
            insertedCustomer = true;
        } else {
            System.out.println("Pickup Queue | Customer rejected");
        }
        if (insertedCustomer) {
            customer.setPickupQueueEntry(myModel.presentTime().getTimeAsDouble());
            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Queue: Customer" + customer.getName() + " arrived");
            data.chronoLogger("pq", myModel.presentTime().getTimeAsDouble());
        }
        if (insertedCustomer && !myModel.freePickupWindow.isEmpty()) {
            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Queue: Customer" + customer.getName() + " left");
            myModel.pickupQueue.remove(customer);
            customer.setPickupQueueExit(myModel.presentTime().getTimeAsDouble());

            PickupEntity pickup = myModel.freePickupWindow.first();
            myModel.freePickupWindow.remove(pickup);
            myModel.busyPickupWindow.insert(pickup);

            Double newPickupTime = myModel.getPickupTime()- customer.getPickupQueueTime();
            if (newPickupTime < 0.083) {
                newPickupTime = 0.083;
            }

            PickupExitEvent pickupExit = new PickupExitEvent(myModel, "Pickup Exit", true);
            pickupExit.schedule(customer, new TimeSpan(newPickupTime));

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Pickup Window: Customer" + customer.getName() + " arrived");
            data.chronoLogger("pw", myModel.presentTime().getTimeAsDouble());
        }
    }
}
