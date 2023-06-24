package simulation;

import desmoj.core.simulator.*;

public class CustomerNewEvent extends Event<CustomerEntity> {

    private DT_model myModel;

    public CustomerNewEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine(CustomerEntity customer) {
        CustomerEntity newCustomer = new CustomerEntity(myModel, "Customer", true);

    }
}