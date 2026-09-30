# Compte rendu – TP Java : Générateur de QR Code

## 1. Présentation

Ce projet est une application Java qui génère des QR codes.

L'utilisateur saisit un texte ou un lien dans une fenêtre, puis clique sur **Générer le PDF**. L'application :

1. crée le QR code correspondant au texte ;
2. l'affiche dans la fenêtre ;
3. crée un fichier `qrcode.pdf` contenant le QR code ;
4. ouvre automatiquement ce PDF.

Le projet respecte l'architecture **MVC** (Modèle – Vue – Contrôleur).

---

## 2. Réalisation des tâches du sujet

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

**Résultat : les 4 tests passent.**

---

## 8. Difficultés rencontrées

- **Les dépendances d'iText** : iText a besoin d'autres bibliothèques (SLF4J, Jackson…). Elles doivent toutes être ajoutées au projet.
- **Le format de l'image** : ZXing produit une image Java (`BufferedImage`) qu'iText ne sait pas lire directement. Il faut d'abord la convertir en PNG.
- **Le PDF déjà ouvert** : Windows empêche de remplacer un fichier ouvert. Un message d'erreur a été prévu pour ce cas.

---

## 9. Lancer le projet

- **Application** : ouvrir le projet dans IntelliJ, puis lancer `controleur.Controle` avec la flèche verte ▶.
- **Tests** : faire un clic droit sur le dossier `test`, puis **Run 'All Tests'**.
