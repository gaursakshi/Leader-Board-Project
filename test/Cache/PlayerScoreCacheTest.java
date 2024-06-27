package Cache;

import exceptions.CacheInitializationException;
import exceptions.CacheUpdateFailureException;
import models.PlayerScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Cache.PlayerScoreCache;

import java.util.Arrays;
import java.util.List;



import static org.junit.Assert.assertEquals;

public class PlayerScoreCacheTest {

    private PlayerScoreCache cacheService;

    @BeforeEach
    public void setUp() {
        cacheService = new PlayerScoreCache();
    }

    //Test case for
    @Test
    public void testInitialize() throws CacheInitializationException {
        List<PlayerScore> initialData = Arrays.asList(
                new PlayerScore("player1", 100, "Alice"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie"),
                new PlayerScore("player4", 180, "David")
        );

        cacheService.initialize(3, initialData);
        List<PlayerScore> topPlayers = cacheService.getTopNPlayers();

        List<PlayerScore> expectedOrder = Arrays.asList(
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player4", 180, "David"),
                new PlayerScore("player3", 150, "Charlie")
        );
        assertEquals(topPlayers,expectedOrder);
    }

    @Test
    public void testAddToCache() throws CacheInitializationException, CacheUpdateFailureException {
        List<PlayerScore> initialData = Arrays.asList(
                new PlayerScore("player1", 100, "Alice"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie")
        );

        cacheService.initialize(3, initialData);
        cacheService.addToCache(new PlayerScore("player5", 210, "Eve"));

        List<PlayerScore> topPlayers = cacheService.getTopNPlayers();

        List<PlayerScore> expectedOrder = Arrays.asList(
                new PlayerScore("player5", 210, "Eve"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie")
        );

        assertEquals(topPlayers,expectedOrder);
    }

    @Test
    public void testUpdatePlayerScoreHigher() throws CacheInitializationException, CacheUpdateFailureException {
        List<PlayerScore> initialData = Arrays.asList(
                new PlayerScore("player1", 100, "Alice"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie")
        );

        cacheService.initialize(3, initialData);
        cacheService.addToCache(new PlayerScore("player3", 220, "Charlie"));

        List<PlayerScore> topPlayers = cacheService.getTopNPlayers();

        List<PlayerScore> expectedOrder = Arrays.asList(
                new PlayerScore("player3", 220, "Charlie"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player1", 100, "Alice")
        );

        assertEquals(topPlayers,expectedOrder);
    }

    @Test
    public void testUpdatePlayerScoreLower() throws CacheInitializationException, CacheUpdateFailureException {
        List<PlayerScore> initialData = Arrays.asList(
                new PlayerScore("player1", 100, "Alice"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie")
        );

        cacheService.initialize(3, initialData);
        cacheService.addToCache(new PlayerScore("player3", 120, "Charlie"));

        List<PlayerScore> topPlayers = cacheService.getTopNPlayers();

        List<PlayerScore> expectedOrder = Arrays.asList(
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie"),
                new PlayerScore("player1", 100, "Alice")

        );

        assertEquals(topPlayers,expectedOrder);
    }

    @Test
    public void testAddNewPlayerLowerScore() throws CacheInitializationException, CacheUpdateFailureException {
        List<PlayerScore> initialData = Arrays.asList(
                new PlayerScore("player1", 100, "Alice"),
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie")
        );

        cacheService.initialize(3, initialData);
        cacheService.addToCache(new PlayerScore("player6", 90, "Frank"));

        List<PlayerScore> topPlayers = cacheService.getTopNPlayers();

        List<PlayerScore> expectedOrder = Arrays.asList(
                new PlayerScore("player2", 200, "Bob"),
                new PlayerScore("player3", 150, "Charlie"),
                new PlayerScore("player1", 100, "Alice")
        );

        assertEquals(topPlayers,expectedOrder);
    }
}
