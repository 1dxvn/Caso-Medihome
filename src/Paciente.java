public class Paciente extends Usuario implements INotificable {
    private String telefono;
    private String direccion;

    public Paciente() {
    }

    public Paciente(String identificacion, String nombre, String correo,
                    String telefono, String direccion) {
        super(identificacion, nombre, correo);
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("[Notificacion a paciente " + getNombre()
                + " | tel: " + telefono + "] " + mensaje);
    }
}
