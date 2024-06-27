package services.Scores;


import exceptions.DatabaseStorageException;
import models.PlayerScore;

public interface ScoreIngestionToStorage {
	public void publishToDatabaseStore(PlayerScore newScore) throws DatabaseStorageException;
}
