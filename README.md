# Compte rendu – TP Java : Générateur de QR Code (Partie 1 et 2)

## 1. Présentation

Ce projet est une application Java qui génère des QR codes.

L'utilisateur saisit un texte ou un lien dans une fenêtre, puis clique sur **Générer le PDF**. L'application :

1. crée le QR code correspondant au texte ;
2. l'affiche dans la fenêtre ;
3. crée un fichier `qrcode.pdf` contenant le QR code ;
4. ouvre automatiquement ce PDF.

Le projet respecte l'architecture **MVC** (Modèle – Vue – Contrôleur).

---

## 2. Réalisation des tâches du sujet (Partie 1)

| Tâche | Réalisation |
| --- | --- |
| 1. Interface Java Swing | Fenêtre avec une zone de saisie, un bouton, l'image du QR code et un message |
| 2. Génération de PDF | Bibliothèque **iText** |
| 3. Génération de QR codes | Bibliothèque **ZXing** |
| 4. QR code intégré au PDF | Le PDF contient un titre, le QR code et le texte saisi |
| 5. Gestion des erreurs | Un message s'affiche au lieu de faire planter l'application |
| 6. Tests unitaires | 4 tests avec **JUnit 5**, un commit par test |
| 7. Documentation | Code commenté et ce compte rendu |

---

## 3. Architecture MVC

Le code est séparé en 3 parties. La Vue et le Modèle ne communiquent jamais directement : tout passe par le Contrôleur.

| Partie | Fichier | Rôle |
| --- | --- | --- |
| **Vue** | `vue/FrmQRCode.java` | La fenêtre : elle affiche les éléments et transmet le clic au Contrôleur. Elle ne fait aucun calcul. |
| **Contrôleur** | `controleur/Controle.java` | Contient le `main`. Il reçoit la demande de la Vue, fait travailler le Modèle, puis indique à la Vue quoi afficher. |
| **Modèle** | `modele/GenerateurQRCode.java` | Fait le vrai travail : il crée l'image du QR code et le fichier PDF. |

### Déroulement d'un clic sur « Générer le PDF »

1. La Vue récupère le texte saisi et appelle `demandeGenererPDF(texte)` du Contrôleur.
2. Le Contrôleur appelle `creerPDF(texte, "qrcode.pdf")` du Modèle.
3. Le Modèle crée l'image du QR code avec ZXing, la convertit en PNG, puis crée le PDF avec iText.
4. Le Contrôleur demande à la Vue d'afficher l'image et le message de réussite, puis d'ouvrir le PDF.
5. En cas de problème, le Contrôleur demande à la Vue d'afficher un message d'erreur.

---

## 4. Le Modèle en détail

La classe `GenerateurQRCode` contient deux méthodes.

**`creerImage(texte)`**

- Elle vérifie que le texte n'est pas vide. Sinon, elle lance une `IllegalArgumentException`.
- Elle utilise ZXing pour créer un QR code de 300 x 300 pixels et le renvoie sous forme d'image.

**`creerPDF(texte, fichier)`**

- Elle appelle `creerImage(texte)` pour obtenir le QR code.
- Elle convertit l'image au format PNG, un format qu'iText sait lire.
- Elle crée le PDF avec un titre, le QR code et le texte, puis ferme le document pour l'enregistrer.

---

## 5. Bibliothèques utilisées

Java ne sait pas créer de QR code ni de PDF sans aide. Les bibliothèques nécessaires sont dans le dossier `lib/` et déclarées dans le fichier `QRCodeSimple.iml`.

| Bibliothèque | Utilité |
| --- | --- |
| ZXing 3.5.4 | Création du QR code |
| iText 9.8.0 | Création du PDF |
| SLF4J, Jackson, FastDoubleParser | Nécessaires au fonctionnement d'iText |
| JUnit 5 | Tests unitaires |

---

## 6. Gestion des erreurs

| Situation | Message affiché |
| --- | --- |
| Le texte est vide (ou ne contient que des espaces) | « Erreur : Le texte est vide. » |
| Le PDF ne peut pas être créé (par exemple, il est déjà ouvert) | « Erreur : impossible de créer le PDF. Est-il déjà ouvert ? » |
| Le PDF est créé mais ne peut pas s'ouvrir automatiquement | « PDF créé, mais impossible de l'ouvrir automatiquement. » |

Dans tous les cas, l'application continue de fonctionner.

---

## 7. Tests unitaires

Les tests portent sur le Modèle, car c'est lui qui contient le traitement. Ils se trouvent dans `test/modele/GenerateurQRCodeTest.java`.

| Test | Ce qui est vérifié |
| --- | --- |
| `testImageCreee` | L'image est créée, mesure 300 x 300 pixels et contient bien du noir |
| `testTexteVideRefuse` | Un texte vide, composé d'espaces ou `null` est refusé pour l'image |
| `testPDFCree` | Le fichier PDF est créé, n'est pas vide et commence bien par `%PDF` |
| `testPDFTexteVideRefuse` | Un texte vide est refusé pour le PDF, et aucun fichier n'est créé |

**Résultat : les 4 tests passent.** (les tests de la partie 2 sont plus bas)

---

