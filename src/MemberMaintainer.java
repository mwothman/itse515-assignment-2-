import java.io.*;

public class MemberMaintainer {
    public static void main(String[] args) {
        Membership3 membs = null;
        try {
            FileInputStream fis = new FileInputStream("Membership3.data");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ClubMember.membershipNumber = ois.readInt();
            membs = (Membership3) ois.readObject();
        } catch (FileNotFoundException e) {
            membs = new Membership3(5);
        } catch (Exception e) {
            System.out.println("Object read error! " + e.getMessage());
            System.exit(1);
        }
        // Code needs to be included here to add and/or remove members specified
        // by command line arguments.
        
        for( int i = 0; i < args.length; i++ ) {
            if( args[i].length() <2){
            System. out.println ( "Argument syntax error, command too short: " + args[i]);
            continue; //causes a jump out of the current body, reducing the
            // need for 'else' statements
            }
            switch (args[i].charAt (0)) {
            case '+':
            int sepIdx = args[i].indexOf('+',1);
            
            if( sepIdx < 2 ){
            System. out.println ("Argument syntax error, null name or missing address separator: "+ args[i]);
            continue;
            }
            membs.addMember (args[i].substring(1, sepIdx), args[i].substring(sepIdx+1));
            
            break;
            
            case '-':
            try {
            membs. removeMember (Integer.parseInt (args[i].substring(1)) );
            
            } catch (NumberFormatException e) {
            System. out.println ( "Argument syntax error, invalid integer in remove command: " + args[i]);
            } catch (InvalidMemberNumberException e) {
            System. out.println ( e.getMessage () );
            }
            break;
            
            default:
            System. out.println ("Argument syntax error, unrecognised command: "+ args[i]);
            }
        }

        System.out.println(membs);
        try {
            FileOutputStream fos = new FileOutputStream("Membership3.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(ClubMember.membershipNumber);
            oos.writeObject(membs);
        } catch (Exception e) {
            System.out.println("Object write error! " + e.getMessage());
        }

    }
}

// java MemberMaintainer -4
// java MemberMaintainer +Timothy+LS16_7QW
// java MemberMaintainer +Benjamin+IV3 5RF -2