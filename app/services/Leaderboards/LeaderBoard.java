package services.Leaderboards;

import exceptions.CacheInitializationException;
import exceptions.LeaderboardNotInitializedException;
import exceptions.LeaderboardUpdateFailureException;
import models.PlayerScore;

import java.util.List;

public interface LeaderBoard {


    public void createBoard(int topN) throws CacheInitializationException, LeaderboardNotInitializedException;
    public List<PlayerScore> getTopNPlayers() throws LeaderboardNotInitializedException;
    public void publish(PlayerScore newScore) throws LeaderboardUpdateFailureException;
}
