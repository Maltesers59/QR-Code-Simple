package controleur;

import java.awt.image.BufferedImage;

import modele.GenerateurQRCode;
import vue.FrmQRCode;

/**
 * Le contrôleur : il fait le lien entre la vue et le modèle.
 */
public class Controle {

    private FrmQRCode frmQRCode;
    private GenerateurQRCode generateur = new GenerateurQRCode();

    /**
     * Point de départ du programme.
     */
    public static void main(String[] args) {
        new Controle();
    }

    /**
     * Ouvre la fenêtre.
     */
    public Controle() {
        frmQRCode = new FrmQRCode(this);
        frmQRCode.setVisible(true);
    }

    /**
     * Demande de la vue : créer le PDF avec le QR code du texte saisi.
     *
     * @param texte le texte tapé par l'utilisateur
     */
    public void demandeGenererPDF(String texte) {
        try {
            generateur.creerPDF(texte, "qrcode.pdf");
            BufferedImage image = generateur.creerImage(texte);
            frmQRCode.afficheQRCode(image);
            frmQRCode.afficheMessage("PDF créé : qrcode.pdf (dans le dossier du projet)");
            frmQRCode.ouvrirPDF("qrcode.pdf");   // le PDF s'ouvre tout seul
        } catch (IllegalArgumentException e) {
            frmQRCode.afficheMessage("Erreur : " + e.getMessage());   // texte vide
        } catch (Exception e) {
            frmQRCode.afficheMessage("Erreur : impossible de créer le PDF. Est-il déjà ouvert ?");
        }
    }
}
