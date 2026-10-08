import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        Paciente paciente = new Paciente("1085123456", "Maria Lopez",
                "maria.lopez@correo.com", "3001234567", "Calle 18 # 25-40, Pasto");

        ProfesionalSalud profesional = new ProfesionalSalud("98765432", "Carlos Rodriguez",
                "carlos.rodriguez@medihome.com", "RP-2024-0157", "Medicina General");

        EquipoAtencion equipo = new EquipoAtencion("EQ-01", "Equipo Norte", "Comuna 2");
        equipo.agregarProfesional(profesional);

        ServicioDomiciliario servicio = new ServicioDomiciliario("SD-001", paciente,
                "Control de presion arterial");
        servicio.programar(LocalDateTime.now().withSecond(0).withNano(0));
        servicio.asignarProfesional(profesional);

        servicio.iniciarAtencion();
        AtencionMedica atencion = servicio.getAtencionMedica();

        MedicionSignosVitales medicion = new MedicionSignosVitales(36.8, 78, 135, 88, 97.0);
        medicion.realizarMedicion();
        atencion.agregarMedicion(medicion);

        atencion.setObservaciones("Paciente consciente y orientada. Presion levemente elevada.");
        atencion.setRecomendaciones("Reducir consumo de sal, caminar 30 minutos diarios "
                + "y control nuevamente en 15 dias.");

        servicio.finalizar();

        imprimirReporte(servicio, equipo);
    }

    private static void imprimirReporte(ServicioDomiciliario servicio, EquipoAtencion equipo) {
        Paciente p = servicio.getPaciente();
        ProfesionalSalud pr = servicio.getProfesionalSalud();
        AtencionMedica a = servicio.getAtencionMedica();
        String linea = "=".repeat(60);

        System.out.println();
        System.out.println(linea);
        System.out.println("          MEDIHOME - REPORTE DE ATENCION PRESTADA");
        System.out.println(linea);

        System.out.println("SERVICIO DOMICILIARIO");
        System.out.println("  Codigo:            " + servicio.getCodigo());
        System.out.println("  Fecha programada:  " + formato(servicio.getFechaProgramada()));
        System.out.println("  Direccion:         " + servicio.getDireccionAtencion());
        System.out.println("  Motivo:            " + servicio.getMotivo());
        System.out.println("  Estado:            " + servicio.getEstado());

        System.out.println("\nPACIENTE");
        System.out.println("  Identificacion:    " + p.getIdentificacion());
        System.out.println("  Nombre:            " + p.getNombre());
        System.out.println("  Correo:            " + p.getCorreo());
        System.out.println("  Telefono:          " + p.getTelefono());

        System.out.println("\nPROFESIONAL DE LA SALUD");
        System.out.println("  Nombre:            " + pr.getNombre());
        System.out.println("  Registro:          " + pr.getNumeroRegistroProfesional());
        System.out.println("  Especialidad:      " + pr.getEspecialidad());
        System.out.println("  Equipo:            " + equipo.getNombre() + " (" + equipo.getZonaCobertura() + ")");

        System.out.println("\nATENCION MEDICA");
        System.out.println("  Inicio:            " + formato(a.getFechaHoraInicio()));
        System.out.println("  Fin:               " + formato(a.getFechaHoraFin()));
        System.out.println("  Observaciones:     " + a.getObservaciones());
        System.out.println("  Recomendaciones:   " + a.getRecomendaciones());

        System.out.println("\nSIGNOS VITALES");
        int i = 1;
        for (MedicionSignosVitales m : a.getMediciones()) {
            System.out.println("  Medicion #" + i++ + " (" + formato(m.getFechaHora()) + ")");
            System.out.println("    Temperatura:          " + m.getTemperatura() + " C");
            System.out.println("    Frecuencia cardiaca:  " + m.getFrecuenciaCardiaca() + " lpm");
            System.out.println("    Presion arterial:     " + m.getPresionSistolica() + "/"
                    + m.getPresionDiastolica() + " mmHg");
            System.out.println("    Saturacion oxigeno:   " + m.getSaturacionOxigeno() + " %");
        }
        System.out.println(linea);
    }

    private static String formato(LocalDateTime fecha) {
        return fecha == null ? "-" : fecha.format(FORMATO);
    }
}
