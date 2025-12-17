package model;

import model.map.Grid;
import model.player.Player;
import model.trap.Trap;

public interface IslandListener {
    void notifyPlaceIslandEntity(Trap entity, Player player); // notifie la vue dans le but de faire apparaitre l'écran de placement pour placer un nouveau piège en cours de partie
    // Une fois que le joueur aura séléctionné la case sur laquelle il veut placer le piège il pourra appeler depuis le controller la méthode Player.placeNewTrap(Trap trap, Coordinate coord) qui s'occupe de placer le piège au bon endroit dans le modèle.

    void notifyWeaponFind(EntityType weaponType); // notifie la vue pour lui indiquer qu'une arme a été trouvé
    void notifyTrapWrongPlacement(); // message envoyé par le modèle si le joueur essaye de placer son piège sur une case qui est déjà prise ou déjà touché
}
