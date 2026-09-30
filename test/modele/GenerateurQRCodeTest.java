package modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires du modèle.
 */
public class GenerateurQRCodeTest {

    private GenerateurQRCode generateur = new GenerateurQRCode();

    @Test
    public void testImageCreee() throws Exception {
        BufferedImage image = generateur.creerImage("Bonjour");
        assertNotNull(image);
        assertEquals(300, image.getWidth());
        assertEquals(300, image.getHeight());
    }

    @Test
    public void testTexteVideRefuse() {
        assertThrows(IllegalArgumentException.class, () -> generateur.creerImage(""));
    }

    @Test
    public void testPDFCree() throws Exception {
        File fichier = new File("test.pdf");
        generateur.creerPDF("https://www.google.fr", "test.pdf");

        assertTrue(fichier.exists());
        assertTrue(fichier.length() > 0);

        fichier.delete();   // on nettoie après le test
    }

    @Test
    public void testPDFTexteVideRefuse() {
        assertThrows(IllegalArgumentException.class, () -> generateur.creerPDF("", "test.pdf"));
    }
}
