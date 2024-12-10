import java.util.*;

public class WaitingList implements java.io.Serializable {
    private Vector addresses, names;

    public WaitingList() {
        addresses = new Vector();
        names = new Vector();
    }

    public void add(String name, String address) {
        names.addElement(name);
        addresses.addElement(address);
    }

    public boolean isEmpty() {
        return names.isEmpty();
    }

    public String[] getFirst() {
        if (isEmpty())
            return null;

        String[] first = new String[2];
        first[0] = (String) names.firstElement();
        first[1] = (String) addresses.firstElement();
        names.removeElementAt(0);
        addresses.removeElementAt(0);
        return first;
    }

    public Enumeration names() {
        return names.elements();
    }

    public String toString() {
        return ("Names: " + names + System.getProperty("line.separator") + "Addresses: " + addresses);
    }

}