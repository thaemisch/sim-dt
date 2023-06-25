package simulation;

public class data {

    /**
     * Prints a string to the console if the model is not in quiet mode.
     * @param s The string to print.
     */
    static void silentScreamer(String s) {
        if (!DT_model.quiet)
            System.out.println(s);
    }
}
