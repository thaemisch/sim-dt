package simulation;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DT_model extends Model {
    // General
    static String user;
    public static int customerCounter = 0;
    public static Boolean save = false;
    public static Boolean quiet = false;
    public static Boolean switchToNebenzeit = false;
    public static Boolean switchToStoßzeit = false;
    // Variables
    static double startTime = 0.0;
    static double endTime = 240.0;
    static Boolean stoßzeit = false;
    public static void setStoßzeit(){
        stoßzeit = true;
        nebenzeit = false;
    }
    static Boolean nebenzeit = false;
    public static void setNebenzeit(){
        nebenzeit = true;
        stoßzeit = false;
    }
    static Boolean halfOrderSize = false;
    static double arrivalTimeDiff = 1.0;
    static double orderTimeStartDiff = 1.0;
    static double orderTimeEndDiff = 1.0;
    static double orderTimeMeanDiff = 1.0;
    static double pickupTimeStartDiff = 1.0;
    static double pickupTimeEndDiff = 1.0;
    static double pickupTimeMeanDiff =1.0;

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
    private ContDistTriangular orderTime;
    public double getOrderTime() {
        return orderTime.sample();
    }
    private static int orderQueueLimit = 0;
    public static int getOrderQueueLimit() {
        return orderQueueLimit;
    }
    protected Queue<CustomerEntity> orderQueue;
    protected Queue<OrderEntity> freeOrderWindow;
    protected Queue<OrderEntity> busyOrderWindow;

    protected Queue<CustomerEntity> stuckInOrder;

    /*
     * Pickup
     */
    private ContDistTriangular pickupTime;
    public double getPickupTime() {
        return pickupTime.sample();
    }
    private static int pickupQueueLimit = 0;
    public static int getPickupQueueLimit() {
        return pickupQueueLimit;
    }
    protected Queue<CustomerEntity> pickupQueue;
    protected Queue<PickupEntity> freePickupWindow;
    protected Queue<PickupEntity> busyPickupWindow;

    /*
     * General
     */

    private ContDistUniform salesVolumePerCustomer;
    public double getSalesVolumePerCustomer() {
        return salesVolumePerCustomer.sample();
    }
    static double salesVolumePerCustomerMinDiff = 1.0;
    static double salesVolumePerCustomerMaxDiff = 1.0;


    public DT_model(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }

    public void doInitialSchedules() {
        CustomerNewEvent firstCustomer = new CustomerNewEvent(this, "FirstCustomer", true);
        firstCustomer.schedule(new TimeInstant(getCustomerArrivalTime()));

        if (switchToNebenzeit){
            SwitchToNebenzeitEvent switchToNebenzeit = new SwitchToNebenzeitEvent(this, "SwitchToNebenzeit", true);
            switchToNebenzeit.schedule(new TimeSpan((endTime - startTime)/2));
        }
    }

    public void init() {
        if (halfOrderSize){
            orderTimeEndDiff = 0.5;
            orderTimeMeanDiff = 0.5;

            pickupTimeEndDiff = 0.5;
            pickupTimeMeanDiff = 0.5;

            salesVolumePerCustomerMaxDiff = 0.5;
        }
        if (stoßzeit){
            customInit(0.833*arrivalTimeDiff, 0.167*orderTimeStartDiff, 2.2833*orderTimeEndDiff, 0.967*orderTimeMeanDiff, 0.0833*pickupTimeStartDiff, 8.3167*pickupTimeEndDiff, 1.833*pickupTimeMeanDiff, 5.0*salesVolumePerCustomerMinDiff, 30.0*salesVolumePerCustomerMaxDiff);
        } else if (nebenzeit){
            customInit(1.33*arrivalTimeDiff, 0.33*orderTimeStartDiff, 1.383*orderTimeEndDiff, 0.5833*orderTimeMeanDiff, 0.133*pickupTimeStartDiff, 6.6*pickupTimeEndDiff, 2.5*pickupTimeMeanDiff, 5.0*salesVolumePerCustomerMinDiff, 30.0*salesVolumePerCustomerMaxDiff);
        } else {
            customInit(1.33*arrivalTimeDiff, 0.33*orderTimeStartDiff, 1.383*orderTimeEndDiff, 0.5833*orderTimeMeanDiff, 0.133*pickupTimeStartDiff, 6.6*pickupTimeEndDiff, 2.5*pickupTimeMeanDiff, 5.0*salesVolumePerCustomerMinDiff, 30.0*salesVolumePerCustomerMaxDiff);
        }
    }
    public void initSwitch(){
        if (halfOrderSize){
            orderTimeEndDiff = 0.5;
            orderTimeMeanDiff = 0.5;

            pickupTimeEndDiff = 0.5;
            pickupTimeMeanDiff = 0.5;

            salesVolumePerCustomerMaxDiff = 0.5;
        }
        if (stoßzeit){
            customInitSwitch(0.833*arrivalTimeDiff, 0.167*orderTimeStartDiff, 2.2833*orderTimeEndDiff, 0.967*orderTimeMeanDiff, 0.0833*pickupTimeStartDiff, 8.3167*pickupTimeEndDiff, 1.833*pickupTimeMeanDiff, 5.0*salesVolumePerCustomerMinDiff, 30.0*salesVolumePerCustomerMaxDiff);
        } else if (nebenzeit){
            customInitSwitch(1.33*arrivalTimeDiff, 0.33*orderTimeStartDiff, 1.383*orderTimeEndDiff, 0.5833*orderTimeMeanDiff, 0.133*pickupTimeStartDiff, 6.6*pickupTimeEndDiff, 2.5*pickupTimeMeanDiff, 5.0*salesVolumePerCustomerMinDiff, 30.0*salesVolumePerCustomerMaxDiff);
        } else {
            customInitSwitch(1.33*arrivalTimeDiff, 0.33*orderTimeStartDiff, 1.383*orderTimeEndDiff, 0.5833*orderTimeMeanDiff, 0.133*pickupTimeStartDiff, 6.6*pickupTimeEndDiff, 2.5*pickupTimeMeanDiff, 5.0*salesVolumePerCustomerMinDiff, 30.0*salesVolumePerCustomerMaxDiff);
        }
    }


    public void customInitSwitch(Double arrivalTime, Double orderTimeStart, Double orderTimeEnd, Double orderTimeMean, Double pickupTimeStart, Double pickupTimeEnd, Double pickupTimeMean, Double salesVolumePerCustomerMin, Double salesVolumePerCustomerMax) {
        // Order
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", arrivalTime, true, false);
        customerArrivalTime.setNonNegative(true);
        orderTime = new ContDistTriangular(this, "OrderTime", orderTimeStart, orderTimeEnd, orderTimeMean, true, false);

        // Pickup
        pickupTime = new ContDistTriangular(this, "PickupTime", pickupTimeStart, pickupTimeEnd, pickupTimeMean, true, false);

        // General
        salesVolumePerCustomer = new ContDistUniform(this, "SalesVolumePerCustomer", salesVolumePerCustomerMin, salesVolumePerCustomerMax, true, false);
    }

    public void customInit(Double arrivalTime, Double orderTimeStart, Double orderTimeEnd, Double orderTimeMean, Double pickupTimeStart, Double pickupTimeEnd, Double pickupTimeMean, Double salesVolumePerCustomerMin, Double salesVolumePerCustomerMax) {
        // Order
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", arrivalTime, true, false);
        customerArrivalTime.setNonNegative(true);
        orderTime = new ContDistTriangular(this, "OrderTime", orderTimeStart, orderTimeEnd, orderTimeMean, true, false);
        orderQueue = new Queue<CustomerEntity>(this, "OrderQueue", true, true);
        freeOrderWindow = new Queue<OrderEntity>(this, "FreeOrderWindow", true, true);
        OrderEntity order;
        order = new OrderEntity(this, "Order", true);
        freeOrderWindow.insert(order);
        busyOrderWindow = new Queue<OrderEntity>(this, "BusyOrderWindow", true, true);
        stuckInOrder = new Queue<CustomerEntity>(this, "StuckInOrder", true, true);

        // Pickup
        pickupTime = new ContDistTriangular(this, "PickupTime", pickupTimeStart, pickupTimeEnd, pickupTimeMean, true, false);
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
                    case "--start" -> {
                        startTime = Double.parseDouble(args[i + 1]);
                        i++;
                    }
                    case "--end" -> {
                        endTime = Double.parseDouble(args[i + 1]);
                        i++;
                    }
                    case "--stoßzeit", "--st" -> stoßzeit = true;
                    case "--nebenzeit", "-ne" -> nebenzeit = true;
                    case "--stoßtonebenzeit", "--stne" -> {
                        stoßzeit = true;
                        switchToNebenzeit = true;
                    }
                    case "--nebentostoßzeit", "--nest" -> {
                        nebenzeit = true;
                        switchToStoßzeit = true;
                    }
                    case "--orderqueuelimit", "--oql" -> {
                        orderQueueLimit = Integer.parseInt(args[i + 1]);
                        i++;
                    }
                    case "--pickupqueuelimit", "--pql" -> {
                        pickupQueueLimit = Integer.parseInt(args[i + 1]);
                        i++;
                    }
                    case "--halfordersize", "--hos" -> {
                        halfOrderSize = true;
                    }
                    case "--user", "-u" -> {
                        user = args[i + 1];
                        i++;
                    }
                    case "--save", "-s" -> save = true;
                    case "--quiet", "-q" -> quiet = true;
                    case "--help", "-h" -> {
                        System.out.println("Options:");
                        System.out.println("    --start <time> ________________________ Set the start time of the simulation (Default: 0.0)");
                        System.out.println("    --end <time> __________________________ Set the end time of the simulation(Default: 240.0)");
                        System.out.println("    --stoßzeit, --st ______________________ Set the simulation to Stoßzeit");
                        System.out.println("    --nebenzeit, --ne _____________________ Set the simulation to Nebenzeit");
                        System.out.println("    --switchtonebenzeit, --stne ___________ Set the simulation to switch from Stoß- to Nebenzeit");
                        System.out.println("    --switchtostoßzeit, --nest ____________ Set the simulation to switch from Neben- to Stoßzeit");
                        System.out.println("    --orderqueuelimit, --oql <limit> ______ Set the order queue limit (Default: 10)");
                        System.out.println("    --pickupqueuelimit, --pql <limit> _____ Set the pickup queue limit (Default: 10)");
                        System.out.println("    --halfordersize, --hos ________________ Set the order size to half");
                        System.out.println("    --user, -u <name> _____________________ Set the user name");
                        System.out.println("    --save, -s ____________________________ Save the results to a file");
                        System.out.println("    --quiet, -q ___________________________ Don't print the results to the console");
                        System.out.println("    --help, -h ____________________________ Show this help");
                        System.exit(0);
                    }
                    default -> {
                        System.err.println("Unknown argument: " + arg);
                        main(new String[]{"--help"});
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
            File debug = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_debug.html");
            File error = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_error.html");
            File report= new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_report.html");
            File trace = new File("/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/src/simulation/dt-event_trace.html");
            debug.delete();
            error.delete();
            report.delete();
            trace.delete();
        } else if (user.contains("eli")) {
            File debug = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_debug.html");
            File error = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_error.html");
            File report = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_report.html");
            File trace = new File("C:/Users/elihi/IdeaProjects/sim-dt/src/simulation/dt-event_trace.html");
            debug.delete();
            error.delete();
            report.delete();
            trace.delete();
        }
        data.printLog();
        data.missingCustomers();
        if (save)
            data.writeListsToFile();
        System.exit(0);
    }
}