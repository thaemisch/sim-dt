package simulation;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;

import java.io.File;
import java.util.Objects;

public class DT_model extends Model {
    static String user;
    // Variables
    static double startTime = 0.0;
    static double endTime = 240.0;
    static Boolean stoßzeit = false;
    static Boolean nebenzeit = false;
    public static Boolean quiet = false;

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

    /*
     * Order
     */
    private ContDistExponential customerArrivalTime;
    public double getCustomerArrivalTime() {
        return customerArrivalTime.sample();
    }
    private ContDistUniform orderTime;
    public double getOrderTime() {
        return orderTime.sample();
    }
    static double orderTimeStartDiff = 0.0;
    static double orderTimeEndDiff = 0.0;
    private static int orderQueueLimit = 0;
    public int getOrderQueueLimit() {
        return orderQueueLimit;
    }
    protected Queue<CustomerEntity> orderQueue;
    protected Queue<OrderEntity> freeOrderWindow;
    protected Queue<OrderEntity> busyOrderWindow;

    /*
     * Pickup
     */
    private ContDistUniform pickupTime;
    public double getPickupTime() {
        return pickupTime.sample();
    }

    static double pickupTimeStartDiff = 0.0;
    static double pickupTimeEndDiff = 0.0;
    private static int pickupQueueLimit = 0;
    public int getPickupQueueLimit() {
        return pickupQueueLimit;
    }
    protected Queue<CustomerEntity> pickupQueue;
    protected Queue<PickupEntity> freePickupWindow;
    protected Queue<PickupEntity> busyPickupWindow;

    /*
     * General
     */

    static double arrivalTimeDiff = 0.0;
    private ContDistUniform salesVolumePerCustomer;
    public double getSalesVolumePerCustomer() {
        return salesVolumePerCustomer.sample();
    }
    static double salesVolumePerCustomerMinDiff = 0.0;
    static double salesVolumePerCustomerMaxDiff = 0.0;


    public DT_model(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    public void doInitialSchedules() {
        CustomerNewEvent firstCustomer = new CustomerNewEvent(this, "FirstCustomer", true);

        firstCustomer.schedule(new TimeInstant(getCustomerArrivalTime()));
    }

    public void init() {
        if (stoßzeit){
            customInit(1.07+arrivalTimeDiff, 0.17+orderTimeStartDiff, 2.28+orderTimeEndDiff, 0.1+pickupTimeStartDiff, 4.87+pickupTimeEndDiff, 5.0+salesVolumePerCustomerMinDiff, 30.0+salesVolumePerCustomerMaxDiff);
        } else if (nebenzeit){
            customInit(1.3+arrivalTimeDiff, 0.3+orderTimeStartDiff, 1.38+orderTimeEndDiff, 0.13+pickupTimeStartDiff, 3.3+pickupTimeEndDiff, 5.0+salesVolumePerCustomerMinDiff, 30.0+salesVolumePerCustomerMaxDiff);
        } else {
            customInit(1.07+arrivalTimeDiff, 0.17+orderTimeStartDiff, 2+orderTimeEndDiff, 0.1+pickupTimeStartDiff, 2+pickupTimeEndDiff, 5.0+salesVolumePerCustomerMinDiff, 30.0+salesVolumePerCustomerMaxDiff);
            //Angepasste Zeiten um realen Avg zu simulieren
        }
    }

    public void customInit(Double arrivalTime, Double orderTimeStart, Double orderTimeEnd, Double pickupTimeStart, Double pickupTimeEnd, Double salesVolumePerCustomerMin, Double salesVolumePerCustomerMax) {
        // Order
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", arrivalTime, true, false);
        customerArrivalTime.setNonNegative(true);
        orderTime = new ContDistUniform(this, "OrderTime", orderTimeStart, orderTimeEnd, true, false);
        orderQueue = new Queue<CustomerEntity>(this, "OrderQueue", true, true);
        freeOrderWindow = new Queue<OrderEntity>(this, "FreeOrderWindow", true, true);
        OrderEntity order;
        order = new OrderEntity(this, "Order", true);
        freeOrderWindow.insert(order);
        busyOrderWindow = new Queue<OrderEntity>(this, "BusyOrderWindow", true, true);

        // Pickup
        pickupTime = new ContDistUniform(this, "PickupTime", pickupTimeStart, pickupTimeEnd, true, false);
        pickupQueue = new Queue<CustomerEntity>(this, "PickupQueue", true, true);
        freePickupWindow = new Queue<PickupEntity>(this, "FreePickupWindow", true, true);
        PickupEntity pickup;
        pickup = new PickupEntity(this, "Pickup", true);
        freePickupWindow.insert(pickup);
        busyPickupWindow = new Queue<PickupEntity>(this, "BusyPickupWindow", true, true);

        // General
        salesVolumePerCustomer = new ContDistUniform(this, "SalesVolumePerCustomer", salesVolumePerCustomerMin, salesVolumePerCustomerMax, true, false);
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
                    case "--stoßzeit" -> stoßzeit = true;
                    case "--nebenzeit" -> nebenzeit = true;
                    case "--orderqueuelimit", "--oql" -> {
                        orderQueueLimit = Integer.parseInt(args[i + 1]);
                        i++;
                    }
                    case "--pickupqueuelimit", "--pql" -> {
                        pickupQueueLimit = Integer.parseInt(args[i + 1]);
                        i++;
                    }
                    case "--user", "-u" -> {
                        user = args[i + 1];
                        i++;
                    }
                    case "--quiet", "-q" -> quiet = true;
                    case "--help", "-h" -> {
                        System.out.println("Options:");
                        System.out.println("-s, --start <time>      Set the start time of the simulation (Default: 0.0)");
                        System.out.println("-e, --end <time>        Set the end time of the simulation(Default: 240.0)");
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

        dtExperiment.stop(new TimeInstant(endTime));

        dtExperiment.start();
        dtExperiment.finish();
        if (user.contains("tim")) {
            File file0 = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_debug.html");
            File file1 = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_error.html");
            File file2 = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_report.html");
            File file3 = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_trace.html");
            file0.delete();
            file1.delete();
            file2.delete();
            file3.delete();
        }
        if (user.contains("eli")) {
            File file0 = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_debug.html");
            File file1 = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_error.html");
            File file2 = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_report.html");
            File file3 = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_trace.html");
            file0.delete();
            file1.delete();
            file2.delete();
            file3.delete();
        }
        data.printLog();
        data.writeListsToFile();
        System.exit(0);
    }
}