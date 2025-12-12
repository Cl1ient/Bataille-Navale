import controller.GameController;

public class Main {

    public static void main(String[] args) {
        new GameController();

        /*
        System.out.println("Main");

        Map<EntityType, List<Coordinate>> placement = new HashMap<>();
        placement.put(EntityType.CRUISER, new ArrayList<>(List.of(new Coordinate(1,2), new Coordinate(1,3), new Coordinate(1,4), new Coordinate(1,5))));
        placement.put(EntityType.BLACK_HOLE, new ArrayList<>(List.of(new Coordinate(4,2))));
        placement.put(EntityType.BLACK_HOLE, new ArrayList<>(List.of(new Coordinate(6,6))));
        GameConfiguration config = new GameConfiguration(10, placement, false, "Valentin");

        Game game = new Game(config);
        game.addListener(new TestGameListener());

        Weapon missile = new Bombe();
        //game.processAttack(game.getM_computerPlayer(), missile, new Coordinate(4,2));
        game.processAttack(game.getM_computerPlayer(), missile, new Coordinate(6,6));
        game.displayGridPlayer();

        //game.processAttack(computer, missile, new Coordinate(1,3));
        //game.processAttack(computer, missile, new Coordinate(1,4));
        //game.processAttack(computer, missile, new Coordinate(1,5));
        //game.processComputerAttack(); // Ordinateur joue
        */

    }
}
