package services.Cache;

import exceptions.CacheInitializationException;
import exceptions.CacheUpdateFailureException;
import models.PlayerScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Implementation of CacheServices for leaderboard caching.
 */
public class PlayerScoreCache implements CacheServices {

    private int topN; // Number of top players to cache
    private PriorityQueue<PlayerScore> minHeap; // Min-heap to store top N scores
    private Map<String, PlayerScore> playerToScore; // Map to quickly access player scores by ID

    private static final Logger logger = LoggerFactory.getLogger(PlayerScoreCache.class);

    /**
     * Initializes the cache with top N scores from the provided data set.
     *
     * @param topN    Number of top players to cache.
     * @param dataSet List of player scores to initialize the cache with.
     * @throws CacheInitializationException If cache initialization fails.
     */
    @Override
    public  void initialize(int topN, List<PlayerScore> dataSet) throws CacheInitializationException {
        this.topN = topN;
        try {
            minHeap = new PriorityQueue<>();
            playerToScore = new HashMap<>();
            // Populate min-heap and player map with initial data
            for (PlayerScore score : dataSet) {
                if (minHeap.size() < topN) {
                    minHeap.add(score);
                    playerToScore.put(score.getPlayerId(), score);
                } else {
                    // Replace lowest score in heap if current score is higher
                    if (score.getScore() > minHeap.peek().getScore()) {
                        PlayerScore removedScore = minHeap.poll();
                        minHeap.add(score);
                        playerToScore.remove(removedScore.getPlayerId());
                        playerToScore.put(score.getPlayerId(), score);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to initialize cache - {}", e.getMessage());
            throw new CacheInitializationException("Failed to initialize cache");
        }
    }

    /**
     * Adds or updates a player score in the cache.
     *
     * @param score Player score to add or update.
     * @throws CacheUpdateFailureException If cache update fails.
     */
    @Override
    public void addToCache(PlayerScore score) throws CacheUpdateFailureException {
        try {
            if (playerToScore.containsKey(score.getPlayerId())) {
                // Update existing score if new score is higher
                PlayerScore existingScore = playerToScore.get(score.getPlayerId());
                if (existingScore.getScore() < score.getScore()) {
                    logger.info(String.format("Updating {}'s score to {}", existingScore.getPlayerId(), score.getScore()));
                    minHeap.remove(existingScore);
                    minHeap.add(score);
                    playerToScore.put(score.getPlayerId(), score);
                }
                return;
            }
            // Add new score to cache if there's space or if it's higher than the lowest score in heap
            if (minHeap.size() < topN) {
                minHeap.add(score);
                playerToScore.put(score.getPlayerId(), score);
            } else {
                if (score.getScore() > minHeap.peek().getScore()) {
                    PlayerScore removedScore = minHeap.poll();
                    minHeap.add(score);
                    playerToScore.remove(removedScore.getPlayerId());
                    playerToScore.put(score.getPlayerId(), score);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to update cache - {}", e.getMessage());
            throw new CacheUpdateFailureException("Failed to update cache");
        }
    }

    /**
     * Retrieves the top N players from the cache.
     *
     * @return List of top N player scores.
     */
    @Override
    public  List<PlayerScore> getTopNPlayers() {
        List<PlayerScore> result = new ArrayList<>(minHeap); // Convert min-heap to list
        Collections.sort(result, Collections.reverseOrder()); // Sort scores in descending order
        return result;
    }
}
