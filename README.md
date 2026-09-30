# Compte rendu – TP Java : Générateur de QR Code

## 1. C'est quoi ce projet ?

Le but du TP, c'est de faire une petite application en Java qui fabrique des QR codes.

En gros :

- tu tapes un texte ou un lien dans une fenêtre ;
- tu cliques sur un bouton ;
- le programme fabrique le QR code et te l'affiche ;
- il crée aussi un fichier PDF avec le QR code dedans, et l'ouvre tout seul.

Si tu scannes le QR code avec ton téléphone, tu retrouves le texte que tu as tapé. Et si c'est un lien, ton téléphone te propose d'ouvrir le site.

Le prof demandait d'utiliser la méthode **MVC**. J'explique plus bas ce que ça veut dire.

---

## 2. Ce que demandait le sujet, et ce que j'ai fait

| Ce que demandait le sujet | Ce que j'ai fait |
| --- | --- |
| 1. Faire une fenêtre avec Java Swing pour taper un texte ou un lien | Une fenêtre avec une zone de texte, un bouton, la place pour l'image et un message en bas |
| 2. Créer des fichiers PDF avec une bibliothèque comme iText | J'ai utilisé **iText** pour créer le fichier `qrcode.pdf` |
| 3. Créer un QR code à partir de ce que tape l'utilisateur | J'ai utilisé **ZXing** pour transformer le texte en image de QR code |
| 4. Mettre le QR code dans le PDF | Le PDF contient un titre, l'image du QR code et le texte |
| 5. Gérer les erreurs pour que ça ne plante pas | Si le texte est vide ou si le PDF ne peut pas être créé, un message s'affiche au lieu de planter |
| 6. Faire des tests unitaires | 4 tests avec **JUnit 5** (un commit par test sur GitHub) |
| 7. Commenter le code et écrire un rapport | Le code est commenté, et ce README sert de rapport |

---

## 3. Comment on s'en sert ?

1. On lance le programme, et une fenêtre s'ouvre.
2. On tape un texte ou un lien dans la case **Texte ou lien**, par exemple `https://www.google.fr`.
3. On clique sur **Générer le PDF**.
4. Le QR code apparaît dans la fenêtre.
5. En bas de la fenêtre, un message dit que le PDF a été créé.
6. Le PDF s'ouvre tout seul dans le lecteur PDF de l'ordinateur.

Le fichier `qrcode.pdf` est enregistré dans le dossier du projet. Si on recommence avec un autre texte, il est remplacé par le nouveau.

---

## 4. Comment le projet est organisé : le MVC

MVC, ça veut dire **Modèle – Vue – Contrôleur**. L'idée, c'est de couper le programme en 3 parties qui ont chacune leur rôle. Comme ça, on ne mélange pas tout, et c'est plus facile à comprendre et à modifier.

On peut comparer ça à un restaurant :

- **la Vue**, c'est la salle et le serveur : c'est ce que le client voit ;
- **le Contrôleur**, c'est celui qui prend la commande et la transmet en cuisine ;
- **le Modèle**, c'est la cuisine : c'est là que le vrai travail se fait.

Le client ne va jamais directement en cuisine : tout passe par celui qui prend les commandes. C'est pareil ici, la Vue et le Modèle ne se parlent jamais directement, tout passe par le Contrôleur.

### Les 3 fichiers du projet

```
src/
├── vue/         FrmQRCode.java          → la fenêtre
├── controleur/  Controle.java           → le chef d'orchestre (il y a le main)
└── modele/      GenerateurQRCode.java   → fabrique le QR code et le PDF
test/
└── modele/      GenerateurQRCodeTest.java → les 4 tests
lib/             → les bibliothèques (ZXing, iText, JUnit...)
```

### Ce qui se passe quand on clique sur le bouton

1. **La Vue** voit qu'on a cliqué. Elle récupère le texte tapé et le donne au Contrôleur (méthode `demandeGenererPDF`).
2. **Le Contrôleur** demande au Modèle de créer le PDF (méthode `creerPDF`).
3. **Le Modèle** fabrique le QR code, puis le PDF.
4. **Le Contrôleur** redemande l'image du QR code au Modèle (méthode `creerImage`).
5. **Le Contrôleur** dit à la Vue d'afficher l'image, d'afficher le message « PDF créé », puis d'ouvrir le PDF.
6. Si quelque chose s'est mal passé, le Contrôleur dit à la Vue d'afficher un message d'erreur à la place.

