package tasks;
import java.sql.*;
import java.util.*;

public class Billing {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, str1,str2,str3;
	static String method,date,address;
	static Integer ssn, account_number, card_number,expiry_month,expiry_year,routing_number; 
	static Scanner reader = new Scanner(System.in);

	public Billing (Connection conn) {
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

	public void menu() {
		System.out.println("\n\t Billing Reports\t\n");
		while(true){
			System.out.println("\n\nSelect the required alternative\n"
					+ "1. Enter information of the new payer Information \n" 
					+ "2. Display all charges for a patient  \n" 
					+ "3. Display itemized billing information for a patient\n"
					+ "4. Update a payer's information\n" 
					+ "5. Delete a payer's information"
					+ "6. Exit\n" 
					);
			int input = reader.nextInt();
			switch (input) {
				case 1:			
					System.out.println("Enter the payer SSN: ");
					String ssn = reader.next();	
					System.out.println("Enter the payment method: ");
					String method = reader.next();
					System.out.println("Enter the account number: ");
					String account_number = reader.next();
					System.out.println("Enter the routing number: ");
					String routing_number = reader.next();
					System.out.println("Enter the card number: ");
					String card_number = reader.next();
					System.out.println("Enter the expiry month: ");
					String expiry_month = reader.next();
					System.out.println("Enter expiry year: ");
					String expiry_year = reader.next();
					System.out.println("Enter billing address: ");
					String address = reader.next();
					str = "INSERT into billingAccount VALUES (" + ssn + ", " + method + ", " + account_number + ", " + routing_number + ", " + card_number + ", " + expiry_month + ", " expiry_year + ", " + address + ");";
					executeInsert(str);
					System.out.print("Enter the Patient ID they are paying for: ");
					String toPay_pid = reader.next();
					str = "INSERT IGNORE into paidBy(payerSSN, pId) values ( "+ ssn+","+toPay_pid +")";
					executeInsert(str);
					break;

				case 2:
					System.out.println("Enter the ID of patient to display all charges for : ");
					String patient_id = reader.next();
					str = "Select max(mId) from hasRecords where pId = "+patient_id+";";
					int required_MID = getVariable(str);
					
					str = "SELECT MedicalRecord.endDate FROM MedicalRecord where MID = "+required_MID+";";
					
					int iVal = 0;
					rs = conn.executeQuery(str);
					if (rs.next()) {
    					if (rs.wasNull()) {
							// handle NULL field value
							System.out.println("The patient is not yet checked out and so does not have an")
							str1 = "SELECT " 
							+ "sum(Tests.Tcost) AS 'Test Fees', Doctor.ConsultationFee AS 'Consultation Fees',  sum("
							+ "Drugs.drugCost) AS 'Drug Fees', MedicalRecord.regFee AS 'Registration Fee' "
							+ "FROM ( MedicalRecord " 
							+ "INNER JOIN prescribesTests ON MedicalRecord.mId = prescribesTests.mId "
							+ "INNER JOIN prescribesDrugs ON MedicalRecord.mId = prescribesDrugs.mId "
							+ "INNER JOIN assigns ON MedicalRecord.mId = assigns.mId "
							+ "INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId "
							+ "INNER JOIN Tests ON prescribesTests.tId = Tests.tId "
							+ "INNER JOIN consults ON MedicalRecord.mId = consults.mId "
							+ "INNER JOIN Doctor ON consults.sId = Doctor.sID ) "
							+ "WHERE MedicalRecord.mId = "+required_MID+";";
						}	
						else{
							str1 = "SELECT "
							+ "Ward.wCost*DATEDIFF(MedicalRecord.enddate,MedicalRecord.startdate) AS 'Accomodation Fees', "
							+ "sum(Tests.Tcost) AS 'Test Fees', Doctor.ConsultationFee AS 'Consultation Fees',  sum("
							+ "Drugs.drugCost) AS 'Drug Fees', MedicalRecord.regFee AS 'Registration Fee' "
							+ "FROM ( MedicalRecord "
							+ "INNER JOIN prescribesTests ON MedicalRecord.mId = prescribesTests.mId "
							+ "INNER JOIN prescribesDrugs ON MedicalRecord.mId = prescribesDrugs.mId "
							+ "INNER JOIN assigns ON MedicalRecord.mId = assigns.mId "
							+ "INNER JOIN Ward ON assigns.wNumber = Ward.wNumber "
							+ "INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId "
							+ "INNER JOIN Tests ON prescribesTests.tId = Tests.tId "
							+ "INNER JOIN consults ON MedicalRecord.mId = consults.mId "
							+ "INNER JOIN Doctor ON consults.sId = Doctor.sID ) "
							+ "WHERE MedicalRecord.mId = " + required_MID + ";";

						}
					}
					
					executeTheQuery(str1);
					break;

				case 3:
					System.out.println("Enter the ID of patient to generate itemized bills for: ");
					String patient_id = reader.next();
					str = "Select MAX(mId) from hasRecords where PID = "+patient_id +";";
					String required_MID = getVariable(str);
					
					//tests charge
					System.out.println("Tests bill: ");
					str = "SELECT Tests.TID, Tests.TName,Tests.Tcost AS 'Test Fees' "
					+ "FROM ( MedicalRecord "
					+ "INNER JOIN prescribesTests ON MedicalRecord.mId = prescribesTests.mId "
					+ "INNER JOIN assigns ON MedicalRecord.mId = assigns.mId "
					+ "INNER JOIN Tests ON prescribesTests.tId = Tests.tId ) "
					+ "WHERE MedicalRecord.mId = "+required_MID+";";
					executeTheQuery(str);

					//drugs charge
					System.out.println("Drugs bill:")
					str = "SELECT Drugs.DrugID, Drugs.DrugName, Drugs.DrugCost "
					+ "FROM ( MedicalRecord "
					+ "INNER JOIN prescribesDrugs ON MedicalRecord.mId = prescribesDrugs.mId "
					+ "INNER JOIN assigns ON MedicalRecord.mId = assigns.mId " 
					+ "INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId ) "
					+ "WHERE MedicalRecord.mId = "+required_MID+";";
					executeTheQuery(str);

					//registration charge
					System.out.println("Registration fees information :);
					str =  "SELECT  regFee AS 'Registration Fee' "
					+ "FROM ( MedicalRecord "
					+ "WHERE mId = "+required_MID + ";";
					executeTheQuery(str);

					//doctor consulation fee
					System.out.println("Doctor's Consultation fee for the patient :");
					str = "SELECT Staff.ConsultationFee AS 'Consultation Fees "
					+ "FROM ( MedicalRecord "
					+ "INNER JOIN consults ON MedicalRecord.mId = consults.mId "
					+ "INNER JOIN Doctor ON consults.sId = Staff.sId ) "
					+ "WHERE MedicalRecord.mId = "+required_MID + ";";
					executeTheQuery(str);

					//ward charge if checked out -> end date is not NULL
					System.out.println("Accomodation fees for the patient (if checked out) : ");
					str = "SELECT "
					+ "Ward.wCost*DATEDIFF(CheckInInfo.enddate,CheckInInfo.startdate) AS 'Accomodation Fees' "
					+ "INNER JOIN assigns ON MedicalRecord.mId = assigns.mId "
					+ "INNER JOIN Ward ON assigns.wNumber = Ward.wNumber "
					+ "WHERE MedicalRecord.mId = "+required_MID+" AND CheckInInfo.enddate IS NOT NULL);";
					executeTheQuery(str);
					break;	

				case 4:	
					System.out.println("Enter the payer SSN to be updated: ");
					String ssn = reader.next();	
					System.out.println("Enter the payment method: ");
					String method = reader.next();
					System.out.println("Enter the account number: ");
					String account_number = reader.next();
					System.out.println("Enter the routing number: ");
					String routing_number = reader.next();
					System.out.println("Enter the card number: ");
					String card_number = reader.next();
					System.out.println("Enter the expiry month: ");
					String expiry_month = reader.next();
					System.out.println("Enter expiry year: ");
					String expiry_year = reader.next();
					System.out.println("Enter billing address: ");
					String address = reader.next();

					str = "UPDATE billingAccount SET payerSSN = "+ ssn + ", payMethod = "+ method + ", accountNumber = "+account_number+", routingNumber = "+routing_number+", cardNumber = "+card_number+", expiryMonth = "+expiry_month+", expiryYear = "+expiry_month+", address = "+address+" where payerSSN = "+ssn+";";
					executeUpdate(str);	
					break;

				case 5:
					System.out.println("Enter the Payer's SSN to be deleted : ");
					String ssn = reader.next();
					str = "DELETE from billingAccount where payerSSN = "+ssn+";";
					executeDelete(str);
					break;

				case 6: return;
				
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
			if (action == 1)
				System.out.println("Inserted Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
	}	

	public void executeDelete (String str){
		try {
			stmt = conn.prepareStatement(str);
			int out = stmt.executeUpdate();
			if (action == 1)
				System.out.println("Deleted Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
	}	

	public void executeUpdate (String str){
		try {
			stmt = conn.prepareStatement(str);
			int out = stmt.executeUpdate();
			if (action == 1)
				System.out.println("Updated Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
	}
}
