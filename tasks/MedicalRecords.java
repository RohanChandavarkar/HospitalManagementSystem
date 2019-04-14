package tasks;
import java.sql.*;
import java.util.*;

public class MedicalRecords{
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	String str;

	public MedicalRecords(Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void menu(){
		while (True){
			System.out.println("Updating Medical Records\n");
			System.out.println("Enter patient ID:");
			String pid = reader.next();
			str = "SELECT MAX(mId) from hasRecord where pId = "+pid+";";
			int v = getVariable(str);
			if(v == null){
				System.out.println("Could not retrieve some information");
				break;
			}

			str = "SELECT enddate from MedicalRecord where mId= " + v + ";";
			try{
				stmt = conn.prepareStatement(str);
				rs = stmt.executeQuery();
				if (!rs.wasNull()){
					System.out.println("Patient checked out. Update not possible");
					break;
				}
				else{
					System.out.println("\t1. Update Treatment in MedicalRecord\n\t2. Update Tests in MedicalRecord\n\t3. Update Drugs in MedicalRecord\n\t4. Exit");
					Scanner reader = new Scanner(System.in);
					int input = reader.nextInt();

					switch(input){
						case 1:
							System.out.println("Select the Treatment plan value");
							int tpt = reader.next();

							str = "Update MedicalRecord set proTreatPlan=" + tpt + "where mId=" + v+";";
							executeUpdate(str);
							break;

						case 2: 
							System.out.println("Enter Test Id to update");
							int tpt = reader.next();
							str = "insert into prescribesTests VALUES("+v+","+tpt+");";

							executeInsert(str);
							break;

						case 3:
							System.out.println("Enter Test Id to update");
							int tpt = reader.next();
							str = "insert into prescribesDrugs VALUES("+v+","+tpt+");";

							executeInsert(str);
							break;

						case 4:
							return;

						default:
							System.out.println("Please select a valid option");
							break;
							 
					}
				}
		    } catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed! Retry.");
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

	public int getVariable(String str){
		try {
			int x;
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
	}

}


