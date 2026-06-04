package edu.touro.mcon364.finalreview.treesandthreads.exercises;

import edu.touro.mcon364.finalreview.treesandthreads.model.ScoreEntry;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * In-class Exercise 3 - Concurrent Leaderboard (ConcurrentSkipListSet + ExecutorService)
 *
 * Scenario: a game server receives score submissions from many player threads at
 * the same time. The leaderboard must always reflect current top scores in
 * sorted order (highest first) and must be safe when read and written concurrently.
 *
 * This exercise practises:
 * - ConcurrentSkipListSet as the thread-safe sorted cousin of TreeSet.
 * - Why TreeSet is NOT safe for concurrent access.
 * - ExecutorService and Runnable to simulate concurrent score submissions.
 * - AtomicInteger for a safe submission counter.
 * - Stream operations to produce a ranked snapshot from the sorted set.
 *
 * Before coding, think about:
 * - What would happen if two threads called TreeSet.add() simultaneously?
 * - ConcurrentSkipListSet keeps elements sorted by compareTo.
 *   Look at ScoreEntry.compareTo: which score appears first in iteration?
 * - Each ScoreEntry is unique by (playerName, score, timestamp).
 *   If a player submits a new score, does the old one disappear?
 *
 * Requirements:
 * - submitScore(entry) adds a ScoreEntry thread-safely.
 * - getTopN(n) returns the top n ScoreEntry objects as an immutable list, highest first.
 * - getTotalSubmissions() returns the number of times submitScore has been called.
 * - runSimulation(players, scoresEach) uses an ExecutorService to have each player
 *   submit scoresEach random scores concurrently, then shuts down the pool and waits.
 *
 * Do not use synchronized blocks. Rely on ConcurrentSkipListSet and AtomicInteger.
 */
public class ConcurrentLeaderboard {

    // ScoreEntry.compareTo sorts highest score first
    private final ConcurrentSkipListSet<ScoreEntry> leaderboard = new ConcurrentSkipListSet<>();
    private final AtomicInteger totalSubmissions = new AtomicInteger(0);

    /**
     * Adds a score entry to the leaderboard thread-safely.
     *
     * @param entry the score entry to add
     */
    public void submitScore(ScoreEntry entry) {
       //TODO
        leaderboard.add(entry);
        totalSubmissions.incrementAndGet();
    }

    /**
     * Returns the top n scores as an immutable list, highest score first.
     *
     * @param n number of top entries to return
     * @return immutable top-n list
     */
    public List<ScoreEntry> getTopN(int n) {
        // TODO
        return leaderboard.stream()   // stream the sorted set
                .limit(n)            // take the top n entries
                .toList();           // collect as immutable list
    }

    /**
     * Returns how many times submitScore has been called since creation.
     */
    public int getTotalSubmissions() {
        // TODO
     return totalSubmissions.get();

    }

    /**
     * Simulates concurrent score submissions using an ExecutorService.
     *
     * Each player in the list submits scoresEach random scores on a separate thread.
     * Wait for all threads to finish before returning.
     *
     * @param players    list of player names
     * @param scoresEach number of random scores each player submits
     */
    public void runSimulation(List<String> players, int scoresEach) throws InterruptedException {

        // Create a thread pool with as many threads as there are players.
        // Each thread will handle one player's score submissions.
        ExecutorService pool = Executors.newFixedThreadPool(players.size());
        // Random instance for generating score values. Shared across threads but not a problem here
        // because each submission is independent and Random methods are thread-safe for practical purposes.
         Random rand = new Random();

        // Submit a task for each player to the thread pool.
        for (String player : players) {
        pool.submit(() -> {
            // Each player submits 'scoresEach' scores
            for (int i = 0; i < scoresEach; i++) {
                int score = rand.nextInt(1000);

                // Create a new ScoreEntry with player name, score, and current timestamp
                // Timestamp ensures uniqueness even if two scores are the same
                ScoreEntry entry = new ScoreEntry(
                        player,
                        score,
                        System.currentTimeMillis()
                );

                // Submit the score entry to the leaderboard.
                // Thread-safe because submitScore uses ConcurrentSkipListSet and AtomicInteger.
                submitScore(entry);
            }
        });
    }

pool.shutdown();

        // Wait for all tasks to finish, up to 1 minute.
        // If tasks are still running after 1 minute, throw an exception.
        // This ensures the simulation completes or fails fast.
    if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
        throw new RuntimeException("Simulation timed out!");
    }

    }
}
