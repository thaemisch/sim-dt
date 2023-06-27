package simulation;

import desmoj.core.simulator.*;

public class CustomerEntity extends Entity {
    private static double pickupQueueEntry;
    private static double pickupQueueExit;
    public CustomerEntity(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    public static void setPickupQueueEntry(double time) {
        pickupQueueEntry = time;
    }
    public static void setPickupQueueExit(double time) {
        pickupQueueExit = time;
    }

    public static Double getPickupQueueTime() {
        return pickupQueueExit - pickupQueueEntry;
    }

}