package modele;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la sauvegarde et du chargement des projets.
 */
public class ProjetTest {

    /**
     * Test 7 : un projet sauvegardé puis rechargé garde le même contenu.
     */
    @Test
    public void testSauvegarderEtChargerProjet() throws Exception {
        Projet projet = new Projet("Mon site préféré", "https://www.google.fr", "C:\\images\\logo.png", "Avant le QR code", 150);
        projet.sauvegarder("test.projet");

        Projet projetCharge = Projet.charger("test.projet");
        assertEquals("Mon site préféré", projetCharge.getTexte());
        assertEquals("https://www.google.fr", projetCharge.getLien());
        assertEquals("C:\\images\\logo.png", projetCharge.getImage());
        assertEquals("Avant le QR code", projetCharge.getPosition());
        assertEquals(150, projetCharge.getLargeurImage());

        new File("test.projet").delete();   // on nettoie après le test
    }
}
