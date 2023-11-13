import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.naming.ldap.StartTlsResponse;
import java.util.Date; 
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Main {
    public static Scanner input = new Scanner(System.in);
    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db40?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group40";
    public static String dbPassword = "CSCI3170";

    public static Connection connectToMySQL(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e){
            System.out.println(e);
        }
        return con;
    }
    public static void Menu() {
        //Scanner input = new Scanner(System.in);
        do{
            System.out.println("-----Main menu-----");
            System.out.println("What kind of operation would like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operations for salesperson");
            System.out.println("3. Operations for manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter your choice: ");
            
            int option = input.nextInt();
            
            switch(option) {
                case 1:
                    Administrator();
                    break;
                case 2:
                    Salesperson();
                    break;
                case 3:
                    Manager();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("[ERROR] Invalid Input");
            }
        } while(true);
    }

    public static void Administrator(){
        do{
            System.out.println("-----What kinds of operation would like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to the main menu");
            System.out.println("Enter your choice: ");

            int option=input.nextInt();
            switch (option){
                case 1:
                    String categorySql = "create table if not exists category ("
                                + "cid integer, "
                                + "cName char(20)"
                                + ");";
                    String manufacturerSql = "create table if not exists manufacturer ("
                                + "mid integer, "
                                + "mName char(20), "
                                + "mAddress char(50), "
                                + "mPhoneNumber integer"
                                + ");";
                    String partSql = "create table if not exists part ("
                                + "pid integer, "
                                + "pName char(20), "
                                + "pPrice integer, "
                                + "mid integer, "
                                + "cid integer, "
                                + "pWarrantyPeriod integer, "
                                + "pAvailableQuantity integer"
                                + ");";
                    String salespersonSql = "create table if not exists salesperson ("
                                + "sid integer, "
                                + "sName char(20), "
                                + "sAddress char(50), "
                                + "sPhoneNumber integer,"
                                + "sExperience integer"
                                + ");";
                    String transactionSql = "create table if not exists transaction ("
                                + "tid integer, "
                                + "pid integer, "
                                + "sid integer, "
                                + "tDate datetime"
                                + ");";
                    try {
                        System.out.print("Processing...");
                        Connection mysql = connectToMySQL();
                        Statement sql = mysql.createStatement();
                        sql.executeUpdate(categorySql);
                        sql.executeUpdate(manufacturerSql);
                        sql.executeUpdate(partSql);
                        sql.executeUpdate(salespersonSql);
                        sql.executeUpdate(transactionSql);
                        System.out.println("Done! Tables are created");
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 2:
                    String disableFK  = "set foreign_key_checks = 0;";
                    String dropCategoty = "drop table if exists category";
                    String dropManufacturer = "drop table if exists manufacturer";
                    String dropPart = "drop table if exists part";
                    String dropSalesperson = "drop table if exists salesperson";
                    String dropTransaction = "drop table if exists transaction";
                    
                    String EnableFK = "set foreign_key_checks = 1";
                    try {
                        System.out.print("Processing...");
                        Connection mysql = connectToMySQL();
                        Statement sql = mysql.createStatement();
                        sql.executeUpdate(disableFK);
                        sql.executeUpdate(dropCategoty);
                        sql.executeUpdate(dropManufacturer);
                        sql.executeUpdate(dropPart);
                        sql.executeUpdate(dropSalesperson);
                        sql.executeUpdate(dropTransaction);
                        sql.executeUpdate(EnableFK);
                        System.out.print("Done! Tables are deleted!\n");
                    } catch(Exception e) {
                        System.out.print(e);
                    }
                    break;
                case 3:
                    String[][] categoryInfo = new String [10000][2];
                    String[][] manufacturerInfo = new String [10000][4];
                    String[][] partInfo = new String [10000][7];
                    String[][] salespersonInfo = new String [10000][5];
                    String[][] transactionInfo = new String [10000][4];
                    
                    System.out.println("Please enter the folder path");
                    // updated
                    String path = input.next();
                    input.nextLine();
                    System.out.print("Processing...");

                    try {
                            File file = new File(path + "/category.txt");
                            BufferedReader br = new BufferedReader(new FileReader(file)); 
                            String st;
                            int count = 0;
                            while ((st = br.readLine()) != null) {
                                categoryInfo[count] = st.split("\t");
                                count++;
                            }
                            br.close();
                        } catch(Exception e) {
                            System.out.print(e);
                        }
                        
                    try {
                        File file = new File(path + "/manufacturer.txt");
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st;
                        int count = 0;
                        while ((st = br.readLine()) != null) {
                            manufacturerInfo[count] = st.split("\t");
                            count++;
                        }
                        br.close();
                    } catch(Exception e) {
                        System.out.print(e);
                    }
                                    
                    try {
                        File file = new File(path + "/part.txt");
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st;
                        int count = 0;
                        while ((st = br.readLine()) != null) {
                            partInfo[count] = st.split("\t");
                            count++;
                        }
                        br.close();
                    } catch(Exception e) {
                        System.out.print(e);
                    }
                    
                    try {
                        File file = new File(path + "/salesperson.txt");
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st;
                        int count = 0;
                        while ((st = br.readLine()) != null) {
                            salespersonInfo[count] = st.split("\t");
                            count++;
                        }
                        br.close();
                    } catch(Exception e) {
                        System.out.print(e);
                    }
                
                    try {
                        File file = new File(path + "/transaction.txt");
                        BufferedReader br = new BufferedReader(new FileReader(file)); 
                        String st;
                        int count = 0;
                        while ((st = br.readLine()) != null) {
                            transactionInfo[count] = st.split("\t");
                            count++;
                        }
                        br.close();
                    } catch(Exception e) {
                        System.out.print(e);
                    }

                    String categoryInsert = "insert into category values(?, ?)";
                    String manufacturerInsert = "insert into manufacturer values(?, ?, ?, ?)";
                    String partInsert = "insert into part values(?, ?, ?, ?, ?, ?, ?)";
                    String salespersonInsert = "insert into salesperson values(?, ?, ?, ?, ?)";
                    String transactionInsert = "insert into transaction values(?, ?, ?, ?)";
                    
                    try{
                        Connection mysql = connectToMySQL();
                        Statement sql = mysql.createStatement();
                        PreparedStatement categoryPS = mysql.prepareStatement(categoryInsert);
                        PreparedStatement manufacturerPS = mysql.prepareStatement(manufacturerInsert);
                        PreparedStatement partPS = mysql.prepareStatement(partInsert);
                        PreparedStatement salespersonPS = mysql.prepareStatement(salespersonInsert);
                        PreparedStatement transactionPS = mysql.prepareStatement(transactionInsert);
                        for(int i = 0; categoryInfo[i] != null; i++) {
                            categoryPS.setInt(1, Integer.parseInt(categoryInfo[i][0]));
                            categoryPS.setString(2, categoryInfo[i][1]);
                            categoryPS.executeUpdate();
                        }
                        for(int i = 0; manufacturerInfo[i][0] != null; i++) {
                            manufacturerPS.setInt(1, Integer.parseInt(manufacturerInfo[i][0]));
                            manufacturerPS.setString(2, manufacturerInfo[i][1]);
                            manufacturerPS.setString(3, manufacturerInfo[i][2]);
                            manufacturerPS.setInt(4,Integer.parseInt(manufacturerInfo[i][3]));
                            manufacturerPS.executeUpdate();
                        }
                        for(int i = 0; partInfo[i][0] != null; i++) {
                            partPS.setInt(1, Integer.parseInt(partInfo[i][0]));
                            partPS.setString(2, partInfo[i][1]);
                            partPS.setString(3, partInfo[i][2]);
                            partPS.setInt(4,Integer.parseInt(partInfo[i][3]));
                            partPS.setInt(5,Integer.parseInt(partInfo[i][4]));
                            partPS.setInt(6,Integer.parseInt(partInfo[i][5]));
                            partPS.setInt(7,Integer.parseInt(partInfo[i][6]));
                            partPS.executeUpdate();
                        }
                        for(int i = 0; salespersonInfo[i][0] != null; i++) {
                            salespersonPS.setInt(1, Integer.parseInt(salespersonInfo[i][0]));
                            salespersonPS.setString(2, salespersonInfo[i][1]);
                            salespersonPS.setString(3, partInfo[i][2]);
                            salespersonPS.setInt(4,Integer.parseInt(partInfo[i][3]));
                            salespersonPS.setInt(5,Integer.parseInt(partInfo[i][4]));
                            salespersonPS.executeUpdate();
                        }
                        for(int i = 0; transactionInfo[i][0] != null; i++) {
                            SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
                            java.util.Date date = formatter.parse(transactionInfo[i][3]);
                            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                            transactionPS.setInt(1, Integer.parseInt(transactionInfo[i][0]));
                            transactionPS.setInt(2, Integer.parseInt(salespersonInfo[i][1]));
                            transactionPS.setInt(3, Integer.parseInt(transactionInfo[i][2]));
                            salespersonPS.setDate(4,sqlDate);
                            salespersonPS.executeUpdate();
                        }
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 4:

                case 5:
                    return;
                default:
                    System.out.println("[ERROR] Invalid Input");
            }
        } while(true);
    }

    public static void Salesperson(){

    }

    public static void Manager(){

    }


    public static void main(String[] args) {
         Menu();
         return;
    }
}
