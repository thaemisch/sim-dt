package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class data {
    private static String directoryPath = "/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/data/";
    private static Double[] orderQueue = new Double[1000];
    private static int orderQueueIndex = 0;

    private static Double[] orderWindow= new Double[1000];
    private static int orderWindowIndex = 0;

    private static Double[] orderExit= new Double[1000];
    private static int orderExitIndex = 0;

    private static Double[] pickupQueue= new Double[1000];
    private static int pickupQueueIndex = 0;

    private static Double[] pickupWindow= new Double[1000];
    private static int pickupWindowIndex = 0;

    private static Double[] pickupExit= new Double[1000];
    private static int pickupExitIndex = 0;

    private static Double[] salesVolume = new Double[1000];
    private static int salesVolumeIndex = 0;

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
            case "orderQueue", "orderqueue", "oq" -> {
                orderQueue[orderQueueIndex] = value;
                orderQueueIndex++;
            }
            case "orderWindow", "orderwindow", "ow" -> {
                orderWindow[orderWindowIndex] = value;
                orderWindowIndex++;
            }
            case "orderExit", "orderexit", "oe" -> {
                orderExit[orderExitIndex] = value;
                orderExitIndex++;
            }
            case "pickupQueue", "pickupqueue", "pq" -> {
                pickupQueue[pickupQueueIndex] = value;
                pickupQueueIndex++;
            }
            case "pickupWindow", "pickupwindow", "pw" -> {
                pickupWindow[pickupWindowIndex] = value;
                pickupWindowIndex++;
            }
            case "pickupExit", "pickupexit", "pe" -> {
                pickupExit[pickupExitIndex] = value;
                pickupExitIndex++;
            }
            case "salesVolume", "salesvolume", "sv" -> {
                salesVolume[salesVolumeIndex] = value;
                salesVolumeIndex++;
            }
            default -> {
                System.out.println("ERROR: INVALID TYPE FOR LOGGING");
            }
        }
    }

    public static Double getTotalSalesVolume() {
        Double sum = 0.0;
        for (Double d : salesVolume) {
            if (d == null) {
                break;
            }
            sum += d;
        }
        return sum;
    }

    public static void writeListsToFile() {
        // Convert the lists to a JSON-serializable format
        List<List<Double>> jsonLists = List.of(convertDoubleArrayToList(orderQueue), convertDoubleArrayToList(orderWindow), convertDoubleArrayToList(pickupQueue), convertDoubleArrayToList(pickupWindow), convertDoubleArrayToList(pickupExit));
        if (DT_model.user.contains("tim")) {
            String directoryPath = "/home/tim/Documents/Uni/Informatik/S4/sim/sim-dt/data/";
        } else if (DT_model.user.contains("eli")) {
            String directoryPath = "C:/Users/elihi/IdeaProjects/sim-dt/data/";
        } else {
            System.exit(1);
        }

        File directory = new File(directoryPath);

        // Convert the lists to a JSON string
        String jsonStr = convertListsToJsonString(jsonLists);

        // Write the JSON string to the file
        File file = new File(directory, "simulation.json");
        try {
            writeStringToFile(file, jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Double> convertDoubleArrayToList(Double[] array) {
        List<Double> list = new ArrayList<>();
        for (Double d : array) {
            list.add(d);
            if (d == null) {
                break;
            }
        }
        return list;
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
        //System.out.println("Sales Volume: " + Arrays.toString(salesVolume));
        System.out.println("Sales Volume: " + getTotalSalesVolume());
    }
}
