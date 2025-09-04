import java.util.*;

class Move {
    String name;
    int power;
    int speedMod;
    boolean critAllowed;
    boolean stunChance;        // Gibt an, ob bei diesem Move betäubt werden kann
    double stunProbability;    // Wahrscheinlichkeit einer Betäubung
    boolean slowsSelf;         // Verlangsamt der Move den Anwender selbst
    boolean fastRound;         // Führt dieser Move dazu, dass der Anwender vor Gegner zuschlägt (höchste Initiative)
    double critChanceOverride; // Spezielle Kritchance, wenn >0 wird die verwendet statt Standard 20%

    public Move(String name, int power, int speedMod, boolean critAllowed) {
        this.name = name;
        this.power = power;
        this.speedMod = speedMod;
        this.critAllowed = critAllowed;
        this.stunChance = false;
        this.stunProbability = 0;
        this.slowsSelf = false;
        this.fastRound = false;
        this.critChanceOverride = -1;
    }
}

class Character {
    String name;
    int maxHp;
    int hp;
    int attack;
    int defense;
    int baseInitiative;
    List<Move> moves;
    boolean stunned = false;
    int extraAttacks = 0;
    boolean slowed = false;

    public Character(String name, int hp, int attack, int defense, int initiative, List<Move> moves) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.baseInitiative = initiative;
        this.moves = moves;
    }

    public void performAttack(Move move, Character target) {
        if (this.stunned) {
            System.out.println(this.name + " ist betäubt und kann diesen Zug nicht angreifen!");
            this.stunned = false; // Betäubung nur für eine Runde
            return;
        }

        Random rand = new Random();

        double critChance = move.critAllowed ? 0.2 : 0.0;  // Standardkritchance 20%
        if (move.critChanceOverride > 0) {
            critChance = move.critChanceOverride;
        }

        boolean crit = (rand.nextDouble() < critChance);
        int power = move.power * (crit ? 2 : 1);

        int baseDamage = power + this.attack - target.defense;
        int damage = Math.max(1, baseDamage);

        target.hp = Math.max(0, target.hp - damage);

        System.out.println(this.name + " benutzt \"" + move.name + "\" gegen " + target.name
                + (crit ? " -> KRITISCHER TREFFER!" : "")
                + " und verursacht " + damage + " Schaden! (" + target.hp + "/" + target.maxHp + " HP übrig)");

        // Prüfen ob Move verzögert / verlangsamt sich selbst
        if (move.slowsSelf) {
            this.slowed = true;
            System.out.println(this.name + " wird durch " + move.name + " verlangsamt.");
        }

        // Prüfen ob Move betäuben kann
        if (move.stunChance && rand.nextDouble() < move.stunProbability) {
            System.out.println(this.name + " betäubt " + target.name + " für die nächste Runde!");
            target.stunned = true;
        }

        // Prüfen ob Move extra Angriff gibt (z.B. schneller Angriff bei Aladin)
        if (move.fastRound) {
            this.extraAttacks = 1;
            System.out.println(this.name + " erhält durch " + move.name + " einen Extraangriff in dieser Runde!");
        }

        if (target.hp <= 0) {
            System.out.println(target.name + " wurde besiegt!");
        }
    }
}

