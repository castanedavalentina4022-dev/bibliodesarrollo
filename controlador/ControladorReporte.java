package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Cliente;
import modelo.Libro;
import modelo.Prestamo;

public class ControladorReporte {
    private ControladorLibros controladorLibros;
    private ControladorCliente controladorCliente;
    private ControladorPrestamo controladorPrestamo;

    public ControladorReporte(ControladorLibros controladorLibros, ControladorCliente controladorCliente, ControladorPrestamo controladorPrestamo) {
        this.controladorLibros = controladorLibros;
        this.controladorCliente = controladorCliente;
        this.controladorPrestamo = controladorPrestamo;
    }

    public void mostrarReporteGeneral() {
        List<Libro> todosLibros = controladorLibros.obtenerTodosLosLibros();
        List<Cliente> todosClientes = controladorCliente.listarClientes();
        List<Prestamo> prestamosActivos = controladorPrestamo.obtenerPrestamosActivos();
        List<Prestamo> historial = controladorPrestamo.obtenerHistorial();

        System.out.println("\n===== REPORTE GENERAL =====");
        System.out.println("Total de libros registrados: " + todosLibros.size());
        System.out.println("Total de clientes: " + todosClientes.size());
        System.out.println("Prestamos activos: " + prestamosActivos.size());
        System.out.println("Total de prestamos realizados: " + historial.size());

        if (historial.isEmpty()) {
            System.out.println("Cliente con mayor numero de prestamos: No hay prestamos registrados.");
        } else {
            Map<String, Integer> conteoPorDoc = new HashMap<>();
            Map<String, Cliente> clientePorDoc = new HashMap<>();
            for (Prestamo p : historial) {
                String doc = p.getCliente().getDocumento();
                conteoPorDoc.put(doc, conteoPorDoc.getOrDefault(doc, 0) + 1);
                clientePorDoc.putIfAbsent(doc, p.getCliente());
            }

            String mejorDoc = null;
            int maxPrestamos = -1;
            for (Prestamo p : historial) {
                String doc = p.getCliente().getDocumento();
                int cnt = conteoPorDoc.get(doc);
                if (cnt > maxPrestamos) {
                    maxPrestamos = cnt;
                    mejorDoc = doc;
                }
            }

            Cliente cMax = clientePorDoc.get(mejorDoc);
            System.out.println("Cliente con mayor numero de prestamos: " + cMax.getNombre() + " (" + cMax.getDocumento() + ") - " + maxPrestamos + " prestamos");
        }

        if (todosLibros.isEmpty()) {
            System.out.println("Libro menos solicitado: No hay libros registrados.");
        } else {
            Map<Integer, Integer> conteoPorLibro = new HashMap<>();
            for (Prestamo p : historial) {
                int id = p.getLibro().getId();
                conteoPorLibro.put(id, conteoPorLibro.getOrDefault(id, 0) + 1);
            }

            Libro minLibro = null;
            int minPrestamos = Integer.MAX_VALUE;
            for (Libro l : todosLibros) {
                int cnt = conteoPorLibro.getOrDefault(l.getId(), 0);
                if (cnt < minPrestamos) {
                    minPrestamos = cnt;
                    minLibro = l;
                }
            }

            System.out.println("Libro menos solicitado: [" + minLibro.getId() + "] " + minLibro.getTitulo() + " - " + minPrestamos + " prestamos");
        }
    }
}
