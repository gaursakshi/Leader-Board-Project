package controllers;

import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;
import rabbitMq.PlayerScoreConsumer;
import rabbitMq.RabbitMqClient;

public class HealthController extends Controller {

    @Inject
    PlayerScoreConsumer playerScoreConsumer;
    public Result getHealthCheckup() {
        playerScoreConsumer.startConsumer();
        return ok("Healthy");
    }
}