---

## 5. Le code expliqué, fichier par fichier

### La Vue : `FrmQRCode.java`

C'est la fenêtre. Elle contient :

- une étiquette « Texte ou lien : » et une zone de texte (`JTextField`) pour taper ;
- un bouton **Générer le PDF** (`JButton`) ;
- une zone pour afficher l'image du QR code (`JLabel`) ;
- une zone pour le message en bas (`JLabel`).

Quand on clique sur le bouton, cette ligne est exécutée :

```java
btnGenerer.addActionListener(e -> controle.demandeGenererPDF(txtTexte.getText()));
```

En clair : « quand on clique, prends le texte tapé et envoie-le au contrôleur ».

La Vue a aussi 3 méthodes que le Contrôleur utilise pour lui dire quoi faire :

- `afficheQRCode(image)` affiche l'image du QR code ;
- `afficheMessage(message)` écrit un message en bas de la fenêtre ;
- `ouvrirPDF(fichier)` ouvre le PDF avec le lecteur PDF de l'ordinateur.

La Vue ne fait **aucun calcul**. Elle affiche, c'est tout.

### Le Contrôleur : `Controle.java`

C'est lui qui démarre tout, parce que c'est lui qui a le `main`. Au démarrage, il crée la fenêtre et l'affiche.

Ensuite, il a une méthode importante, `demandeGenererPDF(texte)`, qui est appelée quand on clique :

```java
try {
    generateur.creerPDF(texte, "qrcode.pdf");
    BufferedImage image = generateur.creerImage(texte);
    frmQRCode.afficheQRCode(image);
    frmQRCode.afficheMessage("PDF créé : qrcode.pdf (dans le dossier du projet)");
    frmQRCode.ouvrirPDF("qrcode.pdf");
} catch (IllegalArgumentException e) {
    frmQRCode.afficheMessage("Erreur : " + e.getMessage());
} catch (Exception e) {
    frmQRCode.afficheMessage("Erreur : impossible de créer le PDF. Est-il déjà ouvert ?");
}
```

En clair :

- dans le `try`, il essaie de tout faire : créer le PDF, récupérer l'image, l'afficher, afficher le message, ouvrir le PDF ;
- si le texte est vide, on tombe dans le premier `catch`, et il affiche « Erreur : Le texte est vide. » ;
- si autre chose plante (le PDF ne peut pas être écrit), on tombe dans le deuxième `catch`, et il affiche un autre message d'erreur.

### Le Modèle : `GenerateurQRCode.java`

C'est lui qui fait le vrai travail. Il a 2 méthodes.

**`creerImage(texte)`** fabrique l'image du QR code :

1. Elle vérifie d'abord que le texte n'est pas vide. S'il est vide, elle lance une erreur (`IllegalArgumentException`).
2. Elle demande à ZXing de calculer le QR code, en 300 x 300 pixels.
3. Elle transforme le résultat en image et la renvoie.

**`creerPDF(texte, fichier)`** fabrique le PDF :

1. Elle appelle `creerImage(texte)` pour avoir l'image du QR code.
2. Elle transforme l'image au format PNG, parce que c'est un format qu'iText sait lire.
3. Elle crée le PDF avec iText et met dedans le titre « Mon QR Code », l'image du QR code, puis le texte.
4. Elle ferme le document pour que le fichier soit bien enregistré.

---

## 6. Les bibliothèques utilisées

Java ne sait pas fabriquer un QR code ou un PDF tout seul. Il faut ajouter des **bibliothèques** : du code déjà écrit par d'autres, qu'on réutilise. Elles sont toutes dans le dossier `lib/`.

| Bibliothèque | À quoi elle sert |
| --- | --- |
| **ZXing** (version 3.5.4) | Fabriquer le QR code. C'est une bibliothèque faite par Google. |
| **iText** (version 9.8.0) | Fabriquer le fichier PDF. |
| **SLF4J, Jackson, FastDoubleParser** | iText en a besoin pour fonctionner. On ne les utilise pas directement. |
| **JUnit 5** | Écrire et lancer les tests unitaires. |

