package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.EstadoLibro;
import modelo.Libro;

public class ControladorLibros {
    private ArrayList<Libro> libros;

    public ControladorLibros() {
        this.libros = new ArrayList<>();
    }

    //Crud:
    public boolean agregarLibro(Libro libro) {
        if (buscarLibro(libro.getId()) != null) {
            System.out.println("El libro con ID " + libro.getId() + " ya existe. No se puede agregar.");
            return false;
        }
        libros.add(libro);
        return true;
    }

    // El estado no se edita aqui: se cambia con cambiarEstadoLibro o con los prestamos/devoluciones
    public boolean actualizarLibro(int id, String titulo, String autor, String editorial, int anioPublicacion, Categoria categoria) {
        Libro libro = buscarLibro(id);
        if (libro == null) {
            System.out.println("El libro con ID " + id + " no existe.");
            return false;
        }
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setCategoria(categoria);
        return true;
    }

    public boolean eliminarLibro(int id) {
        Libro libro = buscarLibro(id);
        if (libro == null) {
            System.out.println("El libro con ID " + id + " no existe.");
            return false;
        }
        if (EstadoLibro.PRESTADO.equals(libro.getEstado())) {
            System.out.println("No se puede eliminar un libro que esta prestado. Registre primero su devolucion.");
            return false;
        }
        libros.remove(libro);
        return true;
    }

    public Libro buscarLibro(int id) {
        for (Libro libro : libros) {
            if (libro.getId() == id) {
                return libro;
            }
        }
        return null;
    }

    //Metodos pedidos:
    public void mostarInformacionLibro(int id) {
        Libro libro = buscarLibro(id);
        if (libro != null) {
            System.out.println("ID: " + libro.getId());
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor());
            System.out.println("Editorial: " + libro.getEditorial());
            System.out.println("Año de Publicación: " + libro.getAnioPublicacion());
            System.out.println("Categoría: " + libro.getCategoria());
            System.out.println("Estado: " + libro.getEstado());
        } else {
            System.out.println("El libro con ID " + id + " no existe.");
        }
    }

    // Solo permite pasar entre DISPONIBLE y RETIRADO. PRESTADO lo gestiona ControladorPrestamo.
    public boolean cambiarEstadoLibro(int id, EstadoLibro nuevoEstado) {
        Libro libro = buscarLibro(id);
        if (libro == null) {
            System.out.println("El libro con ID " + id + " no existe.");
            return false;
        }
        if (!nuevoEstado.equals(EstadoLibro.DISPONIBLE) && !nuevoEstado.equals(EstadoLibro.RETIRADO)) {
            System.out.println("Estado no valido. Un libro solo pasa a prestado registrando un prestamo.");
            return false;
        }
        if (EstadoLibro.PRESTADO.equals(libro.getEstado())) {
            System.out.println("El libro esta prestado. Registre primero su devolucion para cambiar su estado.");
            return false;
        }
        libro.setEstado(nuevoEstado);
        System.out.println("El estado del libro con ID " + id + " ha sido cambiado a " + nuevoEstado + ".");
        return true;
    }

    //Otros metodos:
    public ArrayList<Libro> obtenerTodosLosLibros() {
        return new ArrayList<>(libros);
    }

    public ArrayList<Libro> obtenerLibrosDisponibles() {
        return obtenerPorEstado(EstadoLibro.DISPONIBLE);
    }

    public ArrayList<Libro> obtenerLibrosPrestados() {
        return obtenerPorEstado(EstadoLibro.PRESTADO);
    }

    private ArrayList<Libro> obtenerPorEstado(EstadoLibro estado) {
        ArrayList<Libro> resultado = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.getEstado().equals(estado)) {
                resultado.add(libro);
            }
        }
        return resultado;
    }

    public void mostrarLibros(List<Libro> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay libros para mostrar.");
            return;
        }
        for (Libro l : lista) {
            System.out.println("ID: " + l.getId() + " | " + l.getTitulo() + " | " + l.getAutor()
                    + " | " + l.getEditorial() + " | " + l.getAnioPublicacion()
                    + " | " + l.getCategoria() + " | " + l.getEstado());
        }
    }
}
