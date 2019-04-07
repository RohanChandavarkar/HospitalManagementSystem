package tasks;
import java.sql.*;
import java.util.*;

public class Reports {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, str1,str2,str3;
	static String month, year; 
	static Scanner reader = new Scanner(System.in);

	public Reports(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		// closes the connection, statement and result set.
		close(rs);
		close(stmt);
		close(conn);
	}

	public void myMenu() {
		System.out.println("\n\tReports\t\n");
		while(true){
			System.out.println("\n\nEnter the report you want to view:\n"
					+ "1. Medical history for a given Patient for a certain time period\n" 
					+ "2. Current usage status for all wards/ beds\n"
					+ "3. Number of Pateints in a given month in a year \n" 
					+ "4. Ward-usage Percentage\n"
					+ "5. Ward-usage Percentage in each ward\n" 
					+ "6. Number of available beds in each type of room\n" 
					+ "7. Information on all the patients a given doctor is currently responsible for\n"
					+ "8. Information on hospital staff grouped by their role\n"
					+ "9. Exit\n");
			int input = reader.nextInt();
			switch (input) {
				case 1:			
					System.out.println("Enter the Patient ID : ");
					int pid = reader.nextInt();
					System.out.println("Enter the Month (MM) : ");
					month = reader.next();
					System.out.println("Enter the Year (YYYY) : ");
					year = reader.next();		
					str = "Select MR.MID, Diagnosis  From MedicalRecord as MR " 
					+ "Where MR.MID IN ( Select aT.MID  From addedTo as aT " 
					+ "Where aT.CID IN (Select CID from CheckInInfo " 
					+ "where startdate between '"+year+"-"+month+"-01 00:00:00' AND '"+ year +"-"+ month +"-31 23:59:59')) " 
					+ "AND MR.MID IN (Select hr.MID From hasRecord as hr where PID =" + pid + ")";
	
					executeTheQuery(str);
					break;

				case 2:
					str1 = "select SUM(avail) from Ward";
					str2 = "select wNumber, avail from Ward";
					str3 = "select type, sum(avail) from Ward group by type";
			
					System.out.println("\nReturning total number of available beds in the hospital");
					executeTheQuery(str1);
					
					System.out.println("\nReturning total number of available beds in the hospital");
					executeTheQuery(str2);
			
					System.out.println("\nReturning total number of available beds in the hospital");
					executeTheQuery(str3);
					break;

				case 3:
					System.out.println("Enter the Month (MM) : ");
					month = reader.next();
					System.out.println("Enter the Year (YYYY) : ");
					year = reader.next();		
					str = "Select count(*) from CheckInInfo "
					+ "where startdate between '"+year+"-"+month+"-01 00:00:00' AND '"+year+"-"+month+"-31 23:59:59'";

					executeTheQuery(str);
					break;	

				case 4:	
					str = "select (sum(type) - SUM(avail))/sum(type)*100 from Ward";	
	
					executeTheQuery(str);	
					break;

				case 5:
					str = "select wNumber, (type - avail)/type*100 AS 'usage%' from Ward";

					executeTheQuery(str);
					break;

				case 6:
					str = "Select type, (sum(type) - SUM(avail))/sum(type)*100 as 'Usage%' from Ward group by type";

					executeTheQuery(str);
					break;

				case 7:
					System.out.println("Enter the Staff ID");
					int sid = reader.nextInt();
					str = "SELECT hR.pId FROM hasRecord as hR WHERE hR.mId IN (SELECT c.mId FROM consults AS c WHERE sId =" +sid+")  AND hR.mId IN (SELECT aT.mId FROM addedTo AS aT WHERE aT.cId IN (SELECT C.cId FROM CheckInInfo AS C WHERE C.enddate IS NULL))";

					executeTheQuery(str);
					break;

				case 8:
					str1 = "Select jobTitle, count(*) AS 'No. of Staff'  from Staff group by jobTitle";
					str2 = "SELECT * FROM Staff ORDER BY jobTitle";
			
					System.out.println("\nCounts of each of the roles");
					executeTheQuery(str1);
	
					System.out.println("\nStaff ordered by role");
					executeTheQuery(str2);

					break;
		
				case 9: return;
					default:
					System.out.println("Please select a valid option");
					break;

			}
		}
	}

	public void printOutput(ResultSet rs)throws SQLException {

		// Prepare metadata object and get the number of columns.
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int columnsNumber = rsmd.getColumnCount();
		System.out.println();
    		// Print column names (a header).
    		for (int i = 1; i <= columnsNumber; i++) {
        		if (i > 1) System.out.print("\t|\t");
        		System.out.print(rsmd.getColumnName(i));
    		}
		System.out.println("");

    		while (rs.next()) {
        		for (int i = 1; i <= columnsNumber; i++) {
            			if (i > 1) System.out.print("\t|\t");
            			System.out.print(rs.getString(i));
        		}
        		System.out.println("");
    		}
	}

	public void executeTheQuery(String str){
		try {
			stmt = conn.prepareStatement(str);
			rs = stmt.executeQuery();
			printOutput(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
	}

	// closing the connection, statement and result set
	static void close(Connection conn) {
		if (conn != null) {
			try{
				conn.close();
			} catch (Throwable whatever) {
			}
		}
	}

	static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Throwable whatever) {
			}
		}
	}

	static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Throwable whatever) {
			}
		}
	}
}