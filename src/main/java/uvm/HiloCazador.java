package uvm;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class HiloCazador extends Thread {
    private int posX;
    private int posY;
    private int velocidad;
    private boolean enEjecucion;
    private boolean pausado;
    private JLabel etiquetaInterfaz;

    // Constructor para inicializar los valores
    public HiloCazador(int posXInicial, int posYInicial, int velocidad, JLabel etiquetaInterfaz) {
        this.posX = posXInicial;
        this.posY = posYInicial;
        this.velocidad = velocidad;
        this.etiquetaInterfaz = etiquetaInterfaz;
        this.enEjecucion = true;
        this.pausado = false;
    }

    // Método para pausar o reanudar el hilo
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
        if (!pausado) {
            synchronized (this) {
                notify(); // Despierta al hilo si estaba en espera
            }
        }
    }

    // Método para detener por completo el hilo
    public void detenerHilo() {
        this.enEjecucion = false;
        setPausado(false); // Lo despierta para que pueda salir del ciclo
    }

    @Override
    public void run() {
        try {
            while (enEjecucion) {
                synchronized (this) {
                    while (pausado) {
                        wait(); // El hilo entra en pausa
                    }
                }

                if (!enEjecucion) break;

                // Lógica del cazador (mover coordenadas, etc.)
                posX += 2;
                posY += 1;

                if (etiquetaInterfaz != null) {
                    SwingUtilities.invokeLater(() -> {
                        etiquetaInterfaz.setText("Cazador -> X: " + posX + ", Y: " + posY);
                    });
                }

                // Controlar la velocidad del hilo
                Thread.sleep(velocidad);
            }
        } catch (InterruptedException e) {
            System.out.println("El hilo cazador fue interrumpido.");
        }
    }
}