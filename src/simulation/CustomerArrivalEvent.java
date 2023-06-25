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

            data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Queue: Customer arrived");
            data.chronoLogger("oq", myModel.presentTime().getTimeAsDouble());

            if (!myModel.freeOrderWindow.isEmpty()) {
                myModel.orderQueue.remove(customer);

                OrderEntity order = myModel.freeOrderWindow.first();
                myModel.freeOrderWindow.remove(order);
                myModel.busyOrderWindow.insert(order);

                data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Order Window: Customer arrived");
                data.chronoLogger("ow", myModel.presentTime().getTimeAsDouble());

                OrderExitEvent orderExit = new OrderExitEvent(myModel, "Order Exit", true);
                orderExit.schedule(customer, new TimeSpan(myModel.getOrderTime()));
            }
        }
}