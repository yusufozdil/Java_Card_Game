import javax.swing.*;

class Card extends JButton {
    private boolean isFlipped;
    private String frontImage;
    private String backImage;

    public Card(String frontImage, String backImage) {
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.isFlipped = false;
        setIcon(new ImageIcon(backImage)); // Arka yüz resmi ile başlat
    }

    public void flip() {
        if (isFlipped) {
            setIcon(new ImageIcon(backImage));
        } else {
            setIcon(new ImageIcon(frontImage));
        }
        isFlipped = !isFlipped;
    }

    public boolean isFlipped() {
        return isFlipped;
    }
    public void setFrontImage(String frontImage){this.frontImage=frontImage;}
    public String getFrontImage() {
        return frontImage;
    }
}
