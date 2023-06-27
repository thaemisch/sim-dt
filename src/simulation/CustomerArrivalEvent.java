package simulation;

import desmoj.core.simulator.*;

public class CustomerArrivalEvent extends Event<CustomerEntity>{

    private DT_model myModel;
    private Boolean insertedCustomer = false;

    public CustomerArrivalEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        if (myModel.getOrderQueueLimit() == 0) {
            myModel.orderQueue.insert(customer);
            insertedCustomer = true;
        } else if (myModel.getOrderQueueLimit() > 0 && myModel.orderQueue.length() <= myModel.getOrderQueueLimit()) {
            myModel.orderQueue.insert(customer);
            insertedCustomer = true;
        }
        if (insertedCustomer) {
            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Queue: Customer" + customer.getName() + " arrived");
            data.chronoLogger("oq", myModel.presentTime().getTimeAsDouble());
        }
        if (insertedCustomer && !myModel.freeOrderWindow.isEmpty()) {
            myModel.orderQueue.remove(customer);
            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Queue: Customer" + customer.getName() + " left");

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Window: Customer" + customer.getName() + " arrived");
            data.chronoLogger("ow", myModel.presentTime().getTimeAsDouble());

            OrderEntity order = myModel.freeOrderWindow.first();
            myModel.freeOrderWindow.remove(order);
            myModel.busyOrderWindow.insert(order);

            OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
            orderExit.schedule(customer, new TimeSpan(myModel.getOrderTime()));
        }
    }
}