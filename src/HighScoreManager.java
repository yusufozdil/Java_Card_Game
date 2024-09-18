import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String FILE_NAME = "highscores.dat";
    private List<HighScore> highScores;

    public HighScoreManager() {
        highScores = loadHighScores();
    }

    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
        Collections.sort(highScores);
        saveHighScores();
    }

    public List<HighScore> getTopHighScores(int count) {
        return highScores.subList(0, Math.min(count, highScores.size()));
    }

    private List<HighScore> loadHighScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<HighScore>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
