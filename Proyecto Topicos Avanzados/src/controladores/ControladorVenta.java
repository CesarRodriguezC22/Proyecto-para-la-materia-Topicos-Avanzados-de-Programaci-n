package controladores;

import proyectotap.BaseDeDatos;
import modelos.Venta;
import modelos.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorVenta {
    private final Connection conn = BaseDeDatos.getConnection();

    public int insertarVenta(Venta venta) throws SQLException {
        String sql = "INSERT INTO venta (fecha, total) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, venta.getFecha());
            stmt.setDouble(2, venta.getTotal());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public void insertarDetalleVenta(DetalleVenta detalle) throws SQLException {
        String sql = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getSubtotal());
            stmt.executeUpdate();
        }
    }

    public List<Venta> listarVentas() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta ORDER BY fecha DESC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("id_venta"),
                    rs.getString("fecha"),
                    rs.getDouble("total")
                );
                ventas.add(v);
            }
        }
        return ventas;
    }
    
    public List<DetalleVenta> obtenerDetallesVenta(int idVenta) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT dv.*, p.nombre as nombre_producto FROM detalle_venta dv JOIN producto p ON dv.id_producto = p.id_producto WHERE dv.id_venta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetalleVenta dv = new DetalleVenta(
                    rs.getInt("id_detalle"),
                    rs.getInt("id_venta"),
                    rs.getInt("id_producto"),
                    rs.getString("nombre_producto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("subtotal")
                );
                detalles.add(dv);
            }
        }
        return detalles;
    }
    
    public Venta buscarVentaPorId(int idVenta) throws SQLException {
        String sql = "SELECT * FROM venta WHERE id_venta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Venta venta = new Venta(
                    rs.getInt("id_venta"),
                    rs.getString("fecha"),
                    rs.getDouble("total")
                );
                venta.setDetalles(obtenerDetallesVenta(idVenta));
                return venta;
            }
        }
        return null;
    }
}