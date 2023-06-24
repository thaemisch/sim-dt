package simulation;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class DT_model extends Model {
    public String description() {
        return "DT_model (Ereignisorientiert):" +
                "simulates a drive-through of a fast-food restaurant" +
                "Steps:" +
                "   1. arriving customer enters order queue" +
                "   2. customer orders" +
                "   3. customer enters pickup queue" +
                "   4. customer picks up order" +
                "   5. customer leaves";
    }

    // Customer arrival time
    private ContDistExponential customerArrivalTime;

    public double getCustomerArrivalTime() {
        return customerArrivalTime.sample();
    }

    // Order time
    private ContDistUniform orderTime;

    public double getOrderTime() {
        return orderTime.sample();
    }

    // Pickup time
    private ContDistUniform pickupTime;

    public double getPickupTime() {
        return pickupTime.sample();
    }

    // Order queue

    // Pickup queue

    // free / occupied windows

    public DT_model(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    public void doInitialSchedules() {
        // Schedule the first customer arrival
        //CustomerArrivalEvent customerArrivalEvent = new CustomerArrivalEvent(this, "CustomerArrivalEvent", true);
        //customerArrivalEvent.schedule(new TimeSpan(getCustomerArrivalTime()));
    }

    public void init() {
        // Initialize the distributions
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", 2.0, true, false);
        orderTime = new ContDistUniform(this, "OrderTime", 0.5, 1.5, true, false);
        pickupTime = new ContDistUniform(this, "PickupTime", 0.5, 1.5, true, false);
    }

    public static void main(java.lang.String[] args){
        Experiment dtExperiment = new Experiment("dt-ereignis");

        DT_model dt_model = new DT_model(null, "DT Model", true, true);

        dt_model.connectToExperiment(dtExperiment);

        dtExperiment.setShowProgressBar(true);
        dtExperiment.tracePeriod(new TimeInstant(0.0), new TimeInstant(240.0));
        dtExperiment.debugPeriod(new TimeInstant(0.0), new TimeInstant(240.0));

        dtExperiment.stop(new TimeInstant(240.0));

        dtExperiment.start();

        dtExperiment.report();

        dtExperiment.finish();
    }
}