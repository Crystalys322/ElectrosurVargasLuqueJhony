package ModeloDAO;

import Config.ClsConexion;
import Interfaces.IPermisoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermisoDAO implements IPermisoDAO {

    private final ClsConexion conexion;

    public PermisoDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public void registrarPermiso(Permiso permiso) throws SQLException {
        String sql = "INSERT INTO permisos (id_empleado, fecha_hora_salida, fecha_hora_retorno, motivo, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection con = conexion.getConnection();
        if (con == null) {
            return;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, permiso.getIdEmpleado());
            ps.setTimestamp(2, permiso.getFechaHoraSalida());
            ps.setTimestamp(3, permiso.getFechaHoraRetorno());
            ps.setString(4, permiso.getMotivo());
            ps.setString(5, permiso.getEstado());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Permiso> listarPorEmpleado(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM permisos WHERE id_empleado = ? ORDER BY fecha_hora_salida DESC";
        return consultarPermisos(sql, idEmpleado);
    }

    @Override
    public List<Permiso> listarPorArea(int idJefeArea) throws SQLException {
        String sql = "SELECT p.* FROM permisos p INNER JOIN usuarios u ON p.id_empleado = u.id "
                + "WHERE u.id_jefe_area = ? AND (p.estado = 'PENDIENTE_AREA' OR p.estado = 'ENVIADO_RRHH') "
                + "ORDER BY p.fecha_hora_salida DESC";
        return consultarPermisos(sql, idJefeArea);
    }

    @Override
    public List<Permiso> listarPendientesRRHH() throws SQLException {
        String sql = "SELECT * FROM permisos WHERE estado = 'ENVIADO_RRHH' OR estado = 'PENDIENTE_RRHH' "
                + "ORDER BY fecha_hora_salida DESC";
        return consultarPermisos(sql, null);
    }

    @Override
    public void actualizarEstadoPorJefeArea(int idPermiso, String nuevoEstado, String observaciones, int idJefeArea) throws SQLException {
        String sql = "UPDATE permisos SET estado = ?, observaciones = ?, id_jefe_area = ?, firmado_jefe_area = ?, fecha_actualizacion = NOW() "
                + "WHERE id = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            return;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, observaciones);
            ps.setInt(3, idJefeArea);
            ps.setBoolean(4, "APROBADO_AREA".equalsIgnoreCase(nuevoEstado));
            ps.setInt(5, idPermiso);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizarEstadoPorRRHH(int idPermiso, String nuevoEstado, int idRRHH, boolean marcarPerdidaDia) throws SQLException {
        String sql = "UPDATE permisos SET estado = ?, id_rrhh = ?, firmado_rrhh = ?, perdida_dia = ?, fecha_actualizacion = NOW() "
                + "WHERE id = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            return;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idRRHH);
            ps.setBoolean(3, "APROBADO_RRHH".equalsIgnoreCase(nuevoEstado));
            ps.setBoolean(4, marcarPerdidaDia);
            ps.setInt(5, idPermiso);
            ps.executeUpdate();
        }
    }

    private List<Permiso> consultarPermisos(String sql, Integer parametro) throws SQLException {
        List<Permiso> permisos = new ArrayList<>();
        Connection con = conexion.getConnection();
        if (con == null) {
            return permisos;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setInt(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permisos.add(mapearPermiso(rs));
                }
            }
        }
        return permisos;
    }

    private Permiso mapearPermiso(ResultSet rs) throws SQLException {
        Permiso permiso = new Permiso();
        permiso.setId(rs.getInt("id"));
        permiso.setIdEmpleado(rs.getInt("id_empleado"));
        int idJefeArea = rs.getInt("id_jefe_area");
        if (rs.wasNull()) {
            permiso.setIdJefeArea(null);
        } else {
            permiso.setIdJefeArea(idJefeArea);
        }
        int idRRHH = rs.getInt("id_rrhh");
        if (rs.wasNull()) {
            permiso.setIdRRHH(null);
        } else {
            permiso.setIdRRHH(idRRHH);
        }
        permiso.setFechaHoraSalida(rs.getTimestamp("fecha_hora_salida"));
        permiso.setFechaHoraRetorno(rs.getTimestamp("fecha_hora_retorno"));
        permiso.setMotivo(rs.getString("motivo"));
        permiso.setEstado(rs.getString("estado"));
        permiso.setObservaciones(rs.getString("observaciones"));
        permiso.setFirmadoJefeArea(rs.getBoolean("firmado_jefe_area"));
        permiso.setFirmadoRRHH(rs.getBoolean("firmado_rrhh"));
        permiso.setPerdidaDia(rs.getBoolean("perdida_dia"));
        return permiso;
    }
}
