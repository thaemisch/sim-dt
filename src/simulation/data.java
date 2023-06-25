package simulation;

public class data {

    private static String[] orderQueue = new String[1000];
    private static int orderQueueIndex = 0;

    private static String[] orderWindow= new String[1000];
    private static int orderWindowIndex = 0;

    private static String[] orderExit= new String[1000];
    private static int orderExitIndex = 0;

    private static String[] pickupQueue= new String[1000];
    private static int pickupQueueIndex = 0;

    private static String[] pickupWindow= new String[1000];
    private static int pickupWindowIndex = 0;

    private static String[] pickupExit= new String[1000];
    private static int pickupExitIndex = 0;

    /**
     * Prints a string to the console if the model is not in quiet mode.
     * @param s The string to print.
     */
    static void silentScreamer(String s) {
        if (!DT_model.quiet)
            System.out.println(s);
    }

    /**
     * Logs Timestamps for the given type.
     * @param type orderQueue, orderWindow, pickupQueue, pickupWindow
     */
    static void chronoLogger(String type, Double time){
        switch (type) {
            case "orderQueue", "orderqueue", "oq" -> {
                orderQueue[orderQueueIndex] = time.toString();
                orderQueueIndex++;
            }
            case "orderWindow", "orderwindow", "ow" -> {
                orderWindow[orderWindowIndex] = time.toString();
                orderWindowIndex++;
            }
            case "orderExit", "orderexit", "oe" -> {
                orderExit[orderExitIndex] = time.toString();
                orderExitIndex++;
            }
            case "pickupQueue", "pickupqueue", "pq" -> {
                pickupQueue[pickupQueueIndex] = time.toString();
                pickupQueueIndex++;
            }
            case "pickupWindow", "pickupwindow", "pw" -> {
                pickupWindow[pickupWindowIndex] = time.toString();
                pickupWindowIndex++;
            }
            case "pickupExit", "pickupexit", "pe" -> {
                pickupExit[pickupExitIndex] = time.toString();
                pickupExitIndex++;
            }
            default -> {
                System.out.println("ERROR: INVALID TYPE FOR LOGGING");
            }
        }
    }

    static void printLog(){
        //System.out.println("Order Queue: " + Arrays.toString(orderQueue));
        System.out.println("Order Queue: " + orderQueueIndex);
        //System.out.println("Order Window: " + Arrays.toString(orderWindow));
        System.out.println("Order Window: " +orderWindowIndex);
        //System.out.println("Order Exit: " + Arrays.toString(orderExit));
        System.out.println("Order Exit: " + orderExitIndex);
        //System.out.println("Pickup Queue: " + Arrays.toString(pickupQueue));
        System.out.println("Pickup Queue: " + pickupQueueIndex);
        //System.out.println("Pickup Window: " + Arrays.toString(pickupWindow));
        System.out.println("Pickup Window: " + pickupWindowIndex);
        //System.out.println("Pickup Exit: " + Arrays.toString(pickupExit));
        System.out.println("Pickup Exit: " + pickupExitIndex);
    }
}
