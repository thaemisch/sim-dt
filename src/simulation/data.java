package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class data {
    private static String dirPath;
    private static String fileName;
    private static List<Double> orderQueue = new ArrayList<>();

    private static List<Double> orderWindow = new ArrayList<>();

    private static List<Double> orderExit = new ArrayList<>();

    private static List<Double> pickupQueue = new ArrayList<>();

    private static List<Double> pickupWindow = new ArrayList<>();

    private static List<Double> pickupExit = new ArrayList<>();
    private static List<Double> customersLost = new ArrayList<>();
    private static List<Double> salesVolumeLost = new ArrayList<>();

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
            case "customersLost", "customerslost", "cl" -> customersLost.add(value);
            case "salesVolumeLost", "salesvolumelost", "svl" -> salesVolumeLost.add(value);
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

    public static Double getTotalSalesVolumeLost() {
        Double sum = 0.0;
        for (Double d : salesVolumeLost) {
            sum += d;
        }
        return sum;
    }

    public static void writeListsToFile(){
        writeListsToFile1();
        writeListsToFile2();
    }

    public static void writeListsToFile1() {
        // Convert the lists to a JSON-serializable format
        List<List<Double>> jsonLists = List.of(orderQueue, orderWindow, pickupQueue, pickupWindow, pickupExit, customersLost);
        if (DT_model.user.contains("tim")) {
            dirPath = "/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/data/raw/";
        } else if (DT_model.user.contains("eli")) {
            dirPath = "C:/Users/elihi/IdeaProjects/sim-dt/data/raw/";
        } else {
            System.out.println("No user, no output! Exiting...");
            System.exit(1);
        }

        File directory = new File(dirPath);

        // Convert the lists to a JSON string
        String jsonStr = convertListsToJsonString1(jsonLists);
        StringBuilder sb = new StringBuilder();
        sb.append("sim");
        if (DT_model.getEndTime() != 240.0)
            sb.append(DT_model.getEndTime().toString().replace('.', '-'));

        if (DT_model.switchToStoßzeit)
            sb.append("-nest");
        else if (DT_model.switchToNebenzeit)
            sb.append("-stne");
        if (DT_model.getOrderQueueLimit() != 10)
            sb.append("-oql").append(DT_model.getOrderQueueLimit());
        if (DT_model.getPickupQueueLimit() != 5)
            sb.append("-pql").append(DT_model.getPickupQueueLimit());
        if (DT_model.halfOrderSize) {
            sb.append("-hos");
        } else if (DT_model.threeQuarterOrderSize) {
            sb.append("-tos");
        }
        if (sb.equals("sim")) {
            sb.append("-default");
        }
        sb.append(".json");

        // Write the JSON string to the file
        File file = new File(directory, sb.toString());
        try {
            writeStringToFile(file, jsonStr);
        } catch (IOException e) {
            System.out.println("ERROR: Could not write to file");
            e.printStackTrace();
        }
    }

    public static void writeListsToFile2() {
        // Convert the lists to a JSON-serializable format
        List<List<Double>> jsonLists = List.of(salesVolume, salesVolumeLost);
        if (DT_model.user.contains("tim")) {
            dirPath = "/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/data/raw/";
        } else if (DT_model.user.contains("eli")) {
            dirPath = "C:/Users/elihi/IdeaProjects/sim-dt/data/raw/";
        } else {
            System.out.println("No user, no output! Exiting...");
            System.exit(1);
        }

        File directory = new File(dirPath);

        // Convert the lists to a JSON string
        String jsonStr = convertListsToJsonString2(jsonLists);
        StringBuilder sb = new StringBuilder();
        sb.append("extended");
        if (DT_model.getEndTime() != 240.0)
            sb.append(DT_model.getEndTime().toString().replace('.', '-'));

        if (DT_model.switchToStoßzeit)
            sb.append("-nest");
        else if (DT_model.switchToNebenzeit)
            sb.append("-stne");
        if (DT_model.getOrderQueueLimit() != 10)
            sb.append("-oql").append(DT_model.getOrderQueueLimit());
        if (DT_model.getPickupQueueLimit() != 5)
            sb.append("-pql").append(DT_model.getPickupQueueLimit());
        if (DT_model.halfOrderSize) {
            sb.append("-hos");
        } else if (DT_model.threeQuarterOrderSize) {
            sb.append("-tos");
        }
        if (sb.equals("extended")) {
            sb.append("-default");
        }
        sb.append(".json");

        // Write the JSON string to the file
        File file = new File(directory, sb.toString());
        try {
            writeStringToFile(file, jsonStr);
        } catch (IOException e) {
            System.out.println("ERROR: Could not write to file");
            e.printStackTrace();
        }
    }

    public static String convertListsToJsonString1(List<List<Double>> lists) {
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

    public static String convertListsToJsonString2(List<List<Double>> lists) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("[");
            for (int j = 0; j < lists.get(i).size(); j++) {
                try{
                    sb.append("\"").append(lists.get(i).get(j)).append("\"");
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
        System.out.println("Order Queue: " + orderQueue.size());
        System.out.println("Order Window: " +orderWindow.size());
        System.out.println("Order Exit: " + orderExit.size());
        System.out.println("Pickup Queue: " + pickupQueue.size());
        System.out.println("Pickup Window: " + pickupWindow.size());
        System.out.println("Pickup Exit: " + pickupExit.size());
        System.out.println("Sales Volume: " + getTotalSalesVolume());
        System.out.println("Customers Lost: " + customersLost.size());
        System.out.println("Sales Volume Lost: " + getTotalSalesVolumeLost());
    }
}
