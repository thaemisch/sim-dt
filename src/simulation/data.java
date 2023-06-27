package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import desmoj.core.simulator.Model;

public class data {
    private static String dirPath;
    private static String fileName;
    private static List<Double> orderQueue = new ArrayList<>();

    private static List<Double> orderWindow = new ArrayList<>();

    private static List<Double> orderExit = new ArrayList<>();

    private static List<Double> pickupQueue = new ArrayList<>();

    private static List<Double> pickupWindow = new ArrayList<>();

    private static List<Double> pickupExit = new ArrayList<>();

    private static List<Double> salesVolume = new ArrayList<>();

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
    static void chronoLogger(String type, Double value){
        switch (type) {
            case "orderQueue", "orderqueue", "oq" -> orderQueue.add(value);
            case "orderWindow", "orderwindow", "ow" -> orderWindow.add(value);
            case "orderExit", "orderexit", "oe" -> orderExit.add(value);
            case "pickupQueue", "pickupqueue", "pq" -> pickupQueue.add(value);
            case "pickupWindow", "pickupwindow", "pw" -> pickupWindow.add(value);
            case "pickupExit", "pickupexit", "pe" -> pickupExit.add(value);
            case "salesVolume", "salesvolume", "sv" -> salesVolume.add(value);
            default -> {
                System.out.println("ERROR: INVALID TYPE FOR LOGGING");
            }
        }
    }

    public static Double getTotalSalesVolume() {
        Double sum = 0.0;
        for (Double d : salesVolume) {
            sum += d;
        }
        return sum;
    }

    public static void writeListsToFile() {
        // Convert the lists to a JSON-serializable format
        List<List<Double>> jsonLists = List.of(orderQueue, orderWindow, orderExit, pickupQueue, pickupWindow, pickupExit);
        if (DT_model.user.contains("tim")) {
            dirPath = "/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/data/";
        } else if (DT_model.user.contains("eli")) {
            dirPath = "C:/Users/elihi/IdeaProjects/sim-dt/data/";
        } else {
            System.exit(1);
        }

        File directory = new File(dirPath);

        // Convert the lists to a JSON string
        String jsonStr = convertListsToJsonString(jsonLists);
        StringBuilder sb = new StringBuilder();
        sb.append("sim");

        if (DT_model.switchToStoßzeit) {
            sb.append("-nest");
        } else if (DT_model.switchToNebenzeit) {
            sb.append("-stne");
        }
        if (DT_model.halfOrderSize) {
            sb.append("-half");
        }
        if (sb.equals("sim")) {
            sb.append("-default");
        }

        // Write the JSON string to the file
        File file = new File(directory, sb.toString());
        try {
            writeStringToFile(file, jsonStr);
        } catch (IOException e) {
            System.out.println("ERROR: Could not write to file");
            e.printStackTrace();
        }
    }

    public static String convertListsToJsonString(List<List<Double>> lists) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("[");
            for (int j = 0; j < lists.get(i).size(); j++) {
                try{
                    sb.append("\"").append(convertMinutesToTimeString(lists.get(i).get(j))).append("\"");
                }catch (NullPointerException e){
                    sb.append("null").append("\"");
                }

                if (j < lists.get(i).size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            if (i < lists.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void writeStringToFile(File file, String str) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(str);
        writer.close();
    }

    public static String convertMinutesToTimeString(double minutes) {
        int hours = (int) (minutes / 60);
        int remainingMinutes = (int) (minutes % 60);
        int seconds = (int) ((minutes * 60) % 60);

        StringBuilder sb = new StringBuilder();
        if (hours < 10) {
            sb.append("0");
        }
        sb.append(hours).append(":");
        if (remainingMinutes < 10) {
            sb.append("0");
        }
        sb.append(remainingMinutes).append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);

        return sb.toString();
    }


    static void printLog(){
        //System.out.println("Order Queue: " + Arrays.toString(orderQueue));
        System.out.println("Order Queue: " + orderQueue.size());
        //System.out.println("Order Window: " + Arrays.toString(orderWindow));
        System.out.println("Order Window: " +orderWindow.size());
        //System.out.println("Order Exit: " + Arrays.toString(orderExit));
        System.out.println("Order Exit: " + orderExit.size());
        //System.out.println("Pickup Queue: " + Arrays.toString(pickupQueue));
        System.out.println("Pickup Queue: " + pickupQueue.size());
        //System.out.println("Pickup Window: " + Arrays.toString(pickupWindow));
        System.out.println("Pickup Window: " + pickupWindow.size());
        //System.out.println("Pickup Exit: " + Arrays.toString(pickupExit));
        System.out.println("Pickup Exit: " + pickupExit.size());
        //System.out.println("Sales Volume: " + Arrays.toString(salesVolume));
        System.out.println("Sales Volume: " + getTotalSalesVolume());

        System.out.println("--------------------");
        System.out.println("Problems:");
        if (orderQueue.size()- orderWindow.size() > DT_model.getOrderQueueLimit()) {
            System.out.println("OrderQueue: " + (orderQueue.size()- orderWindow.size()- DT_model.getOrderQueueLimit()) + " too many");
        }
        if (pickupQueue.size()- pickupWindow.size() > DT_model.getPickupQueueLimit()) {
            System.out.println("PickupQueue: " + (pickupQueue.size()- pickupWindow.size()- DT_model.getPickupQueueLimit()) + " too many");
        }
    }
}
