package rabbitMq;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.PlayerScore;
import services.Scores.ScoreIngestionService;

public class PlayerScoreConsumer  extends RabbitMqClient{

    @Inject
    ScoreIngestionService scoreIngestor;
    @Override
    public void processMessage(String message) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PlayerScore playerScore = objectMapper.readValue(message, PlayerScore.class);
            scoreIngestor.publish(playerScore);
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    public void startConsumer(){
        receiveMessage("player_score");
    }
}
