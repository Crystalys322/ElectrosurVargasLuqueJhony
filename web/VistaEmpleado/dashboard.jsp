<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ModeloDAO.Permiso"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Empleado - Boletas de Permiso</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container-fluid">
                <span class="navbar-brand">Portal de Permisos</span>
                <div class="d-flex">
                    <a class="btn btn-outline-light" href="AuthController">Cerrar Sesión</a>
                </div>
            </div>
        </nav>
        <div class="container py-4">
            <div class="row">
                <div class="col-lg-5">
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white">Registrar boleta de permiso</div>
                        <div class="card-body">
                            <form action="PermisoController" method="post">
                                <input type="hidden" name="accion" value="crear">
                                <div class="mb-3">
                                    <label class="form-label" for="fechaPermiso">Fecha de salida</label>
                                    <input type="date" class="form-control" id="fechaPermiso" name="fechaPermiso" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="horaSalida">Hora de salida</label>
                                    <input type="time" class="form-control" id="horaSalida" name="horaSalida" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="fechaRetorno">Fecha de retorno</label>
                                    <input type="date" class="form-control" id="fechaRetorno" name="fechaRetorno" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="horaRetorno">Hora de retorno</label>
                                    <input type="time" class="form-control" id="horaRetorno" name="horaRetorno" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="motivo">Motivo del permiso</label>
                                    <textarea class="form-control" id="motivo" name="motivo" rows="3" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-success">Enviar a jefe inmediato</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-7">
                    <div class="card">
                        <div class="card-header bg-secondary text-white">Historial de solicitudes</div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Salida</th>
                                            <th>Retorno</th>
                                            <th>Motivo</th>
                                            <th>Estado</th>
                                            <th>Observaciones</th>
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
                                            <td><%= formato.format(permiso.getFechaHoraSalida()) %></td>
                                            <td><%= formato.format(permiso.getFechaHoraRetorno()) %></td>
                                            <td><%= permiso.getMotivo() %></td>
                                            <td><span class="badge bg-info text-dark"><%= permiso.getEstado() %></span></td>
                                            <td><%= permiso.getObservaciones() != null ? permiso.getObservaciones() : "" %></td>
                                        </tr>
                                        <%
                                                }
                                            } else {
                                        %>
                                        <tr>
                                            <td colspan="5" class="text-center">Aún no registra solicitudes.</td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    </body>
</html>
