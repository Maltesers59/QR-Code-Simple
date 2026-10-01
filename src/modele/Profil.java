package modele;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Un profil = le style du PDF : la police, la couleur et la taille du texte.
 * Il peut être sauvegardé dans un fichier et rechargé plus tard.
 */
public class Profil {

    /** Les polices proposées. */
    public static final String[] POLICES = { "Helvetica", "Times", "Courier" };
    /** Les couleurs proposées. */
    public static final String[] COULEURS = { "Noir", "Bleu", "Rouge", "Vert" };

    private String police;
    private String couleur;
    private int taille;

    /**
     * Crée un profil.
     *
     * @param police  "Helvetica", "Times" ou "Courier"
     * @param couleur "Noir", "Bleu", "Rouge" ou "Vert"
     * @param taille  taille du texte (ex. 14)
     */
    public Profil(String police, String couleur, int taille) {
        this.police = police;
        this.couleur = couleur;
        this.taille = taille;
    }

    public String getPolice() {
        return police;
    }

    public String getCouleur() {
        return couleur;
    }

    public int getTaille() {
        return taille;
    }

    /**
     * Sauvegarde le profil dans un fichier texte.
     * Le fichier contient une ligne par réglage, par exemple "police=Courier".
     *
     * @param fichier chemin du fichier à créer
     * @throws IOException si le fichier ne peut pas être écrit
     */
    public void sauvegarder(String fichier) throws IOException {
        Properties reglages = new Properties();
        reglages.setProperty("police", police);
        reglages.setProperty("couleur", couleur);
        reglages.setProperty("taille", String.valueOf(taille));
        try (Writer ecrivain = Files.newBufferedWriter(Paths.get(fichier))) {
            reglages.store(ecrivain, "Profil QR Code");
        }
    }

    /**
     * Charge un profil depuis un fichier sauvegardé avant.
     *
     * @param fichier chemin du fichier à lire
     * @return le profil lu
     * @throws IOException si le fichier n'existe pas ou ne peut pas être lu
     */
    public static Profil charger(String fichier) throws IOException {
        Properties reglages = new Properties();
        try (Reader lecteur = Files.newBufferedReader(Paths.get(fichier))) {
            reglages.load(lecteur);
        }
        return new Profil(
                reglages.getProperty("police", "Helvetica"),
                reglages.getProperty("couleur", "Noir"),
                Integer.parseInt(reglages.getProperty("taille", "14")));
    }
}
