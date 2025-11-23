# Architecture Générale
L’architecture du système repose sur une séparation nette entre trois espaces fonctionnels : le modèle, la vue et le contrôleur. Cette organisation suit naturellement le principe du pattern MVC, qui vise à dissocier la logique métier, l’affichage et la gestion des interactions utilisateur. Dans cette structure, le modèle regroupe l’ensemble des mécanismes de jeu tels que la gestion des grilles, des entités, des joueurs… tandis que les vues se concentrent uniquement sur la représentation graphique et les éléments d’interface. Le contrôleur joue quant à lui un rôle d’intermédiaire : il reçoit les actions issues de la vue, les interprète et appelle les méthodes adéquates du modèle.

Cette séparation offre plusieurs avantages. Elle permet notamment de modifier l’interface sans toucher à la logique interne du jeu, ou d’améliorer le moteur sans impacter la partie visuelle. Elle favorise également la clarté du code, en limitant les dépendances entre modules. La mise en place d’un système d’écoute entre le modèle et la vue, via l’interface “GameListener”, renforce cette indépendance car plutôt que d’interroger en permanence l’état du jeu, la vue est directement informée des événements importants tels que la fin d’un tour, la mise à jour d’une cellule... Cette communication réactive contribue à une architecture fluide et dynamique.

# Conception du modèle

Pour ce qui est du modèle, la class principal est la class “Game” qui s’occupe de gérer : alternance de tours, gestion des attaques (offensives, scans), placement initial des entités, conditions de victoire, exploration d’îles… Cela permet d’avoir une meilleure répartition des responsabilités, évitant ainsi aux autres classes d’avoir à faire plus de choses qu'elles ne le devraient.

Concernant la <span style="font-size: 15px;">**gestion des joueurs** </span>nous avons opté pour une classe abstraite de “Player”  qui serait extend par “ComputerPlayer” car globalement le joueur humain et l’ordinateur sont censé faire la même chose à l'exception du choix de l’emplacement du tir ainsi que de l’arme utilisé à chaque tour qui serait faite de manière automatique pour l’ordinateur tandis que le joueur humain les choisirait sur l’affichage graphique directement.

Pour ce qui est des <span style="font-size: 15px;"> **armes, des pièges et des bateaux**</span>, nous avons choisis de partir sur une interface accompagnée d’une Factory afin de fluidifier leur création mais aussi limité la connaissance de ces éléments vis à vie des joueurs ou de la grille. De cette manière, le joueur par exemple, n’est pas obligé de connaître toutes les armes pour les créer, il lui suffit de connaître la factory. Par ailleurs, les <span style="font-size: 15px;">**items trouvables sur l’île**</span> sont eux aussi pourvu d’une factory, cependant nous n’avons pas jugé nécessaire de leur mettre une interface car contrairement aux armes, aux pièges ou encore aux bateaux ils ne sont jamais contenu dans une liste ou autre type de ce genre nécessitant de les généraliser.

De plus, concernant les <span style="font-size: 15px;">**éléments plaçable dans la grille** </span> nous avons décidé de les regrouper par le billet de l’interface “GridEntity” permettant ainsi de faire pleinement usage du polymorphisme notamment grâce à la méthode “onHit()” qui permet donc de définir un comportement différent à adopter pour chaque éléments placé sur la grille lorsqu’il est touché. Cet usage du polymorphisme permet notamment au code d'être plus extensible, rendant l’ajout d'éléments plaçable sur la grille possible sans modifier la classe principale “Game”.

# Conception vue/controller

Pour ce qui est du package “controller” nous avons pour l’instant qu’un seul élément, le “GameController” qui s’occupe de choisir quelle vue afficher ainsi que d’appeler les fonctions du modèle correspondant aux actions du joueur sur l’interface graphique.
Quant aux package “view” nous avons pour l’instant le minimum comprenant une vue pour la configuration, une vue pour le placement des éléments, une vue pour le déroulement des tours et enfin une vue pour la fin du jeu.

# Points d’améliorations possibles sur notre architecture actuelle

### Réduire la duplication de code dans les bateaux
Les classes de bateaux (Submarine, Torpedo, Cruiser, etc.) partagent de nombreux attributs et comportements identiques. Introduire une classe abstraite commune pourrait simplifier le code, éviter les répétitions et faciliter l’ajout de nouveaux types.

### Harmoniser et rationaliser les enums
Les types d’entités (EntityType) et les types de bateaux (BoatType) se recoupent partiellement. Une réunification de ces enums permettrait d’éviter la redondance.
### Séparer davantage les responsabilités dans la classe Game
La classe Game joue un rôle central mais accumule beaucoup de responsabilités. Extraire certaines logiques dans des classes spécialisées améliorerait la lisibilité et favoriserait un design plus modulaire.
### Clarifier la gestion des coordonnées
L’utilisation intensive de Coordinate dans l’ensemble du modèle pourrait être optimisée en introduisant un service dédié à la manipulation ou la validation des coordonnées. Cela éviterait de disperser ces logiques dans plusieurs classes.
### Limiter l'accès direct aux factories depuis certaines entités
Les classes comme NewBomb ou NewStorm utilisent des factories externes (WeaponFactory ou TrapFactory). Cela c’est sans doute pas le choix de concéption le plus pertinent mais à voir dans la suite du projet comment nous pourrons améliorer cela.
