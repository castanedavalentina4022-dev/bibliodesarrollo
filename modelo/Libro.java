package modelo;

import java.util.ArrayList;

/**
 * Representa un ejemplar físico de la biblioteca. Cada ejemplar tiene un id único,
 * incluso si existen varios con el mismo título.
 */
public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private Categoria categoria;
    private EstadoLibro estado;
    private ArrayList<Cliente> reservas;

    public Libro(int id, String titulo, String autor, String editorial, int anioPublicacion, Categoria categoria, EstadoLibro estado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.estado = estado;
        this.reservas = new ArrayList<>();
    }

    // Un libro nuevo siempre entra como disponible
    public Libro(int id, String titulo, String autor, String editorial, int anioPublicacion, Categoria categoria) {
        this(id, titulo, autor, editorial, anioPublicacion, categoria, EstadoLibro.DISPONIBLE);
    }

    //Getters y Setters:
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }
    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstadoLibro getEstado() {
        return estado;
    }
    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }

    public ArrayList<Cliente> getReservas() {
        return reservas;
    }

    public boolean isDisponible() {
        return EstadoLibro.DISPONIBLE.equals(estado);
    }
}
