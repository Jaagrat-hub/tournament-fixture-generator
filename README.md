# Tournament Fixture Generator (Java)

Generates a round-robin schedule: every team plays every other team exactly once, and no team plays twice in the same round. Handles an odd number of teams by giving one team a bye each round.

## How it works
Uses the circle method: one team stays fixed, the others rotate each round. For n teams this gives n - 1 rounds (n / 2 matches each) in O(n^2) time.

## Run
```
mkdir out
javac -d out src/*.java test/*.java
java -cp out FixtureGenerator        # prints a 12-team schedule
java -cp out FixtureGeneratorTest    # runs the tests
```

## Tests
10 checks: round and match counts, every pair meets exactly once, no clashes within a round, odd team counts, smallest input, and invalid input (1 team, duplicate names).

## Background
Built from a real problem: organising inter-college cricket tournaments for 12 teams at Dublin Business School.