public class BattleSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        // Moves für Aladin
        Move aladinNormal = new Move("Normaler Angriff", 5, 0, true);
        aladinNormal.critChanceOverride = 0.10; // 10% krit

        Move aladinStark = new Move("Starker Angriff", 9, -2, true);
        aladinStark.stunChance = true;
        aladinStark.stunProbability = 0.5; // 50% betäuben

        Move aladinSchnell = new Move("Schneller Angriff", 3, +5, true);
        aladinSchnell.critChanceOverride = 0.20; // 20% krit
        aladinSchnell.fastRound = true;         // schnelle Runde = extra Angriff

        List<Move> movesAladin = Arrays.asList(aladinNormal, aladinStark, aladinSchnell);

        // Moves für Vera
        Move veraNormal = new Move("Normaler Angriff", 4, 0, true);

        Move veraStark = new Move("Starker Angriff", 8, -2, true);
        veraStark.slowsSelf = true;              // verlangsamt Vera selbst

        Move veraSchnell = new Move("Schneller Angriff", 2, 1000, true); // sehr hoher SpeedMod = vor Aladin
        veraSchnell.fastRound = true;            // schlägt vor Aladin zu

        List<Move> movesVera = Arrays.asList(veraNormal, veraStark, veraSchnell);

        Character aladin = new Character("Aladin", 300, 5, 2, 5, movesAladin);
        Character vera = new Character("Vera", 600, 4, 1, 6, movesVera);

        int round = 1;

        System.out.println("Kampf beginnt zwischen Aladin und Vera!");

        while (aladin.hp > 0 && vera.hp > 0) {
            System.out.println("\n--- Runde " + round + " ---");
            System.out.println("Aladin: " + aladin.hp + "/" + aladin.maxHp + " HP");
            System.out.println("Vera: " + vera.hp + "/" + vera.maxHp + " HP");

            // Angriffs-Auswahl Aladin
            System.out.println("Wähle Aladins Angriff:");
            for (int i = 0; i < aladin.moves.size(); i++) {
                Move m = aladin.moves.get(i);
                System.out.println((i + 1) + ": " + m.name + " (Power: " + m.power + ", SpeedMod: " + m.speedMod + ")");
            }

            int choice = 0;
            while (choice < 1 || choice > aladin.moves.size()) {
                System.out.print("Deine Wahl (1-" + aladin.moves.size() + "): ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    scanner.next();
                }
            }
            Move moveA = aladin.moves.get(choice - 1);

            // Zufallsangriff Vera
            Move moveB = vera.moves.get(rand.nextInt(vera.moves.size()));

            // Initiative berechnen (Basis + Modifikatoren)
            int initA = aladin.baseInitiative + moveA.speedMod - (aladin.slowed ? 2 : 0);
            int initB = vera.baseInitiative + moveB.speedMod - (vera.slowed ? 2 : 0);

            // Extra-Angriffe zurücksetzen
            aladin.extraAttacks = 0;

            // Wer schlägt zuerst in der Runde? - Max SpeedMod garantiert höchste Initiative (für Vera schneller Angriff)
            Character first;
            Character second;

            if (initA > initB) {
                first = aladin;
                second = vera;
            } else if (initB > initA) {
                first = vera;
                second = aladin;
            } else {
                // Gleichstand
                first = rand.nextBoolean() ? aladin : vera;
                second = (first == aladin) ? vera : aladin;
            }

            // Zug-Array für mögliche Extra-Attacken von Aladin
            Character[] actionOrder = new Character[3];
            int actionCount = 0;

            // Erster Angriff
            actionOrder[actionCount++] = first;
            if (first == aladin && moveA.fastRound) {
                aladin.extraAttacks = 1; // Extra-Angriff durch schnellen Angriff
                actionOrder[actionCount++] = aladin; // Aladin greift nochmal direkt an
            }
            actionOrder[actionCount++] = second;

            for (int i = 0; i < actionCount; i++) {
                Character attacker = actionOrder[i];

                // Stunned überspringen
                if (attacker.stunned) {
                    System.out.println(attacker.name + " ist betäubt und kann nicht angreifen.");
                    attacker.stunned = false;
                    continue;
                }

                // Welcher Move wird für den Angreifer verwendet?
                Move currentMove;
                if (attacker == aladin) currentMove = moveA;
                else currentMove = moveB;

                if (attacker.hp <= 0) continue; // Überspringen falls tot

                attacker.performAttack(currentMove, (attacker == aladin) ? vera : aladin);

                // Nach Angriff prüfen ob Ziel gefallen ist
                if (aladin.hp <= 0 || vera.hp <= 0) break;
            }

            // Verlangsamt-Status nach Runde löschen
            if (aladin.slowed) {
                aladin.slowed = false;
                System.out.println("Aladin ist nicht mehr verlangsamt.");
            }
            if (vera.slowed) {
                vera.slowed = false;
                System.out.println("Vera ist nicht mehr verlangsamt.");
            }

            round++;
        }

        System.out.println("\n--- Kampfende ---");
        if (aladin.hp > 0 && vera.hp <= 0) {
            System.out.println("🏆 Aladin gewinnt!");
        } else if (vera.hp > 0 && aladin.hp <= 0) {
            System.out.println("🏆 Vera gewinnt!");
        } else {
            System.out.println("Unentschieden!");
        }

        scanner.close();
    }
}
