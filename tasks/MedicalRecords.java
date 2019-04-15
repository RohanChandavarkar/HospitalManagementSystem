package tasks;
import java.sql.*;
import java.util.*;

/***
 * Task 2 : update a new medical record 
 * for each treatment, test, and check-in.
 */
public class MedicalRecords{
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	String str;
	static Scanner reader = new Scanner(System.in);

	public MedicalRecords(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void menu(){
		while (true){
			System.out.println("Updating Medical Records\n");
			System.out.println("Enter patient ID:");
			String pid = reader.next();
			str = "SELECT MAX(mId) from hasRecord where pId = "+pid+";";
			int v = getVariable(str);			
					System.out.println("\t1. Update Treatment in MedicalRecord\n\t2. Update Tests in MedicalRecord\n\t3. Update Drugs in MedicalRecord\n\t4. Exit");
					int input = reader.nextInt();

					switch(input){
						case 1:{
							System.out.println("Select the Treatment plan value");
							String tpt = reader.next();

							str = "Update MedicalRecord set proTreatPlan=" + tpt + " where mId=" + v+";";
							executeUpdate(str);
							break;
						}
				case 2: {
							str = "select * from Tests;";
							executeTheQuery(str);
							System.out.println("Enter Test Id to update");
							String tpt = reader.next();
							str = "insert into prescribesTests VALUES("+v+","+tpt+");";

							executeInsert(str);
							break;
						}

						case 3:{
							str = "select * from Drugs;";
							executeTheQuery(str);
							System.out.println("Enter Drugs Id to update");
							String tpt = reader.next();
							str = "insert into prescribesDrugs VALUES("+v+","+tpt+");";

							executeInsert(str);
							break;
						}
						case 4:
							return;

						default:
							System.out.println("Please select a valid option");
							break;
							 
					}
				
		    

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

	public void executeInsert(String str){
	        try {
	            stmt = conn.prepareStatement(str);
	            int out = stmt.executeUpdate();
	            if (out == 1)
	                System.out.println("Inserted Successfully");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Failed! Retry.");
	        }
    }   

    public void executeUpdate (String str){
	        try {
	            stmt = conn.prepareStatement(str);
	            int out = stmt.executeUpdate();
	            if (out == 1)
	                System.out.println("Updated Successfully");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Failed! Retry.");
	        }
    }  
	public Boolean checkNull(String str){
		try{
			int iVal = 0;
			stmt = conn.prepareStatement(str);
			rs = stmt.executeQuery();
			if (rs.next()) 
				if (rs.wasNull()) 
					return true;
				return false;
		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("Failed! Some error occured.");
		}
		return false;
	} 

	public int getVariable(String str){
		int x = 0;
		try {
			stmt = conn.prepareStatement(str);
			rs = stmt.executeQuery();
			while (rs.next()) {
				x = rs.getInt(1);
			}
			return x;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
		return x;
	}
	public String getStrVariable(String str){
		String x = "";
		try {
			stmt = conn.prepareStatement(str);
			rs = stmt.executeQuery();
			while (rs.next()) {
				x = rs.getString(1);
			}
			return x;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
		return x;
	}

	/***
     * closes the connection, statement and result set.
     */
	public void close() {
		close(rs);
		close(stmt);
		close(conn);
	}

	/***
     * closes the connection, statement and result set.
     */
	static void close(Connection conn) {
		if (conn != null) {
			try{
				conn.close();
			} catch (Throwable whatever) {
			}
		}
	}

	/***
     * closes the connection, statement and result set.
     */
	static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Throwable whatever) {
			}
		}
	}

	/***
     * closes the connection, statement and result set.
     */
	static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Throwable whatever) {
			}
		}
	}

}

