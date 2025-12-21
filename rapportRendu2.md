# Rapport Final : Évolution de la Conception

Ce rapport explique comment l'architecture du projet "Bataille Navale" a évolué entre la conception initiale et le code final pour résoudre les problèmes de complexité et de maintenance.

## 1. Architecture Générale : Un MVC Strict

Nous avons appliqué une séparation stricte pour ne jamais mélanger les responsabilités :

* **Le Modèle (`model`) :** Contient uniquement la logique (règles, grille, bateaux). Il ignore l'existence de l'interface graphique.
* **La Vue (`view`) :** S'occupe uniquement de l'affichage. Elle ne prend aucune décision et obéit au Contrôleur.
* **Le Contrôleur (`controller`) :** Fait le lien. Il reçoit les actions du joueur, met à jour le Modèle et rafraîchit la Vue.

**Pourquoi ?** Cette séparation nous a permis de corriger des bugs de jeu sans jamais casser l'affichage, et inversement.

## 2. La Vue : Une Approche "LEGO"

Au départ, nous avions imaginé une seule grosse classe `GameView` pour tout gérer. C'était trop complexe. Nous l'avons donc découpée en petits panneaux indépendants :

* **`GridPanel` :** Pour la grille et les clics.
* **`InfoPanel` :** Pour les scores.
* **`WeaponPanel` :** Pour le choix des armes.
* **`GameLogPanel` :** Pour l'historique textuel.

**Avantage :** La `GameView` ne fait qu'assembler ces blocs. Modifier l'un (ex: l'historique) n'impacte pas les autres.

## 3. Le Placement : Validation avant Création

Le placement des bateaux s'est révélé plus complexe que prévu (rotations, règles, limites). Nous avons créé un **`PlacementModel`**.

C'est un "brouillon" temporaire. L'utilisateur configure sa flotte dans ce modèle dédié. Ce n'est qu'une fois la configuration valide (tous les bateaux placés correctement) que les données sont envoyées au jeu principal. Cela évite de lancer une partie avec des bugs.

## 4. Modèle et Entités

### Polymorphisme (`GridEntity`)

Tout ce qui est sur la grille (Bateau, Piège) implémente l'interface `GridEntity`.
Grâce à la méthode commune `onHit()`, chaque objet réagit différemment quand il est touché (le bateau perd une vie, le trou noir renvoie le tir). Cela a rendu l'ajout du "Mode Île" très facile.

### Usines (`Factory`)

Pour éviter de dupliquer du code, nous utilisons des **Factories** pour créer les bateaux et les pièges. Le code de création est centralisé : le reste du programme demande simplement un objet à l'usine sans se soucier de sa construction.

## 5. Communication : Réactive 

Pour éviter que l'interface ne demande en permanence "C'est à moi ?", nous utilisons des **Écouteurs (`Listeners`)**.

Le Modèle envoie une notification instantanée quand un événement arrive (fin de tour, trésor trouvé, tir reçu). La Vue réagit immédiatement. Cela rend le jeu fluide et le code beaucoup plus léger.

## 6. Analyse Comparative : De l'UML Initial à l'UML Final

Cette section détaille les transformations majeures opérées sur notre diagramme de classes. Elle met en évidence les limites de notre conception initiale (Rendu 1) et comment la version finale les a résolues.
### A. La Structure Globale (Organisation des Packages)

Ce qui posait problème (Rendu 1) : Dans notre premier diagramme, les classes étaient souvent reliées directement les unes aux autres sans frontières claires. La classe Game avait tendance à tout connaître, et la distinction entre ce qui relevait de l'affichage et de la logique n'était pas assez marquée. 
    
La solution actuelle : L'UML final montre des paquetages hermétiques (model, view, controller). Les flèches de dépendance sont maintenant unidirectionnelles et passent obligatoirement par le GameController.
    
Gain : Une architecture propre où l'on peut remplacer toute l'interface graphique sans toucher une seule ligne des règles du jeu.

### B. Décomposition de l'Interface (La fin de la "God Class")

Ce qui posait problème (Rendu 1) : Nous avions conçu la GameView comme une classe monolithique géante. Elle devait gérer l'affichage de la grille, les boutons de tir, le chat textuel et les scores. Une telle classe aurait probablement dépassé les 1000 lignes de code, devenant illisible et impossible à déboguer.

La solution actuelle : Nous avons adopté le principe de Composition. La GameView n'est plus qu'un conteneur qui assemble des briques spécialisées :

- GridPanel (réutilisé pour le joueur et l'adversaire).

- InfoPanel (gestion des stats).

- GameLogPanel (gestion de l'historique).

- Gain : Chaque classe est petite, simple et a une responsabilité unique.



### C. Communication et Réactivité (Les Listeners)

Ce qui posait problème (Rendu 1) : Les interactions étaient pensées de manière directe (la Vue demande au Modèle son état). Cela crée un couplage fort et rend difficile la gestion d'événements imprévus (comme le déclenchement d'un piège sur l'île).

La solution actuelle : Nous avons enrichi le système d'observation avec des interfaces spécifiques : GameListener pour le jeu classique et IslandListener pour les événements spéciaux. C'est le Modèle qui notifie la Vue ("J'ai changé !"), et non l'inverse.

- Gain : Une fluidité totale. L'interface réagit instantanément aux actions de l'ordinateur ou aux pièges, sans logique complexe dans la Vue.

## Conclusion

Nous sommes passés d'une architecture théorique à une structure pratique et modulaire. Le découpage de la vue en composants, la validation stricte du placement et l'utilisation d'écouteurs ont permis de produire un code propre et robuste.