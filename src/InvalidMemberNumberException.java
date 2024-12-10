
public class InvalidMemberNumberException extends Exception {

    public InvalidMemberNumberException(int number) {
        super("Invalid member number: " + number);

    }
}