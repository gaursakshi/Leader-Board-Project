package services.Cache;

import exceptions.CacheInitializationException;
import exceptions.CacheUpdateFailureException;
import models.PlayerScore;

import java.util.List;


public interface CacheServices {

    void initialize(int topN, List<PlayerScore> dataSet) throws CacheInitializationException;
    void addToCache(PlayerScore score) throws CacheUpdateFailureException;
    List<PlayerScore> getTopNPlayers();

}
