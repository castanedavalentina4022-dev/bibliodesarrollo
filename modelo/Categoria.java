package modelo;

/**
 * Categoria de un libro. Es una clase normal: las cuatro categorias permitidas
 * de la biblioteca estan disponibles como constantes y en la lista TODAS.
 */
public class Categoria {
    public static final Categoria LITERATURA = new Categoria("Literatura");
    public static final Categoria CIENCIA = new Categoria("Ciencia");
    public static final Categoria HISTORIA = new Categoria("Historia");
    public static final Categoria TECNOLOGIA = new Categoria("Tecnología");

    public static final Categoria[] TODAS = {LITERATURA, CIENCIA, HISTORIA, TECNOLOGIA};

    private String nombre;

    public Categoria(String nombre) {
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
        if (!(otro instanceof Categoria)) {
            return false;
        }
        return nombre.equals(((Categoria) otro).nombre);
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
