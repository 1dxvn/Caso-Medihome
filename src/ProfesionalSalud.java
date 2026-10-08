import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProfesionalSalud extends Usuario implements INotificable {
    private String numeroRegistroProfesional;
    private String especialidad;

    private final List<ServicioDomiciliario> serviciosAsignados = new ArrayList<>();

    public ProfesionalSalud() {
    }

    public ProfesionalSalud(String identificacion, String nombre, String correo,
                            String numeroRegistroProfesional, String especialidad) {
        super(identificacion, nombre, correo);
        this.numeroRegistroProfesional = numeroRegistroProfesional;
        this.especialidad = especialidad;
    }

    public String getNumeroRegistroProfesional() {
        return numeroRegistroProfesional;
    }

    public void setNumeroRegistroProfesional(String numeroRegistroProfesional) {
        this.numeroRegistroProfesional = numeroRegistroProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<ServicioDomiciliario> getServiciosAsignados() {
        return serviciosAsignados;
    }

    void agregarServicio(ServicioDomiciliario servicio) {
        if (!serviciosAsignados.contains(servicio)) {
            serviciosAsignados.add(servicio);
        }
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("[Notificacion a profesional " + getNombre()
                + " | correo: " + getCorreo() + "] " + mensaje);
    }

    public boolean estaDisponible(LocalDateTime fecha) {
        for (ServicioDomiciliario s : serviciosAsignados) {
            boolean activo = !ServicioDomiciliario.FINALIZADO.equals(s.getEstado())
                    && !ServicioDomiciliario.CANCELADO.equals(s.getEstado());
            if (activo && s.getFechaProgramada() != null && s.getFechaProgramada().equals(fecha)) {
                return false;
            }
        }
        return true;
    }
}
