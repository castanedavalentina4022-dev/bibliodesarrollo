package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
import modelo.EstadoLibro;
import modelo.Libro;
import modelo.Prestamo;

/**
 * Centraliza las operaciones de prestamo y devolucion y mantiene coherentes
 * el estado del libro y la variable "prestado" del cliente.
 */
public class ControladorPrestamo {
    private ControladorLibros controladorLibros;
    private ControladorCliente controladorCliente;
    private ArrayList<Prestamo> prestamos;

    public ControladorPrestamo(ControladorLibros controladorLibros, ControladorCliente controladorCliente) {
        this.controladorLibros = controladorLibros;
        this.controladorCliente = controladorCliente;
        this.prestamos = new ArrayList<>();
    }

    public boolean registrarPrestamo(int idLibro, String documentoCliente) {
        Cliente cliente = controladorCliente.buscarCliente(documentoCliente);
        if (cliente == null) {
            System.out.println("El cliente con documento " + documentoCliente + " no esta registrado.");
            return false;
        }
        Libro libro = controladorLibros.buscarLibro(idLibro);
        if (libro == null) {
            System.out.println("El libro con ID " + idLibro + " no existe.");
            return false;
        }
        if (EstadoLibro.PRESTADO.equals(libro.getEstado())) {
            System.out.println("El libro ya esta prestado a otro cliente.");
            return false;
        }
        if (EstadoLibro.RETIRADO.equals(libro.getEstado())) {
            System.out.println("El libro fue retirado del inventario y no se puede prestar.");
            return false;
        }
        if (cliente.isPrestado()) {
            System.out.println("El cliente ya tiene un libro prestado. Debe devolverlo antes de pedir otro.");
            return false;
        }

        libro.getReservas().removeIf(c -> c.getDocumento().equals(documentoCliente));

        prestamos.add(new Prestamo(libro, cliente));
        libro.setEstado(EstadoLibro.PRESTADO);
        cliente.setPrestado(true);
        System.out.println("Prestamo registrado: \"" + libro.getTitulo() + "\" para " + cliente.getNombre() + ".");
        return true;
    }

    public boolean registrarDevolucion(int idLibro) {
        Libro libro = controladorLibros.buscarLibro(idLibro);
        if (libro == null) {
            System.out.println("El libro con ID " + idLibro + " no existe.");
            return false;
        }
        Prestamo prestamo = buscarPrestamoActivoPorLibro(idLibro);
        if (prestamo == null) {
            System.out.println("El libro con ID " + idLibro + " no esta prestado, no hay devolucion que registrar.");
            return false;
        }

        prestamo.registrarDevolucion();
        libro.setEstado(EstadoLibro.DISPONIBLE);
        prestamo.getCliente().setPrestado(false);

        if (prestamo.getMulta() > 0) {
            prestamo.getCliente().agregarMulta(prestamo.getMulta());
            System.out.println("Devolucion con " + prestamo.getDiasRetraso() + " dia(s) de retraso. Multa generada: $"
                    + prestamo.getMulta() + " COP. Total pendiente: $" + prestamo.getCliente().getMultaPendiente() + " COP.");
        } else {
            System.out.println("Devolucion registrada: \"" + libro.getTitulo() + "\" de " + prestamo.getCliente().getNombre()
                    + ". El libro vuelve a estar disponible.");
        }

        if (!libro.getReservas().isEmpty()) {
            Cliente siguiente = libro.getReservas().get(0);
            System.out.println("Siguiente cliente en la lista de espera: " + siguiente.getNombre() + " (" + siguiente.getDocumento() + ")");
        }
        return true;
    }

    public boolean registrarReserva(int idLibro, String documento) {
        Cliente cliente = controladorCliente.buscarCliente(documento);
        if (cliente == null) {
            System.out.println("El cliente con documento " + documento + " no esta registrado.");
            return false;
        }
        Libro libro = controladorLibros.buscarLibro(idLibro);
        if (libro == null) {
            System.out.println("El libro con ID " + idLibro + " no existe.");
            return false;
        }
        if (EstadoLibro.DISPONIBLE.equals(libro.getEstado())) {
            System.out.println("El libro esta disponible, registre un prestamo en lugar de una reserva.");
            return false;
        }
        if (EstadoLibro.RETIRADO.equals(libro.getEstado())) {
            System.out.println("El libro fue retirado del inventario y no se puede reservar.");
            return false;
        }
        Prestamo prestamoActivo = buscarPrestamoActivoPorLibro(idLibro);
        if (prestamoActivo != null && prestamoActivo.getCliente().getDocumento().equals(documento)) {
            System.out.println("El cliente ya tiene actualmente este libro en prestamo.");
            return false;
        }
        for (Cliente c : libro.getReservas()) {
            if (c.getDocumento().equals(documento)) {
                System.out.println("El cliente ya se encuentra en la lista de reservas de este libro.");
                return false;
            }
        }

        libro.getReservas().add(cliente);
        int posicion = libro.getReservas().size();
        System.out.println("Reserva registrada con exito. Posicion en la lista de espera: " + posicion + ".");
        return true;
    }

    public void mostrarReservasLibro(int idLibro) {
        Libro libro = controladorLibros.buscarLibro(idLibro);
        if (libro == null) {
            System.out.println("El libro con ID " + idLibro + " no existe.");
            return;
        }
        if (libro.getReservas().isEmpty()) {
            System.out.println("El libro con ID " + idLibro + " no tiene reservas registradas.");
            return;
        }
        System.out.println("Reservas para \"" + libro.getTitulo() + "\":");
        for (int i = 0; i < libro.getReservas().size(); i++) {
            Cliente c = libro.getReservas().get(i);
            System.out.println((i + 1) + ". " + c.getNombre() + " (" + c.getDocumento() + ")");
        }
    }

    public void quitarClienteDeReservas(String documento) {
        for (Libro libro : controladorLibros.obtenerTodosLosLibros()) {
            libro.getReservas().removeIf(c -> c.getDocumento().equals(documento));
        }
    }

    public void cargarPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public Prestamo buscarPrestamoActivoPorLibro(int idLibro) {
        for (Prestamo p : prestamos) {
            if (p.estaActivo() && p.getLibro().getId() == idLibro) {
                return p;
            }
        }
        return null;
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        ArrayList<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.estaActivo()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public List<Prestamo> obtenerHistorial() {
        return new ArrayList<>(prestamos);
    }

    public void mostrarPrestamosActivos() {
        mostrarPrestamos(obtenerPrestamosActivos(), "No hay prestamos activos.");
    }

    public void mostrarHistorial() {
        mostrarPrestamos(obtenerHistorial(), "Todavia no se ha registrado ningun prestamo.");
    }

    private void mostrarPrestamos(List<Prestamo> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        for (Prestamo p : lista) {
            System.out.println("Libro [" + p.getLibro().getId() + "] " + p.getLibro().getTitulo()
                    + " | Cliente: " + p.getCliente().getNombre() + " (" + p.getCliente().getDocumento() + ")"
                    + " | Prestado: " + p.getFechaPrestamo()
                    + " | Limite: " + p.getFechaLimite()
                    + " | Devuelto: " + (p.estaActivo() ? "pendiente" : p.getFechaDevolucion())
                    + (!p.estaActivo() && p.getMulta() > 0 ? " | Multa: $" + p.getMulta() + " COP" : ""));
        }
    }
}
