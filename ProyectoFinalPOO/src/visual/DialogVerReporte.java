package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logico.Enfermedad;

public class DialogVerReporte extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private Map<?, Integer> datosReporte; 
    private String tituloReporte;
    private int tipoReporte; 

    private final Color COLOR_HEADER = new Color(10, 186, 181); 
    private final Color COLOR_FONDO = new Color(245, 247, 250); 
    private final Color COLOR_ITEM_BG = Color.WHITE;
    private final Color COLOR_BARRA = new Color(0, 168, 107); 
    
    private final Color[] COLORES_GRAFICOS = {
        new Color(0, 168, 107), 
        new Color(10, 186, 181), 
        new Color(255, 107, 107), 
        new Color(78, 205, 196), 
        new Color(255, 206, 86), 
        new Color(54, 162, 235), 
        new Color(153, 102, 255), 
        new Color(201, 203, 207)  
    };

    public DialogVerReporte(Map<?, Integer> datos, String titulo, int tipo) {
        this.datosReporte = datos;
        this.tituloReporte = titulo;
        this.tipoReporte = tipo;

        setTitle("Visualizador de Reportes");
        setBounds(100, 100, 800, 650); 
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel(titulo.toUpperCase());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        String fechaStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JLabel lblFecha = new JLabel(fechaStr);
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(new Color(230, 255, 250));
        headerPanel.add(lblFecha, BorderLayout.EAST);
        
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        generarListadoDetallado(); 
        
        tabbedPane.addTab("Listado Detallado", scrollPane);
        tabbedPane.addTab("Gráfico Circular", new PanelGraficoCircular());
        tabbedPane.addTab("Gráfico de Barras", new PanelGraficoBarras());

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(Color.WHITE);
        buttonPane.setBorder(new LineBorder(new Color(230, 230, 230), 1, false));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnDescargar = new JButton("Descargar PDF / TXT");
        btnDescargar.setBackground(new Color(50, 50, 50));
        btnDescargar.setForeground(Color.WHITE);
        btnDescargar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDescargar.setFocusPainted(false);
        btnDescargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarReporteEnCarpeta();
            }
        });
        buttonPane.add(btnDescargar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrar.setBackground(new Color(240, 240, 240));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCerrar);
    }

    private void generarListadoDetallado() {
        if (datosReporte == null || datosReporte.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay datos para mostrar.");
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            contentPanel.add(lblVacio);
            return;
        }

        ArrayList<Map.Entry<?, Integer>> lista = new ArrayList<>(datosReporte.entrySet());
        Collections.sort(lista, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int maxValor = 0;
        if (!lista.isEmpty()) maxValor = lista.get(0).getValue();

        for (Map.Entry<?, Integer> entrada : lista) {
            String nombre = (tipoReporte == 1) ? ((Enfermedad) entrada.getKey()).getNombre() : (String) entrada.getKey();
            int valor = entrada.getValue();

            JPanel pnlCard = new JPanel(new BorderLayout(10, 5));
            pnlCard.setBackground(COLOR_ITEM_BG);
            pnlCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 15, 10, 15)
            ));
            pnlCard.setMaximumSize(new Dimension(1000, 70));
            pnlCard.setAlignmentX(JPanel.CENTER_ALIGNMENT);

            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblNombre.setForeground(new Color(50, 50, 50));
            
            JLabel lblValor = new JLabel(valor + " Casos");
            lblValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblValor.setForeground(COLOR_HEADER);

            JPanel pnlTextos = new JPanel(new BorderLayout());
            pnlTextos.setBackground(COLOR_ITEM_BG);
            pnlTextos.add(lblNombre, BorderLayout.WEST);
            pnlTextos.add(lblValor, BorderLayout.EAST);
            pnlCard.add(pnlTextos, BorderLayout.NORTH);

            JProgressBar barra = new JProgressBar(0, maxValor);
            barra.setValue(valor);
            barra.setStringPainted(false);
            barra.setForeground(COLOR_BARRA);
            barra.setBackground(new Color(230, 230, 230));
            barra.setBorderPainted(false);
            barra.setPreferredSize(new Dimension(100, 8));
            pnlCard.add(barra, BorderLayout.CENTER);

            contentPanel.add(pnlCard);
            contentPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void guardarReporteEnCarpeta() {
        try {
            File carpeta = new File("Reportes");
            if (!carpeta.exists()) carpeta.mkdir();

            String fechaStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "Reporte_" + (tipoReporte == 1 ? "Enfermedades_" : "Vacunas_") + fechaStr + ".txt";
            File archivoDestino = new File(carpeta, nombreArchivo);

            PrintWriter escritor = new PrintWriter(new FileWriter(archivoDestino));
            escritor.println("==========================================");
            escritor.println("          DOC.JAVA - REPORTE CLINICO");
            escritor.println("==========================================");
            escritor.println("TITULO: " + tituloReporte.toUpperCase());
            escritor.println("FECHA: " + LocalDateTime.now());
            escritor.println("==========================================\n");
            
            for (Map.Entry<?, Integer> entrada : datosReporte.entrySet()) {
                String nombre = "";
                if (tipoReporte == 1) {
                    nombre = ((Enfermedad) entrada.getKey()).getNombre();
                } else {
                    nombre = (String) entrada.getKey();
                }
                escritor.printf("%-30s : %d\n", nombre, entrada.getValue());
            }
            escritor.close();

            JOptionPane.showMessageDialog(this, 
                    "Reporte guardado en: " + archivoDestino.getAbsolutePath(), 
                    "Guardado", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private class PanelGraficoCircular extends JPanel {
        private static final long serialVersionUID = 1L;

        public PanelGraficoCircular() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (datosReporte == null || datosReporte.isEmpty()) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int total = 0;
            for (Integer val : datosReporte.values()) total += val;

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height) - 100;
            int x = 50;
            int y = (height - diameter) / 2;

            int startAngle = 90;
            int colorIndex = 0;
            int leyendaY = 50;
            int leyendaX = x + diameter + 50;

            ArrayList<Map.Entry<?, Integer>> lista = new ArrayList<>(datosReporte.entrySet());
            Collections.sort(lista, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            for (Map.Entry<?, Integer> entrada : lista) {
                String nombre = (tipoReporte == 1) ? ((Enfermedad) entrada.getKey()).getNombre() : (String) entrada.getKey();
                int valor = entrada.getValue();
                int arcAngle = (int) Math.round((valor * 360.0) / total);

                g2d.setColor(COLORES_GRAFICOS[colorIndex % COLORES_GRAFICOS.length]);
                g2d.fillArc(x, y, diameter, diameter, startAngle, arcAngle);
                
                g2d.fillRect(leyendaX, leyendaY, 15, 15);
                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                
                double porcentaje = (valor * 100.0) / total;
                String texto = String.format("%s: %d (%.1f%%)", nombre, valor, porcentaje);
                g2d.drawString(texto, leyendaX + 25, leyendaY + 12);

                startAngle += arcAngle;
                colorIndex++;
                leyendaY += 25;
            }
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2d.drawString("TOTAL REGISTROS: " + total, leyendaX, leyendaY + 20);
        }
    }

    private class PanelGraficoBarras extends JPanel {
        private static final long serialVersionUID = 1L;

        public PanelGraficoBarras() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (datosReporte == null || datosReporte.isEmpty()) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ArrayList<Map.Entry<?, Integer>> lista = new ArrayList<>(datosReporte.entrySet());
            Collections.sort(lista, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); 

            int maxValor = 0;
            if (!lista.isEmpty()) maxValor = lista.get(0).getValue();
            if (maxValor == 0) maxValor = 1; 

            int paddingLeft = 50;
            int paddingBottom = 50;
            int paddingTop = 50;
            int width = getWidth() - 100;
            int height = getHeight() - 100;
            
            g2d.setColor(Color.GRAY);
            g2d.drawLine(paddingLeft, height + paddingTop, paddingLeft + width, height + paddingTop); 
            g2d.drawLine(paddingLeft, height + paddingTop, paddingLeft, paddingTop); 

            int barWidth = width / (lista.size() * 2); 
            int gap = barWidth; 
            int x = paddingLeft + gap/2;

            int colorIndex = 0;
            
            for (Map.Entry<?, Integer> entrada : lista) {
                String nombre = (tipoReporte == 1) ? ((Enfermedad) entrada.getKey()).getNombre() : (String) entrada.getKey();
                int valor = entrada.getValue();

                int barHeight = (int) ((double) valor / maxValor * height);
                int y = (height + paddingTop) - barHeight;

                g2d.setColor(COLORES_GRAFICOS[colorIndex % COLORES_GRAFICOS.length]);
                g2d.fillRect(x, y, barWidth, barHeight);
                
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRect(x, y, barWidth, barHeight);

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valorStr = String.valueOf(valor);
                int strWidth = g2d.getFontMetrics().stringWidth(valorStr);
                g2d.drawString(valorStr, x + (barWidth - strWidth) / 2, y - 5);

                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                if (nombre.length() > 8) nombre = nombre.substring(0, 6) + "..";
                
                strWidth = g2d.getFontMetrics().stringWidth(nombre);
                g2d.drawString(nombre, x + (barWidth - strWidth) / 2, height + paddingTop + 15);

                x += barWidth + gap;
                colorIndex++;
            }
        }
    }
}