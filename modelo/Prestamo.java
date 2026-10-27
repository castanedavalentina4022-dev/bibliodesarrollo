package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    public static final int PLAZO_DIAS = 7;
    public static final int MULTA_POR_DIA = 1000;

    private Libro libro;
    private Cliente cliente;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private int multa;

    public Prestamo(Libro libro, Cliente cliente) {
        this.libro = libro;
        this.cliente = cliente;
        this.fechaPrestamo = LocalDate.now();
        this.fechaLimite = this.fechaPrestamo.plusDays(PLAZO_DIAS);
        this.fechaDevolucion = null;
        this.multa = 0;
    }

    // Usado SOLO para cargar desde archivos
    public Prestamo(Libro libro, Cliente cliente, LocalDate fechaPrestamo, LocalDate fechaLimite, LocalDate fechaDevolucion, int multa) {
        this.libro = libro;
        this.cliente = cliente;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.multa = multa;
    }

    public Libro getLibro() {
        return libro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public int getMulta() {
        return multa;
    }

    public int getDiasRetraso() {
        if (fechaDevolucion == null || !fechaDevolucion.isAfter(fechaLimite)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public void registrarDevolucion() {
        fechaDevolucion = LocalDate.now();
        multa = getDiasRetraso() * MULTA_POR_DIA;
    }
}
