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

    // Queue for orders
    private ProcessQueue<Customer> orderQueue;

}