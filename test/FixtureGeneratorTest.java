import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Simple test runner, no libraries needed. Run with: java -cp out FixtureGeneratorTest */
public class FixtureGeneratorTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(boolean condition, String name) {
        if (condition) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }

    private static List<String> teams(int n) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add("T" + i);
        }
        return list;
    }

    /** Every pair of teams meets exactly once. */
    private static boolean everyPairMeetsOnce(List<List<FixtureGenerator.Match>> schedule, int n) {
        Set<String> seen = new HashSet<>();
        for (List<FixtureGenerator.Match> round : schedule) {
            for (FixtureGenerator.Match m : round) {
                String a = m.home().compareTo(m.away()) < 0 ? m.home() : m.away();
                String b = m.home().compareTo(m.away()) < 0 ? m.away() : m.home();
                if (!seen.add(a + "|" + b)) {
                    return false; // pair seen twice
                }
            }
        }
        return seen.size() == n * (n - 1) / 2;
    }

    /** No team appears twice in one round. */
    private static boolean noClashInRounds(List<List<FixtureGenerator.Match>> schedule) {
        for (List<FixtureGenerator.Match> round : schedule) {
            Set<String> playing = new HashSet<>();
            for (FixtureGenerator.Match m : round) {
                if (!playing.add(m.home()) || !playing.add(m.away())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Even number of teams (the real cricket case: 12 teams)
        List<List<FixtureGenerator.Match>> s12 = FixtureGenerator.generate(teams(12));
        check(s12.size() == 11, "12 teams -> 11 rounds");
        check(s12.stream().mapToInt(List::size).sum() == 66, "12 teams -> 66 matches");
        check(everyPairMeetsOnce(s12, 12), "12 teams -> every pair meets exactly once");
        check(noClashInRounds(s12), "12 teams -> no team plays twice in a round");

        // Odd number of teams (one team has a bye each round)
        List<List<FixtureGenerator.Match>> s7 = FixtureGenerator.generate(teams(7));
        check(s7.stream().mapToInt(List::size).sum() == 21, "7 teams -> 21 matches");
        check(everyPairMeetsOnce(s7, 7), "7 teams -> every pair meets exactly once");
        check(noClashInRounds(s7), "7 teams -> no team plays twice in a round");

        // Smallest valid input
        List<List<FixtureGenerator.Match>> s2 = FixtureGenerator.generate(teams(2));
        check(s2.size() == 1 && s2.get(0).size() == 1, "2 teams -> 1 round, 1 match");

        // Invalid input
        boolean threw = false;
        try {
            FixtureGenerator.generate(teams(1));
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check(threw, "1 team -> rejected");

        threw = false;
        try {
            FixtureGenerator.generate(List.of("A", "A", "B"));
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check(threw, "duplicate team names -> rejected");

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }
}
