
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

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
        
        System.out.println("Welcome to sales system!");
        //Scanner input = new Scanner(System.in);
        do{
            System.out.println("\n-----Main menu-----");
            System.out.println("What kind of operation would like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operations for salesperson");
            System.out.println("3. Operations for manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter your choice: ");
            
            int option = input.nextInt();

            switch(option) {
                case 1:
                    Administrator.Administrator_operation();
                    break;
                case 2:
                    Salesperson.Salesperson_operation();
                    break;
                case 3:
                    Manager.Manager_operation();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("[ERROR] Invalid Input");
            }
        } while(true);
    }


    public static void main(String[] args) {
         Menu();
         return;
    }
}
