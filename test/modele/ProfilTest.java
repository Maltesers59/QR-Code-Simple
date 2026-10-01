package modele;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

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
}
