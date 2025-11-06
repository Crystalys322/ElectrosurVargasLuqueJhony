package ModeloDAO;

import Config.ClsConexion;
import Interfaces.IUsuarioDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements IUsuarioDAO {

    private final ClsConexion conexion;

    public UsuarioDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public Usuario autenticar(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, nombre_completo, rol, horas_acumuladas, id_jefe_area, id_area "
                + "FROM usuarios WHERE username = ? AND password = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            return null;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Optional<Usuario> obtenerPorId(int idUsuario) throws SQLException {
        String sql = "SELECT id, username, password, nombre_completo, rol, horas_acumuladas, id_jefe_area, id_area "
                + "FROM usuarios WHERE id = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            return Optional.empty();
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> listarPorRol(String rol) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, username, password, nombre_completo, rol, horas_acumuladas, id_jefe_area, id_area "
                + "FROM usuarios WHERE rol = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            return usuarios;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        }
        return usuarios;
    }

    @Override
    public void actualizarHorasAcumuladas(int idUsuario, double horas) throws SQLException {
        String sql = "UPDATE usuarios SET horas_acumuladas = ? WHERE id = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            return;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, horas);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        usuario.setRol(rs.getString("rol"));
        usuario.setHorasAcumuladas(rs.getDouble("horas_acumuladas"));
        int idJefe = rs.getInt("id_jefe_area");
        if (rs.wasNull()) {
            usuario.setIdJefeArea(null);
        } else {
            usuario.setIdJefeArea(idJefe);
        }
        int idArea = rs.getInt("id_area");
        if (rs.wasNull()) {
            usuario.setIdArea(null);
        } else {
            usuario.setIdArea(idArea);
        }
        return usuario;
    }
}
