package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.Model;
import io.ebean.annotation.UpdateMode;

import javax.persistence.*;

@Entity
@Table(name = "player_scores")
public class PlayerScore extends Model implements Comparable<PlayerScore> {
    @Id
    @OneToOne
    @Column(name = "player_id")
//    @JsonProperty("player_id")
    String playerId;

    Long score;

    String playerName;

    public PlayerScore() {

    }

    public PlayerScore(String playerId, long score, String playerName) {
        this.playerId = playerId;
        this.score = score;
        this.playerName = playerName;
    }

    public long getScore() {
        return this.score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    @Override
    public int compareTo(PlayerScore p) {
        if (this.score == p.getScore()) {
            return this.playerName.compareTo(p.getPlayerName());
        }
        return Long.compare(this.score, p.getScore());
    }

    @Override
    public String toString() {
        return "{" + playerId + " " + score + "}";
    }

    @Override
    public boolean equals(Object o) {
        return this.playerId.equals(((PlayerScore) o).getPlayerId())
                && this.score == ((PlayerScore) o).getScore() && this.playerName.equals(((PlayerScore) o).getPlayerName());
    }
}
