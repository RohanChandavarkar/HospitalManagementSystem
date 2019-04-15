package tasks;
import java.sql.*;
import java.util.*;

/***
 * Task 3 : Maintaining billing accounts. Generate/maintain
 * billing accounts for every visit of every patient. Before 
 * generating an account, make sure there is space in the 
 * hospital for the patient (in case the patient needs to 
 * stay in the hospital).
 */
public class Billing {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	String str, str1;
	boolean THATFLAG;
	static Scanner reader = new Scanner(System.in);

	public Billing (Connection conn) {
		try {
			this.conn = conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void menu() {
		System.out.println("\n\t Billing Reports\t\n");
		while(true){
			System.out.println("\n\nSelect the required alternative\n"
					+ "1. Enter information of the new payer Information \n"
					+ "2. Display all charges for a patient  \n"
					+ "3. Display itemized billing information for a patient\n"
					+ "4. Update a payer's information\n"
					+ "5. Delete a payer's information\n"
					+ "6. Exit\n"
					);
			int input = reader.nextInt();
			switch (input) {
				case 1:	{
					System.out.println("Enter the payer SSN: ");
					String ssn = reader.next();
					System.out.println("Enter billing address: ");
					String address = reader.next();
					System.out.println("Enter the payment method: \n\t1. Account\n\t2. Check \n\t3. Card");
					int method = reader.nextInt();
					switch(method){
						case 1:{
							System.out.println("Enter the account number: ");
							String account_number = reader.next();
							System.out.println("Enter the routing number: ");
							String routing_number = reader.next();
							str = "INSERT into BillingAccount VALUES ( '"
							+ ssn + "', 'Account', " + account_number + ", "
							+ routing_number + ", NULL, NULL, NULL, '" + address + "');";
							break;
						}
						case 2:{
							System.out.println("Enter the account number: ");
							String account_number = reader.next();
							str = "INSERT into BillingAccount VALUES ( '"
							+ ssn + "', 'Check', " + account_number + ", NULL, NULL, NULL, NULL, '"
							+ address + "');";
							break;
						}
						case 3:{
							System.out.println("Enter the card number: ");
							String card_number = reader.next();
							System.out.println("Enter the expiry month (MM): ");
							String expiry_month = reader.next();
							System.out.println("Enter expiry year (YYYY): ");
							String expiry_year = reader.next();
							str = "INSERT into BillingAccount VALUES ( '"
							+ ssn + "', 'Card', NULL, NULL, " + card_number + ", " + expiry_month
							+ ", " + expiry_year + ", '" + address + "');";
							break;
						}
						default: System.out.println("Please select a valid option");
							str = "INSERT into BillingAccount VALUES ( '"
							+ ssn + "', NULL, NULL, NULL, NULL, NULL, NULL,'" + address + "');";				;
					break;
					}
					try {
						conn.setAutoCommit(false);
						executeInsert(str);
						if(THATFLAG == true){

							conn.commit();

							System.out.print("Enter the Patient ID they are paying for: ");
							String toPay_pid = reader.next();
							str = "INSERT IGNORE into paidBy(payerSSN, pId) values ( '"+ ssn+"', "+toPay_pid +")";
							executeInsert(str);
								if(THATFLAG == true){
									conn.commit();
								}
								else{
									conn.rollback();
								}
							}
						else{
							conn.rollback();
						}
						conn.setAutoCommit(true);
					} catch(SQLException e) {
						if (conn != null) {
							try {
								conn.rollback(); // rollback in case of error
								conn.setAutoCommit(true);
								// Auto committing it
							} catch (SQLException e1) {

								e.printStackTrace();
								System.out.println("Failed! Retry.");
							}
						}
					}
					break;
				}
				case 2:{
					System.out.println("Enter the ID of patient to display all charges for : ");
					String patient_id = reader.next();
					str = "Select max(mId) from hasRecord where pId = "+patient_id+";";
					int required_MID = getVariable(str);

					//str = "SELECT MedicalRecord.endDate FROM MedicalRecord where MID = "+required_MID+";";

					str = "SELECT compTreatment FROM MedicalRecord where MID = "+required_MID+";";
					String compTreatment = getStrVariable(str);

					if (compTreatment == "N") {
						// handle NULL field value
						System.out.println("The patient is not yet checked out and so does not have any accomodation fee");
						str1 = "SELECT "
						+ "sum(Tests.Tcost) AS 'Test Fees', Staff.ConsultationFee AS 'Consultation Fees',  sum("
						+ "Drugs.drugCost) AS 'Drug Fees', MedicalRecord.regFee AS 'Registration Fee' "
						+ "FROM ( MedicalRecord "
						+ "LEFT JOIN prescribesTests ON MedicalRecord.mId = prescribesTests.mId "
						+ "LEFT JOIN prescribesDrugs ON MedicalRecord.mId = prescribesDrugs.mId "
						+ "LEFT JOIN assigns ON MedicalRecord.mId = assigns.mId "
						+ "LEFT JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId "
						+ "LEFT JOIN Tests ON prescribesTests.tId = Tests.tId "
						+ "LEFT JOIN consults ON MedicalRecord.mId = consults.mId "
						+ "LEFT JOIN Staff ON consults.sId = Staff.sID ) "
						+ "WHERE MedicalRecord.mId = "+required_MID+";";
					}
					else{
						str1 = "SELECT "
						+ "Ward.wCost*DATEDIFF(MedicalRecord.enddate,MedicalRecord.startdate) AS 'Accomodation Fees', "
						+ "sum(Tests.Tcost) AS 'Test Fees', Staff.ConsultationFee AS 'Consultation Fees', "
						+ "sum(Drugs.drugCost) AS 'Drug Fees', MedicalRecord.regFee AS 'Registration Fee' "
						+ "FROM ( MedicalRecord "
						+ "LEFT JOIN prescribesTests ON MedicalRecord.mId = prescribesTests.mId "
						+ "LEFT JOIN prescribesDrugs ON MedicalRecord.mId = prescribesDrugs.mId "
						+ "LEFT JOIN assigns ON MedicalRecord.mId = assigns.mId "
						+ "LEFT JOIN Ward ON assigns.wNumber = Ward.wNumber "
						+ "LEFT JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId "
						+ "LEFT JOIN Tests ON prescribesTests.tId = Tests.tId "
						+ "LEFT JOIN consults ON MedicalRecord.mId = consults.mId "
						+ "LEFT JOIN Staff ON consults.sId = Staff.sID ) "
						+ "WHERE MedicalRecord.mId = " + required_MID + ";";

					}

					executeTheQuery(str1);
					break;
				}

				case 3: {
					System.out.println("Enter the ID of patient to generate itemized bills for: ");
					String patient_id = reader.next();
					str = "Select MAX(mId) from hasRecord where PID = "+patient_id +";";
					int required_MID = getVariable(str);

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
					System.out.println("Drugs bill:");
					str = "SELECT Drugs.DrugID, Drugs.DrugName, Drugs.DrugCost "
					+ "FROM ( MedicalRecord "
					+ "INNER JOIN prescribesDrugs ON MedicalRecord.mId = prescribesDrugs.mId "
					+ "INNER JOIN assigns ON MedicalRecord.mId = assigns.mId "
					+ "INNER JOIN Drugs ON prescribesDrugs.drugId = Drugs.drugId ) "
					+ "WHERE MedicalRecord.mId = "+required_MID+";";
					executeTheQuery(str);

					//registration charge
					System.out.println("Registration fees information :");
					str =  "SELECT  regFee AS 'Registration Fee' "
					+ "FROM MedicalRecord "
					+ "WHERE mId = "+ required_MID + ";";
					executeTheQuery(str);

					//doctor consulation fee
					System.out.println("Doctor's Consultation fee for the patient :");
					str = "SELECT Staff.ConsultationFee AS 'Consultation Fees' "
					+ "FROM ( MedicalRecord "
					+ "INNER JOIN consults ON MedicalRecord.mId = consults.mId "
					+ "INNER JOIN Staff ON consults.sId = Staff.sId ) "
					+ "WHERE MedicalRecord.mId = "+required_MID + ";";
					executeTheQuery(str);

					//ward charge if checked out -> end date is not NULL
					System.out.println("Accomodation fees for the patient (if checked out) : ");
					str = "SELECT "
					+ "Ward.wCost*DATEDIFF(MedicalRecord.enddate,MedicalRecord.startdate) AS 'Accomodation Fees' "
					+ "FROM ( MedicalRecord INNER JOIN assigns ON MedicalRecord.mId = assigns.mId "
					+ "INNER JOIN Ward ON assigns.wNumber = Ward.wNumber )"
					+ "WHERE MedicalRecord.mId = "+required_MID+" AND MedicalRecord.enddate IS NOT NULL;";
					executeTheQuery(str);
					break;
				}

				case 4:	{
					System.out.println("Enter the payer SSN to be updated: ");
					String ssn = reader.next();
					System.out.println("Enter billing address: ");
					String address = reader.next();
					System.out.println("Enter the payment method: \n\t1. Account\n\t2. Check \n\t3. Card");
					int method = reader.nextInt();
					switch(method){
						case 1:{
							System.out.println("Enter the account number: ");
							String account_number = reader.next();
							System.out.println("Enter the routing number: ");
							String routing_number = reader.next();
							str = "UPDATE BillingAccount SET paymentMethod = 'Account', accountNumber = "+account_number+", routingNumber = "+routing_number+", cardNumber = NULL, expiryMonth = NULL, expiryYear = NULL, address = '"+address+"' where payerSSN = '"+ssn+"';";
							break;
						}
						case 2:{
							System.out.println("Enter the account number: ");
							String account_number = reader.next();
							str = "UPDATE BillingAccount SET paymentMethod = 'Check', accountNumber = "+account_number+", routingNumber = NULL , cardNumber = NULL, expiryMonth = NULL, expiryYear = NULL, address = '"+address+"' where payerSSN = '"+ssn+"';";
							break;
						}
						case 3:{
							System.out.println("Enter the card number: ");
							String card_number = reader.next();
							System.out.println("Enter the expiry month (MM): ");
							String expiry_month = reader.next();
							System.out.println("Enter expiry year (YYYY): ");
							String expiry_year = reader.next();
							str = "UPDATE BillingAccount SET paymentMethod = 'Account', accountNumber = NULL, routingNumber = NULL, cardNumber = "+ card_number +" , expiryMonth = " + expiry_month +", expiryYear = " + expiry_year +", address = '"+address+"' where payerSSN = '"+ssn+"';";
							break;
						}
						default: System.out.println("Please select a valid option");
							str = "UPDATE BillingAccount SET paymentMethod = 'Unknown', accountNumber = NULL, routingNumber = "
							+ "NULL, cardNumber = NULL, expiryMonth = "
							+ "NULL, expiryYear = NULL"
							+ ", address = '"+address+"' where payerSSN = '"+ssn+"';";
									;
					break;
					}

					executeUpdate(str);
					break;
				}

				case 5:{
					System.out.println("Enter the Payer's SSN to be deleted : ");
					String ssn = reader.next();
					str = "DELETE from BillingAccount where payerSSN = '"+ssn+"';";
					executeDelete(str);
					break;
				}

				case 6: return;

				default:
					System.out.println("Please select a valid option");
					break;

				}

			}
		}

	/***
     * Prints SELECT output
     */	
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
     * This method executes SELECT Statements
     * @param str SELECt statement
     */
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
		THATFLAG = false;
		try {
			stmt = conn.prepareStatement(str);
			int out = stmt.executeUpdate();
			if (out == 1){
				System.out.println("Inserted Successfully");
				THATFLAG = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
}

	public void executeDelete (String str){
		try {
			stmt = conn.prepareStatement(str);
			int out = stmt.executeUpdate();
			if (out == 1)
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
			if (out == 1)
				System.out.println("Updated Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
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
