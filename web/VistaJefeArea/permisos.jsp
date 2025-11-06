<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ModeloDAO.Permiso"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Jefe de Área - Solicitudes</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <span class="navbar-brand">Panel Jefe de Área</span>
                <div class="d-flex gap-2">
                    <a class="btn btn-outline-light" href="PermisoController?accion=dashboardJefeArea">Actualizar</a>
                    <a class="btn btn-outline-light" href="AuthController">Cerrar Sesión</a>
                </div>
            </div>
        </nav>
        <div class="container py-4">
            <h2 class="mb-4">Solicitudes pendientes de su equipo</h2>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Empleado</th>
                            <th>Salida</th>
                            <th>Retorno</th>
                            <th>Motivo</th>
                            <th>Observaciones</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Permiso> permisos = (List<Permiso>) request.getAttribute("permisos");
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            if (permisos != null && !permisos.isEmpty()) {
                                for (Permiso permiso : permisos) {
                        %>
                        <tr>
                            <td><strong>#<%= permiso.getId() %></strong></td>
                            <td><%= permiso.getIdEmpleado() %></td>
                            <td><%= permiso.getFechaHoraSalida() != null ? formato.format(permiso.getFechaHoraSalida()) : "" %></td>
                            <td><%= permiso.getFechaHoraRetorno() != null ? formato.format(permiso.getFechaHoraRetorno()) : "" %></td>
                            <td><%= permiso.getMotivo() %></td>
                            <td>
                                <form action="PermisoController" method="post" class="row g-2">
                                    <input type="hidden" name="accion" value="denegarArea">
                                    <input type="hidden" name="idPermiso" value="<%= permiso.getId() %>">
                                    <div class="col-12">
                                        <input type="text" class="form-control" name="observaciones" placeholder="Observaciones" value="<%= permiso.getObservaciones() != null ? permiso.getObservaciones() : "" %>" required>
                                    </div>
                                    <div class="col-12 d-flex gap-2">
                                        <button type="submit" class="btn btn-danger btn-sm">Denegar</button>
                                    </div>
                                </form>
                            </td>
                            <td>
                                <form action="PermisoController" method="post" class="d-inline">
                                    <input type="hidden" name="accion" value="aprobarArea">
                                    <input type="hidden" name="idPermiso" value="<%= permiso.getId() %>">
                                    <button type="submit" class="btn btn-success btn-sm">Aprobar y enviar a RRHH</button>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="7" class="text-center">No existen solicitudes pendientes.</td>
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
