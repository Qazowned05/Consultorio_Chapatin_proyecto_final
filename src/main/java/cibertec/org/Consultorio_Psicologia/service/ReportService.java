package cibertec.org.Consultorio_Psicologia.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import cibertec.org.Consultorio_Psicologia.entity.Cita;
import cibertec.org.Consultorio_Psicologia.entity.EstadoCita;
import cibertec.org.Consultorio_Psicologia.entity.Psicologo;
import cibertec.org.Consultorio_Psicologia.repository.CitaRepository;
import cibertec.org.Consultorio_Psicologia.repository.PsicologoRepository;

@Service
public class ReportService {
    
    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private PsicologoRepository psicologoRepository;
    
    @Autowired
    private QRCodeService qrCodeService;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    /**
     * Genera un reporte de citas en PDF usando OpenPDF con gráfico de barras y QR
     */
    public byte[] generateCitasReport() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        PdfWriter.getInstance(document, baos);
        document.open();
        
        // Agregar título
        addTitle(document);
        
        // Agregar estadísticas
        addStatistics(document);
        
        // Agregar gráfico de barras
        addBarChart(document);
        
        // Agregar tabla de citas
        addCitasTable(document);
        
        // Agregar código QR
        addQRCode(document);
        
        // Agregar pie de página
        addFooter(document);
        
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * Genera un reporte de psicólogos en PDF con estadísticas de citas
     */
    public byte[] generatePsicologosReport() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        PdfWriter.getInstance(document, baos);
        document.open();
        
        // Agregar título para psicólogos
        addPsicologosTitle(document);
        
        // Agregar estadísticas de psicólogos
        addPsicologosStatistics(document);
        
        // Agregar gráfico de citas por psicólogo
        addPsicologosBarChart(document);
        
        // Agregar tabla de psicólogos
        addPsicologosTable(document);
        
        // Agregar código QR para psicólogos
        addPsicologosQRCode(document);
        
        // Agregar pie de página
        addFooter(document);
        
