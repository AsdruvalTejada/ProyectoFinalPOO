package logico;

import java.awt.Color;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneradorReportes {

    private static final Font FUENTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
    private static final Font FUENTE_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.GRAY);
    private static final Font FUENTE_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
    private static final Font FUENTE_HEADER_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

    public static void generarReporteEnfermedades(String rutaArchivo) {
        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            // 1. TÍTULO
            agregarTitulo(documento, "Reporte Epidemiológico: Top 5 Enfermedades");

            // 2. DATOS
            ArrayList<String> top5 = SistemaGestion.getInstance().getTop5Enfermedades();
            DefaultPieDataset dataset = new DefaultPieDataset();

            // Parseamos los strings "Gripe - 5 casos" para sacar datos
            for (String item : top5) {
                try {
                    String[] partes = item.split(" - ");
                    String nombre = partes[0];
                    int cantidad = Integer.parseInt(partes[1].replace(" casos", "").trim());
                    dataset.setValue(nombre, cantidad);
                } catch (Exception e) {
                    continue; 
                }
            }

            // 3. GRÁFICO (Pie Chart)
            JFreeChart chart = ChartFactory.createPieChart(
                    "Distribución de Diagnósticos",
                    dataset,
                    true, true, false);
            
            // Personalización del gráfico (opcional)
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionPaint("Sección 1", new Color(10, 186, 181)); // Tu color Teal

            // Convertir gráfico a Imagen para PDF
            java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(500, 350);
            Image imagenGrafico = Image.getInstance(bufferedImage, null);
            imagenGrafico.setAlignment(Element.ALIGN_CENTER);
            documento.add(imagenGrafico);

            documento.add(Chunk.NEWLINE);

            // 4. TABLA DE DATOS
            PdfPTable tabla = new PdfPTable(2); // 2 Columnas
            tabla.setWidthPercentage(80);
            
            agregarCeldaHeader(tabla, "Enfermedad");
            agregarCeldaHeader(tabla, "Casos Reportados");

            for (String item : top5) {
                String[] partes = item.split(" - ");
                tabla.addCell(new Phrase(partes[0], FUENTE_NORMAL));
                tabla.addCell(new Phrase(partes[1], FUENTE_NORMAL));
            }
            documento.add(tabla);
            
            agregarPiePagina(documento);
            
            documento.close();
            System.out.println("Reporte Enfermedades generado exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteVacunacion(String rutaArchivo) {
        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            agregarTitulo(documento, "Reporte de Estado de Vacunación");

            Map<String, Integer> reporte = SistemaGestion.getInstance().getReporteVacunacion();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Map.Entry<String, Integer> entry : reporte.entrySet()) {
                String key = entry.getKey(); 
                int valor = entry.getValue();
                
                String[] partes = key.split(" - ");
                if(partes.length == 2) {
                    String vacuna = partes[0];
                    String estado = partes[1];
                    dataset.addValue(valor, estado, vacuna);
                }
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Cobertura de Vacunación", 
                    "Vacuna", "Cantidad", 
                    dataset, 
                    PlotOrientation.VERTICAL, 
                    true, true, false);
            
            // Color personalizado
            chart.getCategoryPlot().getRenderer().setSeriesPaint(0, new Color(0, 200, 151)); // Verde Aplicadas
            chart.getCategoryPlot().getRenderer().setSeriesPaint(1, new Color(255, 100, 100)); // Rojo Pendientes

            java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(500, 350);
            Image imagenGrafico = Image.getInstance(bufferedImage, null);
            imagenGrafico.setAlignment(Element.ALIGN_CENTER);
            documento.add(imagenGrafico);

            documento.add(Chunk.NEWLINE);
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(80);
            agregarCeldaHeader(tabla, "Concepto");
            agregarCeldaHeader(tabla, "Cantidad");

            for (Map.Entry<String, Integer> entry : reporte.entrySet()) {
                tabla.addCell(new Phrase(entry.getKey(), FUENTE_NORMAL));
                tabla.addCell(new Phrase(String.valueOf(entry.getValue()), FUENTE_NORMAL));
            }
            documento.add(tabla);

            agregarPiePagina(documento);
            documento.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void agregarTitulo(Document doc, String texto) throws Exception {
        Paragraph titulo = new Paragraph(texto, FUENTE_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        
        Paragraph fecha = new Paragraph("Generado el: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), FUENTE_SUBTITULO);
        fecha.setAlignment(Element.ALIGN_CENTER);
        fecha.setSpacingAfter(20);
        doc.add(fecha);
    }

    private static void agregarCeldaHeader(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_HEADER_TABLA));
        celda.setBackgroundColor(new BaseColor(10, 186, 181)); // Tu color Teal
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
    
    private static void agregarPiePagina(Document doc) throws Exception {
        doc.add(Chunk.NEWLINE);
        Paragraph pie = new Paragraph("Sistema de Gestión Clínica © 2025", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
        pie.setAlignment(Element.ALIGN_CENTER);
        doc.add(pie);
    }
    public static boolean generarHistorialPDF(Paciente paciente, String rutaDestino) {
        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            agregarTitulo(documento, "HISTORIAL CLÍNICO DEL PACIENTE");

            documento.add(new Paragraph("Datos Personales:", FUENTE_SUBTITULO));
            documento.add(new Paragraph("Nombre: " + paciente.getNombreCompleto(), FUENTE_NORMAL));
            documento.add(new Paragraph("Cédula/ID: " + paciente.getId(), FUENTE_NORMAL));
            documento.add(new Paragraph("Edad: " + paciente.getEdad() + " años", FUENTE_NORMAL));
            documento.add(new Paragraph("Tipo de Sangre: " + paciente.getTipoSangre(), FUENTE_NORMAL));

            String contacto = (paciente.getContacto() != null) ? paciente.getContacto() : "No registrado";
            documento.add(new Paragraph("Contacto: " + contacto, FUENTE_NORMAL));
            documento.add(new Paragraph("Estatura: " + paciente.getEstatura() + " m", FUENTE_NORMAL));
            
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);

            documento.add(new Paragraph("Registro de Consultas:", FUENTE_SUBTITULO));
            documento.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2f, 1.5f, 2.5f, 2.5f, 3f}); 

            agregarCeldaHeader(tabla, "Fecha");
            agregarCeldaHeader(tabla, "ID");
            agregarCeldaHeader(tabla, "Médico");
            agregarCeldaHeader(tabla, "Diagnóstico");
            agregarCeldaHeader(tabla, "Tratamiento");

            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            ArrayList<Consulta> historial = paciente.getHistorialConsultas();

            if (historial.isEmpty()) {
                PdfPCell celdaVacia = new PdfPCell(new Phrase("Sin registros médicos.", FUENTE_NORMAL));
                celdaVacia.setColspan(5);
                celdaVacia.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaVacia.setPadding(10);
                tabla.addCell(celdaVacia);
            } else {
                for (Consulta c : historial) {
                    
                    Date fechaDate = java.sql.Timestamp.valueOf(c.getFechaConsulta());
                    
                    tabla.addCell(new Phrase(fmt.format(fechaDate), FUENTE_NORMAL));
                    tabla.addCell(new Phrase(c.getId(), FUENTE_NORMAL));
                    tabla.addCell(new Phrase(c.getMedico().getNombreCompleto(), FUENTE_NORMAL));
                    
                    String diag = (c.getDiagnostico() != null) ? c.getDiagnostico().getNombre() : "N/A";
                    tabla.addCell(new Phrase(diag, FUENTE_NORMAL));
                    
                    String trat = (c.getTratamiento() != null) ? c.getTratamiento() : "N/A";
                    tabla.addCell(new Phrase(trat, FUENTE_NORMAL));
                }
            }

            documento.add(tabla);
            agregarPiePagina(documento);
            
            documento.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}