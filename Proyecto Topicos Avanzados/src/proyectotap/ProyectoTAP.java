package proyectotap;

import controladores.ControladorTiendita;
import javax.swing.SwingUtilities;

public class ProyectoTAP {
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            ControladorTiendita tiendita = new ControladorTiendita();
            tiendita.setVisible(true); // Mostrar la ventana principal
        });
    }
}