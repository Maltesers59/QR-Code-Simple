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
    private static final String[] POSITIONS = { "Avant le QR code", "Après le QR code" };
    private static final Integer[] LARGEURS = { 100, 150, 200, 300 };

    private JTextField txtTexte;
    private JComboBox<String> cboPolice;
    private JComboBox<String> cboCouleur;
    private JComboBox<Integer> cboTaille;
    private JLabel lblNomImage;
    private JComboBox<String> cboPosition;
    private JComboBox<Integer> cboLargeur;
    private JLabel lblImage;
    private JLabel lblMessage;
    private String cheminImage = "";   // "" = pas d'image
    private Controle controle;

    /**
     * Construit la fenêtre.
     */
    public FrmQRCode(Controle controle) {
        this.controle = controle;

        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 760, 470);

        creerMenu();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        Font gras = new Font("Tahoma", Font.BOLD, 12);

        // ----- TEXTE -----
        JLabel lblTexte = new JLabel("Texte ou lien :");
        lblTexte.setBounds(20, 20, 100, 25);
        contentPane.add(lblTexte);

        txtTexte = new JTextField();
        txtTexte.setBounds(120, 20, 270, 25);
        contentPane.add(txtTexte);

        // ----- STYLE DU PDF (profil) -----
        JLabel lblStyle = new JLabel("Style du PDF");
        lblStyle.setFont(gras);
        lblStyle.setBounds(20, 60, 200, 20);
        contentPane.add(lblStyle);

        contentPane.add(etiquette("Police :", 20, 85));
        cboPolice = new JComboBox<>(POLICES);
        cboPolice.setBounds(120, 85, 270, 25);
        contentPane.add(cboPolice);

        contentPane.add(etiquette("Couleur :", 20, 115));
        cboCouleur = new JComboBox<>(COULEURS);
        cboCouleur.setBounds(120, 115, 270, 25);
        contentPane.add(cboCouleur);

        contentPane.add(etiquette("Taille :", 20, 145));
        cboTaille = new JComboBox<>(TAILLES);
        cboTaille.setSelectedItem(14);
        cboTaille.setBounds(120, 145, 270, 25);
        contentPane.add(cboTaille);

        // ----- IMAGE -----
        JLabel lblTitreImage = new JLabel("Image (facultatif)");
        lblTitreImage.setFont(gras);
        lblTitreImage.setBounds(20, 185, 200, 20);
        contentPane.add(lblTitreImage);

        JButton btnImage = new JButton("Choisir une image…");
        btnImage.setBounds(20, 210, 175, 25);
        btnImage.addActionListener(e -> cmdChoisirImage());
        contentPane.add(btnImage);

        JButton btnSansImage = new JButton("Retirer");
        btnSansImage.setBounds(200, 210, 90, 25);
        btnSansImage.addActionListener(e -> afficheImageChoisie(""));
        contentPane.add(btnSansImage);

        lblNomImage = new JLabel("Aucune image");
        lblNomImage.setBounds(298, 210, 110, 25);
        contentPane.add(lblNomImage);

        contentPane.add(etiquette("Position :", 20, 245));
        cboPosition = new JComboBox<>(POSITIONS);
        cboPosition.setSelectedIndex(1);
        cboPosition.setBounds(120, 245, 270, 25);
        contentPane.add(cboPosition);

        contentPane.add(etiquette("Largeur :", 20, 275));
        cboLargeur = new JComboBox<>(LARGEURS);
        cboLargeur.setSelectedItem(200);
        cboLargeur.setBounds(120, 275, 270, 25);
        contentPane.add(cboLargeur);

        // ----- BOUTON GÉNÉRER -----
        JButton btnGenerer = new JButton("Générer le PDF");
        btnGenerer.setBounds(20, 320, 370, 35);
        btnGenerer.addActionListener(e -> controle.demandeGenererPDF(
                txtTexte.getText(), cheminImage, getPosition(), getLargeur(),
                getPolice(), getCouleur(), getTaille()));
        contentPane.add(btnGenerer);

        // ----- QR CODE + MESSAGE -----
        lblImage = new JLabel();
        lblImage.setBounds(420, 20, 300, 300);
        contentPane.add(lblImage);

        lblMessage = new JLabel("");
        lblMessage.setBounds(20, 370, 700, 25);
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
                controle.demandeSauvegarderProjet(txtTexte.getText(), cheminImage,
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
                controle.demandeSauvegarderProfil(getPolice(), getCouleur(), getTaille(), fichier);
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
    public void afficheProjet(String texte, String image, String position, int largeur) {
        txtTexte.setText(texte);
        afficheImageChoisie(image);
        cboPosition.setSelectedItem(position);
        cboLargeur.setSelectedItem(largeur);
    }

    /**
     * Remplit la fenêtre avec un profil chargé.
     */
    public void afficheProfil(String police, String couleur, int taille) {
        cboPolice.setSelectedItem(police);
        cboCouleur.setSelectedItem(couleur);
        cboTaille.setSelectedItem(taille);
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

    private String getPosition() {
        return (String) cboPosition.getSelectedItem();
    }

    private int getLargeur() {
        return (Integer) cboLargeur.getSelectedItem();
    }
}
