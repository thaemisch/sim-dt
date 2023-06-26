package simulation;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;

public class DT_model extends Model {
    // Variables
    static double startTime = 0.0;
    static double endTime = 240.0;
    static Boolean stoßzeit = false;
    static Boolean nebenzeit = false;
    public static Boolean quiet = false;

    static double arrivalTimeDiff = 0.0;
    static double orderTimeStartDiff = 0.0;
    static double orderTimeEndDiff = 0.0;
    static double pickupTimeStartDiff = 0.0;
    static double pickupTimeEndDiff = 0.0;

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
    protected Queue<CustomerEntity> pickupQueue;
    protected Queue<PickupEntity> freePickupWindow;
    protected Queue<PickupEntity> busyPickupWindow;


    public DT_model(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    public void doInitialSchedules() {
        CustomerNewEvent firstCustomer = new CustomerNewEvent(this, "FirstCustomer", true);

        firstCustomer.schedule(new TimeInstant(getCustomerArrivalTime()));
    }

    public void init() {
        if (stoßzeit){
            customInit(1.04+arrivalTimeDiff, 0.1+orderTimeStartDiff, 2.17+orderTimeEndDiff, 0.6+pickupTimeStartDiff, 4.52+pickupTimeEndDiff);
        } else if (nebenzeit){
            customInit(1.2+arrivalTimeDiff, 0.18+orderTimeStartDiff, 1.23+orderTimeEndDiff, 0.08+pickupTimeStartDiff, 3.2+pickupTimeEndDiff);
        } else {
            defaultInit();
        }
    }

    public void defaultInit() {
        // Order
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", 1.09, true, false);
        customerArrivalTime.setNonNegative(true);
        orderTime = new ContDistUniform(this, "OrderTime", 0.1, 2.17, true, false);
        orderQueue = new Queue<CustomerEntity>(this, "OrderQueue", true, true);
        freeOrderWindow = new Queue<OrderEntity>(this, "FreeOrderWindow", true, true);
        OrderEntity order;
        order = new OrderEntity(this, "Order", true);
        freeOrderWindow.insert(order);
        busyOrderWindow = new Queue<OrderEntity>(this, "BusyOrderWindow", true, true);

        // Pickup
        pickupTime = new ContDistUniform(this, "PickupTime", 0.3, 4.52, true, false);
        pickupQueue = new Queue<CustomerEntity>(this, "PickupQueue", true, true);
        freePickupWindow = new Queue<PickupEntity>(this, "FreePickupWindow", true, true);
        PickupEntity pickup;
        pickup = new PickupEntity(this, "Pickup", true);
        freePickupWindow.insert(pickup);
        busyPickupWindow = new Queue<PickupEntity>(this, "BusyPickupWindow", true, true);
    }

    public void customInit(Double arrivalTime, Double orderTimeStart, Double orderTimeEnd, Double pickupTimeStart, Double pickupTimeEnd) {
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
        data.printLog();
        System.exit(0);
    }
}