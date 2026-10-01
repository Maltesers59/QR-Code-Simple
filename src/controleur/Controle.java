package controleur;

import java.awt.image.BufferedImage;

import modele.GenerateurQRCode;
import modele.Profil;
import modele.Projet;
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
     * Demande de la vue : créer le PDF avec le QR code, le style et l'image choisis.
     */
    public void demandeGenererPDF(String texte, String image, String position, int largeurImage,
                                  String police, String couleur, int taille) {
        try {
            Projet projet = new Projet(texte, image, position, largeurImage);
            Profil profil = new Profil(police, couleur, taille);
            generateur.creerPDF(projet, profil, "qrcode.pdf");

            BufferedImage qr = generateur.creerImage(texte);
            frmQRCode.afficheQRCode(qr);
            frmQRCode.afficheMessage("PDF créé : qrcode.pdf (dans le dossier du projet)");
            frmQRCode.ouvrirPDF("qrcode.pdf");
        } catch (IllegalArgumentException e) {
            frmQRCode.afficheErreur(e.getMessage());   // texte vide, image introuvable...
        } catch (Exception e) {
            frmQRCode.afficheErreur("Impossible de créer le PDF. Est-il déjà ouvert ?");
        }
    }

    /**
     * Demande de la vue : sauvegarder le projet (texte + image) dans un fichier.
     */
    public void demandeSauvegarderProjet(String texte, String image, String position, int largeurImage,
                                         String fichier) {
        try {
            new Projet(texte, image, position, largeurImage).sauvegarder(fichier);
            frmQRCode.afficheMessage("Projet sauvegardé : " + fichier);
        } catch (Exception e) {
            frmQRCode.afficheErreur("Impossible de sauvegarder le projet.");
        }
    }

    /**
     * Demande de la vue : charger un projet sauvegardé.
     */
    public void demandeChargerProjet(String fichier) {
        try {
            Projet projet = Projet.charger(fichier);
            frmQRCode.afficheProjet(projet.getTexte(), projet.getImage(),
                    projet.getPosition(), projet.getLargeurImage());
            frmQRCode.afficheMessage("Projet chargé : " + fichier);
        } catch (Exception e) {
            frmQRCode.afficheErreur("Impossible de charger ce projet. Le fichier est-il correct ?");
        }
    }

    /**
     * Demande de la vue : sauvegarder le profil (police, couleur, taille) dans un fichier.
     */
    public void demandeSauvegarderProfil(String police, String couleur, int taille, String fichier) {
        try {
            new Profil(police, couleur, taille).sauvegarder(fichier);
            frmQRCode.afficheMessage("Profil sauvegardé : " + fichier);
        } catch (Exception e) {
            frmQRCode.afficheErreur("Impossible de sauvegarder le profil.");
        }
    }

    /**
     * Demande de la vue : charger un profil sauvegardé.
     */
    public void demandeChargerProfil(String fichier) {
        try {
            Profil profil = Profil.charger(fichier);
            frmQRCode.afficheProfil(profil.getPolice(), profil.getCouleur(), profil.getTaille());
            frmQRCode.afficheMessage("Profil chargé : " + fichier);
        } catch (Exception e) {
            frmQRCode.afficheErreur("Impossible de charger ce profil. Le fichier est-il correct ?");
        }
    }
}
