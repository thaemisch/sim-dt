package simulation;

import desmoj.core.simulator.*;

public class CustomerNewEvent extends ExternalEvent{

    private DT_model myModel;

    public CustomerNewEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine() {
        CustomerEntity customer = new CustomerEntity(myModel, Integer.toString(DT_model.customerCounter), true);
        CustomerArrivalEvent customerArrival = new CustomerArrivalEvent(myModel, "Customer Arrival", true);
        customerArrival.schedule(customer, new TimeSpan(myModel.getCustomerArrivalTime()));
        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Customer" + customer.getName() + " created");

        DT_model.customerCounter++;

        CustomerNewEvent newCustomer = new CustomerNewEvent(myModel, Integer.toString(DT_model.customerCounter), true);
        newCustomer.schedule(new TimeSpan(myModel.getCustomerArrivalTime()));
    }
}