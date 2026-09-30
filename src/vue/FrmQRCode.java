package vue;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controleur.Controle;

/**
 * La vue : la fenêtre de l'application.
 */
public class FrmQRCode extends JFrame {

    private JTextField txtTexte;
    private JLabel lblImage;
    private JLabel lblMessage;
    private Controle controle;

    /**
     * Construit la fenêtre.
     */
    public FrmQRCode(Controle controle) {
        this.controle = controle;

        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 440, 500);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTexte = new JLabel("Texte ou lien :");
        lblTexte.setBounds(20, 20, 100, 25);
        contentPane.add(lblTexte);

        txtTexte = new JTextField();
        txtTexte.setBounds(120, 20, 280, 25);
        contentPane.add(txtTexte);

        JButton btnGenerer = new JButton("Générer le PDF");
        btnGenerer.setBounds(120, 55, 280, 30);
        btnGenerer.addActionListener(e -> controle.demandeGenererPDF(txtTexte.getText()));
        contentPane.add(btnGenerer);

        lblImage = new JLabel();
        lblImage.setBounds(60, 100, 300, 300);
        contentPane.add(lblImage);

        lblMessage = new JLabel("");
        lblMessage.setBounds(20, 420, 400, 25);
        contentPane.add(lblMessage);
    }

    /**
     * Affiche l'image du QR code dans la fenêtre.
     */
    public void afficheQRCode(BufferedImage image) {
        lblImage.setIcon(new ImageIcon(image));
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
     * Affiche un message (réussite ou erreur) en bas de la fenêtre.
     */
    public void afficheMessage(String message) {
        lblMessage.setText(message);
    }
}
