package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.PlayerScore;
import models.Response;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.Scores.ScoreIngestionService;

import com.google.inject.Inject;

public class GameController extends Controller {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    ScoreIngestionService scoreIngestor;
    public Result postScore(Http.Request request) {
        try {
            JsonNode requestNode = request.body().asJson();
            PlayerScore newScore = objectMapper.readValue(requestNode.toString(),PlayerScore.class);
            Response response= scoreIngestor.publish(newScore);
            return ok(response.getMessage());
        } catch (Exception e) {
            System.out.println("Leaderboard Update failed - " + e.getMessage());
            return status(INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}




