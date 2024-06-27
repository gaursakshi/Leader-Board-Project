package services.Scores;


import exceptions.DatabaseStorageException;
import exceptions.LeaderboardUpdateFailureException;
import models.PlayerScore;
import models.Response;

public interface ScoreIngestionService {
	Response publish(PlayerScore newScore) throws LeaderboardUpdateFailureException, DatabaseStorageException;
}
