# TP Java – Générateur de QR Code

## But de l'application

L'utilisateur tape un texte ou un lien, clique sur **Générer le PDF**, et l'application :

1. crée le QR code de ce texte ;
2. l'affiche dans la fenêtre ;
3. crée un fichier `qrcode.pdf` qui contient le QR code.

## Organisation MVC

| Paquet | Classe | Rôle |
|---|---|---|
| `vue` | `FrmQRCode` | La fenêtre : une zone de texte, un bouton, l'image et un message |
| `controleur` | `Controle` | Le `main`. Reçoit le clic de la vue, appelle le modèle, renvoie le résultat à la vue |
| `modele` | `GenerateurQRCode` | Crée l'image du QR code (ZXing) et le fichier PDF (iText) |

## Bibliothèques (dossier `lib/`)

- **ZXing** : crée le QR code.
- **iText** : crée le PDF. Il a besoin de SLF4J, Jackson et FastDoubleParser pour fonctionner.
- **JUnit 5** : tests unitaires.

## Gestion des erreurs

- **Texte vide** : le modèle lance une `IllegalArgumentException`, et le message « Erreur : Le texte est vide. » s'affiche.
- **PDF impossible à écrire** (par exemple déjà ouvert) : le message « Erreur : impossible de créer le PDF » s'affiche.

## Tests unitaires

4 tests dans `test/modele/GenerateurQRCodeTest.java` :

- l'image est bien créée et fait 300 x 300 pixels ;
- un texte vide est refusé pour l'image ;
- le fichier PDF est bien créé ;
- un texte vide est refusé pour le PDF.

## Lancer le projet

- **L'application** : ouvrir le dossier dans IntelliJ, puis lancer `controleur.Controle`.
- **Les tests** : clic droit sur le dossier `test` → **Run 'All Tests'**.
