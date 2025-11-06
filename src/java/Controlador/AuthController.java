package Controlador;

import Interfaces.IUsuarioDAO;
import ModeloDAO.Usuario;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthController extends HttpServlet {

    private IUsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Usuario usuario = usuarioDAO.autenticar(username, password);
            if (usuario != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                redirigirSegunRol(usuario, response);
            } else {
                request.setAttribute("error", "Credenciales incorrectas");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException("Error al autenticar usuario", ex);
        }
    }

    private void redirigirSegunRol(Usuario usuario, HttpServletResponse response) throws IOException {
        if ("EMPLEADO".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect("PermisoController?accion=dashboardEmpleado");
            return;
        }
        if ("JEFE_AREA".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect("PermisoController?accion=dashboardJefeArea");
            return;
        }
        if ("JEFE_RRHH".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect("PermisoController?accion=dashboardRRHH");
            return;
        }
        response.sendRedirect("login.jsp");
    }
}
