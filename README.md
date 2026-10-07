# Compte rendu – TP Java : Générateur de QR Code

## 1. Présentation

Ce projet est une application Java faite avec Swing. Elle permet de créer un QR code à partir d'un lien, puis de le mettre dans un fichier PDF. Le PDF s'ouvre tout seul une fois créé.

Le TP était en deux parties : d'abord faire une appli qui marche (partie 1), puis l'améliorer en ajoutant de la personnalisation, des images et de la sauvegarde (partie 2).

Le projet respecte l'architecture MVC (Modèle – Vue – Contrôleur), comme demandé.

## 2. Partie 1 : l'application de base

Dans la première partie, j'ai fait le cœur de l'appli :

- La fenêtre : une zone pour écrire le lien, un bouton "Générer le PDF", l'aperçu du QR code et un message en bas pour dire si tout s'est bien passé.
- Le QR code : il est créé avec la bibliothèque ZXing, en 300 x 300 pixels.
- Le PDF : il est créé avec la bibliothèque iText. Il contient un titre et le QR code.
- Les erreurs : si le lien est vide ou si le PDF est déjà ouvert dans un autre logiciel, un message s'affiche au lieu de faire planter l'appli.
- Les tests : 4 tests unitaires avec JUnit 5 pour vérifier que l'image et le PDF sont bien créés, et qu'un lien vide est refusé.

## 3. Partie 2 : les améliorations

Dans la deuxième partie, j'ai ajouté plusieurs choses pour rendre l'appli plus complète :

- Personnaliser le PDF : on peut choisir la police (Helvetica, Times, Courier), la couleur (noir, bleu, rouge, vert) et la taille du texte.
- Un texte libre : en plus du lien, on peut écrire un texte qui s'affiche dans le PDF. Le lien, lui, est seulement dans le QR code.
- Ajouter une image : on peut choisir une image sur son PC, la placer avant ou après le QR code et choisir sa largeur.
- Sauvegarder et charger : avec le menu Fichier, on peut sauvegarder son travail et le reprendre plus tard. Un projet contient le contenu (texte, lien, image) et un profil contient le style (police, couleur, taille).
- Plus d'erreurs gérées : image introuvable, fichier qui n'est pas une image, projet ou profil impossible à charger…

Pour ça, j'ai créé deux nouvelles classes : Profil et Projet. Elles enregistrent les réglages dans un petit fichier texte grâce à la classe Properties de Java.

J'ai aussi ajouté 7 nouveaux tests, ce qui fait 11 tests au total, et ils passent tous.

## 4. Organisation du code (MVC)

Le code est rangé en 3 dossiers. La fenêtre et le modèle ne se parlent jamais directement : tout passe par le contrôleur.

- vue : FrmQRCode, c'est la fenêtre. Elle affiche tout et envoie les clics au contrôleur.
- controleur : Controle, il lance l'appli et fait le lien entre la fenêtre et le modèle.
- modele : GenerateurQRCode crée le QR code et le PDF, Profil gère le style du PDF et Projet gère son contenu.

## 5. Bibliothèques utilisées

Elles sont toutes dans le dossier lib :

- ZXing pour créer les QR codes
- iText pour créer les PDF
- SLF4J et Jackson, dont iText a besoin pour fonctionner
- JUnit 5 pour les tests

## 6. Difficultés rencontrées

- iText a besoin de plusieurs autres bibliothèques pour marcher, il a fallu toutes les ajouter au projet.
- ZXing donne une image Java qu'iText ne sait pas lire directement, donc il faut d'abord la convertir en PNG.
- Windows bloque l'écriture d'un PDF déjà ouvert, donc j'ai prévu un message d'erreur pour ce cas.
- Il faut vérifier l'image avant de commencer le PDF, sinon on se retrouve avec un PDF à moitié écrit.

## 7. Lancer le projet

Pour lancer l'appli, il suffit d'ouvrir le projet dans IntelliJ et de lancer la classe Controle avec la flèche verte. Pour les tests, il faut faire un clic droit sur le dossier test puis "Run 'All Tests'".
