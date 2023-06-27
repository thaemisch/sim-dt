package simulation;

import desmoj.core.simulator.*;

public class SwitchToStoßzeit extends ExternalEvent{

    private DT_model myModel;

    public SwitchToStoßzeit(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine() {
        myModel.setStoßzeit();
        myModel.init();
        System.out.println(myModel.presentTime().getTimeAsDouble() + " | Switched to Stoßzeit");
    }
}
