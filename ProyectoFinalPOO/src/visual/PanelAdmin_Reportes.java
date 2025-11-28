package visual;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import logico.Consulta;
import logico.Enfermedad;
import logico.Medico;
import logico.Paciente;
import logico.SistemaGestion;

public class PanelAdmin_Reportes extends JPanel {

    private static final long serialVersionUID = 1L;
    private Color colorAccent;
    
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color[] COLORES_GRAFICOS = {
        new Color(0, 168, 107), new Color(10, 186, 181), new Color(255, 107, 107), 
        new Color(78, 205, 196), new Color(255, 206, 86), new Color(54, 162, 235), 
        new Color(153, 102, 255), new Color(201, 203, 207)
    };

    private JTabbedPane tabbedPane;

    public PanelAdmin_Reportes(Color accent) {
        this.colorAccent = accent;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("CENTRO DE REPORTES DE LA CLÍNICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        pnlHeader.add(lblTitulo, BorderLayout.WEST);
        
        JButton btnExportar = new JButton("Exportar Reporte PDF");
        btnExportar.setBackground(colorAccent);
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.addActionListener(e -> exportarPDF());
        pnlHeader.add(btnExportar, BorderLayout.EAST);
        
        add(pnlHeader, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        tabbedPane.addTab("Vigilancia Epidemiológica", new PanelVigilancia());
        tabbedPane.addTab("Demografía por Enfermedad", new PanelCircularInteractivo());
        tabbedPane.addTab("Tendencia Anual", new PanelLineasInteractivo());
        tabbedPane.addTab("Cobertura de Vacunación", new PanelVacunas());
        tabbedPane.addTab("Top 5 Enfermedades", new PanelTop5());

        tabbedPane.addTab("Productividad Médica", new PanelProductividad());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private class PanelProductividad extends JPanel {
        private static final long serialVersionUID = 1L;
        private Map<String, Integer> datosMedicos;
        private Map<Shape, String> tooltipsMap = new HashMap<>();

        public PanelProductividad() {
            setBackground(COLOR_FONDO);
            setLayout(new BorderLayout());
            setBorder(new LineBorder(new Color(230, 230, 230), 1));
            calcularDatos();
            setToolTipText("");
        }

        private void calcularDatos() {
            datosMedicos = new HashMap<>();
            for (Paciente p : SistemaGestion.getInstance().getListaPacientes()) {
                for (Consulta c : p.getHistorialConsultas()) {
                    if (c.getMedico() != null) {
                        String nombreDoc = "Dr. " + c.getMedico().getApellido();
                        datosMedicos.put(nombreDoc, datosMedicos.getOrDefault(nombreDoc, 0) + 1);
                    }
                }
            }
            
            for (Medico m : SistemaGestion.getInstance().getListaMedicos()) {
                String nombreDoc = "Dr. " + m.getApellido();
                if (!datosMedicos.containsKey(nombreDoc)) {
                    datosMedicos.put(nombreDoc, 0);
                }
            }
        }
        
        @Override
        public String getToolTipText(java.awt.event.MouseEvent event) {
            for (Map.Entry<Shape, String> entry : tooltipsMap.entrySet()) {
                if (entry.getKey().contains(event.getPoint())) return entry.getValue();
            } return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            tooltipsMap.clear();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (datosMedicos == null || datosMedicos.isEmpty()) {
                g2d.setColor(Color.GRAY);
                g2d.drawString("No hay datos de médicos.", 50, 100);
                return;
            }

            int w = getWidth(); int h = getHeight(); int pad = 80;
            g2d.setColor(Color.GRAY); g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(pad, h - pad, pad, pad);
            g2d.drawLine(pad, h - pad, w - pad, h - pad);

            int maxVal = datosMedicos.values().stream().max(Integer::compare).orElse(1);
            if (maxVal == 0) maxVal = 1;
            
            ArrayList<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(datosMedicos.entrySet());
            listaOrdenada.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            int barHeight = (h - 2 * pad) / (listaOrdenada.size() * 2);
            if (barHeight > 50) barHeight = 50;
            int y = pad + 20;
            int colorIdx = 0;

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.drawString("CONSULTAS POR MÉDICO", pad, pad - 40);

            for (Map.Entry<String, Integer> entry : listaOrdenada) {
                int val = entry.getValue();
                int barWidth = (int) ((double) val / maxVal * (w - 2 * pad - 50));
                
                Rectangle barRect = new Rectangle(pad, y, barWidth, barHeight);
                tooltipsMap.put(barRect, entry.getKey() + ": " + val + " consultas");
                
                g2d.setColor(COLORES_GRAFICOS[colorIdx % COLORES_GRAFICOS.length]);
                g2d.fillRect(pad, y, barWidth, barHeight);
                
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2d.drawString(String.valueOf(val), pad + barWidth + 10, y + barHeight/2 + 5);

                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String lbl = entry.getKey();
                int lblW = g2d.getFontMetrics().stringWidth(lbl);
                g2d.drawString(lbl, pad - lblW - 10, y + barHeight/2 + 5);

                y += barHeight + 30;
                colorIdx++;
            }
        }
    }

    private class PanelVigilancia extends JPanel {
        private static final long serialVersionUID = 1L;
        private Map<String, Integer> datosVigilancia;
        private Map<Shape, String> tooltipsMap = new HashMap<>();

        public PanelVigilancia() {
            setBackground(COLOR_FONDO); setLayout(new BorderLayout());
            setBorder(new LineBorder(new Color(230, 230, 230), 1));
            calcularDatos(); setToolTipText("");
        }

        private void calcularDatos() {
            datosVigilancia = new HashMap<>();
            for (Paciente p : SistemaGestion.getInstance().getListaPacientes()) {
                for (Consulta c : p.getHistorialConsultas()) {
                    if (c.getDiagnostico() != null && c.getDiagnostico().getEstaBajoVigilancia()) {
                        String nombre = c.getDiagnostico().getNombre();
                        datosVigilancia.put(nombre, datosVigilancia.getOrDefault(nombre, 0) + 1);
                    }
                }
            }
        }
        
        @Override
        public String getToolTipText(java.awt.event.MouseEvent event) {
            for (Map.Entry<Shape, String> entry : tooltipsMap.entrySet()) {
                if (entry.getKey().contains(event.getPoint())) return entry.getValue();
            } return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); tooltipsMap.clear();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (datosVigilancia.isEmpty()) {
                g2d.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                g2d.setColor(Color.GRAY);
                g2d.drawString("No hay casos bajo vigilancia registrados actualmente.", 50, 100);
                return;
            }

            int w = getWidth(); int h = getHeight(); int pad = 80;
            g2d.setColor(Color.GRAY); g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(pad, h - pad, w - pad, h - pad); g2d.drawLine(pad, h - pad, pad, pad);

            int max = datosVigilancia.values().stream().max(Integer::compare).orElse(1);
            int barWidth = (w - 2 * pad) / (datosVigilancia.size() * 2);
            if (barWidth > 80) barWidth = 80; int x = pad + 30;

            for (Map.Entry<String, Integer> entry : datosVigilancia.entrySet()) {
                int val = entry.getValue();
                int barHeight = (int) ((double) val / max * (h - 2 * pad));
                
                Rectangle barRect = new Rectangle(x, h - pad - barHeight, barWidth, barHeight);
                tooltipsMap.put(barRect, entry.getKey() + ": " + val + " casos");
                
                g2d.setColor(new Color(220, 53, 69)); 
                g2d.fillRect(x, h - pad - barHeight, barWidth, barHeight);
                
                g2d.setColor(Color.BLACK); g2d.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
                String valStr = String.valueOf(val);
                int valW = g2d.getFontMetrics().stringWidth(valStr);
                g2d.drawString(valStr, x + (barWidth - valW)/2, h - pad - barHeight - 5);

                g2d.setColor(Color.DARK_GRAY); 
                String lbl = entry.getKey(); if (lbl.length() > 10) lbl = lbl.substring(0, 8) + "..";
                int lblW = g2d.getFontMetrics().stringWidth(lbl);
                g2d.drawString(lbl, x + (barWidth - lblW)/2, h - pad + 20);
                x += barWidth + 50;
            }
        }
    }

    private class PanelCircularInteractivo extends JPanel {
        private static final long serialVersionUID = 1L;
        private JComboBox<Enfermedad> cbEnfermedades;
        private JPanel panelGrafico;
        private int countM = 0; private int countF = 0;

        public PanelCircularInteractivo() {
            setLayout(new BorderLayout()); setBackground(COLOR_FONDO);
            JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelControl.setBackground(Color.WHITE);
            panelControl.add(new JLabel("Filtrar:"));
            
            cbEnfermedades = new JComboBox<>();
            cbEnfermedades.setRenderer(new DefaultListCellRenderer() {
                private static final long serialVersionUID = 1L;
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if(value instanceof Enfermedad) setText(((Enfermedad)value).getNombre());
                    return this;
                }
            });

            for (Enfermedad enf : SistemaGestion.getInstance().getCatalogoEnfermedades()) cbEnfermedades.addItem(enf);
            cbEnfermedades.addActionListener(e -> { calcularDatos((Enfermedad)cbEnfermedades.getSelectedItem()); panelGrafico.repaint(); });
            panelControl.add(cbEnfermedades); add(panelControl, BorderLayout.NORTH);
            panelGrafico = new JPanel() { private static final long serialVersionUID = 1L; protected void paintComponent(Graphics g) { super.paintComponent(g); dibujarGrafico(g, getWidth(), getHeight()); }};
            panelGrafico.setBackground(COLOR_FONDO); add(panelGrafico, BorderLayout.CENTER);
            if(cbEnfermedades.getItemCount()>0){cbEnfermedades.setSelectedIndex(0); calcularDatos((Enfermedad)cbEnfermedades.getSelectedItem());}
        }

        private void calcularDatos(Enfermedad enf) {
            countM=0; countF=0; if(enf==null)return;
            for(Paciente p : SistemaGestion.getInstance().getListaPacientes())
                for(Consulta c : p.getHistorialConsultas())
                    if(c.getDiagnostico()!=null && c.getDiagnostico().getId().equals(enf.getId()))
                        if("M".equalsIgnoreCase(p.getSexo()) || "Masculino".equalsIgnoreCase(p.getSexo())) countM++; else countF++;
        }

        private void dibujarGrafico(Graphics g, int w, int h) {
            Graphics2D g2d = (Graphics2D)g; g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int total = countM+countF; if(total==0){g2d.setColor(Color.GRAY); g2d.drawString("Sin datos", w/2-30, h/2); return;}
            int d = Math.min(w,h)-150; int x=(w-d)/2-80, y=(h-d)/2;
            int angM = (int)Math.round((double)countM/total*360);
            g2d.setColor(new Color(54,162,235)); g2d.fillArc(x,y,d,d,90,angM);
            g2d.setColor(new Color(255,99,132)); g2d.fillArc(x,y,d,d,90+angM,360-angM);
            int legX = x + d + 50; int legY = h/2 - 40;
            g2d.setColor(new Color(54, 162, 235)); g2d.fillRect(legX, legY, 15, 15);
            g2d.setColor(Color.DARK_GRAY); g2d.setFont(new Font("Segoe UI", Font.BOLD, 14)); g2d.drawString("Masculino: "+countM, legX+30, legY+12);
            g2d.setColor(new Color(255, 99, 132)); g2d.fillRect(legX, legY+30, 15, 15);
            g2d.setColor(Color.DARK_GRAY); g2d.drawString("Femenino: "+countF, legX+30, legY+42);
            g2d.setColor(Color.BLACK); g2d.drawString("TOTAL: "+total, legX, legY+80);
        }
    }

    private class PanelLineasInteractivo extends JPanel {
        private static final long serialVersionUID = 1L;
        private JComboBox<Enfermedad> cbEnfermedades;
        private JPanel panelGrafico;
        private int[] casosPorMes = new int[12];

        public PanelLineasInteractivo() {
            setLayout(new BorderLayout()); setBackground(COLOR_FONDO);
            JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelControl.setBackground(Color.WHITE);
            panelControl.add(new JLabel("Evolución:"));
            
            cbEnfermedades = new JComboBox<>();
            cbEnfermedades.setRenderer(new DefaultListCellRenderer() {
                private static final long serialVersionUID = 1L;
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if(value instanceof Enfermedad) setText(((Enfermedad)value).getNombre());
                    return this;
                }
            });
            
            for (Enfermedad enf : SistemaGestion.getInstance().getCatalogoEnfermedades()) cbEnfermedades.addItem(enf);
            cbEnfermedades.addActionListener(e -> { calcularDatos((Enfermedad)cbEnfermedades.getSelectedItem()); panelGrafico.repaint(); });
            panelControl.add(cbEnfermedades); add(panelControl, BorderLayout.NORTH);
            panelGrafico = new JPanel() { private static final long serialVersionUID = 1L; protected void paintComponent(Graphics g) { super.paintComponent(g); dibujarGrafico(g, getWidth(), getHeight()); }};
            panelGrafico.setBackground(COLOR_FONDO); add(panelGrafico, BorderLayout.CENTER);
            if(cbEnfermedades.getItemCount()>0){cbEnfermedades.setSelectedIndex(0); calcularDatos((Enfermedad)cbEnfermedades.getSelectedItem());}
        }

        private void calcularDatos(Enfermedad enf) {
            for(int i=0; i<12; i++) casosPorMes[i] = 0;
            if (enf == null) return;
            int yearActual = LocalDate.now().getYear();
            for (Paciente p : SistemaGestion.getInstance().getListaPacientes())
                for (Consulta c : p.getHistorialConsultas())
                    if (c.getDiagnostico() != null && c.getDiagnostico().getId().equals(enf.getId()))
                        if (c.getFechaConsulta().getYear() == yearActual) casosPorMes[c.getFechaConsulta().getMonthValue() - 1]++;
        }

        private void dibujarGrafico(Graphics g, int w, int h) {
            Graphics2D g2d = (Graphics2D)g; g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int pad=60, gw=w-2*pad, gh=h-2*pad;
            g2d.setColor(Color.GRAY); g2d.setStroke(new BasicStroke(2)); g2d.drawLine(pad, h-pad, w-pad, h-pad); g2d.drawLine(pad, h-pad, pad, pad);
            int maxVal=0; for(int v:casosPorMes) if(v>maxVal) maxVal=v; if(maxVal==0) maxVal=5;
            int stepX=gw/11; int prevX=-1, prevY=-1; g2d.setStroke(new BasicStroke(3f));
            String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
            for(int i=0; i<12; i++) {
                int val=casosPorMes[i]; int x=pad+i*stepX; int y=h-pad-(int)((double)val/maxVal*gh);
                if(i>0){g2d.setColor(new Color(10,186,181)); g2d.drawLine(prevX,prevY,x,y);}
                g2d.setColor(Color.DARK_GRAY); g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11)); g2d.drawString(meses[i], x-10, h-pad+20);
                prevX=x; prevY=y;
            }
            for(int i=0; i<12; i++) {
                int val=casosPorMes[i]; int x=pad+i*stepX; int y=h-pad-(int)((double)val/maxVal*gh);
                g2d.setColor(Color.WHITE); g2d.fillOval(x-6,y-6,12,12); g2d.setColor(new Color(0,168,107));
                g2d.setStroke(new BasicStroke(2)); g2d.drawOval(x-6,y-6,12,12);
                if(val>0) {g2d.setColor(Color.BLACK); g2d.setFont(new Font("Segoe UI", Font.BOLD, 12)); g2d.drawString(String.valueOf(val), x-3, y-10);}
            }
        }
    }

    private class PanelVacunas extends JPanel {
        private static final long serialVersionUID = 1L;
        private Map<String, Integer> datosVacunas;
        private Map<Shape, String> tooltipsMap = new HashMap<>();

        public PanelVacunas() {
            setBackground(COLOR_FONDO);
            setLayout(new BorderLayout());
            this.datosVacunas = SistemaGestion.getInstance().getReporteVacunacion();
            setToolTipText("");
        }

        @Override
        public String getToolTipText(java.awt.event.MouseEvent event) {
            for (Map.Entry<Shape, String> entry : tooltipsMap.entrySet()) {
                if (entry.getKey().contains(event.getPoint())) {
                    return entry.getValue();
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            tooltipsMap.clear();
            if(datosVacunas == null || datosVacunas.isEmpty()) return;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight(); int pad = 60;
            g2d.setColor(Color.GRAY); g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(pad, h - pad, w - pad, h - pad); g2d.drawLine(pad, h - pad, pad, pad);
            int max = datosVacunas.values().stream().max(Integer::compare).orElse(1);
            int barW = (w - 2 * pad) / (datosVacunas.size() * 2);
            if(barW > 80) barW = 80; int x = pad + 20; int colorIdx = 0;

            for(Map.Entry<String, Integer> e : datosVacunas.entrySet()) {
                int val = e.getValue();
                int barH = (int)((double)val/max * (h - 2 * pad));
                String nombreCompleto = e.getKey();
                String nombreLimpio = nombreCompleto.replace(" (Aplicadas)", "").replace(" (Pendientes)", "");
                
                Rectangle barraRect = new Rectangle(x, h - pad - barH, barW, barH);
                tooltipsMap.put(barraRect, nombreLimpio + ": " + val);
                Rectangle textoRect = new Rectangle(x - 5, h - pad, barW + 10, 40);
                tooltipsMap.put(textoRect, nombreLimpio + ": " + val);

                g2d.setColor(COLORES_GRAFICOS[colorIdx % COLORES_GRAFICOS.length]);
                g2d.fillRect(x, h - pad - barH, barW, barH);
                g2d.setColor(Color.BLACK); g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valStr = String.valueOf(val);
                int valW = g2d.getFontMetrics().stringWidth(valStr);
                g2d.drawString(valStr, x + (barW - valW)/2, h - pad - barH - 5);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                if(nombreLimpio.length() > 12) nombreLimpio = nombreLimpio.substring(0, 10) + "..";
                int lblW = g2d.getFontMetrics().stringWidth(nombreLimpio);
                g2d.drawString(nombreLimpio, x + (barW - lblW)/2, h - pad + 15);
                x += barW + 30; colorIdx++;
            }
        }
    }

    private class PanelTop5 extends JPanel {
        private static final long serialVersionUID = 1L;
        private Map<String, Integer> datosTop5;
        private Map<Shape, String> tooltipsMap = new HashMap<>();

        public PanelTop5() {
            setBackground(COLOR_FONDO);
            setLayout(new BorderLayout());
            setBorder(new LineBorder(new Color(230, 230, 230), 1));
            calcularDatos();
            setToolTipText("");
        }

        private void calcularDatos() {
            Map<Enfermedad, Integer> conteoCompleto = SistemaGestion.getInstance().getReporteEnfermedades();
            ArrayList<Map.Entry<Enfermedad, Integer>> lista = new ArrayList<>(conteoCompleto.entrySet());
            lista.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue())); 
            
            datosTop5 = new HashMap<>();
             int max = Math.min(5, lista.size());
             for(int i=0; i<max; i++) {
                 datosTop5.put(lista.get(i).getKey().getNombre(), lista.get(i).getValue());
             }
        }
        
        @Override
        public String getToolTipText(java.awt.event.MouseEvent event) {
            for (Map.Entry<Shape, String> entry : tooltipsMap.entrySet()) {
                if (entry.getKey().contains(event.getPoint())) return entry.getValue();
            } return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            tooltipsMap.clear();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (datosTop5 == null || datosTop5.isEmpty()) {
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                g2d.drawString("No hay suficientes datos para el Top 5.", 50, 100);
                return;
            }

            int w = getWidth(); int h = getHeight(); int pad = 80;
            g2d.setColor(Color.GRAY); g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(pad, h - pad, w - pad, h - pad); 
            g2d.drawLine(pad, h - pad, pad, pad); 

            int maxVal = datosTop5.values().stream().max(Integer::compare).orElse(1);
            ArrayList<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(datosTop5.entrySet());
            listaOrdenada.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            int barWidth = (w - 2 * pad) / (listaOrdenada.size() * 2);
            if (barWidth > 100) barWidth = 100;
            int x = pad + 40;
            int colorIdx = 0;

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.drawString("TOP 5 ENFERMEDADES MÁS FRECUENTES", pad, pad - 40);

            for (Map.Entry<String, Integer> entry : listaOrdenada) {
                int val = entry.getValue();
                int barHeight = (int) ((double) val / maxVal * (h - 2 * pad));
                
                Rectangle barRect = new Rectangle(x, h - pad - barHeight, barWidth, barHeight);
                tooltipsMap.put(barRect, entry.getKey() + ": " + val + " casos");
                
                g2d.setColor(COLORES_GRAFICOS[colorIdx % COLORES_GRAFICOS.length]);
                g2d.fillRect(x, h - pad - barHeight, barWidth, barHeight);
                
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRect(x, h - pad - barHeight, barWidth, barHeight);
                
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                String valStr = String.valueOf(val);
                int valW = g2d.getFontMetrics().stringWidth(valStr);
                g2d.drawString(valStr, x + (barWidth - valW)/2, h - pad - barHeight - 5);

                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String lbl = entry.getKey();
                if (lbl.length() > 10) lbl = lbl.substring(0, 8) + "..";
                int lblW = g2d.getFontMetrics().stringWidth(lbl);
                g2d.drawString(lbl, x + (barWidth - lblW)/2, h - pad + 20);

                x += barWidth + 60;
                colorIdx++;
            }
        }
    }

    @SuppressWarnings("unused")
	private void exportarPDF() {
        try {
            File carpeta = new File("Reportes");
            if (!carpeta.exists()) carpeta.mkdir();
            String fechaStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "Reporte_Clinico_" + fechaStr + ".pdf";
            File archivo = new File(carpeta, nombreArchivo);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();

            com.itextpdf.text.Font fontTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph titulo = new Paragraph("- SISTEMA DE REPORTES DE LA CLÍNICA -", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            
            Paragraph fecha = new Paragraph("Fecha de generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingAfter(30);
            document.add(fecha);

            Component comp = tabbedPane.getSelectedComponent();
            Dimension size = comp.getSize();
            BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            comp.paint(g2);
            g2.dispose();

            Image pdfImage = Image.getInstance(image, null);
            pdfImage.scaleToFit(500, 500);
            pdfImage.setAlignment(Element.ALIGN_CENTER);
            document.add(pdfImage);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Detalle de Datos:", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("1. Vigilancia Epidemiológica", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD)));
            PdfPTable tablaEnf = new PdfPTable(2);
            tablaEnf.setWidthPercentage(100);
            tablaEnf.setSpacingBefore(5f);
            tablaEnf.setSpacingAfter(15f);
            
            PdfPCell c1 = new PdfPCell(new Phrase("Enfermedad"));
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaEnf.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Cantidad de Casos"));
            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaEnf.addCell(c2);
            
            Map<Enfermedad, Integer> enfData = SistemaGestion.getInstance().getReporteEnfermedades();
            for(Map.Entry<Enfermedad, Integer> e : enfData.entrySet()) {
                tablaEnf.addCell(e.getKey().getNombre());
                tablaEnf.addCell(e.getValue().toString());
            }
            document.add(tablaEnf);

            document.add(new Paragraph("2. Cobertura de Vacunación", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD)));
            PdfPTable tablaVac = new PdfPTable(2);
            tablaVac.setWidthPercentage(100);
            tablaVac.setSpacingBefore(5f);
            
            PdfPCell v1 = new PdfPCell(new Phrase("Vacuna / Estado"));
            v1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaVac.addCell(v1);
            PdfPCell v2 = new PdfPCell(new Phrase("Cantidad"));
            v2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaVac.addCell(v2);
            
            Map<String, Integer> vacData = SistemaGestion.getInstance().getReporteVacunacion();
            for(Map.Entry<String, Integer> e : vacData.entrySet()) {
                tablaVac.addCell(e.getKey());
                tablaVac.addCell(e.getValue().toString());
            }
            document.add(tablaVac);

            document.close();
            
            JOptionPane.showMessageDialog(this, "Reporte PDF guardado en:\n" + archivo.getAbsolutePath());
            try {
                java.awt.Desktop.getDesktop().open(archivo);
            } catch (Exception ex) {}
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al exportar PDF: " + e.getMessage());
        }
    }
}