package conexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class ConexionMysql {
    
    public static Connection conectar() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // <-- ARREGLO DEL DRIVER
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca_java", "root", "1234");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}