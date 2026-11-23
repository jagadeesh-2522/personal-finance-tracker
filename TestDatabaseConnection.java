import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {
    
    public static void main(String[] args) {
        String host = System.getenv("SUPABASE_HOST");
        String port = System.getenv("SUPABASE_PORT");
        String database = System.getenv("SUPABASE_DB");
        String user = System.getenv("SUPABASE_USER");
        String password = System.getenv("SUPABASE_PASSWORD");
        
        if (host == null || port == null || database == null || user == null || password == null) {
            System.out.println("ERROR: One or more environment variables are not set.");
            System.out.println("Required: SUPABASE_HOST, SUPABASE_PORT, SUPABASE_DB, SUPABASE_USER, SUPABASE_PASSWORD");
            return;
        }
        
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: PostgreSQL JDBC Driver not found.");
            System.out.println("Details: " + e.getMessage());
            return;
        }
        
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("SUCCESS: Connected to Supabase PostgreSQL database.");
            
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT current_database()");
                
                if (resultSet.next()) {
                    String dbName = resultSet.getString(1);
                    System.out.println("Current Database: " + dbName);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to connect to database.");
            System.out.println("Details: " + e.getMessage());
        }
    }
}
