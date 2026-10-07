package vue;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import controleur.Controle;

/**
 * La vue : la fenêtre de l'application.
 */
public class FrmQRCode extends JFrame {

    private static final String[] POLICES = { "Helvetica", "Times", "Courier" };
    private static final String[] COULEURS = { "Noir", "Bleu", "Rouge", "Vert" };
    private static final Integer[] TAILLES = { 12, 14, 16, 18, 20 };
    private static final String[] STYLES = { "Normal", "Gras", "Italique", "Gras italique" };
    private static final String[] POSITIONS = { "Avant le QR code", "Après le QR code" };
    private static final Integer[] LARGEURS = { 100, 150, 200, 300 };

    private JTextField txtTexte;
    private JTextField txtLien;
    private JComboBox<String> cboPolice;
    private JComboBox<String> cboCouleur;
    private JComboBox<Integer> cboTaille;
    private JComboBox<String> cboStyle;
    private JLabel lblNomPolice;
    private JLabel lblNomImage;
    private JComboBox<String> cboPosition;
    private JComboBox<Integer> cboLargeur;
    private JLabel lblImage;
    private JLabel lblMessage;
    private String cheminImage = "";   // "" = pas d'image
    private String cheminPolice = "";  // "" = police de base
    private Controle controle;

    /**
     * Construit la fenêtre.
     */
    public FrmQRCode(Controle controle) {
        this.controle = controle;

        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 760, 560);

        creerMenu();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        Font gras = new Font("Tahoma", Font.BOLD, 12);

        // ----- TEXTE ET LIEN -----
        contentPane.add(etiquette("Texte :", 20, 20));
        txtTexte = new JTextField();
        txtTexte.setToolTipText("Texte libre affiché dans le PDF (facultatif)");
        txtTexte.setBounds(120, 20, 270, 25);
        contentPane.add(txtTexte);

        contentPane.add(etiquette("Lien du QR :", 20, 50));
        txtLien = new JTextField();
        txtLien.setToolTipText("Lien (ou texte) mis dans le QR code");
        txtLien.setBounds(120, 50, 270, 25);
        contentPane.add(txtLien);

        // ----- STYLE DU PDF (profil) -----
        JLabel lblStyle = new JLabel("Style du PDF");
        lblStyle.setFont(gras);
        lblStyle.setBounds(20, 90, 200, 20);
        contentPane.add(lblStyle);

        contentPane.add(etiquette("Police :", 20, 115));
        cboPolice = new JComboBox<>(POLICES);
        cboPolice.setBounds(120, 115, 270, 25);
        contentPane.add(cboPolice);

        contentPane.add(etiquette("Couleur :", 20, 145));
        cboCouleur = new JComboBox<>(COULEURS);
        cboCouleur.setBounds(120, 145, 270, 25);
        contentPane.add(cboCouleur);

        contentPane.add(etiquette("Taille :", 20, 175));
        cboTaille = new JComboBox<>(TAILLES);
        cboTaille.setSelectedItem(14);
        cboTaille.setBounds(120, 175, 270, 25);
        contentPane.add(cboTaille);

        contentPane.add(etiquette("Style :", 20, 205));
        cboStyle = new JComboBox<>(STYLES);
        cboStyle.setBounds(120, 205, 270, 25);
        contentPane.add(cboStyle);

        JButton btnPolice = new JButton("Ma police…");
        btnPolice.setToolTipText("Utiliser sa propre police (fichier .ttf ou .otf)");
        btnPolice.setBounds(20, 235, 175, 25);
        btnPolice.addActionListener(e -> cmdChoisirPolice());
        contentPane.add(btnPolice);

        JButton btnSansPolice = new JButton("Retirer");
        btnSansPolice.setBounds(200, 235, 90, 25);
        btnSansPolice.addActionListener(e -> affichePoliceChoisie(""));
        contentPane.add(btnSansPolice);

        lblNomPolice = new JLabel("Police de base");
        lblNomPolice.setBounds(298, 235, 110, 25);
        contentPane.add(lblNomPolice);

