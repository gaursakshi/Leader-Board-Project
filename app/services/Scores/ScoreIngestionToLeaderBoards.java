package services.Scores;

import exceptions.LeaderboardUpdateFailureException;
import models.PlayerScore;
import services.Leaderboards.LeaderBoard;

public interface ScoreIngestionToLeaderBoards {
	public void registerLeaderBoard(LeaderBoard leaderBoard);
	public void publishToLeaderBoards(PlayerScore newScore) throws LeaderboardUpdateFailureException;
}
