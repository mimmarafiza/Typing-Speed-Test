package typingspeedtest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class TypingSpeedTestGUI extends JFrame {

    private static final String[] EASY_WORDS = {
        "cat", "dog", "run", "sun", "hat", "car", "box", "red", "big", "hot",
        "cup", "map", "pen", "key", "fly", "sky", "sea", "one", "two", "six",
        "blue", "fast", "jump", "play", "read", "book", "tree", "star", "moon", "fish",
        "ball", "bird", "hand", "food", "game", "help", "kind", "life", "long", "mind"};
    private static final String[] MEDIUM_WORDS = {
        "animal", "beauty", "castle", "danger", "energy", "forest", "garden", "health",
        "island", "jungle", "market", "nature", "orange", "planet", "quick", "river",
        "silver", "travel", "unique", "valley", "window", "yellow", "zebra", "button",
        "common", "dream", "eagle", "flight", "golden", "humble", "inside", "jacket",
        "kingdom", "little", "motion", "noble", "oxygen", "pencil", "random", "summer"};
    private static final String[] HARD_WORDS = {
        "adventure", "beautiful", "challenge", "dangerous", "education", "freedom",
        "generous", "happiness", "imagine", "knowledge", "laughter", "magazine",
        "national", "ocean", "personal", "question", "remember", "strength",
        "together", "umbrella", "volunteer", "wonderful", "yesterday", "celebrate",
        "delicious", "elephant", "familiar", "government", "hospital", "important",
        "keyboard", "language", "mountain", "nervous", "ordinary", "position",
        "recorder", "sentence", "telephone", "universe"};
    private static final String[] EXPERT_WORDS = {
        "accommodation", "basketball", "communication", "development", "environment",
        "fascinating", "generally", "happiness", "independent", "journalism",
        "kilometer", "laboratory", "mathematics", "northeastern", "opportunity",
        "philosophy", "qualification", "recommendation", "satisfaction", "technology",
        "uncomfortable", "vocabulary", "whether", "xylophone", "yesterday",
        "alternative", "beneficial", "celebration", "demonstration", "electricity",
        "forever", "gratitude", "illustration", "management", "notebook",
        "organization", "particular", "recognition", "sustainable", "transformation"};

    private static final int TEST_DURATION = 30; // seconds, same as Week 5
    private static final String MENU = "MENU";
    private static final String GAME = "GAME";
    private static final String RESULT = "RESULT";
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private List<String> shuffledWords;
    private int currentWordIndex;
    private int correctWords;
    private int totalTyped;
    private int correctChars;
    private long startTime;
    private int secondsLeft;
    private String levelName = "Easy";
    private javax.swing.Timer countdownTimer;
    private JLabel timerValueLabel;
    private JLabel wpmValueLabel;
    private JLabel accuracyValueLabel;
    private JLabel wordDisplayLabel;
    private JLabel feedbackLabel;
    private JTextField inputField;
    private JLabel resultTimeValue;
    private JLabel resultWordsValue;
    private JLabel resultCorrectValue;
    private JLabel resultAccuracyValue;
    private JLabel resultWpmValue;
    private JLabel resultRatingValue;
    public TypingSpeedTestGUI() {
        super("Typing Speed Test - CSE 2216");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        mainContainer.add(buildMenuScreen(), MENU);
        mainContainer.add(buildGameScreen(), GAME);
        mainContainer.add(buildResultScreen(), RESULT);

        add(mainContainer);
        cardLayout.show(mainContainer, MENU);
    }

    // =====================================================================
    //  MAIN MENU SCREEN
    // =====================================================================
    private JPanel buildMenuScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 50, 10, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Typing Speed Test", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("CSE 2216 - Software Development I", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        gbc.gridy = 2;
        panel.add(menuButton("Easy (3-5 chars)", EASY_WORDS, "Easy"), gbc);
        gbc.gridy = 3;
        panel.add(menuButton("Medium (6-8 chars)", MEDIUM_WORDS, "Medium"), gbc);
        gbc.gridy = 4;
        panel.add(menuButton("Hard (9-12 chars)", HARD_WORDS, "Hard"), gbc);
        gbc.gridy = 5;
        panel.add(menuButton("Expert (13+ chars)", EXPERT_WORDS, "Expert"), gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(30, 50, 10, 50);
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton, gbc);

        return panel;
    }

    private JButton menuButton(String label, String[] wordList, String name) {
        JButton button = new JButton(label);
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setFocusPainted(false);
        button.addActionListener(e -> startTest(wordList, name));
        return button;
    }

    // =====================================================================
    //  GAME PLAYING SCREEN
    // =====================================================================
    private JPanel buildGameScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setBackground(Color.WHITE);

        JPanel topBar = new JPanel(new GridLayout(1, 3, 10, 0));
        topBar.setBackground(Color.WHITE);
        timerValueLabel = new JLabel();
        wpmValueLabel = new JLabel();
        accuracyValueLabel = new JLabel();
        topBar.add(statBox("Time Left", timerValueLabel));
        topBar.add(statBox("WPM", wpmValueLabel));
        topBar.add(statBox("Accuracy", accuracyValueLabel));
        panel.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);

        wordDisplayLabel = new JLabel("word");
        wordDisplayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        wordDisplayLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(300, 32));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        inputField.addActionListener(this::onWordSubmitted);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JLabel hint = new JLabel("Type the word above and press Enter", SwingConstants.CENTER);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);

        center.add(Box.createVerticalGlue());
        center.add(wordDisplayLabel);
        center.add(inputField);
        center.add(feedbackLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(hint);
        center.add(Box.createVerticalGlue());
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel statBox(String title, JLabel valueLabel) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(new Color(245, 245, 245));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("SansSerif", Font.PLAIN, 11));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        box.add(t, BorderLayout.NORTH);
        box.add(valueLabel, BorderLayout.CENTER);
        return box;
    }

    // =====================================================================
    //  RESULT SCREEN
    // =====================================================================
    private JPanel buildResultScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Game Over!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        panel.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridLayout(6, 2, 10, 12));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        resultTimeValue = new JLabel();
        resultWordsValue = new JLabel();
        resultCorrectValue = new JLabel();
        resultAccuracyValue = new JLabel();
        resultWpmValue = new JLabel();
        resultRatingValue = new JLabel();

        card.add(grayLabel("Time Taken:"));      card.add(rightAligned(resultTimeValue));
        card.add(grayLabel("Words Typed:"));     card.add(rightAligned(resultWordsValue));
        card.add(grayLabel("Correct Words:"));   card.add(rightAligned(resultCorrectValue));
        card.add(grayLabel("Accuracy:"));        card.add(rightAligned(resultAccuracyValue));
        card.add(grayLabel("Net WPM:"));         card.add(rightAligned(resultWpmValue));
        card.add(grayLabel("Rating:"));          card.add(rightAligned(resultRatingValue));

        panel.add(card, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.setBackground(Color.WHITE);
        JButton playAgain = new JButton("Play Again");
        playAgain.addActionListener(e -> startTest(currentWordListReference, levelName));
        JButton mainMenu = new JButton("Main Menu");
        mainMenu.addActionListener(e -> cardLayout.show(mainContainer, MENU));
        buttons.add(playAgain);
        buttons.add(mainMenu);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel grayLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.GRAY);
        return l;
    }

    private JLabel rightAligned(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        return label;
    }

    // =====================================================================
    //  GAME LOGIC (ported from the Week 5 console version)
    // =====================================================================
    private String[] currentWordListReference;

    private void startTest(String[] wordList, String level) {
        currentWordListReference = wordList;
        levelName = level;

        shuffledWords = new ArrayList<>(Arrays.asList(wordList));
        Collections.shuffle(shuffledWords);

        currentWordIndex = 0;
        correctWords = 0;
        totalTyped = 0;
        correctChars = 0;
        secondsLeft = TEST_DURATION;
        startTime = System.currentTimeMillis();

        feedbackLabel.setText(" ");
        inputField.setText("");
        inputField.setEnabled(true);
        showNextWord();
        updateStatsDisplay();

        cardLayout.show(mainContainer, GAME);
        inputField.requestFocusInWindow();

        if (countdownTimer != null) countdownTimer.stop();
        countdownTimer = new javax.swing.Timer(1000, this::onTick);
        countdownTimer.start();
    }

    private void onTick(ActionEvent e) {
        secondsLeft--;
        updateStatsDisplay();
        if (secondsLeft <= 0 || currentWordIndex >= shuffledWords.size()) {
            countdownTimer.stop();
            showResults();
        }
    }

    private void showNextWord() {
        if (currentWordIndex < shuffledWords.size()) {
            wordDisplayLabel.setText(shuffledWords.get(currentWordIndex).toUpperCase());
        }
    }

    private void onWordSubmitted(ActionEvent e) {
        if (secondsLeft <= 0 || currentWordIndex >= shuffledWords.size()) return;

        String userInput = inputField.getText().trim();
        String currentWord = shuffledWords.get(currentWordIndex);

        // Same character-matching logic as Week 5's processInput()
        totalTyped += userInput.length();
        int minLength = Math.min(userInput.length(), currentWord.length());
        for (int i = 0; i < minLength; i++) {
            if (userInput.charAt(i) == currentWord.charAt(i)) {
                correctChars++;
            }
        }
        if (userInput.equals(currentWord)) {
            correctWords++;
            feedbackLabel.setForeground(new Color(0, 140, 0));
            feedbackLabel.setText("CORRECT!");
        } else {
            feedbackLabel.setForeground(Color.RED);
            feedbackLabel.setText("Incorrect - expected: " + currentWord);
        }

        currentWordIndex++;
        inputField.setText("");
        updateStatsDisplay();

        if (currentWordIndex >= shuffledWords.size()) {
            countdownTimer.stop();
            showResults();
        } else {
            showNextWord();
        }
    }

    /** Same formula as Week 5's calculateWPM(). */
    private int calculateWPM() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 1000) return 0;
        double minutes = elapsed / 60000.0;
        if (minutes == 0) return 0;
        return (int) (correctWords / minutes);
    }

    /** Same formula as Week 5's calculateAccuracy(). */
    private int calculateAccuracy() {
        if (totalTyped == 0) return 0;
        return (correctChars * 100) / totalTyped;
    }

    private void updateStatsDisplay() {
        timerValueLabel.setText(secondsLeft + "s");
        wpmValueLabel.setText(String.valueOf(calculateWPM()));
        accuracyValueLabel.setText(calculateAccuracy() + "%");
    }

    private void showResults() {
        inputField.setEnabled(false);
        long elapsed = System.currentTimeMillis() - startTime;
        int secondsElapsed = (int) (elapsed / 1000);
        int wpm = calculateWPM();
        int accuracy = calculateAccuracy();

        resultTimeValue.setText(secondsElapsed + " seconds");
        resultWordsValue.setText(String.valueOf(currentWordIndex));
        resultCorrectValue.setText(String.valueOf(correctWords));
        resultAccuracyValue.setText(accuracy + "%");
        resultWpmValue.setText(String.valueOf(wpm));

        // Same rating thresholds as Week 5's showResults()
        String rating;
        if (wpm >= 60) rating = "Excellent!";
        else if (wpm >= 40) rating = "Good!";
        else if (wpm >= 20) rating = "Average";
        else rating = "Beginner";
        resultRatingValue.setText(rating);

        cardLayout.show(mainContainer, RESULT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypingSpeedTestGUI().setVisible(true));
    }
}