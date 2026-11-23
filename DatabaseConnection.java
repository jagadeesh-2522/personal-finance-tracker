import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        String host = System.getenv("SUPABASE_HOST");
        String port = System.getenv("SUPABASE_PORT");
        String database = System.getenv("SUPABASE_DB");
        String user = System.getenv("SUPABASE_USER");
        String password = System.getenv("SUPABASE_PASSWORD");
        
        StringBuilder errorMessage = new StringBuilder();
        
        if (host == null || host.isEmpty()) {
            errorMessage.append("SUPABASE_HOST not set. ");
        }
        if (port == null || port.isEmpty()) {
            errorMessage.append("SUPABASE_PORT not set. ");
        }
        if (database == null || database.isEmpty()) {
            errorMessage.append("SUPABASE_DB not set. ");
        }
        if (user == null || user.isEmpty()) {
            errorMessage.append("SUPABASE_USER not set. ");
        }
        if (password == null || password.isEmpty()) {
            errorMessage.append("SUPABASE_PASSWORD not set. ");
        }
        
        if (errorMessage.length() > 0) {
            throw new SQLException("Missing environment variables: " + errorMessage.toString());
        }
        
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
