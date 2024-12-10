import java.util.*;

public class Membership2 implements java.io.Serializable {
    private Hashtable clubMembers;
    private int maxMembers;

    public Membership2(int size) {
        clubMembers = new Hashtable(); // Create a hashtable
        maxMembers = size; // max
    }

    public int getMaxNumMembers() {
        return maxMembers;
    }

    public int getNumMembers() {
        return clubMembers.size(); // number of objects currently in the hashTable
    }

    public void addMember(String name, String address) throws MembershipFullException {
        if (getNumMembers() >= getMaxNumMembers()) {
            throw new MembershipFullException(name, address);
        }

        ClubMember newOne = new ClubMember(name, address);
        clubMembers.put(new Integer(newOne.getNumber()), newOne);
        // Explicit object Integer required because we do not store large numbers, just
        // objects
    }

    public Enumeration getAllNumbers() {
        return clubMembers.keys(); // Scan the hashtable keys
    }

    public Enumeration getAllMembers() {
        return clubMembers.elements(); // Scan the hashtable elements
    }

    public ClubMember getMember(int memberNumber) {
        return (ClubMember) clubMembers.get(new Integer(memberNumber)); // Retrieve membership
    }

    public Enumeration getMember(String name) {
        Vector subset = new Vector(); // Vector to store members
        Enumeration e = clubMembers.elements(); // Scan the hashtable

        while (e.hasMoreElements()) {
            ClubMember cm = (ClubMember) e.nextElement();
            if (cm.getName().equals(name)) {
                subset.addElement(cm); // Add member if name matches
            }
        }
        return subset.elements(); // Return subset
    }

    public void removeMember(int number) throws InvalidMemberNumberException {
        if (clubMembers.remove(new Integer(number)) == null) {
            throw new InvalidMemberNumberException(number);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Membership: number of members = " +
                getNumMembers() + " + max number of members = " + getMaxNumMembers());
        sb.append(clubMembers.toString());
        return new String(sb);
    }
}
