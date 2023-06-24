package simulation;

import desmoj.core.simulator.*;

public class OrderExitEvent extends Event<CustomerEntity> {

    private DT_model myModel;

    public OrderExitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        if (!myModel.orderQueue.isEmpty()) {
            CustomerEntity nextCustomer = myModel.orderQueue.first();
            myModel.orderQueue.remove(nextCustomer);

            OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
            orderExit.schedule(nextCustomer, new TimeSpan(myModel.getOrderTime()));
        } else {
            myModel.orderWindowEmpty = true;
        }
    }
}