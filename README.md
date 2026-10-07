# TP Java – Générateur de QR Code

## C'est quoi ce projet ?

Pour ce TP, j'ai fait une petite appli en Java (avec Swing) qui transforme ce que tu tapes en QR code, puis qui le met dans un fichier PDF. Tu peux y mettre du texte, un lien, une adresse mail ou un numéro de téléphone.

![Fenêtre de l'application](doc/fenetre.png)

## Comment ça marche ?

1. On choisit le type (texte, lien, mail ou téléphone).
2. On tape l'info.
3. On clique sur **Aperçu** pour voir le QR code.
4. On clique sur **Générer le PDF…**, on choisit où l'enregistrer, et c'est bon.

Le PDF contient un titre, le QR code, ce qu'il y a dedans et la date.

![Exemple de PDF](doc/exemple-pdf.png)

## Comment j'ai organisé le code

On devait utiliser le modèle **MVC**, donc j'ai séparé le code en 3 parties :

- **vue** → `FrmQRCode` : la fenêtre. Elle affiche les choses et envoie les clics au contrôleur, c'est tout.
- **controleur** → `Controle` : il lance l'appli et fait le lien entre la fenêtre et le modèle.
- **modele** → là où tout le vrai travail se fait :
  - `TypeContenu` vérifie ce qu'on a tapé et le met au bon format (par exemple `www.google.fr` devient `https://www.google.fr`, un mail devient `mailto:...`)
  - `GenerateurQRCode` crée l'image du QR code
  - `GenerateurPDF` crée le fichier PDF
  - `QRCodeException` sert à renvoyer des messages d'erreur clairs

## Les bibliothèques utilisées

- **ZXing** pour fabriquer les QR codes
- **iText** pour créer les PDF (il a besoin de SLF4J et Jackson pour marcher, ils sont aussi dans `lib/`)
- **JUnit 5** pour les tests

## Les erreurs

J'ai fait en sorte que l'appli ne plante jamais. Si quelque chose ne va pas, un message s'affiche pour expliquer le problème, par exemple :

- rien n'a été tapé
- le lien, le mail ou le numéro n'est pas valide
- le texte est trop long (plus de 1000 caractères)
- le PDF est déjà ouvert dans un autre logiciel ou protégé en écriture

## Les tests

J'ai écrit **28 tests unitaires** avec JUnit 5, et ils passent tous. Ils vérifient surtout :

- que la saisie est bien vérifiée et formatée
- que le QR code créé se relit bien et redonne exactement le texte de départ (même avec des accents)
- que le PDF est bien créé et contient le bon texte

## Lancer le projet

1. Ouvrir le dossier dans **IntelliJ IDEA**.
2. Lancer la classe `controleur.Controle`.
3. Pour les tests : clic droit sur le dossier `test` → **Run 'All Tests'**.

## Ce qui m'a posé problème

- Faire marcher iText, parce qu'il a besoin de plusieurs autres bibliothèques à côté.
- Comprendre qu'un QR code ne peut pas contenir un texte infini, d'où la limite à 1000 caractères.

## Ce qu'on pourrait ajouter

- Choisir la couleur ou la taille du QR code
- Ajouter une image ou changer la police dans le PDF
- Sauvegarder ses réglages pour les retrouver plus tard

Pour plus de détails, il y a aussi le [rapport complet](RAPPORT.md).