        // ----- IMAGE -----
        JLabel lblTitreImage = new JLabel("Image (facultatif)");
        lblTitreImage.setFont(gras);
        lblTitreImage.setBounds(20, 275, 200, 20);
        contentPane.add(lblTitreImage);

        JButton btnImage = new JButton("Choisir une image…");
        btnImage.setBounds(20, 300, 175, 25);
        btnImage.addActionListener(e -> cmdChoisirImage());
        contentPane.add(btnImage);

        JButton btnSansImage = new JButton("Retirer");
        btnSansImage.setBounds(200, 300, 90, 25);
        btnSansImage.addActionListener(e -> afficheImageChoisie(""));
        contentPane.add(btnSansImage);

        lblNomImage = new JLabel("Aucune image");
        lblNomImage.setBounds(298, 300, 110, 25);
        contentPane.add(lblNomImage);

        contentPane.add(etiquette("Position :", 20, 335));
        cboPosition = new JComboBox<>(POSITIONS);
        cboPosition.setSelectedIndex(1);
        cboPosition.setBounds(120, 335, 270, 25);
        contentPane.add(cboPosition);

        contentPane.add(etiquette("Largeur :", 20, 365));
        cboLargeur = new JComboBox<>(LARGEURS);
        cboLargeur.setSelectedItem(200);
        cboLargeur.setBounds(120, 365, 270, 25);
        contentPane.add(cboLargeur);

        // ----- BOUTON GÉNÉRER -----
        JButton btnGenerer = new JButton("Générer le PDF");
        btnGenerer.setBounds(20, 410, 370, 35);
        btnGenerer.addActionListener(e -> controle.demandeGenererPDF(
                txtTexte.getText(), txtLien.getText(), cheminImage, getPosition(), getLargeur(),
                getPolice(), getCouleur(), getTaille(), getStyle(), cheminPolice));
        contentPane.add(btnGenerer);

        // ----- QR CODE + MESSAGE -----
        lblImage = new JLabel();
        lblImage.setBounds(420, 50, 300, 300);
        contentPane.add(lblImage);

