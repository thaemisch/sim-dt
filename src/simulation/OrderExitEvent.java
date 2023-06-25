package simulation;

import desmoj.core.simulator.*;

public class OrderExitEvent extends Event<CustomerEntity> {

    private DT_model myModel;

    public OrderExitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Window: Customer left");
        if (!myModel.orderQueue.isEmpty()) {
            CustomerEntity nextCustomer = myModel.orderQueue.first();
            myModel.orderQueue.remove(nextCustomer);

            OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
            orderExit.schedule(nextCustomer, new TimeSpan(myModel.getOrderTime()));
        } else {
            OrderEntity order = myModel.busyOrderWindow.first();
            myModel.busyOrderWindow.remove(order);
            myModel.freeOrderWindow.insert(order);

            CustomerArrivalPickupEvent customerArrivalPickup = new CustomerArrivalPickupEvent(myModel, "Customer Arrival Pickup", true);
            customerArrivalPickup.schedule(customer, new TimeSpan(myModel.getPickupTime()));
        }
    }
}