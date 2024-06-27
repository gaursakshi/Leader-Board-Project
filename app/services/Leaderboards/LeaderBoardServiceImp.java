package services.Leaderboards;

import com.google.inject.Inject;

import Constants.Constants;

import exceptions.CacheInitializationException;
import exceptions.CacheUpdateFailureException;
import exceptions.LeaderboardNotInitializedException;
import exceptions.LeaderboardUpdateFailureException;
import models.PlayerScore;

import java.util.List;

import play.Logger;
import services.Cache.CacheServices;
import services.Scores.ScoreIngestionToLeaderBoards;
import views.PlayerScoreRepository;

/**
 * Implementation of LeaderBoard interface.
 */
public class LeaderBoardServiceImp implements LeaderBoard {

    private final CacheServices cache;
    private final PlayerScoreRepository scoreRepository;
    private final ScoreIngestionToLeaderBoards scoreIngestor;
    private final Logger.ALogger logger = Logger.of(this.getClass());
    private boolean leaderBoardInitialized;

    /**
     * Constructor for LeaderBoardServiceImp.
     *
     * @param cache            Cache service for leaderboard.
     * @param playScoreRepository Score repository for player scores.
     * @param scoreIngestor    Score ingestion service.
     * @throws LeaderboardNotInitializedException If leaderboard initialization fails.
     * @throws CacheInitializationException      If cache initialization fails.
     */
    @Inject
    public LeaderBoardServiceImp(CacheServices cache, PlayerScoreRepository playScoreRepository, ScoreIngestionToLeaderBoards scoreIngestor) throws LeaderboardNotInitializedException, CacheInitializationException {
        this.scoreIngestor = scoreIngestor;
        this.cache = cache;
        this.scoreRepository = playScoreRepository;
        leaderBoardInitialized = true;
        createBoard(Constants.DEFAULT_LEADERBOARD_SIZE);
    }

    /**
     * Creates the leaderboard with the specified topN players.
     *
     * @param topN Number of top players to include in the leaderboard.
     * @throws CacheInitializationException      If cache initialization fails.
     * @throws LeaderboardNotInitializedException If leaderboard initialization fails.
     */
    @Override
    public void createBoard(int topN) throws CacheInitializationException, LeaderboardNotInitializedException {
        initializeBoard(topN);
    }

    /**
     * Initializes the leaderboard with topN players from the repository.
     *
     * @param topN Number of top players to include in the leaderboard.
     * @throws LeaderboardNotInitializedException If leaderboard initialization fails.
     */
    private void initializeBoard(int topN) throws LeaderboardNotInitializedException {
        try {
            List<PlayerScore> allScores = scoreRepository.findAll();
            cache.initialize(topN, allScores);
            scoreIngestor.registerLeaderBoard(this);
            leaderBoardInitialized = true;
        } catch (CacheInitializationException e) {
            logger.error("Leader Board Initialization Failed - " + e.getMessage());
            throw new LeaderboardNotInitializedException(e.getMessage());
        }
    }

    /**
     * Retrieves the top N players from the leaderboard.
     *
     * @return List of top N players.
     * @throws LeaderboardNotInitializedException If leaderboard is not initialized.
     */
    @Override
    public List<PlayerScore> getTopNPlayers() throws LeaderboardNotInitializedException {
        if (!leaderBoardInitialized) {
            logger.error("Leader Board Not Initialized - Cannot retrieve top players");
            throw new LeaderboardNotInitializedException("LeaderBoard not yet initialized");
        }
        return cache.getTopNPlayers();
    }

    /**
     * Publishes a new score to the leaderboard.
     *
     * @param newScore The new score to publish.
     * @throws LeaderboardUpdateFailureException If leaderboard update fails.
     */
    @Override
    public void publish(PlayerScore newScore) throws LeaderboardUpdateFailureException {
        try {
            cache.addToCache(newScore);
        } catch (CacheUpdateFailureException e) {
            logger.error("Leader Board Update failed - " + e.getMessage());
            throw new LeaderboardUpdateFailureException(e.getMessage());
        }
    }
}
