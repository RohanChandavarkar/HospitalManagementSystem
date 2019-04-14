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
					+ "1. Enter information of the billing account\n" 
					+ "2. Display itemized billing information \n" 
					+ "3. Update information of billing account\n"
					+ "4. Delete information of billing account\n" 
					+ "5. Exit\n" 
					);
			int input = reader.nextInt();
			switch (input) {
				case 1:			
					System.out.println("Enter the payer SSN: ");
					ssn = reader.nextInt();	
					System.out.println("Enter the payment method: ");
					method = reader.next();
					System.out.println("Enter the account number: ");
					account_number = reader.nextInt();
					System.out.println("Enter the routing number: ");
					routing_number = reader.nextInt();
					System.out.println("Enter the card number: ");
					card_number = reader.nextInt();
					System.out.println("Enter the expiry month: ");
					expiry_month = reader.nextInt();
					System.out.println("Enter expiry year: ");
					expiry_year = reader.next();
					System.out.println("Enter billing address: ");
					address = reader.next();
					System.out.print("Enter the patient ID you are paying for: ");
					Integer toPay_pid = reader.nextInt();
					str = "INSERT into billingAccount(payerSSN, payMethod, accountNumber, routingNumber, cardNumber, expiryMonth, expiryYear, address) values (?,?,?,?,?,?,?,?)";
					str2 = "INSERT into paidBy(payerSSN, pId) values ( "+ ssn+","+toPay_pid +")";
					try {
						stmt = conn.prepareStatement(str);
						stmt.setInt(1, ssn);
						stmt.setString(2, method);
						stmt.setInt(3,account_number);
						stmt.setInt(4, routingNumber);
						stmt.setInt(5,cardNumber);
						stmt.setInt(6,expiry_month);
						stmt.setInt(7,expiry_year);
						stmt.setString(8,address);
						action = stmt.executeUpdate();
						executeTheQuery(str2);
						if (action == 1)
							System.out.println("Inserted succesfully");
						action = 0;
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("Insert failed");
					}
					break;
	
					executeTheQuery(str);
					break;

				case 2:
					System.out.println("Enter the ID of patient for the cumulative medical records: ");
					int patient_id = reader.nextInt();
					String required_MID = "";
					stmt = "Select MID from hasRecords where PID = "+patient_id+";";
					stmt = conn.prepareStatement(stmt);
					ResultSet rs = stmt.executeQuery();
					if(rs.next())
						required_MID = rs.getString(1);

					
					s = "SELECT MedicalRecord.endDate FROM MedicalRecord where MID = "+required_MID+";";
					int iVal = 0;
					ResultSet rs = magicallyAppearingStmt.executeQuery(query);
					if (rs.next()) {
    					iVal = rs.getInt("ID_PARENT");
    					if (rs.wasNull()) {
									// handle NULL field value
									stmt = "SELECT
								sum(Tests.Tcost) AS 'Test Fees', Doctor.ConsultationFee AS 'Consultation Fees',  sum(
								Drugs.drugCost) AS 'Drug Fees', MedicalRecord.regFee AS 'Registration Fee'
								FROM ( MedicalRecord 
								-> INNER JOIN prescribesTests ON MedicalRecord.mId =
								prescribesTests.mId
								-> INNER JOIN prescribesDrugs ON MedicalRecord.mId =
								prescribesDrugs.mId
								-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
								-> INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId
								-> INNER JOIN Tests ON prescribesTests.tId = Tests.tId
								-> INNER JOIN consults ON MedicalRecord.mId = consults.mId
								-> INNER JOIN Doctor ON consults.sId = Doctor.sID
									)
								WHERE MedicalRecord.mId = "+required_MID+";";
							}
							else{
								stmt = "SELECT
								Ward.wCost*DATEDIFF(MedicalRecord.enddate,MedicalRecord.startdate) AS 'Accomodation Fees',
								sum(Tests.Tcost) AS 'Test Fees', Doctor.ConsultationFee AS 'Consultation Fees',  sum(
								Drugs.drugCost) AS 'Drug Fees', MedicalRecord.regFee AS 'Registration Fee'
								FROM ( MedicalRecord 
								-> INNER JOIN prescribesTests ON MedicalRecord.mId =
								prescribesTests.mId
								-> INNER JOIN prescribesDrugs ON MedicalRecord.mId =
								prescribesDrugs.mId
								-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
								-> INNER JOIN Ward ON assigns.wNumber = Ward.wNumber
								-> INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId
								-> INNER JOIN Tests ON prescribesTests.tId = Tests.tId
								-> INNER JOIN consults ON MedicalRecord.mId = consults.mId
								-> INNER JOIN Doctor ON consults.sId = Doctor.sID
								)
								WHERE MedicalRecord.mId = "+required_MID+";";

							}
					}
				
					break;

				case 3:
					System.out.println("Enter the ID of patient for generating itemized billing information: ");
					int patient_id = reader.nextInt();
					String required_MID = "";
					stmt = "Select MID from hasRecords where PID = "+patient_id;
					stmt = conn.prepareStatement(stmt);
					ResultSet rs = stmt.executeQuery();
					if(rs.next())
						required_MID = rs.getString(1);
					//tests charge
					System.out.println("Billing Information for Tests: ");
					stmt = "SELECT Tests.TID, Tests.TName,Tests.Tcost AS 'Test Fees'
					FROM ( MedicalRecord 
					-> INNER JOIN prescribesTests ON MedicalRecord.mId =
					prescribesTests.mId
					-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
					-> INNER JOIN Tests ON prescribesTests.tId = Tests.tId
					)
					WHERE MedicalRecord.mId = "+required_MID+";";
					executeTheQuery(stmt);
					//drugs charge
					System.out.println("Billing Information for Drugs:")
					stmt = "SELECT Drugs.DrugID, Drugs.DrugName, Drugs.DrugCost
					FROM ( MedicalRecord
					-> INNER JOIN prescribesDrugs ON MedicalRecord.mId =
					prescribesDrugs.mId
					-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
					-> INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId
					)
					WHERE MedicalRecord.mId = "+required_MID+";";
					executeTheQuery(stmt);

					//registration charge
					System.out.println("Registration fees information ");
					stmt =  "SELECT  MedicalRecord.regFee AS 'Registration Fee'
					FROM ( MedicalRecord
					-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
					-> INNER JOIN consults ON MedicalRecord.mId = consults.mId
					-> INNER JOIN Doctor ON consults.sId = Doctor.sID
					)
					WHERE MedicalRecord.mId = "+required_MID + ";";
					executeTheQuery(stmt);
					//doctor consulation fee
					System.out.println("Doctor's Consultation fee for the patient");
					stmt = "SELECT Doctor.ConsultationFee AS 'Consultation Fees
					FROM ( MedicalRecord 
					-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
					-> INNER JOIN consults ON MedicalRecord.mId = consults.mId
					-> INNER JOIN Doctor ON consults.sId = Doctor.sID
					)
					WHERE MedicalRecord.mId = "+required_MID + ";";
					executeTheQuery(stmt);

					//ward charge if checked out -> end date is not NULL
					System.out.println("Ward fees for the patient");
					stmt = "SELECT
					Ward.wCost*DATEDIFF(CheckInInfo.enddate,CheckInInfo.startdate) AS 'Accomodation Fees'
					-> INNER JOIN assigns ON MedicalRecord.mId = assigns.mId
					-> INNER JOIN Ward ON assigns.wNumber = Ward.wNumber
					-> WHERE MedicalRecord.mId = 1 AND CheckInInfo.enddate IS NOT NULL
					)
					WHERE MedicalRecord.mId = "+required_MID+";";
					executeTheQuery(stmt);

					break;	

				case 4:	
					System.out.println("Enter the payer SSN: ");
					ssn = reader.nextInt();	
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

					stmt = "UPDATE billingAccount SET payerSSN = "+ ssn + ", payMethod = "+ method + ", accountNumber = "+account_number+", routingNumber = "+routing_number+", cardNumber = "+card_number+", expiryMonth = "+expiry_month+", expiryYear = "+expiry_month+", address = "+address+" where payerSSN = "+ssn+";";
					try {
						stmt = conn.prepareStatement(stmt);
						action = stmt.executeUpdate();
						if (action == 1)
							System.out.println("Updated succesfully");
						action = 0;
					} catch (SQLException e) {
						e.printStackTrace();
					}	
					break;

				case 5:
					System.out.println("Enter the Payer's SSN to be deleted");
					Integer ssn = reader.nextInt();
					stmt = "DELETE from billingAccount where payerSSN = "+ssn+";";
					try {
						stmt = conn.prepareStatement(stmt);
						action = stmt.executeUpdate();
						if (action == 1)
							System.out.println("Deleted successfully");
						action = 0;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;

				case 6:
					sSystem.out.println("Exiting the system");
					break;
				default:
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
