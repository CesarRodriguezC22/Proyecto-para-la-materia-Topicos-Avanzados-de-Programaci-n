package modelos;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private int stock;
    private int idCategoria;
    private String nombreCategoria;
    
    public Producto() {}
    
    public Producto(int idProducto, String nombre, double precio, int stock, int idCategoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }
    
    public Producto(int idProducto, String nombre, double precio, int stock, int idCategoria, String nombreCategoria) {
        this(idProducto, nombre, precio, stock, idCategoria);
        this.nombreCategoria = nombreCategoria;
    }
    
    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    
    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}
