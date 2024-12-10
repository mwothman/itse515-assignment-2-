import java.util.*;

public class WaitingListTest {

    public static void main(String[] args) {
        WaitingList wl = new WaitingList();

        System.out.println("//Tests on empty waiting list.");
        System.out.println("isEmpty returns true?" + ((wl.isEmpty() == true) ? "Yes" : "No"));
        System.out.println("getFirst returns null? " + ((wl.getFirst() == null) ? "Yes" : "No"));
        System.out.println("names() Enumeration object has no elements? "
                + ((wl.names().hasMoreElements() == false) ? "Yes" : "No"));
        System.out.println("//toString result below.");
        System.out.println("" + wl);

        wl.add("Brown", "LD1 6TG");
        wl.add("Green", "KP4 9LP");
        wl.add("White", "HT2 6GB");
        wl.add("Smith", "AL2 5RP");

        System.out.println("");
        System.out.println("//Four names/addresses added.");
        System.out.println("isEmpty returns false?" + ((wl.isEmpty() == false) ? "Yes" : "No"));
        System.out.println("//Enumeration of names below.");
        int i = 0;
        for (Enumeration e = wl.names(); e.hasMoreElements();) {
            System.out.println(++i + ":" + e.nextElement());
        }
        System.out.println("//toString result below.");
        System.out.println("" + wl);

        System.out.println("//Result of method getFirst called 4 times below.");
        String[] first;
        for (i = 1; i <= 4; i++) {
            first = wl.getFirst();
            System.out.println(i + ": [" + first[0] + "," + first[1] + "]");
        }
        System.out.println("");
        System.out.println("//Waiting list should be empty again now.");
        System.out.println("isEmpty returns true?" + ((wl.isEmpty() == true) ? "Yes" : "No"));
        System.out.println("getFirst returns null?" + ((wl.getFirst() == null) ? "Yes" : "No"));
        System.out.println("names() Enumeration object has no elements? "
                + ((wl.names().hasMoreElements() == false) ? "Yes" : "No"));
        System.out.println("//toString result below.");
        System.out.println("" + wl);
    }
}