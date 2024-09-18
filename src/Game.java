import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Game extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JMenu gameMenu, aboutMenu, exitMenu;
    private int currentLevel;
    private int currentScore;
    private JLabel highScoreLabel;
    private HighScoreManager highScoreManager;

    public Game() {
        currentScore=0;
        setTitle("Memory Card Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        highScoreManager = new HighScoreManager();

        // Menü çubuğunu oluşturma
        createMenuBar();
        setJMenuBar(menuBar);

        // Ana menü panelini oluşturma
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, "Menu");
        add(mainPanel);

        setMenuBarVisible(false); // Başlangıçta menü çubuğunu gizle
        setVisible(true);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        gameMenu = new JMenu("GAME");
        aboutMenu = new JMenu("ABOUT");
        exitMenu = new JMenu("EXIT");

        JMenuItem restartMenuItem = new JMenuItem("Restart");
        restartMenuItem.addActionListener(e -> restartGame());

        JMenuItem highScoresMenuItem = new JMenuItem("High Scores");
        highScoresMenuItem.addActionListener(e -> showHighScores());

        JMenuItem developerMenuItem = new JMenuItem("Developer");
        developerMenuItem.addActionListener(e -> showDeveloperInfo());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        JMenuItem backToMain = new JMenuItem("Back To Menu");
        backToMain.addActionListener(e->backToMain());

        gameMenu.add(restartMenuItem);
        gameMenu.add(highScoresMenuItem);
        aboutMenu.add(developerMenuItem);
        exitMenu.add(exitMenuItem);
        exitMenu.add(backToMain);

        menuBar.add(gameMenu);
        menuBar.add(aboutMenu);
        menuBar.add(exitMenu);
    }

    private JPanel createMenuPanel() {
        BackgroundPanel panel = new BackgroundPanel("Java Project Assets/background.jpg");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Memory Card Game");
        titleLabel.setFont(new Font("Serif", Font.ITALIC, 42));
        titleLabel.setForeground(Color.CYAN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        JButton selectLevelButton = new JButton("Select Level");
        selectLevelButton.addActionListener(e -> selectLevel());

        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(e -> showInstructions());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        JButton highScoreTable = new JButton("High Scores");
        highScoreTable.addActionListener(e->showHighScores());

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(startButton, gbc);

        gbc.gridy = 2;
        panel.add(selectLevelButton, gbc);

        gbc.gridy = 3;
        panel.add(instructionsButton, gbc);

        gbc.gridy = 4;
        panel.add(highScoreTable,gbc);


        gbc.gridy = 5;
        panel.add(exitButton, gbc);

        highScoreLabel = new JLabel("High Score:  " + getHighScoreString());
        highScoreLabel.setForeground(Color.CYAN);
        gbc.gridy = 6;
        panel.add(highScoreLabel, gbc);

        return panel;
    }

    private String getHighScoreString() {
        List<HighScore> topScores = highScoreManager.getTopHighScores(1);
        return topScores.isEmpty() ? "0" : topScores.getFirst().toString();
    }

    private void backToMain(){
        cardLayout.show(mainPanel, "Menu");
        setMenuBarVisible(false);
    }

    private void startGame() {
        currentLevel = 1;
        currentScore = 0;
        // Yeni bir oyun oluştur
        LevelPanel level1 = new LevelPanel(1, 18, cardLayout, mainPanel, this);
        LevelPanel level2 = new LevelPanel(2, 15, cardLayout, mainPanel, this);
        LevelPanel level3 = new LevelPanel(3, 12, cardLayout, mainPanel, this);

        mainPanel.add(level1, "Level1");
        mainPanel.add(level2, "Level2");
        mainPanel.add(level3, "Level3");

        // Menü panelini göster
        cardLayout.show(mainPanel, "Level"+currentLevel);
        setMenuBarVisible(true);
    }

    private void selectLevel() {
        String[] levels = {"Level 1", "Level 2", "Level 3"};
        String selectedLevel = (String) JOptionPane.showInputDialog(this, "Select Level:", "Level Choice", JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);

        if (selectedLevel != null) {
            switch (selectedLevel) {
                case "Level 1":
                    currentLevel = 1;
                    LevelPanel level1 = new LevelPanel(1, 18, cardLayout, mainPanel, this);
                    mainPanel.add(level1, "Level1");
                    break;
                case "Level 2":
                    currentLevel = 2;
                    LevelPanel level2 = new LevelPanel(2, 15, cardLayout, mainPanel, this);
                    mainPanel.add(level2, "Level2");
                    break;
                case "Level 3":
                    currentLevel = 3;
                    LevelPanel level3 = new LevelPanel(3, 12, cardLayout, mainPanel, this);
                    mainPanel.add(level3, "Level3");
                    break;
            }
            currentScore=0;
            cardLayout.show(mainPanel, "Level" + currentLevel);
            setMenuBarVisible(true);
        }
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(this, "Instructions:\nThere are 3 levels in game. It gets gradually harder!\nMatch all pairs of cards to win!", "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartGame() {
            cardLayout.show(mainPanel, "Menu");
            setMenuBarVisible(false);
            startGame(); // Oyunu yeniden başlat
    }

    private void showHighScores() {
        List<HighScore> topScores = highScoreManager.getTopHighScores(10);
        StringBuilder highScoresText = new StringBuilder("High Scores:\n");
        for (HighScore score : topScores) {
            highScoresText.append(score).append("\n");
        }
        JOptionPane.showMessageDialog(this, highScoresText.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDeveloperInfo() {
        JOptionPane.showMessageDialog(this, "Developer: Yusuf Ozdil 20210702049\n", "Developer Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateHighScore(int newScore) {
        String playerName = JOptionPane.showInputDialog(null, "What's your nickname?");
        HighScore highScore = new HighScore(playerName, newScore);
        highScoreManager.addHighScore(highScore);
        highScoreLabel.setText("High Score:  " + getHighScoreString());
    }

    void setMenuBarVisible(boolean visible) {
        gameMenu.setVisible(visible);
        aboutMenu.setVisible(visible);
        exitMenu.setVisible(visible);
    }

    public int getCurrentScore() { // Mevcut skoru döndüren metot
        return currentScore;
    }

    public void setCurrentScore(int score) { // Mevcut skoru ayarlayan metot
        this.currentScore = score;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}

