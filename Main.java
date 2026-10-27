import controlador.ControladorCliente;
import controlador.ControladorLibros;
import controlador.ControladorPrestamo;
import controlador.ControladorReporte;
import controlador.Persistencia;
import vista.MenuConsola;

public class Main {
    public static void main(String[] args) {
        ControladorLibros controladorLibros = new ControladorLibros();
        ControladorCliente controladorCliente = new ControladorCliente();
        ControladorPrestamo controladorPrestamo = new ControladorPrestamo(controladorLibros, controladorCliente);
        ControladorReporte controladorReporte = new ControladorReporte(controladorLibros, controladorCliente, controladorPrestamo);
        Persistencia persistencia = new Persistencia(controladorLibros, controladorCliente, controladorPrestamo);

        persistencia.cargar();

        new MenuConsola(controladorLibros, controladorCliente, controladorPrestamo, controladorReporte, persistencia).iniciar();
    }
}
