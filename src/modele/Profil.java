package modele;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Un profil = le style du PDF : la police, la couleur, la taille et le style du texte
 * (gras, italique). On peut aussi utiliser sa propre police (fichier .ttf ou .otf).
 * Il peut être sauvegardé dans un fichier et rechargé plus tard.
 */
public class Profil {

    /** Les polices proposées. */
    public static final String[] POLICES = { "Helvetica", "Times", "Courier" };
    /** Les couleurs proposées. */
    public static final String[] COULEURS = { "Noir", "Bleu", "Rouge", "Vert" };
    /** Les styles proposés. */
    public static final String[] STYLES = { "Normal", "Gras", "Italique", "Gras italique" };

    private String police;
    private String couleur;
    private int taille;
    private String style;
    private String fichierPolice;

    /**
     * Crée un profil complet.
     *
     * @param police        "Helvetica", "Times" ou "Courier" (utilisée s'il n'y a pas de police perso)
     * @param couleur       "Noir", "Bleu", "Rouge" ou "Vert"
     * @param taille        taille du texte (ex. 14)
     * @param style         "Normal", "Gras", "Italique" ou "Gras italique"
     * @param fichierPolice chemin d'une police perso (.ttf / .otf), "" si on n'en veut pas
     */
    public Profil(String police, String couleur, int taille, String style, String fichierPolice) {
        this.police = police;
        this.couleur = couleur;
        this.taille = taille;
        this.style = style == null ? STYLES[0] : style;
        this.fichierPolice = fichierPolice == null ? "" : fichierPolice;
    }

    /**
     * Crée un profil simple : style normal et pas de police perso.
     *
     * @param police  "Helvetica", "Times" ou "Courier"
     * @param couleur "Noir", "Bleu", "Rouge" ou "Vert"
     * @param taille  taille du texte (ex. 14)
     */
    public Profil(String police, String couleur, int taille) {
        this(police, couleur, taille, STYLES[0], "");
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

    public String getStyle() {
        return style;
    }

    public String getFichierPolice() {
        return fichierPolice;
    }

    /**
     * @return true si le style contient du gras
     */
    public boolean estGras() {
        return style.contains("Gras");
    }

    /**
     * @return true si le style contient de l'italique
     */
    public boolean estItalique() {
        return style.contains("talique");
    }

    /**
     * @return true si l'utilisateur a choisi sa propre police
     */
    public boolean aUnePolicePerso() {
        return !fichierPolice.isBlank();
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
        reglages.setProperty("style", style);
        reglages.setProperty("fichierPolice", fichierPolice);
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
                Integer.parseInt(reglages.getProperty("taille", "14")),
                reglages.getProperty("style", STYLES[0]),
                reglages.getProperty("fichierPolice", ""));
    }
}
