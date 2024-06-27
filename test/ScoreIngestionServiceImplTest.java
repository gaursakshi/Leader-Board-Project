import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import exceptions.DatabaseStorageException;
import exceptions.LeaderboardNotInitializedException;
import exceptions.LeaderboardUpdateFailureException;
import models.PlayerScore;
import models.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import services.Scores.ScoreIngestionServiceImpl;
import views.PlayerScoreRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ScoreIngestionServiceImplTest {

    // Service instance to be tested
    private ScoreIngestionServiceImpl scoreIngestor;

    // Mock repository for testing without hitting the actual database
    @Mock
    private PlayerScoreRepository scoreRepository;

    // Setup method to initialize mocks and service before each test
    @Before
    public void setUp() {
        // Mock the PlayerScoreRepository to avoid real database interactions
        scoreRepository = Mockito.mock(PlayerScoreRepository.class);
        // Initialize the ScoreIngestionServiceImpl with the mocked repository
        scoreIngestor = new ScoreIngestionServiceImpl(scoreRepository);
    }

    // Test case for a successful score publishing
    @Test
    public void successfulPublishTest() throws LeaderboardUpdateFailureException, DatabaseStorageException {
        // Mock player data for the test
        PlayerScore player = new PlayerScore("player10", 600, "sakshi");
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("player_id", "player10");

        // Simulate finding the player in the repository
        when(scoreRepository.findById(PlayerScore.class, queryParamsMap)).thenReturn(Optional.of(player));

        // Invoke the publish method of the service
        Response response = scoreIngestor.publish(player);

        // Verify that the repository's findById method was called once
        verify(scoreRepository, times(1)).findById(PlayerScore.class, queryParamsMap);
        // Verify that the save method was not called
        verify(scoreRepository, times(0)).save(player, true);

        // Assert that the response message indicates success
        assertEquals(response.getMessage(), "User score ingested successfully");
    }

    // Test case for an unsuccessful score publishing due to a database storage exception
    @Test
    public void unsuccessfulPublishTest() throws LeaderboardUpdateFailureException, DatabaseStorageException {
        // Mock player data for the test
        PlayerScore player = new PlayerScore("player10", 600, "sakshi");
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("player_id", "player10");

        // Simulate a database storage exception when finding the player in the repository
        when(scoreRepository.findById(PlayerScore.class, queryParamsMap))
                .thenThrow(new DatabaseStorageException("Could not publish data to storage: "));

        // Invoke the publish method of the service
        Response response = scoreIngestor.publish(player);

        // Verify that the repository's findById method was called once
        verify(scoreRepository, times(1)).findById(PlayerScore.class, queryParamsMap);

        // Assert that the response message indicates failure
        assertEquals("User score insertion failed", response.getMessage());
    }

    // Test case to publish a score and verify the response message
    @Test
    public void publishScoreAndVerify() throws DatabaseStorageException, LeaderboardUpdateFailureException, LeaderboardNotInitializedException {
        // Invoke the publish method with a new player's score
        Response response = scoreIngestor.publish(new PlayerScore("OP", 700, "sakshi"));

        // Assert that the response message indicates success
        assertEquals(response.getMessage(), "User score ingested successfully");
    }

}
