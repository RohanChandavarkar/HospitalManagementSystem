package tasks;
import java.sql.*;
import java.util.*;

/***
 * Task 1: Information processing: Enter/update/delete basic
 * information about staff, patients, and wards. Check available
 * wards/beds, assign wards/beds to patients according to their
 * requests (1-bed, 2-bed, and 4-bed ward), and reserve/release 
 * wards/beds.
 */
public class InformationProcessing {
	Connection conn;
	static PreparedStatement stmt;
	static ResultSet rs;
	static String str, str1,str2,str3;
	static String month, year;
	boolean THATFLAG;
	static Scanner reader = new Scanner(System.in);

	public InformationProcessing (Connection conn) {
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
		System.out.println("\n\tInformation Processing\t\n");
		while(true){

			System.out.println("\n\nChoose:\n"
					+ "1. Enter patient Information\n"
					+ "2. Update patient Information\n"
					+ "3. Delete patient Information \n"
					+ "4. Enter staff Information\n"
					+ "5. Update staff Information\n"
					+ "6. Delete staff Information \n"
					+ "7. Enter ward Information\n"
					+ "8. Update ward Information\n"
					+ "9. Delete ward Information \n"
					+ "10. Check In\n"
					+ "11. Check Out\n"
					+ "12. Enter drug Information\n"
					+ "13. Update drug Information\n"
					+ "14. Delete drug Information \n"
					+ "15. Enter test Information\n"
					+ "16. Update test Information\n"
					+ "17. Delete test Information \n"
					+ "18. Enter department Information\n"
					+ "19. Update department Information\n"
					+ "20. Delete department Information \n"
					+ "21. Exit\n");

			int input = reader.nextInt();
			switch (input) {

				case 1:{
					System.out.println("Enter the Patient ID : ");
					String pid = reader.next();
					System.out.println("Enter the Patient Name : ");
					String pname = reader.next();
					System.out.println("Enter the Patient SSN : ");
					String pssn = reader.next();
					System.out.println("DOB (YYYY-MM-DD): ");
					String pdob = reader.next();
					System.out.println("Gender (M/F): ");
					String pgender = reader.next();
					System.out.println("Phone : ");
					String pphone = reader.next();
					System.out.println("Address : ");
					String paddr = reader.nextLine();

					str = "INSERT INTO `Patient`"
					+ "VALUES ( " + pid + " , '"+ pname + "', '"+ pssn +"', '"+pdob +"', '"
					+ pgender + "', "+ pphone +", '" + paddr + "');";

					executeInsert(str);
					break;
				}
				case 2: {
					System.out.println("Enter the Patient ID to update: ");
					String pid = reader.next();
					System.out.println("Enter the Patient Name : ");
					reader.nextLine();
					String pname = reader.nextLine();
					System.out.println("Enter the Patient SSN : ");
					String pssn = reader.next();
					System.out.println("DOB (YYYY-MM-DD): ");
					String pdob = reader.next();
					System.out.println("Gender (M/F): ");
					String pgender = reader.next();
					System.out.println("Phone : ");
					String pphone = reader.next();
					System.out.println("Address : ");
					reader.nextLine();
					String paddr = reader.nextLine();

					str = "UPDATE Patient SET pName = '"  + pname
					+ "', SSN = '" + pssn +"', DOB = '"+pdob +"', pGender = '"  + pgender
					+ "', pPhone = "+ pphone +", pAddress = '" + paddr + "' where pId = " + pid + ";";

					executeUpdate(str);
					break;
				}

				case 3: {
					System.out.println("Enter the Patient ID to delete: ");
					int pid = reader.nextInt();

					str = "DELETE from Patient where pId = " + pid + ";";

					executeDelete(str);
					break;
				}

				case 4: {
                    System.out.println("Enter Staff ID");
                    String sid = reader.next();
                    System.out.println("Enter Staff name");
                    reader.nextLine();
					String sname = reader.nextLine();
					System.out.println("Gender (M/F): ");
                    String pgender = reader.next();
					System.out.println("Enter Professional title (or NULL)");
                    String ptitle = reader.next();
                    System.out.println("Enter Phone Number");
                    String sphone = reader.next();
                    System.out.println("Enter Address");
                    reader.nextLine();
					String saddress = reader.nextLine();

                    System.out.println("Choose the Job Title: \n\t1. Doctor\n\t2. Nurse\n\t3. Billing Operator\n\t4. Front Desk Operator");
                    int jtitle = reader.nextInt();
                    switch(jtitle){
                    	case 1: {
		                	str = "SELECT * FROM Department;";
		                	executeTheQuery(str);
		                  	System.out.println("\nEnter Department ID");
		                    String did = reader.next();
		                    str = "INSERT IGNORE INTO belongsTo VALUES(" + sid +", " +did+");";
		                    executeInsert(str);
		                    System.out.println("Enter consultation fee");
		                    String fee = reader.next();
		                	str = "INSERT INTO `Staff`"
		                    + "VALUES ( " + sid+" , '"+ sname + "', '" + pgender + "', 'Doctor', '"+ptitle+"', "
		                    + sphone + ", '"+ saddress +"', " + fee + ");";
		                	break;
		                }
                    	case 2: {
                    		str = "INSERT INTO Staff "
			                + "VALUES ( " + sid+" , '"+ sname + "', '" + pgender + "', 'Nurse', '"+ptitle+"', "
			                + sphone + ", '"+ saddress +"', NULL);";
	               			break;
	               		}
                    	case 3:{
                    		str = "INSERT INTO Staff "
			                + "VALUES ( " + sid+" , '"+ sname +"', '" + pgender +  "', 'Billing Operator', '"+ptitle+"', "
			                + sphone + ", '"+ saddress +"', NULL);";
	               			break;
	               		}
                    	case 4: {
                    		str = "INSERT INTO Staff "
			                + "VALUES ( " + sid+" , '"+ sname + "', '" + pgender + "', 'Front Desk Operator', '"+ptitle+"', "
			                + sphone + ", '"+ saddress +"', NULL);";
	               			break;
	               		}

                    	default: System.out.println("Please select a valid option");
							break;
                    }
                    executeInsert(str);
                    break;
                }

                case 5:{
                    System.out.println("Enter staff ID");
                    String sid = reader.next();
                    System.out.println("Enter staff name");
                    String sname = reader.next();
					System.out.println("Gender (M/F): ");
                    String pgender = reader.next();
					System.out.println("Enter Professional title");
                    String ptitle = reader.next();
                    System.out.println("Enter phone");
                    String sphone = reader.next();
                    System.out.println("Enter Address");
                    reader.nextLine();
					String saddress = reader.nextLine();

                    System.out.println("Choose the Job Title: \n\t1. Doctor\n\t2. Nurse\n\t3. Billing Operator\n\t4. Front Desk Operator");
                    int jtitle = reader.nextInt();
                    switch(jtitle){
                    	case 1:
                    	System.out.println("Enter consultation fee");
                    	String fee = reader.next();
	                    	str = "UPDATE Staff SET sName = '"  + sname + "', sGender = '" + pgender
	                    	+ "',  jobTitle = 'Doctor', professionalTitle = '"+ptitle +"', pPhone = "  + sphone
	                    	+ ", pAddress = '"+ saddress +"', consultationFee = " + fee +" where sId = " + sid + ";";
                    		break;

                    	case 2:
                    		str = "UPDATE Staff SET sName = '"  + sname + "', sGender = '" + pgender
                    		+ "',  jobTitle = 'Nurse', professionalTitle = '"+ptitle +"', pPhone = "  + sphone
                    		+ ", pAddress = '"+ saddress +"', consultationFee = NULL where sId = " + sid + ";";
                   			break;
                    	case 3:
                    		str = "UPDATE Staff SET sName = '"  + sname + "', sGender = '" + pgender
                    		+ "',  jobTitle = 'Billing Operator', professionalTitle = '"+ptitle +"', pPhone = "  + sphone
                    		+ ", pAddress = '"+ saddress +"', consultationFee = NULL where sId = " + sid + ";";
                   			break;
                    	case 4:
                    		str = "UPDATE Staff SET sName = '"  + sname + "', sGender = '" + pgender
                    		+ "',  jobTitle = 'Front Desk Operator', professionalTitle = '"+ptitle +"', pPhone = "  + sphone
                    		+ ", pAddress = '"+ saddress +"', consultationFee = NULL where sId = " + sid + ";";
                   			break;

                    	default: System.out.println("Please select a valid option");
							break;
                    }

                    executeUpdate(str);
                    break;
                }

                case 6: {
                    System.out.println("Enter the Staff ID to delete: ");
                    int sid = reader.nextInt();

                    str = "DELETE from Staff where sId = " + sid + ";";

                    executeDelete(str);
                    break;
                }

				case 7: {
					System.out.println("Enter the Ward Number : ");
					String wnumber = reader.next();
					System.out.println("Enter the capacity : ");
					String type = reader.next();
					System.out.println("Enter charge per day : ");
					String wcost = reader.next();

					str = "INSERT INTO Ward "
					+ "VALUES ( " + wnumber + ", "+ type + ", "+ type +", "+ wcost +");";
					try {
					  conn.setAutoCommit(false);
					  executeInsert(str);
					  if(THATFLAG == true){

					    conn.commit();

					    str = "SELECT * FROM Staff WHERE jobTitle = 'Nurse'";
					    executeTheQuery(str);
					    System.out.println("Enter the responsible nurse : ");
					    String sid = reader.next();

					    str = "INSERT ignore INTO inChargeOf "
					    + "VALUES ( " + sid + ", "+ wnumber +");";
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

				case 8: {
					System.out.println("Enter the Ward Number to update: ");
					String wnumber = reader.next();
					System.out.println("Enter the capacity : ");
					String type = reader.next();
					System.out.println("Enter charge per day : ");
					String wcost = reader.next();

					str = "UPDATE Ward SET type = "+ type + ", avail = "+ type +", wcost = "+ wcost +" WHERE wNumber = " + wnumber + ";";
					try {
					  conn.setAutoCommit(false);
					  executeUpdate(str);
					  if(THATFLAG == true){

					    conn.commit();

					    str = "SELECT * FROM Staff WHERE jobTitle = 'Nurse'";
					    executeTheQuery(str);
					    System.out.println("Enter the responsible nurse : ");
					    String sid = reader.next();

					    str = "Update inChargeOf SET sId = " + sid + " WHERE wNumber = "+ wnumber +";";
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

				case 9: {
					System.out.println("Enter the Ward Number to delete: ");
					String wnumber = reader.next();
					str = "DELETE FROM Ward WHERE wNumber = " + wnumber + ";";
					executeDelete(str);

					str = "DELETE FROM inChargeOf WHERE wNumber = " + wnumber + ";";
					executeDelete(str);
					break;
				}

				case 10: {
					str = "SELECT sum(avail) FROM Ward;";
					int avail = getVariable(str);

					if(avail <= 0){
						System.out.println("Hospital full. Cannot admit patient.");
						break;
					}

					System.out.println("Do you want to checkIn an existing patient -\n\t1.No\n\t2.Yes?\n");
					int input1 = reader.nextInt();
					System.out.println("Enter the Patient ID : ");
					int pid = reader.nextInt();

					switch (input1) {
						case 1:{
							System.out.println("Enter the Patient Name : ");
							reader.nextLine();
							String pname = reader.nextLine();
							System.out.println("Enter the Patient SSN : ");
							String pssn = reader.next();
							System.out.println("DOB (YYYY-MM-DD): ");
							String pdob = reader.next();
							System.out.println("Gender (M/F): ");
							String pgender = reader.next();
							System.out.println("Phone : ");
							String pphone = reader.next();
							System.out.println("Address : ");
							reader.nextLine();
							String paddr = reader.nextLine();

							str = "INSERT INTO `Patient`"
							+ "VALUES ( " + pid + " , '"+ pname + "', '"+ pssn +"', '"+pdob +"', '"
							+ pgender + "', "+ pphone +", '" + paddr + "');";

							executeInsert(str);
						}

						default: {
							str = " SELECT MAX(mId) FROM MedicalRecord;";
							int mid = getVariable(str);


							System.out.println("Enter check in date (YYYY-MM-DD): ");
							String startdate = reader.next();

							System.out.println("Enter the diagnosis details : ");
							reader.nextLine();
							String dd = reader.nextLine();
							System.out.println("Enter the registration fee : ");
							String regfee = reader.next();
							System.out.println("Enter the processing treatment plan");
							String ptp = reader.next();

							str = "INSERT INTO MedicalRecord VALUES(" + (mid+1) + ", '" + dd+ "', '" +startdate +"', null, "
							+ regfee +", " + ptp + ", 'Y', 'N');";
							try {
							  conn.setAutoCommit(false);
							  executeInsert(str);
							  if(THATFLAG == true){

							    conn.commit();

							    str = "INSERT IGNORE INTO hasRecord VALUES(" +(mid + 1)+", "+ pid + ");";
							    executeInsert(str);
							      if(THATFLAG == true){
							        conn.commit();

							        str = "Select sId, sName FROM Staff where jobTitle = \"Doctor\"";
							        executeTheQuery(str);
							        System.out.println("\nEnter the staff id of the doctor consulted : ");
							        String sid = reader.next();
							        str = "INSERT IGNORE INTO consults VALUES(" +(mid + 1)+", "+ sid + ");";
							        executeInsert(str);

							          if(THATFLAG == true){
							            conn.commit();
							            str = "SELECT * FROM Ward WHERE avail > 0;";
							            executeTheQuery(str);

							            System.out.println("\nChoose the Ward Number from the available list : ");
							            String wnumber = reader.next();

							            str = "INSERT IGNORE INTO assigns VALUES(" +(mid + 1)+", "+ wnumber + ");";
							            executeInsert(str);

							              if(THATFLAG == true){
							                conn.commit();
							                str = "UPDATE Ward SET avail = avail - 1 WHERE wNumber = " + wnumber + ";";
							  							executeUpdate(str);
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
							              }
							          else{
							            conn.rollback();
							              }
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
					}

					break;
				}

				case 11: {
						System.out.println("Enter the Patient ID to check out: ");
						int pid = reader.nextInt();
						str = "SELECT MAX(mId) FROM hasRecord WHERE pid = " + pid + ";";

						int mid = getVariable(str);

						System.out.println("Enter check out date (YYYY-MM-DD): ");
						String enddate = reader.next();

						str = "UPDATE MedicalRecord SET enddate = '" + enddate +"', inWard = 'N', compTreatment = 'Y' where mId = " + mid + " AND enddate IS NULL";
						try {
						  conn.setAutoCommit(false);
						  executeUpdate(str);
						  if(THATFLAG == true){

						    conn.commit();

						    str = "SELECT wNumber FROM assigns where mId = " + mid +";";
						    int wnumber = getVariable(str);

						    str = "UPDATE Ward SET avail = avail + 1 WHERE wNumber = " + wnumber + " AND avail < type;";
						    executeUpdate(str);
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
				case 12: {
					System.out.println("Enter the Drug ID : ");
					int drugid = reader.nextInt();
					System.out.println("Enter the Drug Name : ");
					reader.nextLine();
					String drugname = reader.nextLine();
					System.out.println("Enter the Drug Cost : ");
					String drugcost = reader.next();

					str = "INSERT INTO Drugs "
					+ "VALUES ( " + drugid + ", '"+ drugname + "', "+ drugcost +");";

					executeInsert(str);
					break;
				}

				case 13:{
					System.out.println("Enter the Drug ID : ");
					int drugid = reader.nextInt();
					System.out.println("Enter the Drug Name : ");
					reader.nextLine();
					String drugname = reader.nextLine();
					System.out.println("Enter the Drug Cost : ");
					String drugcost = reader.next();

					str = "UPDATE Drugs SET drugName = '"+ drugname + "', drugCost = "+ drugcost +" where drugId = " + drugid +";";
					executeUpdate(str);
					break;
				}

				case 14: {
					System.out.println("Enter the Drug ID to delete: ");
					int drugid = reader.nextInt();

					str = "DELETE from Drugs where drugId = " + drugid + ";";

					executeDelete(str);
					break;
				}

				case 15: {
					System.out.println("Enter the Test ID : ");
					int tid = reader.nextInt();
					System.out.println("Enter the Test Name : ");
					reader.nextLine();
					String tname = reader.nextLine();
					System.out.println("Enter the Test Cost : ");
					String tcost = reader.next();

					str = "SELECT * from Department;";
					executeTheQuery(str);

					System.out.println("\nEnter the Department ID that it is done in : ");
					String did = reader.next();


					str = "INSERT INTO Tests "
					+ "VALUES ( " + tid + " , '"+ tname + "', "+ tcost +");";
					try {
					  conn.setAutoCommit(false);
					  executeInsert(str);
					  if(THATFLAG == true){

					    conn.commit();

					    str = "INSERT IGNORE INTO doneBy "
					    + "VALUES ( " + tid + " , "+ did +");";
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


				case 16: {
					System.out.println("Enter the Test ID : ");
					int tid = reader.nextInt();
					System.out.println("Enter the Test Name : ");
					reader.nextLine();
					String tname = reader.nextLine();
					System.out.println("Enter the Test Cost : ");
					String tcost = reader.next();

					str = "UPDATE Tests SET tName = '"+ tname + "', tCost = "+ tcost +" where tId = " + tid +";";
					executeUpdate(str);
					break;
				}

				case 17: {
					System.out.println("Enter the Test ID to delete: ");
					int tid = reader.nextInt();

					str = "DELETE from Tests where tId = " + tid + ";";

					executeDelete(str);
					break;
				}


				case 18: {
					System.out.println("Enter the Department ID : ");
					int did = reader.nextInt();
					System.out.println("Enter the Department Name : ");
					reader.nextLine();
					String dname = reader.nextLine();

					str = "INSERT INTO Department "
					+ "VALUES ( " + did + " , '"+ dname +"');";
					executeInsert(str);
					break;
				}

				case 19: {
					System.out.println("Enter the Department ID : ");
					int did = reader.nextInt();
					System.out.println("Enter the Department Name : ");
					reader.nextLine();
					String dname = reader.nextLine();

					str = "UPDATE Department SET deptName = '"+ dname + "' WHERE deptId = " + did + ";";
					executeUpdate(str);
					break;
				}

				case 20: {
					System.out.println("Enter the Department ID to delete: ");
					int did = reader.nextInt();

					str = "DELETE FROM Department WHERE deptId = " + did + ";";
					executeDelete(str);
					break;
				}

				case 21: return;

				default:
					System.out.println("Please select a valid option");
					break;

			}
		}
	}

	/***
     * Gets the returned single value
     */
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
			THATFLAG = false;
			stmt = conn.prepareStatement(str);
			int out = stmt.executeUpdate();
			if (out == 1)
				System.out.println("Updated Successfully");
				THATFLAG = true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed! Retry.");
		}
	}

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
