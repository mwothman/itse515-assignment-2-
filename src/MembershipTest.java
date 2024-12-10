public class MembershipTest {
    public static void main(String[] args) {
        // Create a Membership object with a maximum of 5 members
        Membership club = new Membership(5);

        // Add members to the club
        System.out.println("Adding members...");
        System.out.println(club.addMember("Alice", "123 Main St")); // Expected: true
        System.out.println(club.addMember("Bob", "456 Elm St"));   // Expected: true
        System.out.println(club.addMember("Alice", "789 Oak St")); // Expected: true
        System.out.println(club.addMember("Charlie", "101 Maple St")); // Expected: true
        System.out.println(club.addMember("Diana", "202 Birch St"));   // Expected: true
        System.out.println(club.addMember("Eve", "303 Pine St"));      // Expected: false (max members reached)

        // Display the current number of members and max members
        System.out.println("\nMembership Details:");
        System.out.println("Current members: " + club.getNumMembers());
        System.out.println("Max members: " + club.getMaxNumMembers());

        // Retrieve a member by index
        System.out.println("\nRetrieving members by index...");
        System.out.println(club.getMember(0)); // Expected: ClubMember object for Alice at 123 Main St
        System.out.println(club.getMember(4)); // Expected: ClubMember object for Diana at 202 Birch St
        System.out.println(club.getMember(5)); // Expected: null (invalid index)

        // Retrieve members by name
        System.out.println("\nRetrieving members by name 'Alice'...");
        ClubMember[] alices = club.getMember("Alice");
        for (ClubMember alice : alices) {
            System.out.println(alice);
        }

        // Display all members using toString
        System.out.println("\nAll members:");
        System.out.println(club.toString());
    }
}
