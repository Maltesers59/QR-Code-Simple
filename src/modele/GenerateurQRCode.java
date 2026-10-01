package modele;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

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
     * Crée un PDF simple (style par défaut, sans image).
     *
     * @param texte   le texte à mettre dans le QR code
     * @param fichier le nom du fichier PDF à créer (ex. "qrcode.pdf")
     * @throws IllegalArgumentException si le texte est vide
     * @throws WriterException si ZXing n'arrive pas à créer le QR code
     * @throws IOException si le fichier ne peut pas être écrit (ex. déjà ouvert)
     */
    public void creerPDF(String texte, String fichier) throws WriterException, IOException {
        Projet projet = new Projet(texte, "", Projet.POSITIONS[1], 200);
        Profil profil = new Profil("Helvetica", "Noir", 14);
        creerPDF(projet, profil, fichier);
    }

    /**
     * Crée un PDF personnalisé : le style vient du profil (police, couleur, taille)
     * et le contenu vient du projet (texte du QR code et image éventuelle).
     *
     * @param projet  le contenu du PDF
     * @param profil  le style du PDF
     * @param fichier le nom du fichier PDF à créer
     * @throws IllegalArgumentException si le texte est vide ou si l'image est introuvable / invalide
     * @throws WriterException si ZXing n'arrive pas à créer le QR code
     * @throws IOException si le fichier ne peut pas être écrit (ex. déjà ouvert)
     */
    public void creerPDF(Projet projet, Profil profil, String fichier) throws WriterException, IOException {
        // 1. on crée le QR code (refuse le texte vide)
        BufferedImage qr = creerImage(projet.getTexte());

        // 2. on vérifie l'image AVANT de créer le PDF, pour ne pas laisser un PDF à moitié écrit
        ImageData imageUtilisateur = null;
        if (projet.aUneImage()) {
            if (!new File(projet.getImage()).exists()) {
                throw new IllegalArgumentException("L'image choisie n'existe pas.");
            }
            try {
                imageUtilisateur = ImageDataFactory.create(projet.getImage());
            } catch (Exception e) {
                throw new IllegalArgumentException("Le fichier choisi n'est pas une image valide.");
            }
        }

        // 3. on prépare le style
        PdfFont police = PdfFontFactory.createFont(nomPolice(profil.getPolice()));
        Color couleur = couleur(profil.getCouleur());

        // 4. on écrit le PDF
        PdfDocument pdf = new PdfDocument(new PdfWriter(fichier));
        Document document = new Document(pdf);

        document.add(new Paragraph("Mon QR Code")
                .setFont(police).setFontColor(couleur).setFontSize(profil.getTaille() + 8)
                .setTextAlignment(TextAlignment.CENTER));

        if (imageUtilisateur != null && projet.getPosition().equals(Projet.POSITIONS[0])) {
            document.add(creerImagePDF(imageUtilisateur, projet.getLargeurImage()));
        }

        document.add(new Image(ImageDataFactory.create(enPNG(qr)))
                .setHorizontalAlignment(HorizontalAlignment.CENTER));

        if (imageUtilisateur != null && projet.getPosition().equals(Projet.POSITIONS[1])) {
            document.add(creerImagePDF(imageUtilisateur, projet.getLargeurImage()));
        }

        document.add(new Paragraph(projet.getTexte())
                .setFont(police).setFontColor(couleur).setFontSize(profil.getTaille())
                .setTextAlignment(TextAlignment.CENTER));

        document.close();
    }

    /**
     * Prépare l'image de l'utilisateur pour le PDF : largeur choisie, centrée.
     */
    private Image creerImagePDF(ImageData image, int largeur) {
        return new Image(image)
                .setWidth(largeur)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setMarginTop(10).setMarginBottom(10);
    }

    /**
     * Transforme le nom de police choisi en police iText.
     */
    private String nomPolice(String police) {
        switch (police) {
            case "Times":
                return StandardFonts.TIMES_ROMAN;
            case "Courier":
                return StandardFonts.COURIER;
            default:
                return StandardFonts.HELVETICA;
        }
    }

    /**
     * Transforme le nom de couleur choisi en couleur iText.
     */
    private Color couleur(String couleur) {
        switch (couleur) {
            case "Bleu":
                return ColorConstants.BLUE;
            case "Rouge":
                return ColorConstants.RED;
            case "Vert":
                return new DeviceRgb(0, 128, 0);
            default:
                return ColorConstants.BLACK;
        }
    }

    /**
     * Transforme une image Java en PNG, le format qu'iText sait lire.
     */
    private byte[] enPNG(BufferedImage image) throws IOException {
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        ImageIO.write(image, "png", png);
        return png.toByteArray();
    }
}