        document.close();
        return baos.toByteArray();
    }
    
    private void addTitle(Document document) throws DocumentException {
        // Logo y encabezado elegante
        addBrainLogo(document);
        
        // Título principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("REPORTE DE CITAS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
        
        // Subtítulo
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
        Paragraph subtitle = new Paragraph("Consultorio de Psicología", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
        
        // Fecha de generación
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Paragraph fecha = new Paragraph("Fecha de generación: " + fechaActual, dateFont);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        fecha.setSpacingAfter(20);
        document.add(fecha);
    }
    
    private void addBrainLogo(Document document) throws DocumentException {
        // Crear una tabla para el encabezado con logo más prominente
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(20);
        
        // Celda con múltiples cerebros y diseño más elegante
        Font logoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 48);
        PdfPCell logoCell = new PdfPCell(new Phrase("🧠 💚 🧠", logoFont));
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setBorder(0);
        logoCell.setPadding(15);
        logoCell.setBackgroundColor(new java.awt.Color(248, 249, 250)); // Fondo gris claro
        
        headerTable.addCell(logoCell);
        
        // Título "SALUD MENTAL" con el logo
        Font titleLogoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        PdfPCell titleLogoCell = new PdfPCell(new Phrase("CONSULTORIO DE SALUD MENTAL", titleLogoFont));
        titleLogoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleLogoCell.setBorder(0);
        titleLogoCell.setPadding(8);
        titleLogoCell.setBackgroundColor(new java.awt.Color(248, 249, 250));
        
        headerTable.addCell(titleLogoCell);
        
        // Línea decorativa más elegante
        Font lineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        PdfPCell lineCell = new PdfPCell(new Phrase("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━", lineFont));
        lineCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        lineCell.setBorder(0);
        lineCell.setPadding(10);
        
        headerTable.addCell(lineCell);
        
        document.add(headerTable);
    }
    
    private void addStatistics(Document document) throws DocumentException {
        List<Cita> todasLasCitas = citaRepository.findAll();
        
        // Contar citas por estado
        Map<EstadoCita, Long> estadisticas = todasLasCitas.stream()
                .collect(Collectors.groupingBy(Cita::getEstado, Collectors.counting()));
        
        long totalCitas = todasLasCitas.size();
        long citasConfirmadas = estadisticas.getOrDefault(EstadoCita.CONFIRMADA, 0L);
        long citasPendientes = estadisticas.getOrDefault(EstadoCita.PENDIENTE, 0L);
        long citasCanceladas = estadisticas.getOrDefault(EstadoCita.CANCELADA, 0L);
        
        // Título de estadísticas
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph statsHeader = new Paragraph("ESTADÍSTICAS GENERALES", headerFont);
        statsHeader.setSpacingAfter(10);
        document.add(statsHeader);
        
        // Tabla de estadísticas
        PdfPTable statsTable = new PdfPTable(2);
        statsTable.setWidthPercentage(60);
        statsTable.setSpacingAfter(20);
        
        // Encabezados
        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        PdfPCell headerCell1 = new PdfPCell(new Phrase("Estadística", tableHeaderFont));
        PdfPCell headerCell2 = new PdfPCell(new Phrase("Cantidad", tableHeaderFont));
        headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        statsTable.addCell(headerCell1);
        statsTable.addCell(headerCell2);
        
        // Datos
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        
        statsTable.addCell(new PdfPCell(new Phrase("Total de Citas", cellFont)));
        PdfPCell totalCell = new PdfPCell(new Phrase(String.valueOf(totalCitas), cellFont));
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statsTable.addCell(totalCell);
        
        statsTable.addCell(new PdfPCell(new Phrase("Citas Confirmadas", cellFont)));
        PdfPCell confirmadasCell = new PdfPCell(new Phrase(String.valueOf(citasConfirmadas), cellFont));
        confirmadasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statsTable.addCell(confirmadasCell);
        
        statsTable.addCell(new PdfPCell(new Phrase("Citas Pendientes", cellFont)));
        PdfPCell pendientesCell = new PdfPCell(new Phrase(String.valueOf(citasPendientes), cellFont));
        pendientesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statsTable.addCell(pendientesCell);
        
        statsTable.addCell(new PdfPCell(new Phrase("Citas Canceladas", cellFont)));
        PdfPCell canceladasCell = new PdfPCell(new Phrase(String.valueOf(citasCanceladas), cellFont));
        canceladasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statsTable.addCell(canceladasCell);
        
        document.add(statsTable);
    }
    
    private void addBarChart(Document document) throws DocumentException, IOException {
        List<Cita> todasLasCitas = citaRepository.findAll();
        
        // Contar citas por estado
        Map<EstadoCita, Long> estadisticas = todasLasCitas.stream()
                .collect(Collectors.groupingBy(Cita::getEstado, Collectors.counting()));
        
        // Crear dataset para el gráfico con series separadas para colores individuales
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(estadisticas.getOrDefault(EstadoCita.CONFIRMADA, 0L), "Confirmadas", "Confirmadas");
        dataset.addValue(estadisticas.getOrDefault(EstadoCita.PENDIENTE, 0L), "Pendientes", "Pendientes");
        dataset.addValue(estadisticas.getOrDefault(EstadoCita.CANCELADA, 0L), "Canceladas", "Canceladas");
        
        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Distribución de Citas por Estado",
                "Estado de Citas",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true, // Mostrar leyenda
                true,
                false
        );
        
        // Personalizar el gráfico
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        // Colores distintos y más vibrantes para cada barra
        renderer.setSeriesPaint(0, new Color(40, 167, 69));   // Verde brillante para confirmadas
        renderer.setSeriesPaint(1, new Color(255, 193, 7));   // Amarillo para pendientes  
        renderer.setSeriesPaint(2, new Color(220, 53, 69));   // Rojo para canceladas
        
        // Personalizar el estilo del gráfico
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(200, 200, 200));
        plot.setRangeGridlinePaint(new Color(200, 200, 200));
        chart.setBackgroundPaint(Color.WHITE);
        
        // Personalizar la leyenda
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(Color.WHITE);
        }
        
        // Convertir gráfico a imagen
        BufferedImage chartImage = chart.createBufferedImage(500, 300);
        ByteArrayOutputStream chartBaos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", chartBaos);
        
        // Agregar título del gráfico
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph chartHeader = new Paragraph("GRÁFICO ESTADÍSTICO", headerFont);
        chartHeader.setSpacingAfter(10);
        document.add(chartHeader);
        
        // Agregar imagen al PDF
        Image chartImg = Image.getInstance(chartBaos.toByteArray());
        chartImg.scaleToFit(400, 240);
        chartImg.setAlignment(Element.ALIGN_CENTER);
        chartImg.setSpacingAfter(20);
        document.add(chartImg);
    }
    
    private void addQRCode(Document document) throws DocumentException, IOException {
        try {
            // URL mejorada que funcionará tanto en desarrollo como en producción
            String baseUrl = getBaseUrl();
            
            String qrContent = qrCodeService.generateReportQRContent(baseUrl);
            byte[] qrBytes = qrCodeService.generateQRCodeBytes(qrContent);
            
            // Crear tabla para organizar el QR y la información de manera más elegante
            PdfPTable qrTable = new PdfPTable(2);
            qrTable.setWidthPercentage(90);
            qrTable.setWidths(new float[]{1.2f, 2.5f});
            qrTable.setSpacingAfter(20);
            
            // Celda con el QR (con borde elegante)
            PdfPCell qrCell = new PdfPCell();
            qrCell.setBorder(1);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qrCell.setPadding(10);
            qrCell.setBackgroundColor(java.awt.Color.WHITE);
            
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(110, 110);
            qrCell.addElement(qrImage);
            
            // Celda con información mejorada
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(1);
            infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            infoCell.setPadding(15);
            infoCell.setBackgroundColor(new java.awt.Color(248, 249, 250));
            
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font urlFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            
            Paragraph qrTitle = new Paragraph("🧠 ACCESO DIGITAL RÁPIDO", titleFont);
            qrTitle.setSpacingAfter(10);
            infoCell.addElement(qrTitle);
            
            Paragraph instruction1 = new Paragraph("📱 Escanea con tu móvil", infoFont);
            instruction1.setSpacingAfter(6);
            infoCell.addElement(instruction1);
            
            Paragraph instruction2 = new Paragraph("📥 Descarga automática del reporte", infoFont);
            instruction2.setSpacingAfter(6);
            infoCell.addElement(instruction2);
            
            Paragraph instruction3 = new Paragraph("🌐 Siempre actualizado", infoFont);
            instruction3.setSpacingAfter(8);
            infoCell.addElement(instruction3);
            
            Paragraph urlInfo = new Paragraph("URL: " + baseUrl + "/admin/reportes/citas", urlFont);
            infoCell.addElement(urlInfo);
            
            qrTable.addCell(qrCell);
            qrTable.addCell(infoCell);
            
            // Título del QR con emoji
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph qrHeader = new Paragraph("🔗 CÓDIGO QR - DESCARGA DIRECTA", headerFont);
            qrHeader.setAlignment(Element.ALIGN_CENTER);
            qrHeader.setSpacingAfter(15);
            document.add(qrHeader);
            
            document.add(qrTable);
            
        } catch (Exception e) {
            // Si hay error con QR, agregar texto informativo más elegante
            Font errorFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph errorPara = new Paragraph("❌ Código QR temporalmente no disponible", errorFont);
            errorPara.setAlignment(Element.ALIGN_CENTER);
            errorPara.setSpacingAfter(5);
            document.add(errorPara);
            
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph infoPara = new Paragraph("Accede directamente desde el panel de administrador para descargar reportes actualizados", infoFont);
            infoPara.setAlignment(Element.ALIGN_CENTER);
            infoPara.setSpacingAfter(20);
            document.add(infoPara);
        }
    }
    
    private String getBaseUrl() {
        // Usar la URL configurada en application.properties
        // En producción cambiar app.base-url=https://tu-dominio.com
        return baseUrl;
    }
    
    private void addCitasTable(Document document) throws DocumentException {
        List<Cita> citas = citaRepository.findAll();
        
        // Título de la tabla
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph tableHeader = new Paragraph("LISTADO DE CITAS", headerFont);
        tableHeader.setSpacingAfter(10);
        document.add(tableHeader);
        
        // Crear tabla
        PdfPTable table = new PdfPTable(6); // 6 columnas
        table.setWidthPercentage(100);
        table.setSpacingAfter(20);
        
        // Definir anchos de columna
        float[] columnWidths = {1f, 2.5f, 2.5f, 1.5f, 1.5f, 1.5f};
        table.setWidths(columnWidths);
        
        // Encabezados de tabla
        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        String[] headers = {"ID", "Paciente", "Psicólogo", "Fecha", "Hora", "Estado"};
        
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, tableHeaderFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }
        
        // Datos de las citas
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        
        for (Cita cita : citas) {
            // ID
            PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(cita.getId()), cellFont));
            idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(idCell);
            
            // Paciente
            String pacienteNombre = cita.getPaciente() != null ? 
                    cita.getPaciente().getNombre() + " " + 
                    (cita.getPaciente().getApellido() != null ? cita.getPaciente().getApellido() : "") 
                    : "Sin paciente";
            table.addCell(new PdfPCell(new Phrase(pacienteNombre, cellFont)));
            
            // Psicólogo
            String psicologoNombre = cita.getPsicologo() != null ? 
                    cita.getPsicologo().getNombre() + " " + 
                    (cita.getPsicologo().getApellido() != null ? cita.getPsicologo().getApellido() : "") 
                    : "Sin psicólogo";
            table.addCell(new PdfPCell(new Phrase(psicologoNombre, cellFont)));
            
            // Fecha
            String fecha = cita.getFecha() != null ? cita.getFecha().toString() : "Sin fecha";
            PdfPCell fechaCell = new PdfPCell(new Phrase(fecha, cellFont));
            fechaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(fechaCell);
            
            // Hora
            String hora = cita.getHoraInicio() != null ? cita.getHoraInicio().toString() : "Sin hora";
            PdfPCell horaCell = new PdfPCell(new Phrase(hora, cellFont));
            horaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(horaCell);
            
            // Estado
            String estado = cita.getEstado() != null ? cita.getEstado().toString() : "Sin estado";
            PdfPCell estadoCell = new PdfPCell(new Phrase(estado, cellFont));
            estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(estadoCell);
        }
        
        document.add(table);
    }
    
    private void addFooter(Document document) throws DocumentException {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Paragraph footer = new Paragraph(
                "Este reporte fue generado automáticamente por el Sistema de Gestión de Consultorio de Psicología", 
                footerFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20);
        document.add(footer);
    }
    
    private void addPsicologosTitle(Document document) throws DocumentException {
        // Logo y encabezado elegante
        addBrainLogo(document);
        
        // Título principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("REPORTE DE PSICÓLOGOS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
        
        // Subtítulo
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
        Paragraph subtitle = new Paragraph("Consultorio de Psicología - Personal Especializado", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
        
        // Fecha de generación
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Paragraph fecha = new Paragraph("Fecha de generación: " + fechaActual, dateFont);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        fecha.setSpacingAfter(20);
        document.add(fecha);
    }
    
    private void addPsicologosStatistics(Document document) throws DocumentException {
        List<Psicologo> todosPsicologos = psicologoRepository.findAll();
        List<Cita> todasLasCitas = citaRepository.findAll();
        
        // Contar especialidades únicas
        long especialidadesUnicas = todosPsicologos.stream()
                .map(p -> p.getEspecialidad() != null ? p.getEspecialidad().getNombre() : "Sin especialidad")
                .distinct()
                .count();
        
        // Calcular citas por psicólogo
        Map<String, Long> citasPorPsicologo = todasLasCitas.stream()
                .filter(c -> c.getPsicologo() != null)
                .collect(Collectors.groupingBy(
                    c -> c.getPsicologo().getNombre() + " " + c.getPsicologo().getApellido(),
                    Collectors.counting()
                ));
        
        // Psicólogo más activo
        String psicologoMasActivo = citasPorPsicologo.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        
        // Título de estadísticas
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph statsHeader = new Paragraph("📊 ESTADÍSTICAS GENERALES", headerFont);
        statsHeader.setSpacingAfter(10);
        document.add(statsHeader);
        
        // Crear tabla de estadísticas
        PdfPTable statsTable = new PdfPTable(2);
        statsTable.setWidthPercentage(80);
        statsTable.setSpacingAfter(20);
        
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        
        // Total de psicólogos
        PdfPCell labelCell1 = new PdfPCell(new Phrase("👥 Total de Psicólogos:", labelFont));
        labelCell1.setBorder(1);
        labelCell1.setPadding(8);
        PdfPCell valueCell1 = new PdfPCell(new Phrase(String.valueOf(todosPsicologos.size()), valueFont));
        valueCell1.setBorder(1);
        valueCell1.setPadding(8);
        statsTable.addCell(labelCell1);
        statsTable.addCell(valueCell1);
        
        // Especialidades únicas
        PdfPCell labelCell2 = new PdfPCell(new Phrase("🎯 Especialidades Diferentes:", labelFont));
        labelCell2.setBorder(1);
        labelCell2.setPadding(8);
        PdfPCell valueCell2 = new PdfPCell(new Phrase(String.valueOf(especialidadesUnicas), valueFont));
        valueCell2.setBorder(1);
        valueCell2.setPadding(8);
        statsTable.addCell(labelCell2);
        statsTable.addCell(valueCell2);
        
        // Total de citas atendidas
        PdfPCell labelCell3 = new PdfPCell(new Phrase("📋 Total Citas Registradas:", labelFont));
        labelCell3.setBorder(1);
        labelCell3.setPadding(8);
        PdfPCell valueCell3 = new PdfPCell(new Phrase(String.valueOf(todasLasCitas.size()), valueFont));
        valueCell3.setBorder(1);
        valueCell3.setPadding(8);
        statsTable.addCell(labelCell3);
        statsTable.addCell(valueCell3);
        
        // Psicólogo más activo
        PdfPCell labelCell4 = new PdfPCell(new Phrase("⭐ Psicólogo Más Activo:", labelFont));
        labelCell4.setBorder(1);
        labelCell4.setPadding(8);
        PdfPCell valueCell4 = new PdfPCell(new Phrase(psicologoMasActivo, valueFont));
        valueCell4.setBorder(1);
        valueCell4.setPadding(8);
        statsTable.addCell(labelCell4);
        statsTable.addCell(valueCell4);
        
        document.add(statsTable);
    }
    
    private void addPsicologosBarChart(Document document) throws DocumentException, IOException {
        List<Cita> todasLasCitas = citaRepository.findAll();
        
        // Contar citas por psicólogo
        Map<String, Long> citasPorPsicologo = todasLasCitas.stream()
                .filter(c -> c.getPsicologo() != null)
                .collect(Collectors.groupingBy(
                    c -> c.getPsicologo().getNombre() + " " + c.getPsicologo().getApellido(),
                    Collectors.counting()
                ));
        
        // Si no hay datos, mostrar mensaje
        if (citasPorPsicologo.isEmpty()) {
            Font messageFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph message = new Paragraph("📊 No hay datos de citas por psicólogo disponibles", messageFont);
            message.setAlignment(Element.ALIGN_CENTER);
            message.setSpacingAfter(20);
            document.add(message);
            return;
        }
        
        // Crear dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Colores únicos para cada psicólogo
        Color[] colors = {
            new Color(40, 167, 69),   // Verde
            new Color(0, 123, 255),   // Azul
            new Color(255, 193, 7),   // Amarillo
            new Color(220, 53, 69),   // Rojo
            new Color(108, 117, 125), // Gris
            new Color(102, 16, 242),  // Morado
            new Color(255, 105, 180), // Rosa
            new Color(255, 140, 0),   // Naranja
            new Color(32, 201, 151),  // Verde agua
            new Color(139, 69, 19)    // Marrón
        };
        
        int index = 0;
        for (Map.Entry<String, Long> entry : citasPorPsicologo.entrySet()) {
            String nombreCorto = entry.getKey().length() > 15 ? 
                entry.getKey().substring(0, 15) + "..." : entry.getKey();
            dataset.addValue(entry.getValue(), "Citas", nombreCorto);
            index++;
        }
        
        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Distribución de Citas por Psicólogo",
                "Psicólogos",
                "Número de Citas",
                dataset,
                PlotOrientation.VERTICAL,
                false, // Sin leyenda para más espacio
                true,
                false
        );
        
        // Personalizar el gráfico
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        // Aplicar colores únicos para cada barra
        for (int i = 0; i < citasPorPsicologo.size() && i < colors.length; i++) {
            renderer.setSeriesPaint(0, colors[i % colors.length]);
        }
        
        // Personalizar el estilo del gráfico
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(200, 200, 200));
        plot.setRangeGridlinePaint(new Color(200, 200, 200));
        chart.setBackgroundPaint(Color.WHITE);
        
        // Rotar etiquetas del eje X para mejor legibilidad
        plot.getDomainAxis().setCategoryLabelPositions(
            org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        
        // Convertir gráfico a imagen
        BufferedImage chartImage = chart.createBufferedImage(550, 350);
        ByteArrayOutputStream chartBaos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", chartBaos);
        
        // Agregar título del gráfico
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph chartHeader = new Paragraph("📈 GRÁFICO DE ACTIVIDAD POR PSICÓLOGO", headerFont);
        chartHeader.setSpacingAfter(10);
        document.add(chartHeader);
        
        // Agregar imagen al PDF
        Image chartImg = Image.getInstance(chartBaos.toByteArray());
        chartImg.scaleToFit(450, 280);
        chartImg.setAlignment(Element.ALIGN_CENTER);
        chartImg.setSpacingAfter(20);
        document.add(chartImg);
    }
    
    private void addPsicologosTable(Document document) throws DocumentException {
        List<Psicologo> psicologos = psicologoRepository.findAll();
        List<Cita> todasLasCitas = citaRepository.findAll();
        
        // Contar citas por psicólogo
        Map<Long, Long> citasPorPsicologoId = todasLasCitas.stream()
                .filter(c -> c.getPsicologo() != null)
                .collect(Collectors.groupingBy(
                    c -> c.getPsicologo().getId(),
                    Collectors.counting()
                ));
        
        // Título de la tabla
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph tableHeader = new Paragraph("👥 TABLA DETALLADA DE PSICÓLOGOS", headerFont);
        tableHeader.setSpacingAfter(10);
        document.add(tableHeader);
        
        // Crear tabla con 6 columnas
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.8f, 2f, 2f, 2.5f, 1.5f, 1.2f});
        table.setSpacingAfter(20);
        
        // Encabezados de la tabla
        Font headerCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        
        PdfPCell header1 = new PdfPCell(new Phrase("ID", headerCellFont));
        header1.setBackgroundColor(new java.awt.Color(52, 58, 64));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header1.setPadding(8);
        table.addCell(header1);
        
        PdfPCell header2 = new PdfPCell(new Phrase("NOMBRE", headerCellFont));
        header2.setBackgroundColor(new java.awt.Color(52, 58, 64));
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setPadding(8);
        table.addCell(header2);
        
        PdfPCell header3 = new PdfPCell(new Phrase("APELLIDO", headerCellFont));
        header3.setBackgroundColor(new java.awt.Color(52, 58, 64));
        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
        header3.setPadding(8);
        table.addCell(header3);
        
        PdfPCell header4 = new PdfPCell(new Phrase("ESPECIALIDAD", headerCellFont));
        header4.setBackgroundColor(new java.awt.Color(52, 58, 64));
        header4.setHorizontalAlignment(Element.ALIGN_CENTER);
        header4.setPadding(8);
        table.addCell(header4);
        
        PdfPCell header5 = new PdfPCell(new Phrase("TELÉFONO", headerCellFont));
        header5.setBackgroundColor(new java.awt.Color(52, 58, 64));
        header5.setHorizontalAlignment(Element.ALIGN_CENTER);
        header5.setPadding(8);
        table.addCell(header5);
        
        PdfPCell header6 = new PdfPCell(new Phrase("CITAS", headerCellFont));
        header6.setBackgroundColor(new java.awt.Color(52, 58, 64));
        header6.setHorizontalAlignment(Element.ALIGN_CENTER);
        header6.setPadding(8);
        table.addCell(header6);
        
        // Agregar datos de psicólogos
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        int rowIndex = 0;
        
        for (Psicologo psicologo : psicologos) {
            java.awt.Color bgColor = rowIndex % 2 == 0 ? 
                new java.awt.Color(248, 249, 250) : java.awt.Color.WHITE;
            
            // ID
            PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(psicologo.getId()), cellFont));
            cell1.setBackgroundColor(bgColor);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPadding(6);
            table.addCell(cell1);
            
            // Nombre
            PdfPCell cell2 = new PdfPCell(new Phrase(psicologo.getNombre() != null ? psicologo.getNombre() : "N/A", cellFont));
            cell2.setBackgroundColor(bgColor);
            cell2.setPadding(6);
            table.addCell(cell2);
            
            // Apellido
            PdfPCell cell3 = new PdfPCell(new Phrase(psicologo.getApellido() != null ? psicologo.getApellido() : "N/A", cellFont));
            cell3.setBackgroundColor(bgColor);
            cell3.setPadding(6);
            table.addCell(cell3);
            
            // Especialidad
            String especialidad = psicologo.getEspecialidad() != null ? 
                psicologo.getEspecialidad().getNombre() : "Sin especialidad";
            PdfPCell cell4 = new PdfPCell(new Phrase(especialidad, cellFont));
            cell4.setBackgroundColor(bgColor);
            cell4.setPadding(6);
            table.addCell(cell4);
            
            // Teléfono
            PdfPCell cell5 = new PdfPCell(new Phrase(psicologo.getTelefono() != null ? psicologo.getTelefono() : "N/A", cellFont));
            cell5.setBackgroundColor(bgColor);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setPadding(6);
            table.addCell(cell5);
            
            // Número de citas
            Long numCitas = citasPorPsicologoId.getOrDefault(psicologo.getId(), 0L);
            PdfPCell cell6 = new PdfPCell(new Phrase(String.valueOf(numCitas), cellFont));
            cell6.setBackgroundColor(bgColor);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setPadding(6);
            
            // Color especial para el número de citas
            if (numCitas > 0) {
                cell6.setBackgroundColor(new java.awt.Color(212, 237, 218)); // Verde claro
            }
            
            table.addCell(cell6);
            rowIndex++;
        }
        
        document.add(table);
        
        // Nota explicativa
        Font noteFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Paragraph note = new Paragraph("💡 Nota: Las celdas verdes indican psicólogos con citas registradas.", noteFont);
        note.setAlignment(Element.ALIGN_LEFT);
        note.setSpacingAfter(20);
        document.add(note);
    }
    
    private void addPsicologosQRCode(Document document) throws DocumentException, IOException {
        try {
            // URL específica para el reporte de psicólogos
            String baseUrl = getBaseUrl();
            String qrContent = baseUrl + "/admin/reportes/psicologos";
            
            byte[] qrBytes = qrCodeService.generateQRCodeBytes(qrContent);
            
            // Crear tabla para organizar el QR y la información
            PdfPTable qrTable = new PdfPTable(2);
            qrTable.setWidthPercentage(90);
            qrTable.setWidths(new float[]{1.2f, 2.5f});
            qrTable.setSpacingAfter(20);
            
            // Celda con el QR
            PdfPCell qrCell = new PdfPCell();
            qrCell.setBorder(1);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qrCell.setPadding(10);
            qrCell.setBackgroundColor(java.awt.Color.WHITE);
            
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(110, 110);
            qrCell.addElement(qrImage);
            
            // Celda con información
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(1);
            infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            infoCell.setPadding(15);
            infoCell.setBackgroundColor(new java.awt.Color(248, 249, 250));
            
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font urlFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            
            Paragraph qrTitle = new Paragraph("👥 REPORTE DE PSICÓLOGOS", titleFont);
            qrTitle.setSpacingAfter(10);
            infoCell.addElement(qrTitle);
            
            Paragraph instruction1 = new Paragraph("📱 Escanea para acceso rápido", infoFont);
            instruction1.setSpacingAfter(6);
            infoCell.addElement(instruction1);
            
            Paragraph instruction2 = new Paragraph("📄 Descarga reporte actualizado", infoFont);
            instruction2.setSpacingAfter(6);
            infoCell.addElement(instruction2);
            
            Paragraph instruction3 = new Paragraph("🔄 Siempre la información más reciente", infoFont);
            instruction3.setSpacingAfter(8);
            infoCell.addElement(instruction3);
            
            Paragraph urlInfo = new Paragraph("URL: " + qrContent, urlFont);
            infoCell.addElement(urlInfo);
            
            qrTable.addCell(qrCell);
            qrTable.addCell(infoCell);
            
            // Título del QR
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph qrHeader = new Paragraph("🔗 CÓDIGO QR - REPORTE PSICÓLOGOS", headerFont);
            qrHeader.setAlignment(Element.ALIGN_CENTER);
            qrHeader.setSpacingAfter(15);
            document.add(qrHeader);
            
            document.add(qrTable);
            
        } catch (Exception e) {
            // Si hay error con QR, agregar texto informativo
            Font errorFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph errorPara = new Paragraph("❌ Código QR temporalmente no disponible", errorFont);
            errorPara.setAlignment(Element.ALIGN_CENTER);
            errorPara.setSpacingAfter(5);
            document.add(errorPara);
            
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph infoPara = new Paragraph("Accede al panel de administrador para descargar el reporte de psicólogos", infoFont);
            infoPara.setAlignment(Element.ALIGN_CENTER);
            infoPara.setSpacingAfter(20);
            document.add(infoPara);
        }
    }
}