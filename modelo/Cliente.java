package modelo;

/**
 * Cliente registrado en la biblioteca. Solo puede tener un libro prestado a la vez,
 * lo que se controla con la variable "prestado".
 */
public class Cliente {
    private String documento;   // documento de identidad (es el identificador del cliente)
    private String nombre;      // nombre completo
    private String telefono;    // String: un celular de 10 digitos no cabe en un int
    private String direccion;
    private boolean prestado;   // true si actualmente tiene un libro prestado
    private int multaPendiente; // total acumulado de multas pendientes

    public Cliente(String documento, String nombre, String telefono, String direccion) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.prestado = false;
        this.multaPendiente = 0;
    }

    //Getters y Setters:
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public boolean isPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    public int getMultaPendiente() {
        return multaPendiente;
    }

    public void setMultaPendiente(int multaPendiente) {
        this.multaPendiente = multaPendiente;
    }

    public void agregarMulta(int monto) {
        this.multaPendiente += monto;
    }
}
