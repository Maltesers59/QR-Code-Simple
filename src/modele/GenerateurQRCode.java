package modele;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

/**
 * Le modèle : il crée le QR code (avec ZXing) et le PDF (avec iText).
 */
public class GenerateurQRCode {

    /**
     * Crée l'image du QR code (300 x 300 pixels).
     *
     * @param texte le texte à mettre dans le QR code
     * @return l'image du QR code
     * @throws IllegalArgumentException si le texte est vide
     * @throws WriterException si ZXing n'arrive pas à créer le QR code
     */
    public BufferedImage creerImage(String texte) throws WriterException {
        if (texte == null || texte.isBlank()) {
            throw new IllegalArgumentException("Le texte est vide.");
        }
        BitMatrix matrice = new QRCodeWriter().encode(texte, BarcodeFormat.QR_CODE, 300, 300);
        return MatrixToImageWriter.toBufferedImage(matrice);
    }

    /**
     * Crée un fichier PDF qui contient un titre, le QR code et le texte.
     *
     * @param texte   le texte à mettre dans le QR code
     * @param fichier le nom du fichier PDF à créer (ex. "qrcode.pdf")
     * @throws IllegalArgumentException si le texte est vide
     * @throws WriterException si ZXing n'arrive pas à créer le QR code
     * @throws IOException si le fichier ne peut pas être écrit (ex. déjà ouvert)
     */
    public void creerPDF(String texte, String fichier) throws WriterException, IOException {
        // 1. on crée l'image du QR code
        BufferedImage image = creerImage(texte);

        // 2. on la transforme en PNG, le format qu'iText sait lire
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        ImageIO.write(image, "png", png);

        // 3. on écrit le PDF
        PdfDocument pdf = new PdfDocument(new PdfWriter(fichier));
        Document document = new Document(pdf);
        document.add(new Paragraph("Mon QR Code").setFontSize(20));
        document.add(new Image(ImageDataFactory.create(png.toByteArray())));
        document.add(new Paragraph(texte));
        document.close();
    }
}
