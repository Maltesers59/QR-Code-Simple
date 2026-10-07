package modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la sauvegarde et du chargement des profils.
 */
public class ProfilTest {

    /**
     * Test 5 : un profil sauvegardé puis rechargé garde les mêmes réglages.
     */
    @Test
    public void testSauvegarderEtChargerProfil() throws Exception {
        Profil profil = new Profil("Courier", "Rouge", 18);
        profil.sauvegarder("test.profil");

        Profil profilCharge = Profil.charger("test.profil");
        assertEquals("Courier", profilCharge.getPolice());
        assertEquals("Rouge", profilCharge.getCouleur());
        assertEquals(18, profilCharge.getTaille());

        new File("test.profil").delete();   // on nettoie après le test
    }

    /**
     * Test 6 : charger un profil qui n'existe pas lance une erreur.
     */
    @Test
    public void testChargerProfilInexistant() {
        assertThrows(IOException.class, () -> Profil.charger("profil_qui_n_existe_pas.profil"));
    }

    /**
     * Test 12 : le style (gras, italique) et la police perso sont bien sauvegardés et rechargés.
     */
    @Test
    public void testSauvegarderStyleEtPolicePerso() throws Exception {
        Profil profil = new Profil("Times", "Bleu", 16, "Gras italique", "C:\\polices\\maPolice.ttf");
        profil.sauvegarder("test_style.profil");

        Profil profilCharge = Profil.charger("test_style.profil");
        assertEquals("Gras italique", profilCharge.getStyle());
        assertEquals("C:\\polices\\maPolice.ttf", profilCharge.getFichierPolice());
        assertTrue(profilCharge.estGras());
        assertTrue(profilCharge.estItalique());
        assertTrue(profilCharge.aUnePolicePerso());

        new File("test_style.profil").delete();   // on nettoie après le test
    }
}
