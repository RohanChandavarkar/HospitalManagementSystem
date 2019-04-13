import java.sql.*;
import java.util.*;
import tasks.*;

public class FirstPage
{
	static Connection conn = null;
	
	public static void main(String args[]) throws Exception{
		try{
			String jdbc = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/akesari";
			String username = "akesari";
			String password = "dbmsproject";
			conn=DriverManager.getConnection(jdbc,username,password);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		InformationProcessing iP = new InformationProcessing(conn);
		MedicalRecords mR = new MedicalRecords(conn);
		//Billing b = new Billing(conn);
		Reports r= new Reports(conn);

		boolean flag = true;
		Scanner reader = new Scanner(System.in);
			
		while(flag){
			System.out.println("\n\nWhich task do you want to perform?");
			System.out.println("\n1. InformationProcessing\n2. Handle MedicalRecords\n3. Handle Billing Information\n4. Generate Reports\n5. Exit");
			int choice = 0;
			choice = reader.nextInt();
			switch(choice) {
				case 1:
					iP.menu();
					break;
				case 2:
					mR.menu();
					break;
				case 3:
//					b.menu();
					break;
				case 4:
					r.menu();
					break;
				case 5:
					flag = false;
					break;
				default:
					System.out.println("Please select a valid option");
			}
		}		
		iP.close();
		mR.close();
//		b.close();
		r.close();
		reader.close();
	}
}