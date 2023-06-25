package simulation;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class DT_model extends Model {
    // Variables
    static double startTime = 0.0;
    static double endTime = 240.0;
    static boolean debug = false;
    static boolean trace = false;

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

    /*
     * Order
     */
    private ContDistUniform orderTime;
    public double getOrderTime() {
        return orderTime.sample();
    }
    protected Queue<CustomerEntity> orderQueue;
    protected boolean orderWindowEmpty;

    /*
     * Pickup
     */
    private ContDistUniform pickupTime;
    public double getPickupTime() {
        return pickupTime.sample();
    }
    protected Queue<CustomerEntity> pickupQueue;
    protected boolean pickupWindowEmpty;


    public DT_model(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    public void doInitialSchedules() {
        CustomerNewEvent firstCustomer = new CustomerNewEvent(this, "FirstCustomer", true);

        firstCustomer.schedule(new TimeInstant(getCustomerArrivalTime()));
    }

    public void init() {
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", 2.0, true, false);
        OrderEntity order = new OrderEntity(this, "Order", true);
        orderWindowEmpty = true;
        orderTime = new ContDistUniform(this, "OrderTime", 0.5, 1.5, true, false);
    }

    public static void main(java.lang.String[] args){
        // Parse CLI arguments
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "--start", "-s" -> {
                        startTime = Double.parseDouble(args[i + 1]);
                        i++;
                    }
                    case "--end", "-e" -> {
                        endTime = Double.parseDouble(args[i + 1]);
                        i++;
                    }
                    case "--debug", "-d" -> debug = true;
                    case "--trace", "-t" -> trace = true;
                    case "--help", "-h" -> {
                        System.out.println("Options:");
                        System.out.println("-s, --start <time>      Set the start time of the simulation (Default: 0.0)");
                        System.out.println("-e, --end <time>        Set the end time of the simulation(Default: 240.0)");
                        System.out.println("-d, --debug             Enable debug mode");
                        System.out.println("-t, --trace             Enable trace mode");
                        System.out.println("-h, --help              Show this help");
                        System.exit(0);
                    }
                    default -> {
                        System.err.println("Unknown argument: " + arg);
                        System.exit(1);
                    }
                }
            }
        }

        Experiment dtExperiment = new Experiment("dt-event");

        DT_model dt_model = new DT_model(null, "DT Model", true, true);

        dt_model.connectToExperiment(dtExperiment);

        if (debug)
            dtExperiment.debugPeriod(new TimeInstant(startTime), new TimeInstant(endTime));
        if (trace)
            dtExperiment.tracePeriod(new TimeInstant(startTime), new TimeInstant(endTime));

        dtExperiment.stop(new TimeInstant(endTime));

        dtExperiment.start();
        dtExperiment.report();
        dtExperiment.finish();
    }
}