## 8. Partie 2 : ce que j'ai ajouté

Dans la partie 2, le but était d'améliorer l'appli : pouvoir changer le style du PDF, y mettre une image et sauvegarder son travail pour le reprendre plus tard.

| Tâche du sujet | Ce que j'ai fait |
| --- | --- |
| 1. Personnaliser le PDF | On peut choisir la **police** (Helvetica, Times, Courier), la **couleur** (noir, bleu, rouge, vert) et la **taille** du texte |
| 2. Ajouter une image | On choisit une image sur son PC, on décide si elle va **avant ou après** le QR code et on choisit sa **largeur** |
| 3. Sauvegarder / charger | Un menu **Fichier** permet de sauvegarder et recharger un **projet** et un **profil** |
| 4. Interface (facultatif) | Des messages s'affichent pour dire si tout s'est bien passé ou ce qui ne va pas |
| 5. Tests et erreurs | 5 nouveaux tests, et l'appli ne plante pas si un fichier est introuvable ou abîmé |
| 6. Documentation | Code commenté et ce compte rendu mis à jour |

### Les nouvelles classes

J'ai ajouté deux classes dans le **Modèle** :

- **`Profil`** : c'est le style du PDF (police, couleur, taille). En gros, ce sont tes réglages préférés.
- **`Projet`** : c'est le contenu du PDF (le texte du QR code, et l'image si on en a mis une, avec sa position et sa largeur).

Les deux ont une méthode `sauvegarder()` et une méthode `charger()`. Elles enregistrent tout dans un petit fichier texte avec une ligne par réglage, par exemple `police=Courier`. Pour ça j'ai utilisé la classe `Properties` de Java, qui fait déjà presque tout le travail.

La classe `GenerateurQRCode` a maintenant une nouvelle version de `creerPDF(projet, profil, fichier)` qui utilise le style du profil et ajoute l'image du projet. L'ancienne version marche toujours : elle utilise juste un style par défaut.

### Dans la fenêtre

- Une partie **Style du PDF** avec 3 listes déroulantes : police, couleur, taille.
- Une partie **Image (facultatif)** avec un bouton pour choisir l'image, sa position et sa largeur.
- Un menu **Fichier** avec : Sauvegarder le projet, Charger un projet, Sauvegarder le profil, Charger un profil.

Comme pour la partie 1, la fenêtre ne fait aucun calcul : elle envoie tout au **Contrôleur**, qui fait travailler le **Modèle**.

### Les nouvelles erreurs gérées

| Situation | Message affiché |
| --- | --- |
| L'image choisie n'existe plus | « L'image choisie n'existe pas. » |
| Le fichier choisi n'est pas une image | « Le fichier choisi n'est pas une image valide. » |
| Le projet ou le profil ne peut pas être chargé | « Impossible de charger ce projet / profil. Le fichier est-il correct ? » |
| La sauvegarde ne marche pas | « Impossible de sauvegarder le projet / le profil. » |

J'ai fait en sorte que l'image soit vérifiée **avant** de commencer le PDF, comme ça on ne se retrouve pas avec un PDF à moitié écrit.

### Les nouveaux tests

| Test | Ce qui est vérifié |
| --- | --- |
| `testSauvegarderEtChargerProfil` | Un profil sauvegardé puis rechargé garde les mêmes réglages |
| `testChargerProfilInexistant` | Charger un profil qui n'existe pas lance bien une erreur |
| `testSauvegarderEtChargerProjet` | Un projet sauvegardé puis rechargé garde le même contenu |
| `testPDFPersonnaliseAvecImage` | Un PDF avec police, couleur, taille et image est bien créé |
| `testImageIntrouvableRefusee` | Une image introuvable est refusée et aucun PDF n'est créé |

**Résultat : les 9 tests (4 de la partie 1 + 5 de la partie 2) passent.** Comme pour la partie 1, j'ai fait un commit par test.

---

## 9. Difficultés rencontrées

**Partie 1**

- **Les dépendances d'iText** : iText a besoin d'autres bibliothèques (SLF4J, Jackson…). Elles doivent toutes être ajoutées au projet.
- **Le format de l'image** : ZXing produit une image Java (`BufferedImage`) qu'iText ne sait pas lire directement. Il faut d'abord la convertir en PNG.
- **Le PDF déjà ouvert** : Windows empêche de remplacer un fichier ouvert. Un message d'erreur a été prévu pour ce cas.

**Partie 2**

- **Les polices et couleurs** : iText ne comprend pas directement « Bleu » ou « Times ». J'ai dû faire une petite correspondance entre ce qu'on choisit dans la fenêtre et ce qu'iText attend.
- **L'image abîmée ou supprimée** : si on vérifie l'image trop tard, le PDF commence à s'écrire puis plante. J'ai donc vérifié l'image en premier.
- **Sauvegarder simplement** : plutôt que d'inventer un format compliqué, j'ai utilisé `Properties`, qui crée un fichier lisible et facile à recharger.

---

## 10. Lancer le projet

- **Application** : ouvrir le projet dans IntelliJ, puis lancer `controleur.Controle` avec la flèche verte ▶.
- **Tests** : faire un clic droit sur le dossier `test`, puis **Run 'All Tests'**.
