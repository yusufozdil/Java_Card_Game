import java.io.Serializable;

public class HighScore implements Serializable, Comparable<HighScore> {
    private String nickname;
    private int score;

    public HighScore(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return nickname + ": " + score;
    }
}
