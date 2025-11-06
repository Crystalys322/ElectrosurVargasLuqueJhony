<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="ModeloDAO.Permiso"%>
<%@page import="ModeloDAO.Usuario"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Recursos Humanos - Permisos</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-success">
            <div class="container-fluid">
                <span class="navbar-brand">Panel Recursos Humanos</span>
                <div class="d-flex gap-2">
                    <a class="btn btn-outline-light" href="PermisoController?accion=dashboardRRHH">Actualizar</a>
                    <a class="btn btn-outline-light" href="AuthController">Cerrar Sesión</a>
                </div>
            </div>
        </nav>
        <div class="container py-4">
            <% if ("horas".equalsIgnoreCase(request.getParameter("alerta"))) { %>
            <div class="alert alert-warning">El empleado supera las 50 horas acumuladas. La solicitud fue rechazada automáticamente.</div>
            <% } %>
            <h2 class="mb-4">Solicitudes en revisión</h2>
            <div class="table-responsive">
                <table class="table table-bordered align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Empleado</th>
                            <th>Horas acumuladas</th>
                            <th>Salida</th>
                            <th>Retorno</th>
                            <th>Motivo</th>
                            <th>Firmas</th>
                            <th>Acción RRHH</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Permiso> permisos = (List<Permiso>) request.getAttribute("permisos");
                            Map<Integer, Usuario> mapaEmpleados = (Map<Integer, Usuario>) request.getAttribute("mapaEmpleados");
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            if (permisos != null && !permisos.isEmpty()) {
                                for (Permiso permiso : permisos) {
                                    Usuario empleado = mapaEmpleados != null ? mapaEmpleados.get(permiso.getIdEmpleado()) : null;
                        %>
                        <tr>
                            <td>#<%= permiso.getId() %></td>
                            <td><%= empleado != null ? empleado.getNombreCompleto() : permiso.getIdEmpleado() %></td>
                            <td>
                                <% if (empleado != null) { %>
                                    <span class="badge bg-secondary"><%= empleado.getHorasAcumuladas() %> h</span>
                                <% } %>
                            </td>
                            <td><%= permiso.getFechaHoraSalida() != null ? formato.format(permiso.getFechaHoraSalida()) : "" %></td>
                            <td><%= permiso.getFechaHoraRetorno() != null ? formato.format(permiso.getFechaHoraRetorno()) : "" %></td>
                            <td><%= permiso.getMotivo() %></td>
                            <td>
                                <ul class="list-unstyled mb-0">
                                    <li>Jefe Área: <span class="badge <%= permiso.isFirmadoJefeArea() ? "bg-success" : "bg-warning text-dark" %>"><%= permiso.isFirmadoJefeArea() ? "Firmado" : "Pendiente" %></span></li>
                                    <li>RRHH: <span class="badge <%= permiso.isFirmadoRRHH() ? "bg-success" : "bg-warning text-dark" %>"><%= permiso.isFirmadoRRHH() ? "Firmado" : "Pendiente" %></span></li>
                                </ul>
                            </td>
                            <td>
                                <form action="PermisoController" method="post" class="row g-2">
                                    <input type="hidden" name="accion" value="resolverRRHH">
                                    <input type="hidden" name="idPermiso" value="<%= permiso.getId() %>">
                                    <div class="col-md-5">
                                        <select class="form-select form-select-sm" name="decision" required>
                                            <option value="APROBADO_RRHH">Aprobar</option>
                                            <option value="DENEGADO_RRHH">Denegar</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4 form-check mt-1">
                                        <input class="form-check-input" type="checkbox" value="perdida" id="perdida<%= permiso.getId() %>" name="marcar">
                                        <label class="form-check-label" for="perdida<%= permiso.getId() %>">Perdida día</label>
                                    </div>
                                    <div class="col-md-3">
                                        <button type="submit" class="btn btn-primary btn-sm w-100">Guardar</button>
                                    </div>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="8" class="text-center">No hay permisos pendientes para Recursos Humanos.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    </body>
</html>
