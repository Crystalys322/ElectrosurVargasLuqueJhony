package Interfaces;

import ModeloDAO.Usuario;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUsuarioDAO {
    Usuario autenticar(String username, String password) throws SQLException;
    Optional<Usuario> obtenerPorId(int idUsuario) throws SQLException;
    List<Usuario> listarPorRol(String rol) throws SQLException;
    void actualizarHorasAcumuladas(int idUsuario, double horas) throws SQLException;
}