        lblMessage = new JLabel("");
        lblMessage.setBounds(20, 460, 700, 25);
        contentPane.add(lblMessage);
    }

    /**
     * Crée le menu « Fichier » (sauvegarde et chargement).
     */
    private void creerMenu() {
        JMenuBar barre = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");

        JMenuItem sauverProjet = new JMenuItem("Sauvegarder le projet…");
        sauverProjet.addActionListener(e -> {
            String fichier = choisirFichier("projet", true);
            if (fichier != null) {
                controle.demandeSauvegarderProjet(txtTexte.getText(), txtLien.getText(), cheminImage,
                        getPosition(), getLargeur(), fichier);
            }
        });

        JMenuItem chargerProjet = new JMenuItem("Charger un projet…");
        chargerProjet.addActionListener(e -> {
            String fichier = choisirFichier("projet", false);
            if (fichier != null) {
                controle.demandeChargerProjet(fichier);
            }
        });

        JMenuItem sauverProfil = new JMenuItem("Sauvegarder le profil…");
        sauverProfil.addActionListener(e -> {
            String fichier = choisirFichier("profil", true);
            if (fichier != null) {
                controle.demandeSauvegarderProfil(getPolice(), getCouleur(), getTaille(), getStyle(),
                        cheminPolice, fichier);
            }
        });

        JMenuItem chargerProfil = new JMenuItem("Charger un profil…");
        chargerProfil.addActionListener(e -> {
            String fichier = choisirFichier("profil", false);
            if (fichier != null) {
                controle.demandeChargerProfil(fichier);
            }
        });

        menuFichier.add(sauverProjet);
        menuFichier.add(chargerProjet);
        menuFichier.addSeparator();
        menuFichier.add(sauverProfil);
        menuFichier.add(chargerProfil);
        barre.add(menuFichier);
        setJMenuBar(barre);
    }

    // ----- ACTIONS -----

    /**
     * Ouvre une fenêtre pour choisir l'image à ajouter au PDF.
     */
    private void cmdChoisirImage() {
        JFileChooser choix = new JFileChooser();
        choix.setFileFilter(new FileNameExtensionFilter("Images (png, jpg)", "png", "jpg", "jpeg"));
        if (choix.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            afficheImageChoisie(choix.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Ouvre une fenêtre pour choisir sa propre police (.ttf ou .otf).
     */
    private void cmdChoisirPolice() {
        JFileChooser choix = new JFileChooser();
        choix.setFileFilter(new FileNameExtensionFilter("Polices (ttf, otf)", "ttf", "otf"));
        if (choix.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            affichePoliceChoisie(choix.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Ouvre une fenêtre pour choisir un fichier de projet ou de profil.
     *
     * @param extension  "projet" ou "profil"
     * @param enregistrer true pour sauvegarder, false pour ouvrir
     * @return le chemin du fichier, ou null si l'utilisateur a annulé
     */
    private String choisirFichier(String extension, boolean enregistrer) {
        JFileChooser choix = new JFileChooser();
        choix.setFileFilter(new FileNameExtensionFilter("Fichier " + extension + " (*." + extension + ")", extension));
        int reponse = enregistrer ? choix.showSaveDialog(this) : choix.showOpenDialog(this);
        if (reponse != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        String chemin = choix.getSelectedFile().getAbsolutePath();
        if (enregistrer && !chemin.endsWith("." + extension)) {
            chemin += "." + extension;   // on ajoute l'extension si elle manque
        }
        return chemin;
    }

    // ----- AFFICHAGES DEMANDÉS PAR LE CONTRÔLEUR -----

    /**
     * Affiche l'image du QR code dans la fenêtre.
     */
    public void afficheQRCode(BufferedImage image) {
        lblImage.setIcon(new ImageIcon(image));
    }

    /**
     * Remplit la fenêtre avec un projet chargé.
     */
    public void afficheProjet(String texte, String lien, String image, String position, int largeur) {
        txtTexte.setText(texte);
        txtLien.setText(lien);
        afficheImageChoisie(image);
        cboPosition.setSelectedItem(position);
        cboLargeur.setSelectedItem(largeur);
    }

    /**
     * Remplit la fenêtre avec un profil chargé.
     */
    public void afficheProfil(String police, String couleur, int taille, String style, String fichierPolice) {
        cboPolice.setSelectedItem(police);
        cboCouleur.setSelectedItem(couleur);
        cboTaille.setSelectedItem(taille);
        cboStyle.setSelectedItem(style);
        affichePoliceChoisie(fichierPolice);
    }

    /**
     * Ouvre le PDF avec le lecteur PDF de l'ordinateur.
     */
    public void ouvrirPDF(String fichier) {
        try {
            Desktop.getDesktop().open(new File(fichier));
        } catch (Exception e) {
            afficheMessage("PDF créé, mais impossible de l'ouvrir automatiquement.");
        }
    }

    /**
     * Affiche un message (réussite) en bas de la fenêtre.
     */
    public void afficheMessage(String message) {
        lblMessage.setText(message);
    }

    /**
     * Affiche un message d'erreur dans une petite fenêtre et en bas de la fenêtre.
     */
    public void afficheErreur(String message) {
        lblMessage.setText("Erreur : " + message);
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // ----- OUTILS -----

    private void afficheImageChoisie(String chemin) {
        cheminImage = chemin;
        lblNomImage.setText(chemin.isEmpty() ? "Aucune image" : new File(chemin).getName());
    }

    private void affichePoliceChoisie(String chemin) {
        cheminPolice = chemin;
        lblNomPolice.setText(chemin.isEmpty() ? "Police de base" : new File(chemin).getName());
    }

    private JLabel etiquette(String texte, int x, int y) {
        JLabel label = new JLabel(texte);
        label.setBounds(x, y, 100, 25);
        return label;
    }

    private String getPolice() {
        return (String) cboPolice.getSelectedItem();
    }

    private String getCouleur() {
        return (String) cboCouleur.getSelectedItem();
    }

    private int getTaille() {
        return (Integer) cboTaille.getSelectedItem();
    }

    private String getStyle() {
        return (String) cboStyle.getSelectedItem();
    }

    private String getPosition() {
        return (String) cboPosition.getSelectedItem();
    }

    private int getLargeur() {
        return (Integer) cboLargeur.getSelectedItem();
    }
}
