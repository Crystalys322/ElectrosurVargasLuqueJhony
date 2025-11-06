package ModeloDAO;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nombreCompleto;
    private String rol;
    private double horasAcumuladas;
    private Integer idJefeArea;
    private Integer idArea;

    public Usuario() {
    }

    public Usuario(int id, String username, String password, String nombreCompleto, String rol, double horasAcumuladas, Integer idJefeArea, Integer idArea) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.horasAcumuladas = horasAcumuladas;
        this.idJefeArea = idJefeArea;
        this.idArea = idArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public double getHorasAcumuladas() {
        return horasAcumuladas;
    }

    public void setHorasAcumuladas(double horasAcumuladas) {
        this.horasAcumuladas = horasAcumuladas;
    }

    public Integer getIdJefeArea() {
        return idJefeArea;
    }

    public void setIdJefeArea(Integer idJefeArea) {
        this.idJefeArea = idJefeArea;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }
}
