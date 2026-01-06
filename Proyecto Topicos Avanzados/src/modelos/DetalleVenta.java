package modelos;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    private double subtotal;
    
    public DetalleVenta() {}
    
    public DetalleVenta(int idDetalle, int idVenta, int idProducto, String nombreProducto, int cantidad, double subtotal) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    
    public DetalleVenta(Producto producto, int cantidad, double precioUnitario) {
        this.idProducto = producto.getIdProducto();
        this.nombreProducto = producto.getNombre();
        this.cantidad = cantidad;
        this.subtotal = cantidad * precioUnitario;
    }
    
    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
