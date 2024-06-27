package bindings;

import com.google.inject.AbstractModule;
import com.rabbitmq.client.ConnectionFactory;
import services.Cache.CacheServices;
import services.Cache.PlayerScoreCache;
import services.Leaderboards.LeaderBoard;
import services.Leaderboards.LeaderBoardServiceImp;
import services.Scores.ScoreIngestionService;
import services.Scores.ScoreIngestionServiceImpl;
import services.Scores.ScoreIngestionToLeaderBoards;
import services.Scores.ScoreIngestionToStorage;

//Dependecy Injection
public class Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(CacheServices.class).to(PlayerScoreCache.class);
        bind(ScoreIngestionToLeaderBoards.class).to(ScoreIngestionServiceImpl.class);
        bind(ScoreIngestionToStorage.class).to(ScoreIngestionServiceImpl.class);
        bind(ScoreIngestionService.class).to(ScoreIngestionServiceImpl.class);
        bind(LeaderBoard.class).to(LeaderBoardServiceImp.class);
    }
}



