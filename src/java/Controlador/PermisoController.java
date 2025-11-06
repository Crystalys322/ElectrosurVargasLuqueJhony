package Controlador;

import Interfaces.IPermisoDAO;
import Interfaces.IUsuarioDAO;
import ModeloDAO.Permiso;
import ModeloDAO.PermisoDAO;
import ModeloDAO.Usuario;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PermisoController extends HttpServlet {

    private IPermisoDAO permisoDAO;
    private IUsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.permisoDAO = new PermisoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("AuthController");
            return;
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "dashboardEmpleado";
        }
        try {
            switch (accion) {
                case "dashboardEmpleado":
                    mostrarDashboardEmpleado(request, response, usuario);
                    break;
                case "dashboardJefeArea":
                    mostrarDashboardJefeArea(request, response, usuario);
                    break;
                case "dashboardRRHH":
                    mostrarDashboardRRHH(request, response, usuario);
                    break;
                default:
                    response.sendRedirect("AuthController");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Error al consultar permisos", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("AuthController");
            return;
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect("PermisoController?accion=dashboardEmpleado");
            return;
        }
        try {
            switch (accion) {
                case "crear":
                    registrarPermiso(request, response, usuario);
                    break;
                case "aprobarArea":
                    actualizarComoJefeArea(request, response, usuario, true);
                    break;
                case "denegarArea":
                    actualizarComoJefeArea(request, response, usuario, false);
                    break;
                case "resolverRRHH":
                    actualizarComoRRHH(request, response, usuario);
                    break;
                default:
                    response.sendRedirect("PermisoController?accion=dashboardEmpleado");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Error al procesar permiso", ex);
        }
    }

    private void mostrarDashboardEmpleado(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws SQLException, ServletException, IOException {
        List<Permiso> permisos = permisoDAO.listarPorEmpleado(usuario.getId());
        request.setAttribute("permisos", permisos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("VistaEmpleado/dashboard.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarDashboardJefeArea(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws SQLException, ServletException, IOException {
        List<Permiso> permisos = permisoDAO.listarPorArea(usuario.getId());
        request.setAttribute("permisos", permisos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("VistaJefeArea/permisos.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarDashboardRRHH(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws SQLException, ServletException, IOException {
        List<Permiso> permisos = permisoDAO.listarPendientesRRHH();
        Map<Integer, Usuario> mapaEmpleados = new HashMap<>();
        for (Permiso permiso : permisos) {
            usuarioDAO.obtenerPorId(permiso.getIdEmpleado()).ifPresent(u -> mapaEmpleados.put(permiso.getIdEmpleado(), u));
        }
        request.setAttribute("permisos", permisos);
        request.setAttribute("mapaEmpleados", mapaEmpleados);
        RequestDispatcher dispatcher = request.getRequestDispatcher("VistaJefeRRHH/permisos.jsp");
        dispatcher.forward(request, response);
    }

    private void registrarPermiso(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws SQLException, IOException {
        String fechaPermiso = request.getParameter("fechaPermiso");
        String horaSalida = request.getParameter("horaSalida");
        String fechaRetorno = request.getParameter("fechaRetorno");
        String horaRetorno = request.getParameter("horaRetorno");
        String motivo = request.getParameter("motivo");

        LocalDateTime salida = combinarFechaHora(fechaPermiso, horaSalida);
        LocalDateTime retorno = combinarFechaHora(fechaRetorno, horaRetorno);

        Permiso permiso = new Permiso();
        permiso.setIdEmpleado(usuario.getId());
        permiso.setFechaHoraSalida(Timestamp.valueOf(salida));
        permiso.setFechaHoraRetorno(Timestamp.valueOf(retorno));
        permiso.setMotivo(motivo);
        permiso.setEstado("PENDIENTE_AREA");
        permisoDAO.registrarPermiso(permiso);
        response.sendRedirect("PermisoController?accion=dashboardEmpleado");
    }

    private void actualizarComoJefeArea(HttpServletRequest request, HttpServletResponse response, Usuario usuario, boolean aprobar) throws SQLException, IOException {
        int idPermiso = Integer.parseInt(request.getParameter("idPermiso"));
        String observaciones = request.getParameter("observaciones");
        String nuevoEstado = aprobar ? "ENVIADO_RRHH" : "DENEGADO_AREA";
        if (aprobar && (observaciones == null || observaciones.trim().isEmpty())) {
            observaciones = "Permiso aprobado y enviado a RRHH";
        }
        if (!aprobar && (observaciones == null || observaciones.trim().isEmpty())) {
            observaciones = "Permiso denegado por el jefe de área";
        }
        permisoDAO.actualizarEstadoPorJefeArea(idPermiso, nuevoEstado, observaciones, usuario.getId());
        response.sendRedirect("PermisoController?accion=dashboardJefeArea");
    }

    private void actualizarComoRRHH(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws SQLException, IOException {
        int idPermiso = Integer.parseInt(request.getParameter("idPermiso"));
        String decision = request.getParameter("decision");
        boolean marcarPerdida = "perdida".equalsIgnoreCase(request.getParameter("marcar"));

        if (decision == null || decision.trim().isEmpty()) {
            response.sendRedirect("PermisoController?accion=dashboardRRHH");
            return;
        }

        Permiso permiso = buscarPermisoPorIdParaRRHH(idPermiso);
        if (permiso == null) {
            response.sendRedirect("PermisoController?accion=dashboardRRHH");
            return;
        }
        Usuario empleado = usuarioDAO.obtenerPorId(permiso.getIdEmpleado()).orElse(null);
        if (empleado != null && empleado.getHorasAcumuladas() > 50 && "APROBADO_RRHH".equalsIgnoreCase(decision)) {
            permisoDAO.actualizarEstadoPorRRHH(idPermiso, "DENEGADO_RRHH", usuario.getId(), marcarPerdida);
            response.sendRedirect("PermisoController?accion=dashboardRRHH&alerta=horas");
            return;
        }
        permisoDAO.actualizarEstadoPorRRHH(idPermiso, decision, usuario.getId(), marcarPerdida);
        response.sendRedirect("PermisoController?accion=dashboardRRHH");
    }

    private Permiso buscarPermisoPorIdParaRRHH(int idPermiso) throws SQLException {
        List<Permiso> permisos = permisoDAO.listarPendientesRRHH();
        for (Permiso permiso : permisos) {
            if (permiso.getId() == idPermiso) {
                return permiso;
            }
        }
        return null;
    }

    private LocalDateTime combinarFechaHora(String fecha, String hora) {
        LocalDate dia = LocalDate.parse(fecha);
        LocalTime tiempo = LocalTime.parse(hora);
        return LocalDateTime.of(dia, tiempo);
    }
}
