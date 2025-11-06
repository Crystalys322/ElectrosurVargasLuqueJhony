package Interfaces;

import ModeloDAO.Permiso;
import java.sql.SQLException;
import java.util.List;

public interface IPermisoDAO {
    void registrarPermiso(Permiso permiso) throws SQLException;
    List<Permiso> listarPorEmpleado(int idEmpleado) throws SQLException;
    List<Permiso> listarPorArea(int idJefeArea) throws SQLException;
    List<Permiso> listarPendientesRRHH() throws SQLException;
    void actualizarEstadoPorJefeArea(int idPermiso, String nuevoEstado, String observaciones, int idJefeArea) throws SQLException;
    void actualizarEstadoPorRRHH(int idPermiso, String nuevoEstado, int idRRHH, boolean marcarPerdidaDia) throws SQLException;
}
