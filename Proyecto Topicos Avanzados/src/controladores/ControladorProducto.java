package controladores;

import proyectotap.BaseDeDatos;
import modelos.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorProducto {
    private final Connection conn = BaseDeDatos.getConnection();

    public void insertarProducto(Producto prod) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio, stock, id_categoria) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, prod.getNombre());
            stmt.setDouble(2, prod.getPrecio());
            stmt.setInt(3, prod.getStock());
            stmt.setInt(4, prod.getIdCategoria());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                prod.setIdProducto(rs.getInt(1));
            }
        }
    }

    public void actualizarProducto(Producto prod) throws SQLException {
        String sql = "UPDATE producto SET nombre=?, precio=?, stock=?, id_categoria=? WHERE id_producto=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prod.getNombre());
            stmt.setDouble(2, prod.getPrecio());
            stmt.setInt(3, prod.getStock());
            stmt.setInt(4, prod.getIdCategoria());
            stmt.setInt(5, prod.getIdProducto());
            stmt.executeUpdate();
        }
    }

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Producto> listarProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as nombre_categoria FROM producto p LEFT JOIN categoria c ON p.id_categoria = c.id_categoria";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("id_categoria"),
                    rs.getString("nombre_categoria")
                );
                productos.add(p);
            }
        }
        return productos;
    }
    
    public Producto obtenerProductoPorId(int id) throws SQLException {
        String sql = "SELECT p.*, c.nombre as nombre_categoria FROM producto p LEFT JOIN categoria c ON p.id_categoria = c.id_categoria WHERE p.id_producto=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("id_categoria"),
                    rs.getString("nombre_categoria")
                );
            }
        }
        return null;
    }
    
    public Producto buscarProductoPorNombre(String nombre) throws SQLException {
        String sql = "SELECT p.*, c.nombre as nombre_categoria FROM producto p LEFT JOIN categoria c ON p.id_categoria = c.id_categoria WHERE p.nombre=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("id_categoria"),
                    rs.getString("nombre_categoria")
                );
            }
        }
        return null;
    }
    
    public void actualizarStock(int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idProducto);
            stmt.executeUpdate();
        }
    }
}