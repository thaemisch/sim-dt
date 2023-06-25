package simulation;

import desmoj.core.simulator.*;

public class CustomerNewEvent extends ExternalEvent{

    private DT_model myModel;

    public CustomerNewEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine() {
        CustomerEntity customer = new CustomerEntity(myModel, "Customer", true);
        CustomerArrivalEvent customerArrival = new CustomerArrivalEvent(myModel, "Customer Arrival", true);
        customerArrival.schedule(customer, new TimeSpan(myModel.getCustomerArrivalTime()));

        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Customer created");

        CustomerNewEvent newCustomer = new CustomerNewEvent(myModel, "New Customer", true);
        newCustomer.schedule(new TimeSpan(myModel.getCustomerArrivalTime()));
    }
}