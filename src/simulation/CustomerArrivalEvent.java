package simulation;

import desmoj.core.simulator.*;

public class CustomerArrivalEvent extends Event<CustomerEntity>{

        private DT_model myModel;

        public CustomerArrivalEvent(Model owner, String name, boolean showInTrace) {
            super(owner, name, showInTrace);
            myModel = (DT_model) owner;
        }

        public void eventRoutine(CustomerEntity customer) {
            System.out.println("Customer arrives at " + myModel.presentTime().getTimeAsDouble());

            myModel.orderQueue.insert(customer);
            myModel.sendTraceNote("Order Queue length: " + myModel.orderQueue.length());

            if (myModel.orderWindowEmpty){
                myModel.orderWindowEmpty = false;
                myModel.orderQueue.remove(customer);

            }

        }
}