package services.Scores;

import exceptions.DatabaseStorageException;
import exceptions.LeaderboardUpdateFailureException;
import models.PlayerScore;
import models.Response;
import services.Leaderboards.LeaderBoard;
import views.PlayerScoreRepository;

import javax.inject.Inject;
import java.util.*;

/**
 * Implementation of ScoreIngestionToLeaderBoards, ScoreIngestionToStorage, and ScoreIngestionService.
 */
public class ScoreIngestionServiceImpl implements ScoreIngestionToLeaderBoards, ScoreIngestionToStorage, ScoreIngestionService {

    // List to store registered leaderboards
    public static List<LeaderBoard> leaderBoards = new ArrayList<>();

    private final PlayerScoreRepository scoreRepository;

    @Inject
    public ScoreIngestionServiceImpl(PlayerScoreRepository playScoreRepository) {
        this.scoreRepository = playScoreRepository;
    }

    /**
     * Publishes a new player score to the database storage.
     *
     * @param newScore The new player score to publish.
     * @throws DatabaseStorageException If database storage operation fails.
     */
    @Override
    public void publishToDatabaseStore(PlayerScore newScore) throws DatabaseStorageException {
        try {
            // Check if a score for the player already exists and is higher
            Map<String, Object> queryParamsMap = new HashMap<>();
            queryParamsMap.put("player_id", newScore.getPlayerId());
            Optional<PlayerScore> scoreAlreadyPresent = scoreRepository.findById(PlayerScore.class, queryParamsMap);
            if (scoreAlreadyPresent.isPresent() && scoreAlreadyPresent.get().getScore() >= newScore.getScore()) {
                return; // Skip saving if existing score is higher or equal
            }
            scoreRepository.save(newScore, scoreAlreadyPresent.isPresent()); // Save the new score if it's higher
        } catch (Exception e) {
            throw new DatabaseStorageException("Could not publish data to storage: " + e.getMessage());
        }
    }

    /**
     * Registers a leaderboard to receive updates.
     *
     * @param leaderBoard The leaderboard to register.
     */
    @Override
    public void registerLeaderBoard(LeaderBoard leaderBoard) {
        leaderBoards.add(leaderBoard); // Add the leaderboard to the list of registered leaderboards
        System.out.println("Registered leaderboards: " + leaderBoards.size());
    }

    /**
     * Publishes a new player score to all registered leaderboards.
     *
     * @param newScore The new player score to publish.
     * @throws LeaderboardUpdateFailureException If leaderboard update operation fails.
     */
    @Override
    public void publishToLeaderBoards(PlayerScore newScore) throws LeaderboardUpdateFailureException {
        for (LeaderBoard leaderBoard : leaderBoards) {
            leaderBoard.publish(newScore); // Publish the new score to each registered leaderboard
        }
    }

    /**
     * Publishes a new player score to both database storage and all registered leaderboards.
     *
     * @param newScore The new player score to publish.
     * @throws LeaderboardUpdateFailureException If leaderboard update operation fails.
     * @throws DatabaseStorageException          If database storage operation fails.
     */
    @Override
    public Response publish(PlayerScore newScore) throws LeaderboardUpdateFailureException, DatabaseStorageException {
        try {
            publishToDatabaseStore(newScore); // Publish to database storage
            publishToLeaderBoards(newScore);  // Publish to registered leaderboards
        } catch (LeaderboardUpdateFailureException | DatabaseStorageException e) {
            return Response.builder().message("User score insertion failed").build();
        }
        return Response.builder().message("User score ingested successfully").build();
    }
}
