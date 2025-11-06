package ModeloDAO;

import java.sql.Timestamp;

public class Permiso {
    private int id;
    private int idEmpleado;
    private Integer idJefeArea;
    private Integer idRRHH;
    private Timestamp fechaHoraSalida;
    private Timestamp fechaHoraRetorno;
    private String motivo;
    private String estado;
    private String observaciones;
    private boolean firmadoJefeArea;
    private boolean firmadoRRHH;
    private boolean perdidaDia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdJefeArea() {
        return idJefeArea;
    }

    public void setIdJefeArea(Integer idJefeArea) {
        this.idJefeArea = idJefeArea;
    }

    public Integer getIdRRHH() {
        return idRRHH;
    }

    public void setIdRRHH(Integer idRRHH) {
        this.idRRHH = idRRHH;
    }

    public Timestamp getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Timestamp fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public Timestamp getFechaHoraRetorno() {
        return fechaHoraRetorno;
    }

    public void setFechaHoraRetorno(Timestamp fechaHoraRetorno) {
        this.fechaHoraRetorno = fechaHoraRetorno;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isFirmadoJefeArea() {
        return firmadoJefeArea;
    }

    public void setFirmadoJefeArea(boolean firmadoJefeArea) {
        this.firmadoJefeArea = firmadoJefeArea;
    }

    public boolean isFirmadoRRHH() {
        return firmadoRRHH;
    }

    public void setFirmadoRRHH(boolean firmadoRRHH) {
        this.firmadoRRHH = firmadoRRHH;
    }

    public boolean isPerdidaDia() {
        return perdidaDia;
    }

    public void setPerdidaDia(boolean perdidaDia) {
        this.perdidaDia = perdidaDia;
    }
}
