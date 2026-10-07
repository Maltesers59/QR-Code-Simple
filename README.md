# TP Java – Générateur de QR Code

## Le projet

C'est une appli Java (Swing) qui crée un QR code à partir d'un lien et le met dans un PDF. Le projet utilise le modèle **MVC**.

## Partie 1

- Une fenêtre pour écrire un lien
- Le QR code est créé avec **ZXing**
- Le PDF est créé avec **iText**, puis il s'ouvre tout seul
- Si le lien est vide ou si le PDF est déjà ouvert, un message d'erreur s'affiche au lieu de planter
- 4 tests unitaires avec **JUnit 5**

## Partie 2

- **Style du PDF** : on choisit la police, la couleur et la taille du texte
- **Texte libre** : on peut écrire un texte qui s'affiche dans le PDF, à part du lien
- **Image** : on peut ajouter une image, avant ou après le QR code, et choisir sa largeur
- **Sauvegarde** : le menu Fichier permet de sauvegarder et recharger un projet (texte, lien, image) et un profil (police, couleur, taille)
- 7 nouveaux tests, donc **11 tests** en tout, et ils passent tous

## Organisation du code (MVC)

- **vue** → `FrmQRCode` : la fenêtre
- **controleur** → `Controle` : fait le lien entre la fenêtre et le modèle
- **modele** → `GenerateurQRCode` (QR code + PDF), `Profil` (le style), `Projet` (le contenu)

## Difficultés

- iText a besoin d'autres bibliothèques pour marcher (SLF4J, Jackson…)
- L'image du QR code doit être convertie en PNG pour qu'iText puisse la lire
- Il faut vérifier l'image avant de créer le PDF, sinon le PDF reste à moitié écrit

## Lancer le projet

Ouvrir le projet dans IntelliJ et lancer `controleur.Controle`. Pour les tests : clic droit sur `test` → **Run 'All Tests'**.
