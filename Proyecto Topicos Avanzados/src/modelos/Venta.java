package modelos;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int idVenta;
    private String fecha;
    private double total;
    private List<DetalleVenta> detalles = new ArrayList<>();
    
    public Venta() {}
    
    public Venta(int idVenta, String fecha, double total) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
    }
    
    // Getters y Setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
    
    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
        this.total += detalle.getSubtotal();
    }
}