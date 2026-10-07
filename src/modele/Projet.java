package modele;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Un projet = ce que l'utilisateur met dans son PDF :
 * un texte libre (facultatif), le lien du QR code et, s'il le veut,
 * une image (avec sa position et sa taille).
 * Il peut être sauvegardé dans un fichier et rechargé plus tard.
 */
public class Projet {

    /** Les positions possibles de l'image. */
    public static final String[] POSITIONS = { "Avant le QR code", "Après le QR code" };

    private String texte;
    private String lien;
    private String image;
    private String position;
    private int largeurImage;

    /**
     * Crée un projet avec un texte libre et un lien pour le QR code.
     *
     * @param texte        texte libre affiché dans le PDF ("" si pas de texte)
     * @param lien         le lien (ou texte) mis dans le QR code
     * @param image        chemin de l'image à ajouter ("" si pas d'image)
     * @param position     "Avant le QR code" ou "Après le QR code"
     * @param largeurImage largeur de l'image dans le PDF (ex. 200)
     */
    public Projet(String texte, String lien, String image, String position, int largeurImage) {
        this.texte = texte == null ? "" : texte;
        this.lien = lien;
        this.image = image;
        this.position = position;
        this.largeurImage = largeurImage;
    }

    /**
     * Crée un projet sans texte libre : seulement le lien du QR code.
     *
     * @param lien         le lien (ou texte) mis dans le QR code
     * @param image        chemin de l'image à ajouter ("" si pas d'image)
     * @param position     "Avant le QR code" ou "Après le QR code"
     * @param largeurImage largeur de l'image dans le PDF (ex. 200)
     */
    public Projet(String lien, String image, String position, int largeurImage) {
        this("", lien, image, position, largeurImage);
    }

    /**
     * @return le texte libre affiché dans le PDF ("" s'il n'y en a pas)
     */
    public String getTexte() {
        return texte;
    }

    /**
     * @return le lien (ou texte) mis dans le QR code
     */
    public String getLien() {
        return lien;
    }

    /**
     * @return true si l'utilisateur a écrit un texte libre
     */
    public boolean aUnTexte() {
        return !texte.isBlank();
    }

    public String getImage() {
        return image;
    }

    public String getPosition() {
        return position;
    }

    public int getLargeurImage() {
        return largeurImage;
    }

    /**
     * @return true si l'utilisateur a choisi une image
     */
    public boolean aUneImage() {
        return image != null && !image.isBlank();
    }

    /**
     * Sauvegarde le projet dans un fichier texte.
     *
     * @param fichier chemin du fichier à créer
     * @throws IOException si le fichier ne peut pas être écrit
     */
    public void sauvegarder(String fichier) throws IOException {
        Properties reglages = new Properties();
        reglages.setProperty("texte", texte);
        reglages.setProperty("lien", lien);
        reglages.setProperty("image", image);
        reglages.setProperty("position", position);
        reglages.setProperty("largeurImage", String.valueOf(largeurImage));
        try (Writer ecrivain = Files.newBufferedWriter(Paths.get(fichier))) {
            reglages.store(ecrivain, "Projet QR Code");
        }
    }

    /**
     * Charge un projet depuis un fichier sauvegardé avant.
     *
     * @param fichier chemin du fichier à lire
     * @return le projet lu
     * @throws IOException si le fichier n'existe pas ou ne peut pas être lu
     */
    public static Projet charger(String fichier) throws IOException {
        Properties reglages = new Properties();
        try (Reader lecteur = Files.newBufferedReader(Paths.get(fichier))) {
            reglages.load(lecteur);
        }
        // anciens fichiers (sans "lien") : le "texte" était le contenu du QR code
        String texte = reglages.getProperty("texte", "");
        String lien = reglages.getProperty("lien");
        if (lien == null) {
            lien = texte;
            texte = "";
        }
        return new Projet(
                texte,
                lien,
                reglages.getProperty("image", ""),
                reglages.getProperty("position", POSITIONS[1]),
                Integer.parseInt(reglages.getProperty("largeurImage", "200")));
    }
}