Pour qu'IntelliJ les trouve, elles sont déclarées dans le fichier `QRCodeSimple.iml`.

---

## 7. La gestion des erreurs

Le sujet demandait que l'application ne plante pas. Voilà les cas prévus :

| Ce qui se passe | Ce que fait le programme |
| --- | --- |
| On clique sans avoir rien tapé | Le Modèle lance une `IllegalArgumentException`. Le Contrôleur l'attrape et affiche « Erreur : Le texte est vide. » |
| On tape seulement des espaces | Pareil, c'est considéré comme vide |
| Le PDF est déjà ouvert dans un autre logiciel, donc impossible de le remplacer | Le Contrôleur attrape l'erreur et affiche « Erreur : impossible de créer le PDF. Est-il déjà ouvert ? » |
| Le PDF est créé mais ne peut pas s'ouvrir tout seul | La Vue affiche « PDF créé, mais impossible de l'ouvrir automatiquement. » |

Dans tous les cas, le programme continue de tourner. On peut corriger et recliquer.

---

## 8. Les tests unitaires

Un **test unitaire**, c'est un petit bout de code qui vérifie automatiquement qu'une méthode fait bien ce qu'elle doit faire. Au lieu de tout tester à la main, on lance les tests, et JUnit nous dit si c'est bon (vert) ou pas (rouge).

J'ai testé le Modèle, parce que c'est lui qui fait le vrai travail. Les 4 tests sont dans `test/modele/GenerateurQRCodeTest.java`, et chaque test a son propre commit sur GitHub.

| Test | Ce qu'il vérifie |
| --- | --- |
| **Test 1** – `testImageCreee` | L'image du QR code est bien créée, fait 300 x 300 pixels et contient bien du noir (ce n'est pas une image vide) |
| **Test 2** – `testTexteVideRefuse` | Un texte vide, un texte avec seulement des espaces ou pas de texte du tout (`null`) sont bien refusés pour l'image |
| **Test 3** – `testPDFCree` | Le fichier PDF est bien créé, n'est pas vide, et c'est un vrai PDF (il commence par `%PDF`) |
| **Test 4** – `testPDFTexteVideRefuse` | Un texte vide est refusé pour le PDF, et aucun fichier PDF n'est créé |

Pour vérifier que le Modèle refuse bien les mauvais textes, j'utilise `assertThrows`. Ça veut dire : « je m'attends à ce que ça lance une erreur ». Si aucune erreur n'est lancée, le test échoue.

**Résultat : les 4 tests passent.**

---

## 9. Ce qui m'a posé problème

- **Les bibliothèques** : iText a besoin d'autres bibliothèques pour marcher (SLF4J, Jackson…). Au début, ça ne marchait pas tant qu'elles n'étaient pas toutes dans `lib/` et ajoutées au projet.
- **Le format de l'image** : ZXing donne une image Java (`BufferedImage`), mais iText ne sait pas la lire directement. Il faut d'abord la transformer en PNG.
- **Le PDF déjà ouvert** : si on laisse le PDF ouvert et qu'on reclique, Windows empêche de le remplacer. J'ai donc ajouté un message d'erreur pour ce cas.

---

## 10. Ce qu'on pourrait améliorer

- Choisir le nom et l'endroit du PDF au lieu de toujours l'appeler `qrcode.pdf`.
- Choisir la taille ou la couleur du QR code.
- Enregistrer aussi le QR code en image (PNG).
- Proposer des types de QR code : e-mail, numéro de téléphone, Wi-Fi…

---

## 11. Comment lancer le projet

**Lancer l'application**

1. Ouvrir le projet dans IntelliJ.
2. Aller dans `src` → `controleur` → `Controle`.
3. Cliquer sur la flèche verte ▶ à côté de `main`, puis sur **Run**.

**Lancer les tests**

1. Faire un clic droit sur le dossier `test`.
2. Cliquer sur **Run 'All Tests'**.
3. Si tout est vert, c'est bon.
