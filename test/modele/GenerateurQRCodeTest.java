package modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires du modèle.
 */
public class GenerateurQRCodeTest {

    private GenerateurQRCode generateur = new GenerateurQRCode();

    /**
     * Test 1 : l'image du QR code est bien créée.
     * On vérifie qu'elle existe, qu'elle fait 300 x 300 pixels
     * et qu'elle contient bien des carrés noirs (sinon ce serait une image vide).
     */
    @Test
    public void testImageCreee() throws Exception {
        BufferedImage image = generateur.creerImage("Bonjour");
        assertNotNull(image);
        assertEquals(300, image.getWidth());
        assertEquals(300, image.getHeight());

        // le pixel au milieu du carré de repère en haut à gauche doit être noir
        assertEquals(0xFF000000, image.getRGB(80, 80));
    }

    /**
     * Test 2 : un texte vide est refusé pour l'image.
     * Texte vide, texte avec seulement des espaces, ou pas de texte du tout (null) :
     * dans les 3 cas, le modèle doit lancer une IllegalArgumentException.
     */
    @Test
    public void testTexteVideRefuse() {
        assertThrows(IllegalArgumentException.class, () -> generateur.creerImage(""));
        assertThrows(IllegalArgumentException.class, () -> generateur.creerImage("   "));
        assertThrows(IllegalArgumentException.class, () -> generateur.creerImage(null));
    }

    /**
     * Test 3 : le fichier PDF est bien créé.
     * On vérifie que le fichier existe, qu'il n'est pas vide,
     * et que c'est bien un PDF (un vrai PDF commence toujours par "%PDF").
     */
    @Test
    public void testPDFCree() throws Exception {
        File fichier = new File("test.pdf");
        generateur.creerPDF("https://www.google.fr", "test.pdf");

        assertTrue(fichier.exists());
        assertTrue(fichier.length() > 0);

        byte[] contenu = Files.readAllBytes(fichier.toPath());
        assertEquals("%PDF", new String(contenu, 0, 4));

        fichier.delete();   // on nettoie après le test
    }

    /**
     * Test 4 : un texte vide est refusé pour le PDF.
     * Le modèle doit lancer une IllegalArgumentException
     * et surtout ne pas créer de fichier PDF vide.
     */
    @Test
    public void testPDFTexteVideRefuse() {
        File fichier = new File("vide.pdf");
        assertThrows(IllegalArgumentException.class, () -> generateur.creerPDF("", "vide.pdf"));
        assertFalse(fichier.exists());
    }

    /**
     * Test 8 : un PDF personnalisé (police, couleur, taille et image) est bien créé.
     */
    @Test
    public void testPDFPersonnaliseAvecImage() throws Exception {
        // on fabrique une petite image pour le test (on réutilise un QR code)
        File image = new File("test_image.png");
        ImageIO.write(generateur.creerImage("image"), "png", image);

        Projet projet = new Projet("https://www.google.fr", "test_image.png", "Avant le QR code", 150);
        Profil profil = new Profil("Courier", "Rouge", 16);
        File fichier = new File("test_perso.pdf");
        generateur.creerPDF(projet, profil, "test_perso.pdf");

        assertTrue(fichier.exists());
        byte[] contenu = Files.readAllBytes(fichier.toPath());
        assertEquals("%PDF", new String(contenu, 0, 4));

        fichier.delete();   // on nettoie après le test
        image.delete();
    }

    /**
     * Test 9 : une image qui n'existe pas est refusée, et aucun PDF n'est créé.
     */
    @Test
    public void testImageIntrouvableRefusee() {
        Projet projet = new Projet("https://www.google.fr", "image_qui_n_existe_pas.png", "Avant le QR code", 150);
        Profil profil = new Profil("Helvetica", "Noir", 14);
        File fichier = new File("test_sans_image.pdf");

        assertThrows(IllegalArgumentException.class, () -> generateur.creerPDF(projet, profil, "test_sans_image.pdf"));
        assertFalse(fichier.exists());
    }
}
