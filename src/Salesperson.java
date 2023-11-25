import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Date;

public class Salesperson {
    private static Scanner input = new Scanner(System.in);
    private static int availableTID = 0;
    private static boolean available = false;
    public static void Salesperson_operation(){
        do{
            try{
                System.out.println("What kinds of operation would you like to perform");
                System.out.println("1. Search for parts");
                System.out.println("2. Sell a part");
                System.out.println("3. Return to the main menu");
                System.out.print("Enter Your Choice: ");
                short choice = input.nextShort();
                switch(choice){
                    case 1:
                        searchForParts();
                        break;
                    case 2:
                        transact();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("[ERROR] Invalid Input");
                }
            }catch(Exception e){
                System.out.println(e);
                input.next();
            }
        }while(true);
    }
    private static void searchForParts(){
        System.out.println("Choose the search criterion");
        System.out.println("1. Part Name");
        System.out.println("2. Manufacturer Name");
        System.out.print("Choose the search criterion: ");
        short choice = input.nextShort();
        input.nextLine();
        System.out.print("Type in the Search Keyword: ");
        String keyword = input.nextLine();
        System.out.println("Choose ordering:");
        System.out.println("1. By price, ascending order");
        System.out.println("2. By price, descending order");
        System.out.print("Choose the search criterion: ");
        short choice1 = input.nextShort();
        if((choice == 1 || choice == 2) && (choice1 == 1 || choice1 == 2) ){
            try{
                Connection conn = Main.connectToMySQL();
                String query = "SELECT P.pid,P.pName,M.mName,C.cName,P.pAvailableQuantity,P.pWarrantyPeriod,P.pPrice "+
                               "FROM part P, manufacturer M, category C ";
                if(choice == 1){
                    query += "WHERE P.pName like ? ";
                }
                else if(choice == 2){
                    query += "WHERE M.mName like ? ";
                }
                query += "AND P.mid = M.mid AND P.cid = C.cid ";
                if(choice1 == 1){
                    query += "ORDER BY P.pPrice ASC;";
                }
                else if (choice1 == 2){
                    query += "ORDER BY P.pPrice DESC;";
                }
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1,"%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.print("| ");
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rsmd.getColumnName(i));
                    System.out.print(" | ");
                }
                System.out.println();
                while (rs.next()) {
                    System.out.print("| ");
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue);
                        System.out.print(" | ");
                    }
                    System.out.println();
                }
                System.out.println("End of query.");
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        else{
            System.out.println("[ERROR] Invalid Input");
        }
    }

    private static void transact(){
        System.out.print("Enter The Part ID: ");
        int pID = input.nextInt();
        System.out.print("Enter The Salesperson ID: ");
        int sID = input.nextInt();
        try{
            // Search for part
            Connection conn = Main.connectToMySQL();
            String query = "SELECT P.pAvailableQuantity,P.pName "+
                           "FROM part P " +
                           "WHERE P.pid = ?;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,pID);
            ResultSet rsP = pstmt.executeQuery();
            // Search for salesperson
            query = "SELECT S.sid " +
                    "FROM salesperson S " +
                    "WHERE S.sid = ?;";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1,sID);
            ResultSet rsS = pstmt.executeQuery();
            if(rsP.next() && rsS.next()){ // Both part and salesperson exist
                int quantity = rsP.getInt("pAvailableQuantity");
                String pName = rsP.getString("pName");
                if(quantity > 0){ // The quantity is greater than 0
                    // Find the available transaction id if there no record before
                    if(!available){
                        query = "SELECT MAX(T.tid) AS MAXtid " + 
                                "FROM transaction T;";
                        pstmt = conn.prepareStatement(query);
                        ResultSet rsT = pstmt.executeQuery();
                        rsT.next();
                        availableTID = rsT.getInt("MAXtid") + 1;
                        available = true;
                    }
                    String insert = "INSERT " + 
                                    "into transaction " + 
                                    "Values (?,?,?,?)";
                    pstmt = conn.prepareStatement(insert);
                    pstmt.setInt(1,availableTID++);
                    pstmt.setInt(2,pID);
                    pstmt.setInt(3,sID);
                    pstmt.setDate(4,new Date(System.currentTimeMillis()));
                    pstmt.execute();
                    // Minus one for the quantity
                    String update = "UPDATE part P " +
                                    "SET P.pAvailableQuantity = P.pAvailableQuantity - 1 " +
                                    "WHERE P.pid = ?";
                    pstmt = conn.prepareStatement(update);
                    pstmt.setInt(1,pID);
                    pstmt.execute();
                    System.out.println("Product: " + pName + 
                                       "(id: " + pID + ") Remaining Quality: " + (quantity-1));
                }
                else{
                    System.out.println("Transaction failed!");
                    System.out.println("Product: " + pName + 
                                       "(id: " + pID + ") Remaining Quality: 0");
                }
            }
            else{
                System.out.println("Unavailable part ID or salesperson ID.");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
