package simulation;

import desmoj.core.simulator.*;

public class OrderExitEvent extends Event<CustomerEntity> {

    private DT_model myModel;
    private Boolean freedOrderWindow = false;
    private Boolean limit = false;

    public OrderExitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        OrderEntity order = myModel.busyOrderWindow.first();
        if (myModel.getPickupQueueLimit() == 0){
            myModel.busyOrderWindow.remove(order);
            myModel.freeOrderWindow.insert(order);
            freedOrderWindow = true;
        } else if (myModel.getPickupQueueLimit() > 0 && myModel.pickupQueue.length() < myModel.getPickupQueueLimit()) {
            myModel.busyOrderWindow.remove(order);
            myModel.freeOrderWindow.insert(order);
            freedOrderWindow = true;
        } else {
            myModel.stuckInOrder.insert(customer);
            CustomerEntity test = myModel.stuckInOrder.first();
            System.out.println("DEBUG1 " + test.getName() +" | "+ myModel.presentTime().getTimeAsDouble());
            /*
            System.out.println("DEBUG1: " + myModel.presentTime().getTimeAsDouble() + " | " + myModel.pickupQueue.length() + " | " + customer.getName());
            OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
            orderExit.schedule(customer, new TimeSpan(myModel.getOrderTime()));
            System.out.println("DEBUG2: " + myModel.presentTime().getTimeAsDouble() + " | " + myModel.pickupQueue.length() + " | " + customer.getName());
            */
        }
        if (freedOrderWindow) {
            System.out.println("DEBUG3: " + myModel.presentTime().getTimeAsDouble() + " | " + myModel.pickupQueue.length() + " | " + customer.getName());
            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Window: Customer" + customer.getName() + " left");
            data.chronoLogger("oe", myModel.presentTime().getTimeAsDouble());
            CustomerArrivalPickupEvent customerArrivalPickup = new CustomerArrivalPickupEvent(myModel, "Customer Arrival Pickup", true);
            customerArrivalPickup.schedule(customer, new TimeInstant(myModel.presentTime().getTimeAsDouble()+0.0000001));

        }
        if (!myModel.orderQueue.isEmpty() && !myModel.freeOrderWindow.isEmpty()) {
            CustomerEntity nextCustomer = myModel.orderQueue.first();
            myModel.orderQueue.remove(nextCustomer);

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Queue: Customer" + nextCustomer.getName() + " left");

            myModel.freeOrderWindow.remove(order);
            myModel.busyOrderWindow.insert(order);

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Window: Customer" + nextCustomer.getName() + " arrived");
            data.chronoLogger("ow", myModel.presentTime().getTimeAsDouble());

            OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
            orderExit.schedule(nextCustomer, new TimeSpan(myModel.getOrderTime()));
        }
    }
}