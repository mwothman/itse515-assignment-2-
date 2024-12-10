public class MembershipFullException extends Exception {

    private String name, address;

    public MembershipFullException(String name, String address) {
        super("Membership is full, cannot add" + name + " of " + address);
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
