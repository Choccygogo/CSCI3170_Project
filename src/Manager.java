import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;

public class Manager {
    public static Scanner input = new Scanner(System.in);
        public static void Manager_operation(){
        do{
            System.out.println("What kinds of operation would like to perform?");
            System.out.println("1. List all salespersons");
            System.out.println("2. Count the no. of sales record of each salesperson under a specific range on years of experience");
            System.out.println("3. Show the total sales value of each manufacturer");
            System.out.println("4. Show the N most popular part");
            System.out.println("5. Return to the main menu");
            System.out.println("Enter your choice: ");

            int option = input.nextInt();
            switch (option){
                case 1:
                    listAllSalesperson();
                    break;
                case 2:
                    countRecordswithRangeofExperience();
                    break;
                case 3:
                    listManufacturersByTotalSales();
                    break;
                case 4:
                    System.out.println("Type in the number of parts: ");
                    int N = input.nextInt();
                    listTopNPopularParts(N);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("[ERROR] Invalid Input");
            }
        } while(true);
    }
    private static void listAllSalesperson(){
        String orderbyAscending = "SELECT sid, sName, sPhoneNumber, sExperience FROM salesperson ORDER BY sExperience ASC;";
        String orderbyDescending = "SELECT sid, sName, sPhoneNumber, sExperience FROM salesperson ORDER BY sExperience DESC;";
        System.out.println("Choose ordering:");
        System.out.println("1. By ascending order");
        System.out.println("2. By descending order");
        System.out.print("Choose the list ordering: ");
        int opt = input.nextInt();
        try{
            Connection mysql = Main.connectToMySQL();
            Statement sql = mysql.createStatement();
            //switch(opt)
            if(opt == 1){
                ResultSet ascendingRS = sql.executeQuery(orderbyAscending);
                // Get the metadata of the ResultSet
                ResultSetMetaData rsmd = ascendingRS.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("| ID | Name | Mobile Phone | Years of Experience |");
                // Iterate through the ResultSet, row by row
                while (ascendingRS.next()) {
                    // Iterate through each column of the row
                    System.out.print("| ");
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(" | ");
                        String columnValue = ascendingRS.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println(" |");
                }
            }
            else if(opt == 2){
                ResultSet descendingRS = sql.executeQuery(orderbyDescending);
                // Get the metadata of the ResultSet
                ResultSetMetaData rsmd = descendingRS.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.print("| ");
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(" | ");
                    System.out.print(rsmd.getColumnName(i));
                }
                System.out.println(" |");
                // Iterate through the ResultSet, row by row
                while (descendingRS.next()) {
                    // Iterate through each column of the row
                    System.out.print("| ");
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(" | ");
                        String columnValue = descendingRS.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println(" |");
                }
            }
            else {
                System.out.println("[ERROR] Invalid Input");
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
    private static void countRecordswithRangeofExperience(){
        String countwithRange = "SELECT s.sid, s.sName, s.sExperience, COUNT(t.sid) as transaction_count FROM salesperson s LEFT JOIN transaction t ON s.sid = t.sid WHERE s.sExperience BETWEEN ? AND ? GROUP BY s.sid ORDER BY s.sid DESC;";
        System.out.println("Type in the lower bound for years of experience: ");
        int opt1 = input.nextInt();
        System.out.println("Type in the uppper bound for years of experience: ");
        int opt2 = input.nextInt();
        try{
            Connection mysql = Main.connectToMySQL();
            Statement sql = mysql.createStatement();
            PreparedStatement countPS = mysql.prepareStatement(countwithRange);
            countPS.setInt(1,opt1);
            countPS.setInt(2,opt2);
            if(opt1<=opt2){
                countPS.setInt(1, opt1);
                countPS.setInt(2, opt2);
                ResultSet result=countPS.executeQuery();
                // Get the metadata of the ResultSet
                ResultSetMetaData rsmd = countPS.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("| ID | Name | Years of Experience | Number of Transaction");
                // Iterate through the ResultSet, row by row
                while (result.next()) {
                    // Iterate through each column of the row
                    System.out.print("| ");
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(" | ");
                        String columnValue = result.getString(i);
                        System.out.print(columnValue);
                    }
                    System.out.println(" |");
                }
            }
            else {
                System.out.println("[ERROR] Invalid Input");
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
    private static void listManufacturersByTotalSales() { 

        String sql = "SELECT m.manufacturerID, m.manufacturerName, SUM(t.salesValue) as totalSalesValue " + 
                     "FROM manufacturer m " + 
                     "JOIN part p ON m.manufacturerID = p.manufacturerID " + 
                     "JOIN transaction t ON p.partID = t.partID " + 
                     "GROUP BY m.manufacturerID, m.manufacturerName " + 
                     "ORDER BY totalSalesValue DESC;";
    
        try {
    
            Connection mysql = Main.connectToMySQL();
    
            Statement stmt = mysql.createStatement();
    
            ResultSet rs = stmt.executeQuery(sql);
    
            System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
    
            while (rs.next()) {
    
                System.out.println("| " + rs.getString("manufacturerID") + " | " + rs.getString("manufacturerName") + " | " + rs.getString("totalSalesValue") + " |");
    
            }
    
        } catch (Exception e) {
    
            System.out.println(e);
    
        }
    
    }
    
    private static void listTopNPopularParts(int N) {
        String sql = "SELECT p.partID, p.partName, COUNT(t.transactionID) as totalTransaction " + 
                     "FROM part p " + 
                     "JOIN transaction t ON p.partID = t.partID " +
                     "GROUP BY p.partID, p.partName " + 
                     "ORDER BY totalTransaction DESC " + 
                     "LIMIT " + N + ";"; 
    
        try {
    
            Connection mysql = Main.connectToMySQL();
    
            Statement stmt = mysql.createStatement();
    
            ResultSet rs = stmt.executeQuery(sql);
    
            System.out.println("| Part ID | Part Name | No. of Transaction |");
    
            while (rs.next()) {
    
                System.out.println("| " + rs.getString("partID") + " | " + rs.getString("partName") + " | " + rs.getString("totalTransaction") + " |");
    
            }
    
        } catch (Exception e) { 
    
            System.out.println(e);
    
        }
    
    }
    
}
