public class Membership {
    // Instance Variables
    private ClubMember[] membership; // declare how many members
    private int numMembers;

    // Constructor
    Membership(int maxMembers) {
        numMembers = 0;
        membership = new ClubMember[maxMembers];
    }

    // Methods
    public int getMaxNumMembers() {
        return membership.length; // how many members can we have in club.
    }

    public int getNumMembers() {
        return numMembers;
    }

    public boolean addMember(String name, String address) {
        if (numMembers < membership.length) {
            ClubMember newOne = new ClubMember(name, address);
            membership[numMembers] = newOne;
            ++numMembers;
            return true; // in the process being successfully added
        } else {
            return false;
        }
    }

    public ClubMember getMember(int memberNumber) {
        if (memberNumber >= 0 && memberNumber < numMembers) {
            return membership[memberNumber];
        } else {
            return null;
        }
    }

    public ClubMember[] getMember(String name) {
        int count = 0;
        for (int i = 0; i < numMembers; i++) {
            if (membership[i].getName().equals(name)) {
                ++count;
            }
        }

        ClubMember[] retArr = new ClubMember[count]; // local array
        count = 0;
        for (int i = 0; i < numMembers; i++) {
            if (membership[i].getName().equals(name)) {
                retArr[count++] = membership[i];
            }
        }
        return retArr;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Membership: number of members=" +
                getNumMembers() + " + max number of members = " + getMaxNumMembers());
        String eol = System.getProperty("line.separator"); // or (char)13 + (char)10 to add CR+LF
        sb.append(eol);
        for (int i = 0; i < numMembers; i++) {
            sb.append(eol);
            sb.append(membership[i].toString());
        }
        return new String(sb);
    }
}
