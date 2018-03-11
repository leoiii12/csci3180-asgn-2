

import java.util.Random;

public abstract class Player {
    protected Pos pos;
    protected int health;
    protected Object equipment;
    protected int index;
    protected String myString;
    protected SurvivalGame game;

    private final int MOBILITY;
    private final String RACE;
    private final int HEALTH_CAPACITY;

    public Player(int healthCap, int mob, int posx, int posy, int index, SurvivalGame game, String race) {
        this.MOBILITY = mob;
        this.RACE = race;
        this.HEALTH_CAPACITY = healthCap;

        this.health = healthCap;
        this.pos = new Pos(posx, posy);
        this.index = index;
        this.game = game;

        this.myString = "" + this.RACE.charAt(0) + index;
    }

    public Pos getPos() {
        return pos;
    }

    public void teleport() {
        Random rand = new Random();

        int randx = rand.nextInt(game.D);
        int randy = rand.nextInt(game.D);

        while (game.positionOccupied(randx, randy)) {
            randx = rand.nextInt(game.D);
            randy = rand.nextInt(game.D);
        }

        pos.setPos(randx, randy);
    }

    public void equip(Object equipment) {
        if (equipment instanceof Weapon)
            this.equipment = (Weapon) equipment;

        if (equipment instanceof Wand)
            this.equipment = (Wand) equipment;
    }

    public void increaseHealth(int h) {
        this.health += h;

        if (this.health > this.HEALTH_CAPACITY)
            this.health = this.HEALTH_CAPACITY;

        if (this.health > 0)
            this.myString = "" + this.RACE.charAt(0) + index;
    }

    public void decreaseHealth(int h) {
        this.health -= h;

        if (this.health <= 0) {
            this.myString = "C" + this.myString.charAt(0);
            this.health = 0;
        }
    }

    public String getName() {
        return myString;
    }

    public String getRace() {
        return RACE;
    }

    public void askForMove() {
        Weapon weapon = null;
        Wand wand = null;

        if (this.equipment instanceof Weapon)
            weapon = (Weapon) this.equipment;
        if (this.equipment instanceof Wand)
            wand = (Wand) this.equipment;

        // Print general information
        System.out.println(String.format("Your health is %d. Your position is (%d,%d). Your mobility is %d.", health, pos.getX(), pos.getY(), this.MOBILITY));

        System.out.println("You now have following options: ");
        System.out.println("1. Move");
        if (weapon != null) System.out.println("2. Attack");
        if (wand != null) System.out.println("2. Heal");
        System.out.println("3. End tne turn");

        int a = SurvivalGame.reader.nextInt();

        if (a == 1) {
            System.out.println("Specify your target position (Input 'x y').");
            int posx = SurvivalGame.reader.nextInt();
            int posy = SurvivalGame.reader.nextInt();

            if (pos.distance(posx, posy) > this.MOBILITY) {
                System.out.println("Beyond your reach. Staying still.");
            } else if (game.positionOccupied(posx, posy)) {
                System.out.println("Position occupied. Cannot move there.");
            } else {
                this.pos.setPos(posx, posy);
                game.printBoard();

                if (weapon != null) System.out.println("You can now \n1.attack\n2.End the turn");
                if (wand != null) System.out.println("You can now \n1.heal\n2.End the turn");

                if (SurvivalGame.reader.nextInt() == 1) {
                    if (weapon != null) System.out.println("Input position to attack. (Input 'x y')");
                    if (wand != null) System.out.println("Input position to heal. (Input 'x y')");

                    int attx = SurvivalGame.reader.nextInt(), atty = SurvivalGame.reader.nextInt();

                    if (weapon != null) weapon.action(attx, atty);
                    if (wand != null) wand.action(attx, atty);

                }
            }
        } else if (a == 2) {
            System.out.println("Input position to attack.");

            int attx = SurvivalGame.reader.nextInt();
            int atty = SurvivalGame.reader.nextInt();

            if (weapon != null) weapon.action(attx, atty);
            if (wand != null) wand.action(attx, atty);
        }
    }

}
