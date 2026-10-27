package modelo;

/**
 * Estado de un ejemplar. Es una clase normal con los tres estados posibles como constantes.
 */
public class EstadoLibro {
    public static final EstadoLibro DISPONIBLE = new EstadoLibro("Disponible");   // en la biblioteca, listo para prestar
    public static final EstadoLibro PRESTADO = new EstadoLibro("Prestado");       // lo tiene un cliente
    public static final EstadoLibro RETIRADO = new EstadoLibro("Retirado");       // retirado por daño o decisión administrativa

    private String nombre;

    public EstadoLibro(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof EstadoLibro)) {
            return false;
        }
        return nombre.equals(((EstadoLibro) otro).nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
