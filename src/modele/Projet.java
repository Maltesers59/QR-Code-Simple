package modele;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Un projet = ce que l'utilisateur met dans son PDF :
 * le texte du QR code et, s'il le veut, une image (avec sa position et sa taille).
 * Il peut être sauvegardé dans un fichier et rechargé plus tard.
 */
public class Projet {

    /** Les positions possibles de l'image. */
    public static final String[] POSITIONS = { "Avant le QR code", "Après le QR code" };

    private String texte;
    private String image;
    private String position;
    private int largeurImage;

    /**
     * Crée un projet.
     *
     * @param texte        le texte ou le lien du QR code
     * @param image        chemin de l'image à ajouter ("" si pas d'image)
     * @param position     "Avant le QR code" ou "Après le QR code"
     * @param largeurImage largeur de l'image dans le PDF (ex. 200)
     */
    public Projet(String texte, String image, String position, int largeurImage) {
        this.texte = texte;
        this.image = image;
        this.position = position;
        this.largeurImage = largeurImage;
    }

    public String getTexte() {
        return texte;
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
        return new Projet(
                reglages.getProperty("texte", ""),
                reglages.getProperty("image", ""),
                reglages.getProperty("position", POSITIONS[1]),
                Integer.parseInt(reglages.getProperty("largeurImage", "200")));
    }
}
