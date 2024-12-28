import java.util.*;

public class Membership3 extends Membership2 implements java.io.Serializable {

    private WaitingList waitList;

    public Membership3(int size) {
        super(size);
        waitList = new WaitingList();
    }

    public String[] getWaitingListFirst() {
        return waitList.getFirst();  // Calls the WaitingList's getFirst() method
    }

    public void addMember(String name, String address) {
        try {
            super.addMember(name, address);
        } catch (MembershipFullException e) {
            waitList.add(name, address);
        }
    }

    public void removeMember(int number) throws InvalidMemberNumberException {

        super.removeMember(number); // throws an exception if unsuccessful
        String[] wlFirst = waitList.getFirst(); // does not run if exception
        // thrown by line above
        if (wlFirst != null) {
            addMember(wlFirst[0], wlFirst[1]);
        }
    }

    public String toString() {
        return super.toString() + "/n" + waitList.toString();
    }

public Enumeration<String> getWaitListNames() {
    return waitList.names();
}

}