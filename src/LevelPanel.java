import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class LevelPanel extends JPanel {
    private int level;
    private int triesLeft;

    private JLabel triesLabel;
    private JLabel scoreLabel;

    private Card firstSelectedCard;
    private Card secondSelectedCard;

    private Timer flipBackTimer;

    private CardLayout cardLayout;

    private JPanel mainPanel;

    private Game game;

    public LevelPanel(int level, int tries, CardLayout cardLayout, JPanel mainPanel, Game game) {
        this.level = level;
        this.triesLeft = tries;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.game = game;

        setLayout(new BorderLayout());

        // Üst panel
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        triesLabel = new JLabel("Tries Left: " + triesLeft);
        scoreLabel = new JLabel("Score: " + game.getCurrentScore());
        triesLabel.setFont(new Font("Serif", Font.BOLD, 24));
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 24));
        triesLabel.setForeground(Color.MAGENTA);
        scoreLabel.setForeground(Color.MAGENTA);
        topPanel.add(triesLabel);
        topPanel.add(scoreLabel);

        add(topPanel, BorderLayout.NORTH);

        // Kart paneli
        JPanel cardsPanel = new JPanel(new GridLayout(4, 4));  // 4x4 grid

        List<Card> cards = new ArrayList<>();
        String frontImagePrefix;
        String backImage;

        switch (level) {
            case 1:
                frontImagePrefix = "Java Project Assets/Level1-InternetAssets/";
                backImage = "Java Project Assets/Level1-InternetAssets/no_image.png";
                break;
            case 2:
                frontImagePrefix = "Java Project Assets/Level2-CyberSecurityAssets/";
                backImage = "Java Project Assets/Level2-CyberSecurityAssets/no_image.png";
                break;
            case 3:
                frontImagePrefix = "Java Project Assets/Level3-GamingComputerAssets/";
                backImage = "Java Project Assets/Level3-GamingComputerAssets/no_image.png";
                break;
            default:
                frontImagePrefix = "";
                backImage = "";
        }
        List<Integer> selectedDesigns = selectRandomDesigns(9, 8);

        for (int i : selectedDesigns) {
            String frontImage = frontImagePrefix + (i) + ".png";
            cards.add(new Card(frontImage, backImage));
            cards.add(new Card(frontImage, backImage));
        }
        Collections.shuffle(cards);

        for (Card card : cards) {
            card.addActionListener(new CardFlipListener());
            cardsPanel.add(card);
        }

        add(cardsPanel, BorderLayout.CENTER);
    }

    private List<Integer> selectRandomDesigns(int totalDesigns, int designsToSelect) {
        List<Integer> designs = new ArrayList<>();
        for (int i = 0; i < totalDesigns; i++) {
            designs.add(i);
        }
        Collections.shuffle(designs);
        return designs.subList(0, designsToSelect);
    }

    private class CardFlipListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Card selectedCard = (Card) e.getSource();

            if (selectedCard.isFlipped() || (firstSelectedCard != null && secondSelectedCard != null)) {
                return; // Halihazırda zaten çevrildi veya iki kart eşleşti
            }

            selectedCard.flip();

            if (firstSelectedCard == null) {
                firstSelectedCard = selectedCard;
            } else {
                secondSelectedCard = selectedCard;

                if (firstSelectedCard.getFrontImage().equals(secondSelectedCard.getFrontImage())) {
                    game.setCurrentScore(game.getCurrentScore() + getScoreIncrement());
                    scoreLabel.setText("Score: " + game.getCurrentScore());
                    firstSelectedCard = null;
                    secondSelectedCard = null;

                    // Tüm kartlar eşleşmiş mi kontrol et
                    if (allCardsMatched()) {
                        JOptionPane.showMessageDialog(LevelPanel.this, "Congrats You Won!", "Aferin La", JOptionPane.INFORMATION_MESSAGE);
                        nextLevel();
                    }
                } else {
                    game.setCurrentScore(game.getCurrentScore() - getScorePenalty());
                    scoreLabel.setText("Score: " + game.getCurrentScore());
                    triesLeft--;
                    triesLabel.setText("Tries Left: " + triesLeft);
                    flipBackTimer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            firstSelectedCard.flip();
                            secondSelectedCard.flip();
                            firstSelectedCard = null;
                            secondSelectedCard = null;
                            flipBackTimer.stop();

                            if (level == 3) {
                                shuffleUnmatchedCards();
                            }
                        }
                    });
                    flipBackTimer.start();
                }
            }

            if (triesLeft <= 0 && !allCardsMatched()) {
                JOptionPane.showMessageDialog(LevelPanel.this, "You Lost, Try Again!", "Her Şey Bitti", JOptionPane.INFORMATION_MESSAGE);
                game.updateHighScore(game.getCurrentScore());
                cardLayout.show(mainPanel, "Menu");
                game.setMenuBarVisible(false);
            }
        }
    }

    private int getScoreIncrement() {
        switch (level) {
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    private int getScorePenalty() {
        switch (level) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    private boolean allCardsMatched() {
        Component[] components = ((JPanel) getComponent(1)).getComponents(); // Kart paneline ulaşmak için
        for (Component component : components) {
            if (component instanceof Card) {
                Card card = (Card) component;
                if (!card.isFlipped()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void nextLevel() {
        if (level < 3) {
            if(level==1){
                game.setCurrentLevel(game.getCurrentLevel()+1);
                LevelPanel level2 = new LevelPanel(2, 15, cardLayout, mainPanel, game);
                mainPanel.add(level2, "Level2");
            }else if(level==2){
                game.setCurrentLevel(game.getCurrentLevel()+1);
                LevelPanel level3 = new LevelPanel(3, 12, cardLayout, mainPanel, game);
                mainPanel.add(level3, "Level3");
            }
            cardLayout.show(mainPanel, "Level" + (level + 1));
        } else {
            JOptionPane.showMessageDialog(this, "Congratulations, You Won!", "Good Job!", JOptionPane.INFORMATION_MESSAGE);
            game.updateHighScore(game.getCurrentScore());
            cardLayout.show(mainPanel, "Menu");
            game.setMenuBarVisible(false);
        }
    }

    private void shuffleUnmatchedCards() {
        List<Card> unmatchedCards = new ArrayList<>();
        List<String> unmatchedImages = new ArrayList<>();

        // Eşlenmemiş kartları topla
        Component[] components = ((JPanel) getComponent(1)).getComponents();
        for (Component component : components) {
            if (component instanceof Card) {
                Card card = (Card) component;
                if (!card.isFlipped()) {
                    unmatchedCards.add(card);
                    unmatchedImages.add(card.getFrontImage());
                }
            }
        }

        // Eşlenmemiş görselleri karıştır
        Collections.shuffle(unmatchedImages);

        // Eşlenmemiş görselleri eşlenmemiş kartlara dağıt
        for (int i = 0; i < unmatchedCards.size(); i++) {
            Card card = unmatchedCards.get(i);
            card.setFrontImage(unmatchedImages.get(i));
        }

        ((JPanel) getComponent(1)).revalidate();
        ((JPanel) getComponent(1)).repaint();
    }


}
