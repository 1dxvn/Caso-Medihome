import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicioDomiciliario {
    public static final String SOLICITADO = "SOLICITADO";
    public static final String PROGRAMADO = "PROGRAMADO";
    public static final String ASIGNADO = "ASIGNADO";
    public static final String EN_ATENCION = "EN ATENCION";
    public static final String FINALIZADO = "FINALIZADO";
    public static final String CANCELADO = "CANCELADO";

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String codigo;
    private LocalDateTime fechaProgramada;
    private String direccionAtencion;
    private String motivo;
    private String estado;

    private Paciente paciente;
    private ProfesionalSalud profesionalSalud;
    private AtencionMedica atencionMedica;

    public ServicioDomiciliario() {
        this.estado = SOLICITADO;
    }

    public ServicioDomiciliario(String codigo, Paciente paciente, String motivo) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.motivo = motivo;
        this.direccionAtencion = paciente != null ? paciente.getDireccion() : null;
        this.estado = SOLICITADO;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getDireccionAtencion() {
        return direccionAtencion;
    }

    public void setDireccionAtencion(String direccionAtencion) {
        this.direccionAtencion = direccionAtencion;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public ProfesionalSalud getProfesionalSalud() {
        return profesionalSalud;
    }

    public AtencionMedica getAtencionMedica() {
        return atencionMedica;
    }

    public void programar(LocalDateTime fecha) {
        if (FINALIZADO.equals(estado) || CANCELADO.equals(estado)) {
            throw new IllegalStateException("No se puede programar un servicio " + estado);
        }
        this.fechaProgramada = fecha;
        this.estado = PROGRAMADO;
        if (paciente != null) {
            paciente.notificar("Su servicio " + codigo + " fue programado para " + fecha.format(FORMATO));
        }
    }

    public void asignarProfesional(ProfesionalSalud profesional) {
        if (fechaProgramada == null) {
            throw new IllegalStateException("Primero se debe programar el servicio");
        }
        if (!profesional.estaDisponible(fechaProgramada)) {
            throw new IllegalStateException("El profesional " + profesional.getNombre()
                    + " no esta disponible en " + fechaProgramada.format(FORMATO));
        }
        this.profesionalSalud = profesional;
        profesional.agregarServicio(this);
        this.estado = ASIGNADO;
        profesional.notificar("Se le asigno el servicio " + codigo + " en " + direccionAtencion);
        if (paciente != null) {
            paciente.notificar("El profesional " + profesional.getNombre() + " atendera su servicio");
        }
    }

    public void iniciarAtencion() {
        if (!ASIGNADO.equals(estado)) {
            throw new IllegalStateException("El servicio debe estar ASIGNADO para iniciar la atencion");
        }
        this.atencionMedica = new AtencionMedica(LocalDateTime.now());
        this.estado = EN_ATENCION;
    }

    public void finalizar() {
        if (!EN_ATENCION.equals(estado)) {
            throw new IllegalStateException("Solo se puede finalizar un servicio EN ATENCION");
        }
        atencionMedica.setFechaHoraFin(LocalDateTime.now());
        this.estado = FINALIZADO;
        if (paciente != null) {
            paciente.notificar("Su servicio " + codigo + " ha finalizado. Gracias por usar MediHome.");
        }
    }

    public void cancelar() {
        if (FINALIZADO.equals(estado)) {
            throw new IllegalStateException("No se puede cancelar un servicio finalizado");
        }
        this.estado = CANCELADO;
        if (paciente != null) {
            paciente.notificar("Su servicio " + codigo + " fue cancelado.");
        }
    }
}
