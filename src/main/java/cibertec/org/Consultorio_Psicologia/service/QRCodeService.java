package cibertec.org.Consultorio_Psicologia.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Service
public class QRCodeService {
    
    private static final int QR_CODE_SIZE = 150;
    
    /**
     * Genera un código QR como imagen BufferedImage
     */
    public BufferedImage generateQRCodeImage(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
        
        BufferedImage image = new BufferedImage(QR_CODE_SIZE, QR_CODE_SIZE, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < QR_CODE_SIZE; x++) {
            for (int y = 0; y < QR_CODE_SIZE; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        
        return image;
    }
    
    /**
     * Genera un código QR como array de bytes en formato PNG
     */
    public byte[] generateQRCodeBytes(String text) throws WriterException, IOException {
        BufferedImage qrImage = generateQRCodeImage(text);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        
        return baos.toByteArray();
    }
    
    /**
     * Genera el contenido del QR para el reporte - URL de descarga
     */
    public String generateReportQRContent(String baseUrl) {
        return baseUrl + "/admin/reportes/citas";
    }
    
    /**
     * Genera el contenido del QR para el reporte de psicólogos
     */
    public String generatePsicologosReportQRContent(String baseUrl) {
        return baseUrl + "/admin/reportes/psicologos";
    }
}
