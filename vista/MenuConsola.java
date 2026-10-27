package vista;

import java.time.Year;
import java.util.NoSuchElementException;
import java.util.Scanner;
import controlador.ControladorCliente;
import controlador.ControladorLibros;
import controlador.ControladorPrestamo;
import controlador.ControladorReporte;
import controlador.Persistencia;
import modelo.Categoria;
import modelo.Cliente;
import modelo.EstadoLibro;
import modelo.Libro;

public class MenuConsola {
    private final Scanner scanner = new Scanner(System.in);
    private final ControladorLibros controladorLibros;
    private final ControladorCliente controladorCliente;
    private final ControladorPrestamo controladorPrestamo;
    private final ControladorReporte controladorReporte;
    private final Persistencia persistencia;
    private boolean datosEjemploCargados = false;

    public MenuConsola(ControladorLibros controladorLibros, ControladorCliente controladorCliente,
                       ControladorPrestamo controladorPrestamo, ControladorReporte controladorReporte,
                       Persistencia persistencia) {
        this.controladorLibros = controladorLibros;
        this.controladorCliente = controladorCliente;
        this.controladorPrestamo = controladorPrestamo;
        this.controladorReporte = controladorReporte;
        this.persistencia = persistencia;
    }

    public void iniciar() {
        try {
            int opcion;
            do {
                mostrarMenuPrincipal();
                opcion = leerEntero("Seleccione una opcion: ");
                switch (opcion) {
                    case 1:
                        menuLibros();
                        break;
                    case 2:
                        menuClientes();
                        break;
                    case 3:
                        menuPrestamos();
                        break;
                    case 4:
                        cargarDatosEjemplo();
                        break;
                    case 5:
                        controladorReporte.mostrarReporteGeneral();
                        break;
                    case 0:
                        persistencia.guardar();
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } while (opcion != 0);
        } catch (NoSuchElementException e) {
            System.out.println("\nSe cerro la entrada de datos. Fin del programa.");
        }
    }

    // ---------------------------------------------------------------- Menus

    private void mostrarMenuPrincipal() {
        System.out.println("\n===== BIBLIOTECA LIBROSYMAS =====");
        System.out.println("1. Gestion de libros");
        System.out.println("2. Gestion de clientes");
        System.out.println("3. Prestamos y devoluciones");
        System.out.println("4. Cargar datos de ejemplo");
        System.out.println("5. Reporte general");
        System.out.println("0. Salir");
    }

    private void menuLibros() {
        int opcion;
        do {
            System.out.println("\n--- LIBROS ---");
            System.out.println("1. Agregar libro");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Listar libros disponibles");
            System.out.println("4. Listar libros prestados");
            System.out.println("5. Ver informacion de un libro");
            System.out.println("6. Actualizar libro");
            System.out.println("7. Cambiar estado (disponible / retirado)");
            System.out.println("8. Eliminar libro");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    agregarLibro();
                    break;
                case 2:
                    controladorLibros.mostrarLibros(controladorLibros.obtenerTodosLosLibros());
                    break;
                case 3:
                    controladorLibros.mostrarLibros(controladorLibros.obtenerLibrosDisponibles());
                    break;
                case 4:
                    controladorLibros.mostrarLibros(controladorLibros.obtenerLibrosPrestados());
                    break;
                case 5:
                    controladorLibros.mostarInformacionLibro(leerEntero("ID del libro: "));
                    break;
                case 6:
                    actualizarLibro();
                    break;
                case 7:
                    cambiarEstadoLibro();
                    break;
                case 8:
                    eliminarLibro();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
            persistencia.guardar();
        } while (opcion != 0);
    }

    private void menuClientes() {
        int opcion;
        do {
            System.out.println("\n--- CLIENTES ---");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por documento");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    controladorCliente.mostrarClientes();
                    break;
                case 3:
                    buscarCliente();
                    break;
                case 4:
                    actualizarCliente();
                    break;
                case 5:
                    eliminarCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
            persistencia.guardar();
        } while (opcion != 0);
    }

    private void menuPrestamos() {
        int opcion;
        do {
            System.out.println("\n--- PRESTAMOS Y DEVOLUCIONES ---");
            System.out.println("1. Registrar prestamo");
            System.out.println("2. Registrar devolucion");
            System.out.println("3. Ver prestamos activos (libros pendientes por devolver)");
            System.out.println("4. Ver historial de prestamos");
            System.out.println("5. Ver multas de un cliente");
            System.out.println("6. Registrar reserva");
            System.out.println("7. Consultar reservas de un libro");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    int idLibro = leerEntero("ID del libro: ");
                    String documento = leerTexto("Documento del cliente: ");
                    controladorPrestamo.registrarPrestamo(idLibro, documento);
                    break;
                case 2:
                    controladorPrestamo.registrarDevolucion(leerEntero("ID del libro que se devuelve: "));
                    break;
                case 3:
                    controladorPrestamo.mostrarPrestamosActivos();
                    break;
                case 4:
                    controladorPrestamo.mostrarHistorial();
                    break;
                case 5:
                    String docMultas = leerTexto("Documento del cliente: ");
                    controladorCliente.mostrarMultasCliente(docMultas);
                    break;
                case 6:
                    int idReserva = leerEntero("ID del libro: ");
                    String docReserva = leerTexto("Documento del cliente: ");
                    controladorPrestamo.registrarReserva(idReserva, docReserva);
                    break;
                case 7:
                    int idConsulta = leerEntero("ID del libro: ");
                    controladorPrestamo.mostrarReservasLibro(idConsulta);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
            persistencia.guardar();
        } while (opcion != 0);
    }

    // ------------------------------------------------------ Acciones: libros

    private void agregarLibro() {
        int id = leerEntero("ID unico del ejemplar: ");
        String titulo = leerTexto("Titulo: ");
        String autor = leerTexto("Autor: ");
        String editorial = leerTexto("Editorial: ");
        int anio = leerAnio();
        Categoria categoria = leerCategoria();
        if (controladorLibros.agregarLibro(new Libro(id, titulo, autor, editorial, anio, categoria))) {
            System.out.println("Libro agregado correctamente.");
        }
    }

    private void actualizarLibro() {
        int id = leerEntero("ID del libro a actualizar: ");
        if (controladorLibros.buscarLibro(id) == null) {
            System.out.println("El libro con ID " + id + " no existe.");
            return;
        }
        String titulo = leerTexto("Nuevo titulo: ");
        String autor = leerTexto("Nuevo autor: ");
        String editorial = leerTexto("Nueva editorial: ");
        int anio = leerAnio();
        Categoria categoria = leerCategoria();
        if (controladorLibros.actualizarLibro(id, titulo, autor, editorial, anio, categoria)) {
            System.out.println("Libro actualizado correctamente.");
        }
    }

    private void cambiarEstadoLibro() {
        int id = leerEntero("ID del libro: ");
        System.out.println("1. Disponible");
        System.out.println("2. Retirado (dano o decision administrativa)");
        int opcion = leerEntero("Nuevo estado: ");
        if (opcion == 1) {
            controladorLibros.cambiarEstadoLibro(id, EstadoLibro.DISPONIBLE);
        } else if (opcion == 2) {
            controladorLibros.cambiarEstadoLibro(id, EstadoLibro.RETIRADO);
        } else {
            System.out.println("Opcion no valida.");
        }
    }

    private void eliminarLibro() {
        if (controladorLibros.eliminarLibro(leerEntero("ID del libro a eliminar: "))) {
            System.out.println("Libro eliminado correctamente.");
        }
    }

    // ---------------------------------------------------- Acciones: clientes

    private void registrarCliente() {
        String documento = leerTexto("Documento de identidad: ");
        String nombre = leerTexto("Nombre completo: ");
        String telefono = leerTelefono();
        String direccion = leerTexto("Direccion: ");
        controladorCliente.crearCliente(new Cliente(documento, nombre, telefono, direccion));
    }

    private void buscarCliente() {
        Cliente c = controladorCliente.buscarCliente(leerTexto("Documento del cliente: "));
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("Documento: " + c.getDocumento());
        System.out.println("Nombre: " + c.getNombre());
        System.out.println("Telefono: " + c.getTelefono());
        System.out.println("Direccion: " + c.getDireccion());
        System.out.println("Libro prestado: " + (c.isPrestado() ? "Si" : "No"));
    }

    private void actualizarCliente() {
        String documento = leerTexto("Documento del cliente a actualizar: ");
        if (controladorCliente.buscarCliente(documento) == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        String nombre = leerTexto("Nuevo nombre completo: ");
        String telefono = leerTelefono();
        String direccion = leerTexto("Nueva direccion: ");
        controladorCliente.actualizarCliente(documento, nombre, telefono, direccion);
        System.out.println("Cliente actualizado correctamente.");
    }

    private void eliminarCliente() {
        String documento = leerTexto("Documento del cliente a eliminar: ");
        if (controladorCliente.buscarCliente(documento) == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        if (controladorCliente.eliminarCliente(documento)) {
            controladorPrestamo.quitarClienteDeReservas(documento);
            System.out.println("Cliente eliminado correctamente.");
        }
    }

    // ------------------------------------------------------------ Datos demo

    private void cargarDatosEjemplo() {
        if (datosEjemploCargados) {
            System.out.println("Los datos de ejemplo ya fueron cargados.");
            return;
        }
        // Los ids 1 y 2 son dos ejemplares del mismo titulo
        controladorLibros.agregarLibro(new Libro(1, "Cien anos de soledad", "Gabriel Garcia Marquez", "Sudamericana", 1967, Categoria.LITERATURA));
        controladorLibros.agregarLibro(new Libro(2, "Cien anos de soledad", "Gabriel Garcia Marquez", "Sudamericana", 1967, Categoria.LITERATURA));
        controladorLibros.agregarLibro(new Libro(3, "Breve historia del tiempo", "Stephen Hawking", "Critica", 1988, Categoria.CIENCIA));
        controladorLibros.agregarLibro(new Libro(4, "Sapiens", "Yuval Noah Harari", "Debate", 2011, Categoria.HISTORIA));
        controladorLibros.agregarLibro(new Libro(5, "Clean Code", "Robert C. Martin", "Prentice Hall", 2008, Categoria.TECNOLOGIA));
        controladorCliente.crearCliente(new Cliente("1094000001", "Ana Maria Lopez", "3101234567", "Calle 21 # 14-30, Armenia"));
        controladorCliente.crearCliente(new Cliente("1094000002", "Carlos Perez", "3207654321", "Carrera 14 # 8-12, Armenia"));
        datosEjemploCargados = true;
        persistencia.guardar();
        System.out.println("Datos de ejemplo cargados: 5 libros y 2 clientes.");
    }

    // ------------------------------------------------- Lectura de datos segura

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("Este campo no puede estar vacio.");
                continue;
            }
            if (linea.contains(";")) {
                System.out.println("No se permite el caracter ';'.");
                continue;
            }
            return linea;
        }
    }

    private String leerTelefono() {
        while (true) {
            String telefono = leerTexto("Telefono: ");
            if (telefono.matches("\\d{7,10}")) {
                return telefono;
            }
            System.out.println("El telefono debe tener solo numeros (entre 7 y 10 digitos).");
        }
    }

    private int leerAnio() {
        int actual = Year.now().getValue();
        while (true) {
            int anio = leerEntero("Anio de publicacion: ");
            if (anio >= 1000 && anio <= actual) {
                return anio;
            }
            System.out.println("Ingrese un anio entre 1000 y " + actual + ".");
        }
    }

    private Categoria leerCategoria() {
        Categoria[] categorias = Categoria.TODAS;
        while (true) {
            System.out.println("Categorias:");
            for (int i = 0; i < categorias.length; i++) {
                System.out.println("  " + (i + 1) + ". " + categorias[i]);
            }
            int opcion = leerEntero("Seleccione la categoria: ");
            if (opcion >= 1 && opcion <= categorias.length) {
                return categorias[opcion - 1];
            }
            System.out.println("Categoria no valida.");
        }
    }
}
