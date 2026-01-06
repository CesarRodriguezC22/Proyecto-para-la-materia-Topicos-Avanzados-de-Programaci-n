package proyectotap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDeDatos {
    private static Connection connection;
    
    // Mejor usar una ruta relativa a la raíz del proyecto
    private static final String DB_PATH = "src/proyectotap/tiendita.db";
    
    public static Connection getConnection() {
        try {
            // Si la conexión es null o está cerrada, crear una nueva
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                System.out.println("Conexión a SQLite establecida.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a SQLite: " + e.getMessage());
            connection = null; // Asegurarse de no mantener una referencia a una conexión fallida
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a SQLite cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        } finally {
            connection = null; // Siempre limpiar la referencia
        }
    }
}