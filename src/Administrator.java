
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Administrator{
        public static Scanner input = new Scanner(System.in);
        public static void Administrator_operation(){
        do{
            System.out.println("What kinds of operation would like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to the main menu");
            System.out.println("Enter your choice: ");

            int option = input.nextInt();
            switch (option){
                case 1:
                    String categorySql = "create table if not exists category ("
                                + "cid integer NOT NULL, "
                                + "cName char(20), "
                                + "CONSTRAINT categoryKey PRIMARY KEY (cid)"
                                + ");";
                    String manufacturerSql = "create table if not exists manufacturer ("
                                + "mid integer NOT NULL, "
                                + "mName char(20), "
                                + "mAddress char(50), "
                                + "mPhoneNumber integer, "
                                + "CONSTRAINT manufacturerKey PRIMARY KEY (mid)"
                                + ");";
                    String partSql = "create table if not exists part ("
                                + "pid integer NOT NULL, "
                                + "pName char(20), "
                                + "pPrice integer, "
                                + "mid integer NOT NULL, "
                                + "cid integer NOT NULL, "
                                + "pWarrantyPeriod integer, "
                                + "pAvailableQuantity integer, "
                                + "CONSTRAINT partKey PRIMARY KEY (pid), "
                                + "FOREIGN KEY (mid) REFERENCES manufacturer(mid), "
                                + "FOREIGN KEY (cid) REFERENCES category(cid)"
                                + ");";
                    String salespersonSql = "create table if not exists salesperson ("
                                + "sid integer NOT NULL, "
                                + "sName char(20), "
                                + "sAddress char(50), "
                                + "sPhoneNumber integer, "
                                + "sExperience integer, "
                                + "CONSTRAINT salespersonKey PRIMARY KEY (sid)"
                                + ");";
                    String transactionSql = "create table if not exists transaction ("
                                + "tid integer NOT NULL, "
                                + "pid integer NOT NULL, "
                                + "sid integer NOT NULL, "
                                + "tDate date, "
                                + "CONSTRAINT transactionKey PRIMARY KEY (tid), "
                                + "FOREIGN KEY (pid) REFERENCES part(pid), "
                                + "FoREIGN KEY (sid) REFERENCES salesperson(sid)"
                                + ");";
                    try {
                        System.out.print("Processing...\n");
                        Connection mysql = Main.connectToMySQL();
                        Statement sql = mysql.createStatement();
                        sql.executeUpdate("set foreign_key_checks = 0;");
                        sql.executeUpdate(categorySql);
                        sql.executeUpdate(manufacturerSql);
                        sql.executeUpdate(partSql);
                        sql.executeUpdate(salespersonSql);
                        sql.executeUpdate(transactionSql);
                        sql.executeUpdate("set foreign_key_checks = 1;");
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
                        System.out.print("Processing...\n");
                        Connection mysql = Main.connectToMySQL();
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
                    System.out.print("Processing...\n");

                    try {
                            File file = new File( "./" + path + "/category.txt");
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
                        Connection mysql = Main.connectToMySQL();
                        Statement sql = mysql.createStatement();
                        PreparedStatement categoryPS = mysql.prepareStatement(categoryInsert);
                        PreparedStatement manufacturerPS = mysql.prepareStatement(manufacturerInsert);
                        PreparedStatement partPS = mysql.prepareStatement(partInsert);
                        PreparedStatement salespersonPS = mysql.prepareStatement(salespersonInsert);
                        PreparedStatement transactionPS = mysql.prepareStatement(transactionInsert);
                        for(int i = 0; categoryInfo[i][0] != null; i++) {
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
                            salespersonPS.setString(3, salespersonInfo[i][2]);
                            salespersonPS.setInt(4,Integer.parseInt(salespersonInfo[i][3]));
                            //salespersonPS.setString(4, salespersonInfo[i][3]);
                            salespersonPS.setInt(5,Integer.parseInt(salespersonInfo[i][4]));
                            salespersonPS.executeUpdate();
                        }
                        for(int i = 0; transactionInfo[i][0] != null; i++) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            java.util.Date date = formatter.parse(transactionInfo[i][3]);
                            //System.out.println(date);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            //System.out.println(date);
                            java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());   
                            transactionPS.setInt(1, Integer.parseInt(transactionInfo[i][0]));
                            transactionPS.setInt(2, Integer.parseInt(transactionInfo[i][1]));
                            transactionPS.setInt(3, Integer.parseInt(transactionInfo[i][2]));
                            transactionPS.setDate(4,sqlDate);
                            transactionPS.executeUpdate();
                        }
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 4:
                    System.out.println("Which table would you like to show: ");
                    String opt1 = input.nextLine();//eliminate \n
                    //System.out.print("opt1:"+opt1);
                    String opt = input.nextLine();
                    //System.out.print("opt:"+opt);
                    String categoryDisplay = "select * from category;";
                    String manufacturerDisplay = "select * from manufacturer;";
                    String partDisplay = "select * from part;";
                    String salespersonDisplay = "select * from salesperson;";
                    String transactionDisplay = "select * from transaction;";
                    try {
                        Connection mysql = Main.connectToMySQL();
                        Statement sql = mysql.createStatement();
                        if(opt.equals("category")){
                            ResultSet categoryRS = sql.executeQuery(categoryDisplay);
                            // Get the metadata of the ResultSet
                            ResultSetMetaData rsmd = categoryRS.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            System.out.print("| ");
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(" | ");
                                System.out.print(rsmd.getColumnName(i));
                            }
                            System.out.println(" |");
                            // Iterate through the ResultSet, row by row
                            while (categoryRS.next()) {
                                // Iterate through each column of the row
                                System.out.print("| ");
                                for (int i = 1; i <= columnsNumber; i++) {
                                    if (i > 1) System.out.print(" | ");
                                    String columnValue = categoryRS.getString(i);
                                    System.out.print(columnValue);
                                }
                                System.out.println(" |");
                            }
                        }
                        else if(opt.equals("manufacturer")){
                            ResultSet manufacturerRS = sql.executeQuery(manufacturerDisplay);
                            // Get the metadata of the ResultSet
                            ResultSetMetaData rsmd = manufacturerRS.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            System.out.print("| ");
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(" | ");
                                System.out.print(rsmd.getColumnName(i));
                            }
                            System.out.println(" |");
                            // Iterate through the ResultSet, row by row
                            while (manufacturerRS.next()) {
                                // Iterate through each column of the row
                                System.out.print("| ");
                                for (int i = 1; i <= columnsNumber; i++) {
                                    if (i > 1) System.out.print(" | ");
                                    String columnValue = manufacturerRS.getString(i);
                                    System.out.print(columnValue);
                                }
                                System.out.println(" |");
                            }
                        }
                        else if(opt.equals("part")){
                            ResultSet partRS = sql.executeQuery(partDisplay);
                            // Get the metadata of the ResultSet
                            ResultSetMetaData rsmd = partRS.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            System.out.print("| ");
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(" | ");
                                System.out.print(rsmd.getColumnName(i));
                            }
                            System.out.println(" |");
                            // Iterate through the ResultSet, row by row
                            while (partRS.next()) {
                                // Iterate through each column of the row
                                System.out.print("| ");
                                for (int i = 1; i <= columnsNumber; i++) {
                                    if (i > 1) System.out.print(" | ");
                                    String columnValue = partRS.getString(i);
                                    System.out.print(columnValue);
                                }
                                System.out.println(" |");
                            }
                        }
                        else if(opt.equals("salesperson")){
                            ResultSet salespersonRS = sql.executeQuery(salespersonDisplay);
                            // Get the metadata of the ResultSet
                            ResultSetMetaData rsmd = salespersonRS.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            System.out.print("| ");
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(" | ");
                                System.out.print(rsmd.getColumnName(i));
                            }
                            System.out.println(" |");
                            // Iterate through the ResultSet, row by row
                            while (salespersonRS.next()) {
                                // Iterate through each column of the row
                                System.out.print("| ");
                                for (int i = 1; i <= columnsNumber; i++) {
                                    if (i > 1) System.out.print(" | ");
                                    String columnValue = salespersonRS.getString(i);
                                    System.out.print(columnValue);
                                }
                                System.out.println(" |");
                            }
                        }
                        else if(opt.equals("transaction")){
                            ResultSet transactionRS = sql.executeQuery(transactionDisplay);
                            // Get the metadata of the ResultSet
                            ResultSetMetaData rsmd = transactionRS.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            System.out.print("| ");
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(" | ");
                                System.out.print(rsmd.getColumnName(i));
                            }
                            System.out.println(" |");
                            // Iterate through the ResultSet, row by row
                            while (transactionRS.next()) {
                                // Iterate through each column of the row
                                System.out.print("| ");
                                for (int i = 1; i <= columnsNumber; i++) {
                                    if (i > 1) System.out.print(" | ");
                                    String columnValue = transactionRS.getString(i);
                                    System.out.print(columnValue);
                                }
                                System.out.println(" |");
                            }
                        }
                        else{
                            System.out.println("[ERROR] Invalid Input");
                        }
                    } catch(Exception e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("[ERROR] Invalid Input");
            }
        } while(true);
    }
}