import java.util.Enumeration;

public class Membership2Test {

    public static void main(String[] args) {
        int i = 0;
        Enumeration en;
        Membership2 ms = new Membership2(4);
        System.out.println("//Test output for a new empty Membership2 object (max 4 members).");
        System.out.println("# of members=0?" + ((ms.getMaxNumMembers() == 0) ? "Yes" : "No"));
        System.out.println("Max # of members=4?" + ((ms.getMaxNumMembers() == 4) ? "Yes" : "No"));
        System.out.println("getAllNumbers() Enumeration object has no elements?" +
                ((ms.getAllNumbers().hasMoreElements() == false) ? "Yes" : "No"));
        System.out.println("getAllMembers() Enumeration object has no elements?" +
                ((ms.getAllNumbers().hasMoreElements() == false) ? "Yes" : "No"));
        System.out.println("getMember(1) returns null?" + ((ms.getMember(1) == null) ? "Ycs" : "No"));
        System.out.println("getMember(\"test1\") Enumeration object has no elements?" +
                ((ms.getMember("testl").hasMoreElements() == false) ? "Yes" : "No"));
        System.out.print("removeMember(1) generates exception? ");
        try {
            ms.removeMember(1);
            System.out.println("No");
        } catch (InvalidMemberNumberException e) {
            System.out.println("Yes -" + e.getMessage());
        }
        System.out.println("//toString result below.");
        System.out.println("" + ms);

        System.out.println("");
        System.out.println("//Adding 4 members.");
        try {
            for (i = 1; i < 4; i++) {
                System.out.println("//Adding member: test" + i + ", address" + i);
                ms.addMember("test" + i, "address" + i);
            }
            System.out.println("//Adding member: test1, address4");
            ms.addMember("test1", "address4");
        } catch (MembershipFullException e) {
            System.out.println("Unexpected Membership full exception -" + e.getMessage());
            return;
        }
        System.out.println("# of members=4?" + ((ms.getMaxNumMembers() == 4) ? "Yes" : "No"));
        System.out.println("Max # of members=4?" + ((ms.getMaxNumMembers() == 4) ? "Yes" : "No"));
        System.out.print("Adding fifth member generates exception? ");
        try {
            ms.addMember("test5", "address5");
            System.out.println("No");
        } catch (MembershipFullException e) {
            System.out.println("Yes -" + e.getMessage());
        }
        System.out.println("//Enumeration of all membership numbers below.");
        for (en = ms.getAllNumbers(), i = 0; en.hasMoreElements();) {
            System.out.println(++i + ";" + en.nextElement());
        }

        System.out.println("//Enumeration of all members below.");
        for (en = ms.getAllMembers(), i = 0; en.hasMoreElements();) {
            System.out.println(++i + ":" + en.nextElement());
        }
        System.out.println("//Enumeration of members with name of test1 below.");
        for (en = ms.getMember("testl"), i = 0; en.hasMoreElements();) {
            System.out.println(++i + ":" + en.nextElement());
        }
        System.out.println("Details of member # 1:" + ms.getMember(1));
        System.out.println("//toString result below.");
        System.out.println("" + ms);

        System.out.print("removeMember(1) does not generates exception?");
        try {
            ms.removeMember(1);
            System.out.println("Yes");
        } catch (InvalidMemberNumberException e) {
            System.out.println("EXCEPTION -" + e.getMessage());
        }
        System.out.println("# of members=3?" + ((ms.getMaxNumMembers() == 3) ? "Yes" : "No"));
        System.out.println("//toString result below.");
        System.out.println("" + ms);

    }
}