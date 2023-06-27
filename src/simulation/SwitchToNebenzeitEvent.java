package simulation;

import desmoj.core.simulator.*;

public class SwitchToNebenzeitEvent extends ExternalEvent{

    private DT_model myModel;

    public SwitchToNebenzeitEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (DT_model) owner;
    }

    public void eventRoutine() {
        myModel.setNebenzeit();
        myModel.initSwitch();
        data.silentScreamer(myModel.presentTime().getTimeAsDouble() + " | Switched to Nebenzeit");
    }
}
