
public class ClubMemberTest {

public static void main( String[] args ) {

ClubMember memb1, memb2, memb3; // ClubMember objects to test.

memb1 = new ClubMember ( "Brown", "LD1 6TG" ) ;
memb2 = new ClubMember ( "Green", "KP4 9LP" );
memb3 = new ClubMember( "White", "HT2 6GB" );

System. out.println ( memb1.toString () ) ;
System. out.println ( memb2.toString() );
System. out.println ( memb3.toString() );

memb2. changeAddress ( "KP9 5SA" );
System. out.println ( memb2.getName () + "'s address changed:" ) ;
System. out.println ( memb2.toString() ) ;

}
}