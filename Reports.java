import java.sql.*;
import java.util.*;

/*This class deals with reports where the following are done
 * 1.report occupancy by city
 * 2.report occupancy by hotel_id
 * 3.report occupancy by room type
 * 4.Rooms occupancy by date range
 * 5.staff grouped by role
 * 6.staff at customer service
 * 7.revenue
 */

public class Reports {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, str1,str2,str3;
	
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

	public void mymenu() {
		System.out.println("Reports\n");
		//list of options for the user to choose in reports
		System.out.println("Enter the report you want to view:\n"
				+ "1. Medical history for a given Patient for a certain time period\n" 
				+ "2. Current usage status for all wards/ beds\n"
				+ "3. Number of Pateints in a given month in a year \n" 
				+ "4. Ward-usage Percentage\n"
				+ "5. Ward-usage Percentage in each ward\n" 
				+ "6. Number of available beds in each type of room\n" 
				+ "7. Information on all the patients a given doctor is currently responsible for\n"
				+ "8. Information on hospital staff grouped by their role");
		input = reader.nextInt();
		switch (input) {
		case 1:			
			// occupancy by city
			System.out.println("Enter the Patient ID : ");
			int pid = reader.nextInt();
			System.out.println("Enter the Month (MM) : ");
			int month = reader.next();
			System.out.println("Enter the Year (YYYY) : ");
			int year = reader.next();		
			str = "Select MR.MID, Diagnosis  From MedicalRecord as MR" 
			+ "Where MR.MID IN ( Select aT.MID  From addedTo as aT" 
			+ "Where aT.CID IN (Select CID from CheckInInfo" 
			+ "where startdate between '"+year+"-"+month+"-01 00:00:00' AND '"+ year +"-"+ month +"-31 23:59:59'))" 
			+ "AND MR.MID IN (Select hr.MID From hasRecord as hr where PID =" + pid + ")";

			try {
				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				printOutput(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
			}
			break;

		case 2:
			str1 = "select SUM(avail) from Ward";
			str2 = "select wNumber, avail from Ward";
			str3 = "select type, sum(avail) from Ward group by type";
			try {
				stmt = conn.prepareStatement(str1);
				rs = stmt.executeQuery();
				System.out.println("\nReturning total number of available beds in the hospital");
				printOutput(rs);
				
				stmt = conn.prepareStatement(str2);
				rs = stmt.executeQuery();
				System.out.println("\nReturning total number of available beds in the hospital");
				printOutput(rs);


				stmt = conn.prepareStatement(str3);
				rs = stmt.executeQuery();
				System.out.println("\nReturning total number of available beds in the hospital");
				printOutput(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
			}
			break;

		case 3:
			// occupancy by room type
			System.out.println("Enter the Month (MM) : ");
			int month = reader.next();
			System.out.println("Enter the Year (YYYY) : ");
			int year = reader.next();		
			str = "Select count(*) from CheckInInfo"
			+ "where startdate between '"+year+"-"+month+"-01 00:00:00' AND '"+year+"-"+month+"-31 23:59:59";

			try {

				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				printOutput(rs);				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
			}
			break;

		case 4:
			// occupancy by date range
			str = "select (sum(type) - SUM(avail))/sum(type)*100 from Ward";

			try {

				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				printOutput(rs);
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
			}
			break;

		case 5:
			// list of staff grouped by their role
			str = "select wNumber, (type - avail)/type*100 AS 'usage%' from Ward";

			try {

				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				printOutput(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
			}
			break;

		case 6:
			str = "Select type, (sum(type) - SUM(avail))/sum(type)*100 as 'Usage%' from Ward group by type";
			try {
				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				printOutput(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
			}
			break;

		case 7:
			// revenue of the particular hotel with in the given date range
			System.out.println("Enter the Staff ID");
			int sid = reader.nextInt();
			str = "SELECT hR.pId FROM hasRecord as hR WHERE hR.mId IN (SELECT c.mId FROM consults AS c WHERE sId =" +sid+")  AND hR.mId IN (SELECT aT.mId FROM addedTo AS aT WHERE aT.cId IN (SELECT C.cId FROM CheckInInfo AS C WHERE C.enddate IS NULL))";

			try {
				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				printOutput(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		case 8:
			// revenue of the particular hotel with in the given date range
			str1 = "Select jobTitle, count(*) AS 'No. of Staff'  from Staff group by jobTitle";
			str2 = "SELECT * FROM Staff ORDER BY jobTitle;"
			try {
				System.out.println("\nCounts of each of the roles");
				stmt = conn.prepareStatement(str1);
				rs = stmt.executeQuery();
				printOutput(rs);


				System.out.println("\nStaff ordered by role");
				stmt = conn.prepareStatement(str1);
				rs = stmt.executeQuery();
				printOutput(rs);

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("something went wrong ,please try again");
			}
			break;

		default:
			System.out.println("choose option from the above");
			break;

		}
	}

	public void printOutput(ResultSet rs, int columnCount){
		ResultSetMetaData rsmd = rs.getMetaData();
   		int columnCount = rsmd.getColumnCount();
		while (rs.next()) {
       	for (int i = 1; i <= columnCount; i++) {
	           if (i > 1) System.out.print(",  ");
	           String columnValue = rs.getString(i);
	           System.out.print(columnValue + " " + rsmd.getColumnName(i));
	       }
	       System.out.println("");
	   }
	}
	// closing the connection, statement and result set
	static void close(Connection conn) {
		if (conn != null) {
			try {while (rs.next()) {
       for (int i = 1; i <= columnsNumber; i++) {
           if (i > 1) System.out.print(",  ");
           String columnValue = rs.getString(i);
           System.out.print(columnValue + " " + rsmd.getColumnName(i));
       }
       System.out.println("");
   }
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