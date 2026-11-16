import java.util.ArrayList;
import java.util.List;

/**
 * Generates a round-robin fixture list: every team plays every other team once,
 * and no team plays more than once in the same round.
 *
 * Uses the "circle method": fix one team in place and rotate the others.
 * Time complexity: O(n^2) for n teams (n - 1 rounds, n / 2 matches per round).
 */
public class FixtureGenerator {

    /** One match between two teams. A null away team means the home team has a bye. */
    public record Match(String home, String away) {
        @Override
        public String toString() {
            return home + " vs " + away;
        }
    }

    /**
     * @param teams team names (at least 2, no duplicates)
     * @return a list of rounds, each round being a list of matches
     */
    public static List<List<Match>> generate(List<String> teams) {
        if (teams == null || teams.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 teams");
        }
        if (teams.stream().distinct().count() != teams.size()) {
            throw new IllegalArgumentException("Team names must be unique");
        }

        List<String> rotation = new ArrayList<>(teams);
        // With an odd number of teams, add a placeholder so one team sits out each round.
        boolean hasBye = rotation.size() % 2 != 0;
        if (hasBye) {
            rotation.add(null);
        }

        int n = rotation.size();
        int rounds = n - 1;
        List<List<Match>> schedule = new ArrayList<>();

        for (int r = 0; r < rounds; r++) {
            List<Match> round = new ArrayList<>();
            for (int i = 0; i < n / 2; i++) {
                String a = rotation.get(i);
                String b = rotation.get(n - 1 - i);
                if (a == null || b == null) {
                    // Bye: the real team simply does not play this round.
                    continue;
                }
                round.add(new Match(a, b));
            }
            schedule.add(round);

            // Keep the first team fixed, rotate the rest by one position.
            String last = rotation.remove(n - 1);
            rotation.add(1, last);
        }
        return schedule;
    }

    /** Prints a schedule in a readable form. */
    public static void print(List<List<Match>> schedule) {
        for (int r = 0; r < schedule.size(); r++) {
            System.out.println("Round " + (r + 1) + ":");
            for (Match m : schedule.get(r)) {
                System.out.println("  " + m);
            }
        }
    }

    public static void main(String[] args) {
        List<String> teams = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            teams.add("Team " + i);
        }
        List<List<Match>> schedule = generate(teams);
        print(schedule);
        int matches = schedule.stream().mapToInt(List::size).sum();
        System.out.println("Total: " + schedule.size() + " rounds, " + matches + " matches");
    }
}
