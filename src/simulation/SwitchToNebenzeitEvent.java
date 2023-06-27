package simulation;

import desmoj.core.simulator.*;

public class SwitchToNebenzeitEvent extends ExternalEvent{

    private DT_model myModel;

    public SwitchToNebenzeitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine() {
        System.out.println(myModel.getCustomerArrivalTime());
        myModel.setNebenzeit();
        myModel.init();
        System.out.println(myModel.getCustomerArrivalTime());
        System.out.println(myModel.presentTime().getTimeAsDouble() + " | Nebenzeit");
    }
}