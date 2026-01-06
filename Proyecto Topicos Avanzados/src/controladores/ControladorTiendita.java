package controladores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import modelos.*;

public class ControladorTiendita extends JFrame {
    private ControladorCategoria controladorCategoria;
    private ControladorProducto controladorProducto;
    private ControladorVenta controladorVenta;

    private JTabbedPane tabbedPane;
    private JTable tablaCategorias, tablaProductos, tablaVentas, tablaDetalleVenta;
    private JTable tablaProductosDisponibles, tablaProductosVenta;
    private DefaultTableModel modeloCategorias, modeloProductos, modeloVentas, modeloDetalleVenta;
    private DefaultTableModel modeloProductosDisponibles, modeloProductosVenta;
    private JTextField txtIdCategoria, txtNombreCategoria;
    private JTextField txtIdProducto, txtNombreProducto, txtPrecioProducto, txtStockProducto;
    private JTextField txtIdVenta, txtFechaVenta, txtTotalVenta;
    private JTextField txtCantidad, txtTotalNuevaVenta;
    private JComboBox<String> comboCategorias, comboFiltroCategoria;

    public ControladorTiendita() {
        super("Sistema de Tiendita");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        try {
            controladorCategoria = new ControladorCategoria();
            controladorProducto = new ControladorProducto();
            controladorVenta = new ControladorVenta();
        } catch (Exception e) {
            mostrarError("Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        initUI();
        cargarDatosIniciales();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Categorías", crearPanelCategorias());
        tabbedPane.addTab("Productos", crearPanelProductos());
        tabbedPane.addTab("Ventas", crearPanelVentas());
        tabbedPane.addTab("Nueva Venta", crearPanelNuevaVenta());
        add(tabbedPane);
    }

    private JPanel crearPanelCategorias() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel formulario = new JPanel(new GridLayout(2, 2, 5, 5));
        txtIdCategoria = new JTextField();
        txtIdCategoria.setEditable(false);
        txtNombreCategoria = new JTextField();

        formulario.add(new JLabel("ID:"));
        formulario.add(txtIdCategoria);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombreCategoria);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");

        btnNuevo.addActionListener(e -> limpiarFormularioCategoria());
        btnGuardar.addActionListener(e -> guardarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Datos de Categoría"));

        modeloCategorias = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloCategorias);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarCategoria();
            }
        });

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCategorias), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));
        txtIdProducto = new JTextField();
        txtIdProducto.setEditable(false);
        txtNombreProducto = new JTextField();
        txtPrecioProducto = new JTextField();
        txtStockProducto = new JTextField();
        comboCategorias = new JComboBox<>();

        formulario.add(new JLabel("ID:"));
        formulario.add(txtIdProducto);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombreProducto);
        formulario.add(new JLabel("Precio:"));
        formulario.add(txtPrecioProducto);
        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStockProducto);
        formulario.add(new JLabel("Categoría:"));
        formulario.add(comboCategorias);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");

        btnNuevo.addActionListener(e -> limpiarFormularioProducto());
        btnGuardar.addActionListener(e -> guardarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Datos de Producto"));

        modeloProductos = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarProducto();
            }
        });

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        txtIdVenta = new JTextField();
        txtIdVenta.setEditable(false);
        txtFechaVenta = new JTextField();
        txtFechaVenta.setEditable(false);
        txtTotalVenta = new JTextField();
        txtTotalVenta.setEditable(false);

        formulario.add(new JLabel("ID Venta:"));
        formulario.add(txtIdVenta);
        formulario.add(new JLabel("Fecha:"));
        formulario.add(txtFechaVenta);
        formulario.add(new JLabel("Total:"));
        formulario.add(txtTotalVenta);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Detalles de Venta"));

        modeloVentas = new DefaultTableModel(new Object[]{"ID", "Fecha", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVentas = new JTable(modeloVentas);
        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetalleVenta();
            }
        });

        modeloDetalleVenta = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDetalleVenta = new JTable(modeloDetalleVenta);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(tablaVentas));
        splitPane.setBottomComponent(new JScrollPane(tablaDetalleVenta));
        splitPane.setDividerLocation(250);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelNuevaVenta() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        comboFiltroCategoria = new JComboBox<>();
        comboFiltroCategoria.addItem("Todas las categorías");
        comboFiltroCategoria.addActionListener(e -> filtrarProductos());

        txtCantidad = new JTextField("1", 5);
        JButton btnAgregar = new JButton("Agregar a la venta");
        btnAgregar.addActionListener(e -> agregarProductoAVenta());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Filtrar por categoría:"));
        panelFiltros.add(comboFiltroCategoria);
        panelFiltros.add(new JLabel("Cantidad:"));
        panelFiltros.add(txtCantidad);
        panelFiltros.add(btnAgregar);

        txtTotalNuevaVenta = new JTextField("0.00", 10);
        txtTotalNuevaVenta.setEditable(false);
        JButton btnFinalizar = new JButton("Finalizar Venta");
        btnFinalizar.addActionListener(e -> finalizarVenta());

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(new JLabel("Total: $"));
        panelTotal.add(txtTotalNuevaVenta);
        panelTotal.add(btnFinalizar);

        modeloProductosDisponibles = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductosDisponibles = new JTable(modeloProductosDisponibles);
        tablaProductosDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        modeloProductosVenta = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductosVenta = new JTable(modeloProductosVenta);

        JButton btnQuitar = new JButton("Quitar seleccionado");
        btnQuitar.addActionListener(e -> quitarProductoDeVenta());

        JPanel panelTablaVenta = new JPanel(new BorderLayout());
        panelTablaVenta.add(new JScrollPane(tablaProductosVenta), BorderLayout.CENTER);
        panelTablaVenta.add(btnQuitar, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(tablaProductosDisponibles));
        splitPane.setBottomComponent(panelTablaVenta);
        splitPane.setDividerLocation(300);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFiltros, BorderLayout.CENTER);
        panelSuperior.add(panelTotal, BorderLayout.SOUTH);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatosIniciales() {
        cargarCategorias();
        cargarProductos();
        cargarVentas();
    }

    private void cargarCategorias() {
        try {
            List<Categoria> categorias = controladorCategoria.listarCategorias();
            modeloCategorias.setRowCount(0);
            comboCategorias.removeAllItems();
            comboFiltroCategoria.removeAllItems();
            comboFiltroCategoria.addItem("Todas las categorías");
            for (Categoria c : categorias) {
                modeloCategorias.addRow(new Object[]{c.getIdCategoria(), c.getNombre()});
                comboCategorias.addItem(c.getNombre());
                comboFiltroCategoria.addItem(c.getNombre());
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar categorías: " + e.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = controladorProducto.listarProductos();
            modeloProductos.setRowCount(0);
            modeloProductosDisponibles.setRowCount(0);
            for (Producto p : productos) {
                modeloProductos.addRow(new Object[]{
                    p.getIdProducto(), p.getNombre(),
                    p.getPrecio(), p.getStock(),
                    p.getNombreCategoria()
                });
                modeloProductosDisponibles.addRow(new Object[]{
                    p.getIdProducto(), p.getNombre(),
                    p.getPrecio(), p.getStock(),
                    p.getNombreCategoria()
                });
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }

    private void cargarVentas() {
        try {
            List<Venta> ventas = controladorVenta.listarVentas();
            modeloVentas.setRowCount(0);
            for (Venta v : ventas) {
                modeloVentas.addRow(new Object[]{
                    v.getIdVenta(),
                    v.getFecha(),
                    v.getTotal()
                });
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar ventas: " + e.getMessage());
        }
    }

    private void seleccionarCategoria() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila >= 0) {
            txtIdCategoria.setText(modeloCategorias.getValueAt(fila, 0).toString());
            txtNombreCategoria.setText(modeloCategorias.getValueAt(fila, 1).toString());
        }
    }

    private void limpiarFormularioCategoria() {
        txtIdCategoria.setText("");
        txtNombreCategoria.setText("");
        tablaCategorias.clearSelection();
    }

    private void guardarCategoria() {
        String nombre = txtNombreCategoria.getText().trim();
        if (nombre.isEmpty()) {
            mostrarError("El nombre de la categoría no puede estar vacío.");
            return;
        }

        try {
            if (txtIdCategoria.getText().isEmpty()) {
                controladorCategoria.insertarCategoria(new Categoria(0, nombre));
                mostrarMensaje("Categoría agregada correctamente.");
            } else {
                int id = Integer.parseInt(txtIdCategoria.getText());
                controladorCategoria.actualizarCategoria(new Categoria(id, nombre));
                mostrarMensaje("Categoría actualizada correctamente.");
            }
            limpiarFormularioCategoria();
            cargarCategorias();
        } catch (SQLException e) {
            mostrarError("Error al guardar categoría: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        if (txtIdCategoria.getText().isEmpty()) {
            mostrarError("Seleccione una categoría para eliminar.");
            return;
        }
        int id = Integer.parseInt(txtIdCategoria.getText());

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la categoría seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controladorCategoria.eliminarCategoria(id);
                mostrarMensaje("Categoría eliminada correctamente.");
                limpiarFormularioCategoria();
                cargarCategorias();
            } catch (SQLException e) {
                mostrarError("Error al eliminar categoría: " + e.getMessage());
            }
        }
    }

    private void seleccionarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
            txtIdProducto.setText(modeloProductos.getValueAt(fila, 0).toString());
            txtNombreProducto.setText(modeloProductos.getValueAt(fila, 1).toString());
            txtPrecioProducto.setText(modeloProductos.getValueAt(fila, 2).toString());
            txtStockProducto.setText(modeloProductos.getValueAt(fila, 3).toString());
            comboCategorias.setSelectedItem(modeloProductos.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarFormularioProducto() {
        txtIdProducto.setText("");
        txtNombreProducto.setText("");
        txtPrecioProducto.setText("");
        txtStockProducto.setText("");
        comboCategorias.setSelectedIndex(-1);
        tablaProductos.clearSelection();
    }

    private void guardarProducto() {
        String nombre = txtNombreProducto.getText().trim();
        String precioStr = txtPrecioProducto.getText().trim();
        String stockStr = txtStockProducto.getText().trim();
        String categoriaNombre = (String) comboCategorias.getSelectedItem();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || categoriaNombre == null) {
            mostrarError("Por favor, complete todos los campos.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            Categoria categoria = controladorCategoria.buscarCategoriaPorNombre(categoriaNombre);
            if (categoria == null) {
                mostrarError("La categoría seleccionada no existe.");
                return;
            }

            if (txtIdProducto.getText().isEmpty()) {
                Producto nuevo = new Producto(0, nombre, precio, stock, categoria.getIdCategoria());
                controladorProducto.insertarProducto(nuevo);
                mostrarMensaje("Producto agregado correctamente.");
            } else {
                int id = Integer.parseInt(txtIdProducto.getText());
                        Producto actualizado = new Producto(id, nombre, precio, stock, categoria.getIdCategoria());
                controladorProducto.actualizarProducto(actualizado);
                mostrarMensaje("Producto actualizado correctamente.");
            }
            limpiarFormularioProducto();
            cargarProductos();
        } catch (NumberFormatException e) {
            mostrarError("Precio y stock deben ser valores numéricos.");
        } catch (SQLException e) {
            mostrarError("Error al guardar producto: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        if (txtIdProducto.getText().isEmpty()) {
            mostrarError("Seleccione un producto para eliminar.");
            return;
        }
        int id = Integer.parseInt(txtIdProducto.getText());

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controladorProducto.eliminarProducto(id);
                mostrarMensaje("Producto eliminado correctamente.");
                limpiarFormularioProducto();
                cargarProductos();
            } catch (SQLException e) {
                mostrarError("Error al eliminar producto: " + e.getMessage());
            }
        }
    }

    private void cargarDetalleVenta() {
        int fila = tablaVentas.getSelectedRow();
        if (fila >= 0) {
            int idVenta = (int) modeloVentas.getValueAt(fila, 0);
            txtIdVenta.setText(String.valueOf(idVenta));
            txtFechaVenta.setText(modeloVentas.getValueAt(fila, 1).toString());
            txtTotalVenta.setText(modeloVentas.getValueAt(fila, 2).toString());

            try {
                List<DetalleVenta> detalles = controladorVenta.obtenerDetallesVenta(idVenta);
                modeloDetalleVenta.setRowCount(0);
                for (DetalleVenta dv : detalles) {
                    modeloDetalleVenta.addRow(new Object[]{
                        dv.getNombreProducto(),
                        dv.getCantidad(),
                        dv.getSubtotal() / dv.getCantidad(),
                        dv.getSubtotal()
                    });
                }
            } catch (SQLException e) {
                mostrarError("Error al cargar detalles de venta: " + e.getMessage());
            }
        }
    }

    private void filtrarProductos() {
        String categoriaSeleccionada = (String) comboFiltroCategoria.getSelectedItem();
        try {
            List<Producto> productos = controladorProducto.listarProductos();
            modeloProductosDisponibles.setRowCount(0);
            for (Producto p : productos) {
                if (categoriaSeleccionada.equals("Todas las categorías") || 
                    p.getNombreCategoria().equals(categoriaSeleccionada)) {
                    modeloProductosDisponibles.addRow(new Object[]{
                        p.getIdProducto(), p.getNombre(),
                        p.getPrecio(), p.getStock(),
                        p.getNombreCategoria()
                    });
                }
            }
        } catch (SQLException e) {
            mostrarError("Error al filtrar productos: " + e.getMessage());
        }
    }

    private void agregarProductoAVenta() {
        int fila = tablaProductosDisponibles.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un producto para agregar.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor que cero.");
                return;
            }

            int idProducto = (int) modeloProductosDisponibles.getValueAt(fila, 0);
            int stock = (int) modeloProductosDisponibles.getValueAt(fila, 3);
            if (cantidad > stock) {
                mostrarError("No hay suficiente stock disponible.");
                return;
            }

            String nombreProducto = (String) modeloProductosDisponibles.getValueAt(fila, 1);
            double precio = (double) modeloProductosDisponibles.getValueAt(fila, 2);
            double subtotal = cantidad * precio;

            // Verificar si el producto ya está en la venta
            for (int i = 0; i < modeloProductosVenta.getRowCount(); i++) {
                if (modeloProductosVenta.getValueAt(i, 0).equals(nombreProducto)) {
                    int nuevaCantidad = (int) modeloProductosVenta.getValueAt(i, 1) + cantidad;
                    double nuevoSubtotal = nuevaCantidad * precio;
                    modeloProductosVenta.setValueAt(nuevaCantidad, i, 1);
                    modeloProductosVenta.setValueAt(nuevoSubtotal, i, 3);
                    actualizarTotalVenta();
                    return;
                }
            }

            // Si no está, agregarlo
            modeloProductosVenta.addRow(new Object[]{
                nombreProducto,
                cantidad,
                precio,
                subtotal
            });

            actualizarTotalVenta();
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número válido.");
        }
    }

    private void quitarProductoDeVenta() {
        int fila = tablaProductosVenta.getSelectedRow();
        if (fila >= 0) {
            modeloProductosVenta.removeRow(fila);
            actualizarTotalVenta();
        } else {
            mostrarError("Seleccione un producto para quitar.");
        }
    }

    private void actualizarTotalVenta() {
        double total = 0;
        for (int i = 0; i < modeloProductosVenta.getRowCount(); i++) {
            total += (double) modeloProductosVenta.getValueAt(i, 3);
        }
        txtTotalNuevaVenta.setText(String.format("%.2f", total));
    }

    private void finalizarVenta() {
        if (modeloProductosVenta.getRowCount() == 0) {
            mostrarError("No hay productos en la venta.");
            return;
        }

        try {
            // Crear la venta
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = sdf.format(new Date());
            double total = Double.parseDouble(txtTotalNuevaVenta.getText());
            
            Venta venta = new Venta(0, fecha, total);
            int idVenta = controladorVenta.insertarVenta(venta);
            
            if (idVenta == -1) {
                throw new SQLException("No se pudo obtener el ID de la venta");
            }

            // Crear los detalles de venta
            for (int i = 0; i < modeloProductosVenta.getRowCount(); i++) {
                String nombreProducto = (String) modeloProductosVenta.getValueAt(i, 0);
                int cantidad = (int) modeloProductosVenta.getValueAt(i, 1);
                double precio = (double) modeloProductosVenta.getValueAt(i, 2);
                
                Producto producto = controladorProducto.buscarProductoPorNombre(nombreProducto);
                if (producto == null) {
                    throw new SQLException("Producto no encontrado: " + nombreProducto);
                }
                
                DetalleVenta detalle = new DetalleVenta(producto, cantidad, precio);
                detalle.setIdVenta(idVenta);
                controladorVenta.insertarDetalleVenta(detalle);
                
                // Actualizar el stock
                controladorProducto.actualizarStock(producto.getIdProducto(), cantidad);
            }

            mostrarMensaje("Venta registrada correctamente.");
            modeloProductosVenta.setRowCount(0);
            txtTotalNuevaVenta.setText("0.00");
            cargarProductos();
            cargarVentas();
        } catch (SQLException e) {
            mostrarError("Error al finalizar venta: " + e.getMessage());
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorTiendita app = new ControladorTiendita();
            app.setVisible(true);
        });
    }
}
