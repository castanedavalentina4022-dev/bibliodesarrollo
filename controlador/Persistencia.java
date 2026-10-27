package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import modelo.Categoria;
import modelo.Cliente;
import modelo.EstadoLibro;
import modelo.Libro;
import modelo.Prestamo;

public class Persistencia {
    private final ControladorLibros controladorLibros;
    private final ControladorCliente controladorCliente;
    private final ControladorPrestamo controladorPrestamo;
    private final File dataDir;

    public Persistencia(ControladorLibros controladorLibros, ControladorCliente controladorCliente, ControladorPrestamo controladorPrestamo) {
        this.controladorLibros = controladorLibros;
        this.controladorCliente = controladorCliente;
        this.controladorPrestamo = controladorPrestamo;
        this.dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public void guardar() {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        try {
            // libros.csv
            File archivoLibros = new File(dataDir, "libros.csv");
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivoLibros), StandardCharsets.UTF_8))) {
                for (Libro l : controladorLibros.obtenerTodosLosLibros()) {
                    pw.println(l.getId() + ";" + l.getTitulo() + ";" + l.getAutor() + ";" + l.getEditorial() + ";"
                            + l.getAnioPublicacion() + ";" + l.getCategoria().getNombre() + ";" + l.getEstado().getNombre());
                }
            }

            // clientes.csv
            File archivoClientes = new File(dataDir, "clientes.csv");
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivoClientes), StandardCharsets.UTF_8))) {
                for (Cliente c : controladorCliente.listarClientes()) {
                    pw.println(c.getDocumento() + ";" + c.getNombre() + ";" + c.getTelefono() + ";" + c.getDireccion() + ";"
                            + c.isPrestado() + ";" + c.getMultaPendiente());
                }
            }

            // prestamos.csv
            File archivoPrestamos = new File(dataDir, "prestamos.csv");
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivoPrestamos), StandardCharsets.UTF_8))) {
                for (Prestamo p : controladorPrestamo.obtenerHistorial()) {
                    String devStr = p.getFechaDevolucion() != null ? p.getFechaDevolucion().toString() : "";
                    pw.println(p.getLibro().getId() + ";" + p.getCliente().getDocumento() + ";"
                            + p.getFechaPrestamo() + ";" + p.getFechaLimite() + ";" + devStr + ";" + p.getMulta());
                }
            }

            // reservas.csv
            File archivoReservas = new File(dataDir, "reservas.csv");
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivoReservas), StandardCharsets.UTF_8))) {
                for (Libro l : controladorLibros.obtenerTodosLosLibros()) {
                    for (Cliente c : l.getReservas()) {
                        pw.println(l.getId() + ";" + c.getDocumento());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar los datos: " + e.getMessage());
        }
    }

    public void cargar() {
        cargarLibros();
        cargarClientes();
        cargarPrestamos();
        cargarReservas();
    }

    private void cargarLibros() {
        File archivo = new File(dataDir, "libros.csv");
        if (!archivo.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";", -1);
                if (partes.length < 7) {
                    System.out.println("Linea mal formada en libros.csv: " + linea);
                    continue;
                }
                try {
                    int id = Integer.parseInt(partes[0].trim());
                    String titulo = partes[1].trim();
                    String autor = partes[2].trim();
                    String editorial = partes[3].trim();
                    int anio = Integer.parseInt(partes[4].trim());
                    Categoria categoria = buscarCategoria(partes[5].trim());
                    EstadoLibro estado = buscarEstado(partes[6].trim());
                    if (categoria == null || estado == null) {
                        System.out.println("Categoria o estado no reconocidos en libros.csv: " + linea);
                        continue;
                    }
                    Libro libro = new Libro(id, titulo, autor, editorial, anio, categoria, estado);
                    controladorLibros.agregarLibro(libro);
                } catch (Exception e) {
                    System.out.println("Error al procesar linea en libros.csv: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer libros.csv: " + e.getMessage());
        }
    }

    private void cargarClientes() {
        File archivo = new File(dataDir, "clientes.csv");
        if (!archivo.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";", -1);
                if (partes.length < 6) {
                    System.out.println("Linea mal formada en clientes.csv: " + linea);
                    continue;
                }
                try {
                    String doc = partes[0].trim();
                    String nombre = partes[1].trim();
                    String tel = partes[2].trim();
                    String dir = partes[3].trim();
                    boolean prestado = Boolean.parseBoolean(partes[4].trim());
                    int multaPendiente = Integer.parseInt(partes[5].trim());

                    Cliente cliente = new Cliente(doc, nombre, tel, dir);
                    cliente.setPrestado(prestado);
                    cliente.setMultaPendiente(multaPendiente);
                    controladorCliente.cargarCliente(cliente);
                } catch (Exception e) {
                    System.out.println("Error al procesar linea en clientes.csv: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer clientes.csv: " + e.getMessage());
        }
    }

    private void cargarPrestamos() {
        File archivo = new File(dataDir, "prestamos.csv");
        if (!archivo.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";", -1);
                if (partes.length < 6) {
                    System.out.println("Linea mal formada en prestamos.csv: " + linea);
                    continue;
                }
                try {
                    int idLibro = Integer.parseInt(partes[0].trim());
                    String docCliente = partes[1].trim();

                    Libro libro = controladorLibros.buscarLibro(idLibro);
                    Cliente cliente = controladorCliente.buscarCliente(docCliente);
                    if (libro == null || cliente == null) {
                        System.out.println("Libro o cliente inexistente en prestamos.csv: " + linea);
                        continue;
                    }

                    LocalDate fechaPrestamo = LocalDate.parse(partes[2].trim());
                    LocalDate fechaLimite = LocalDate.parse(partes[3].trim());
                    String devStr = partes[4].trim();
                    LocalDate fechaDevolucion = devStr.isEmpty() ? null : LocalDate.parse(devStr);
                    int multa = Integer.parseInt(partes[5].trim());

                    Prestamo prestamo = new Prestamo(libro, cliente, fechaPrestamo, fechaLimite, fechaDevolucion, multa);
                    controladorPrestamo.cargarPrestamo(prestamo);
                } catch (Exception e) {
                    System.out.println("Error al procesar linea en prestamos.csv: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer prestamos.csv: " + e.getMessage());
        }
    }

    private void cargarReservas() {
        File archivo = new File(dataDir, "reservas.csv");
        if (!archivo.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";", -1);
                if (partes.length < 2) {
                    System.out.println("Linea mal formada en reservas.csv: " + linea);
                    continue;
                }
                try {
                    int idLibro = Integer.parseInt(partes[0].trim());
                    String docCliente = partes[1].trim();

                    Libro libro = controladorLibros.buscarLibro(idLibro);
                    Cliente cliente = controladorCliente.buscarCliente(docCliente);
                    if (libro == null || cliente == null) {
                        System.out.println("Libro o cliente inexistente en reservas.csv: " + linea);
                        continue;
                    }

                    libro.getReservas().add(cliente);
                } catch (Exception e) {
                    System.out.println("Error al procesar linea en reservas.csv: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer reservas.csv: " + e.getMessage());
        }
    }

    private Categoria buscarCategoria(String nombre) {
        for (Categoria c : Categoria.TODAS) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    private EstadoLibro buscarEstado(String nombre) {
        EstadoLibro[] estados = {EstadoLibro.DISPONIBLE, EstadoLibro.PRESTADO, EstadoLibro.RETIRADO};
        for (EstadoLibro e : estados) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                return e;
            }
        }
        return null;
    }
}
