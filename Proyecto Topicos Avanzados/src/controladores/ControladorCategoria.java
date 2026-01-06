package controladores;

import proyectotap.BaseDeDatos;
import modelos.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorCategoria {
    private final Connection conn = BaseDeDatos.getConnection();

    public void insertarCategoria(Categoria cat) throws SQLException {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cat.getNombre());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cat.setIdCategoria(rs.getInt(1));
            }
        }
    }

    public void actualizarCategoria(Categoria cat) throws SQLException {
        String sql = "UPDATE categoria SET nombre=? WHERE id_categoria=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cat.getNombre());
            stmt.setInt(2, cat.getIdCategoria());
            stmt.executeUpdate();
        }
    }

    public void eliminarCategoria(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Categoria> listarCategorias() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre")
                );
                categorias.add(c);
            }
        }
        return categorias;
    }
    
    public Categoria obtenerCategoriaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id_categoria=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre")
                );
            }
        }
        return null;
    }
    
    public Categoria buscarCategoriaPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nombre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre")
                );
            }
        }
        return null;
    }
}
