package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

public class ControladorCliente {
    private ArrayList<Cliente> clientes;

    public ControladorCliente() {
        this.clientes = new ArrayList<>();
    }

    public boolean crearCliente(Cliente nuevoCliente) {
        if (buscarCliente(nuevoCliente.getDocumento()) != null) {
            System.out.println("El cliente con documento " + nuevoCliente.getDocumento() + " ya existe.");
            return false;
        }
        clientes.add(nuevoCliente);
        System.out.println("Cliente agregado correctamente.");
        return true;
    }

    public void cargarCliente(Cliente cliente) {
        if (buscarCliente(cliente.getDocumento()) == null) {
            clientes.add(cliente);
        }
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    public Cliente buscarCliente(String documento) {
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(documento)) {
                return c;
            }
        }
        return null;
    }

    public boolean actualizarCliente(String documento, String nombre, String telefono, String direccion) {
        Cliente c = buscarCliente(documento);
        if (c == null) {
            return false;
        }
        c.setNombre(nombre);
        c.setTelefono(telefono);
        c.setDireccion(direccion);
        return true;
    }

    public boolean eliminarCliente(String documento) {
        Cliente c = buscarCliente(documento);
        if (c == null) {
            return false;
        }
        if (c.isPrestado()) {
            System.out.println("No se puede eliminar un cliente que tiene un libro prestado. Registre primero la devolucion.");
            return false;
        }
        clientes.remove(c);
        return true;
    }

    public void mostrarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Cliente c : clientes) {
                System.out.println("Documento: " + c.getDocumento() + " | Nombre: " + c.getNombre()
                        + " | Telefono: " + c.getTelefono() + " | Direccion: " + c.getDireccion()
                        + " | Libro prestado: " + (c.isPrestado() ? "Si" : "No"));
            }
        }
    }

    public void mostrarMultasCliente(String documento) {
        Cliente c = buscarCliente(documento);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("Nombre: " + c.getNombre() + " (" + c.getDocumento() + ")");
        System.out.println("Total de multas pendientes: $" + c.getMultaPendiente() + " COP");
    }
}
