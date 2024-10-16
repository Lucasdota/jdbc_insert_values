package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;

public class Program {

	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Connection conn = null;
		PreparedStatement st = null;
		try {
			// establish connection
			conn = DB.getConnection();
			
			// interrogation mark leaves the arguments open to receive values later
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, Birthdate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					// will get the new generated key
					Statement.RETURN_GENERATED_KEYS);
			
			// "Carl Purple" enters in the first interrogation
			st.setString(1, "Carl Purple");
			// and so on
			st.setString(2, "carl@gmail.com");
			// third argument expects a date, import it from the java.sql!!!
			st.setDate(3, new java.sql.Date(sdf.parse("22/04/1985").getTime()));
			st.setDouble(4, 3000.0);
			st.setInt(5, 4);
			
			// when it is an operation to alterate values, you have to call this method
			// the result of it is an int indicating how many rows were affected
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				// will return the new key(s) on a result set table
				ResultSet rs = st.getGeneratedKeys();
				while (rs.next()) {
					// here you get the id of the new added rows
					int id = rs.getInt(1);
					System.out.println("Done! Id = " + id);
				}
			} else {
				System.out.println("No rows affected!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// treat parse exception from sdf.parse
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			// alaways close connection last
			DB.closeConnection();
		}

	}

}
