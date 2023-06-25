package simulation;

import desmoj.core.simulator.*;

public class CustomerArrivalEvent extends Event<CustomerEntity>{

        private DT_model myModel;

        public CustomerArrivalEvent(Model owner, String name, boolean showInTrace) {
            super(owner, name, showInTrace);
            myModel = (DT_model) owner;
        }

        public void eventRoutine(CustomerEntity customer) {
            myModel.orderQueue.insert(customer);
            myModel.sendTraceNote("Order Queue length: " + myModel.orderQueue.length());

            if (!myModel.freeOrderWindow.isEmpty()){
                OrderEntity order = myModel.freeOrderWindow.first();
                myModel.freeOrderWindow.remove(order);
                myModel.busyOrderWindow.insert(order);

                myModel.orderQueue.remove(customer);

                OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
                orderExit.schedule(customer, new TimeSpan(myModel.getOrderTime()));
            }

        }
}