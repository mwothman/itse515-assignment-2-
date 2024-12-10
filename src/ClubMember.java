import java.io.Serializable;

public class ClubMember implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    // Instance Variables
    private String memName, memAddress;
    private int memNumber;
    
    // Static class Variable
    public static int membershipNumber=0;
    
    //Constructor
    ClubMember(String nam,String add){
    memName = nam;
    memAddress = add;
    memNumber = membershipNumber++;
    
    }
    
    // Methods
    public String getName(){
    return memName;
    
    }
    
    public String getAddress(){
    return memAddress;
    }
    public int getNumber(){
    return memNumber;
    }
    
    public void changeAddress(String newAddress){
    memAddress = newAddress;
    
    }
    
    public String toString(){
    return ("ClubMember: Name="+ memName +" Address=" + memAddress + " Number=" + memNumber);
    }
}