package typingspeedtestgame;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class TypingSpeedTestGame {
    private static final String[] EASY_WORDS = {
        "cat", "dog", "run", "sun", "hat", "car", "box", "red", "big", "hot",
        "cup", "map", "pen", "key", "fly", "sky", "sea", "one", "two", "six",
        "blue", "fast", "jump", "play", "read", "book", "tree", "star", "moon", "fish"};
    private static final String[] MEDIUM_WORDS = {
        "animal", "beauty", "castle", "danger", "energy", "forest", "garden", "health",
        "island", "jungle", "market", "nature", "orange", "planet", "quick", "river",
        "silver", "travel", "unique", "valley", "window", "yellow", "zebra", "button"};
    private static final String[] HARD_WORDS = {
        "adventure", "beautiful", "challenge", "dangerous", "education", "freedom",
        "generous", "happiness", "imagine", "knowledge", "laughter", "magazine",
        "national", "ocean", "personal", "question", "remember", "strength",
        "together", "umbrella", "volunteer", "wonderful", "yesterday"};
    private static final String[] EXPERT_WORDS = {
        "accommodation", "basketball", "communication", "development", "environment",
        "fascinating", "generally", "happiness", "independent", "journalism",
        "kilometer", "laboratory", "mathematics", "northeastern", "opportunity",
        "philosophy", "qualification", "recommendation", "satisfaction", "technology"};
    private static final int TEST_DURATION = 30;
    private static final Color COLOR_CORRECT = new Color(46, 204, 113);
    private static final Color COLOR_INCORRECT = new Color(231, 76, 60);
    private static final Color COLOR_PENDING = new Color(180, 180, 180);
    private static final Color COLOR_CURRENT = new Color(255, 255, 255);
    private static final Color COLOR_BG = new Color(40, 40, 70);
    private String[] currentWordList;
    private ArrayList<String> shuffledWords;
    private int currentWordIndex = 0;
    private int correctWords = 0;
    private int totalTyped = 0;
    private int correctChars = 0;
    private boolean testActive = false;
    private long startTime;
    private String currentDifficulty = "";
    private javax.swing.Timer countdownTimer;
    private int timeLeft = TEST_DURATION;
    private JFrame frame;
    private JLabel timerLabel;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel correctWordsLabel;
    private JLabel wrongWordsLabel;
    private JLabel statusIconLabel;
    private JTextPane wordDisplayPane;
    private JTextField inputField;
    private JProgressBar timeProgressBar;
    private JProgressBar wordProgressBar;
    private JLabel difficultyLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel gamePanel;
    private JPanel resultPanel;
    private JLabel resultWPM;
    private JLabel resultAccuracy;
    private JLabel resultCorrectWords;
    private JLabel resultTotalWords;
    private JLabel resultDifficulty;
    private JLabel resultRating;
    public TypingSpeedTestGame() {
        createAndShowGUI();}
    private void createAndShowGUI() {
        frame = new JFrame("Typing Speed Test - CSE 2216");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 680);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        createMenuPanel();
        createGamePanel();
        createResultPanel();
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(resultPanel, "RESULT");
        frame.add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
        frame.setVisible(true);}
    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(30, 30, 60));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        JLabel titleLabel = new JLabel("TYPING SPEED TEST", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        JLabel subtitleLabel = new JLabel("Software Development I - CSE 2216", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
        String[] difficulties = {"Easy", "Medium", "Hard", "Expert"};
        Color[] colors = {
            new Color(46, 204, 113),
            new Color(52, 152, 219),
            new Color(241, 196, 15),
            new Color(231, 76, 60)};
        for (int i = 0; i < difficulties.length; i++) {
            JButton btn = createStyledButton(difficulties[i], colors[i]);
            final int difficulty = i + 1;
            btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            startGame(difficulty);}});
            buttonPanel.add(btn); }
        JButton exitBtn = createStyledButton("Exit", new Color(150, 150, 150));
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            System.exit(0);}});
        buttonPanel.add(exitBtn);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        menuPanel.add(centerPanel, BorderLayout.CENTER);
        JLabel footerLabel = new JLabel("Northern University of Business & Technology, Khulna", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(150, 150, 150));
        menuPanel.add(footerLabel, BorderLayout.SOUTH);}
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
            button.setBackground(bgColor.darker());}
            public void mouseExited(MouseEvent e) {
            button.setBackground(bgColor);}});
        return button;}
    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBackground(COLOR_BG);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        timerLabel = createStatLabel("Time", "30s", new Color(255, 100, 100));
        wpmLabel = createStatLabel("WPM", "0", new Color(100, 200, 255));
        accuracyLabel = createStatLabel("Accuracy", "0%", new Color(100, 255, 100));
        correctWordsLabel = createStatLabel("Correct", "0", new Color(100, 255, 150));
        wrongWordsLabel = createStatLabel("Wrong", "0", new Color(255, 100, 100));
        statsPanel.add(timerLabel);
        statsPanel.add(wpmLabel);
        statsPanel.add(accuracyLabel);
        statsPanel.add(correctWordsLabel);
        statsPanel.add(wrongWordsLabel);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setOpaque(false);
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        difficultyLabel = new JLabel("Easy", SwingConstants.LEFT);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        difficultyLabel.setForeground(new Color(200, 200, 200));
        statusIconLabel = new JLabel("Ready", SwingConstants.RIGHT);
        statusIconLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusIconLabel.setForeground(new Color(200, 200, 200));
        headerRow.add(difficultyLabel, BorderLayout.WEST);
        headerRow.add(statusIconLabel, BorderLayout.EAST);
        wordDisplayPane = new JTextPane();
        wordDisplayPane.setEditable(false);
        wordDisplayPane.setBackground(new Color(25, 25, 50));
        wordDisplayPane.setFont(new Font("Monospaced", Font.BOLD, 56));
        wordDisplayPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 150), 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        wordDisplayPane.setPreferredSize(new Dimension(780, 150));
        centerPanel.add(headerRow, BorderLayout.NORTH);
        centerPanel.add(wordDisplayPane, BorderLayout.CENTER);
        JPanel progressPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        progressPanel.setOpaque(false);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JPanel timeBarPanel = new JPanel(new BorderLayout(10, 0));
        timeBarPanel.setOpaque(false);
        JLabel timeBarLabel = new JLabel("Time:");
        timeBarLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timeBarLabel.setForeground(Color.WHITE);
        timeBarLabel.setPreferredSize(new Dimension(60, 20));
        timeProgressBar = new JProgressBar(0, TEST_DURATION);
        timeProgressBar.setValue(TEST_DURATION);
        timeProgressBar.setStringPainted(true);
        timeProgressBar.setForeground(new Color(100, 255, 100));
        timeProgressBar.setBackground(new Color(60, 60, 100));
        timeProgressBar.setFont(new Font("Arial", Font.BOLD, 12));
        timeBarPanel.add(timeBarLabel, BorderLayout.WEST);
        timeBarPanel.add(timeProgressBar, BorderLayout.CENTER);
        JPanel wordBarPanel = new JPanel(new BorderLayout(10, 0));
        wordBarPanel.setOpaque(false);
        JLabel wordBarLabel = new JLabel("Words:");
        wordBarLabel.setFont(new Font("Arial", Font.BOLD, 12));
        wordBarLabel.setForeground(Color.WHITE);
        wordBarLabel.setPreferredSize(new Dimension(60, 20));
        wordProgressBar = new JProgressBar(0, 100);
        wordProgressBar.setValue(0);
        wordProgressBar.setStringPainted(true);
        wordProgressBar.setForeground(new Color(100, 200, 255));
        wordProgressBar.setBackground(new Color(60, 60, 100));
        wordProgressBar.setFont(new Font("Arial", Font.BOLD, 12));
        wordBarPanel.add(wordBarLabel, BorderLayout.WEST);
        wordBarPanel.add(wordProgressBar, BorderLayout.CENTER);
        progressPanel.add(timeBarPanel);
        progressPanel.add(wordBarPanel);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        JLabel inputLabel = new JLabel("Type here (real-time feedback):");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputLabel.setForeground(Color.WHITE);
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 22));
        inputField.setBackground(new Color(50, 50, 80));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 150), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        inputField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (testActive) {
                updateHighlighting();}}});
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            processInput();}});
        inputField.setEnabled(false);
        bottomPanel.add(inputLabel, BorderLayout.NORTH);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        gamePanel.add(statsPanel, BorderLayout.NORTH);
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerPanel, BorderLayout.CENTER);
        centerWrapper.add(progressPanel, BorderLayout.SOUTH);
        gamePanel.add(centerWrapper, BorderLayout.CENTER);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);}
    private JLabel createStatLabel(String title, String value, Color color) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>" + title + 
            "<br><font size='5' color='rgb(" + color.getRed() + "," + color.getGreen() + 
            "," + color.getBlue() + ")'><b>" + value + "</b></font></div></html>", 
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(Color.WHITE);
        return label;}
    private void updateStatLabel(JLabel label, String title, String value, Color color) {
        label.setText("<html><div style='text-align:center;'>" + title + 
            "<br><font size='5' color='rgb(" + color.getRed() + "," + color.getGreen() + 
            "," + color.getBlue() + ")'><b>" + value + "</b></font></div></html>");}
    private void createResultPanel() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(30, 30, 60));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        JLabel titleLabel = new JLabel("GAME OVER!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(255, 200, 50));
        JPanel resultsGrid = new JPanel(new GridLayout(6, 2, 20, 15));
        resultsGrid.setOpaque(false);
        resultsGrid.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        resultsGrid.add(createResultLabel("Difficulty:"));
        resultDifficulty = createResultValueLabel("");
        resultsGrid.add(resultDifficulty);
        resultsGrid.add(createResultLabel("Words Typed:"));
        resultTotalWords = createResultValueLabel("");
        resultsGrid.add(resultTotalWords);
        resultsGrid.add(createResultLabel("Correct Words:"));
        resultCorrectWords = createResultValueLabel("");
        resultsGrid.add(resultCorrectWords);
        resultsGrid.add(createResultLabel("Accuracy:"));
        resultAccuracy = createResultValueLabel("");
        resultsGrid.add(resultAccuracy);
        resultsGrid.add(createResultLabel("Net WPM:"));
        resultWPM = createResultValueLabel("");
        resultsGrid.add(resultWPM);
        resultsGrid.add(createResultLabel("Rating:"));
        resultRating = createResultValueLabel("");
        resultsGrid.add(resultRating);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        JButton playAgainBtn = createStyledButton("Play Again", new Color(46, 204, 113));
        playAgainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "MENU");}});
        buttonPanel.add(playAgainBtn);
        JButton exitBtn = createStyledButton("Exit", new Color(231, 76, 60));
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            System.exit(0);}});
        buttonPanel.add(exitBtn);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(resultsGrid, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        resultPanel.add(centerPanel, BorderLayout.CENTER);}
    private JLabel createResultLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        return label;}
    private JLabel createResultValueLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(100, 255, 100));
        return label;}
    private void startGame(int difficulty) {
        switch(difficulty) {
            case 1: currentWordList = EASY_WORDS; currentDifficulty = "Easy"; break;
            case 2: currentWordList = MEDIUM_WORDS; currentDifficulty = "Medium"; break;
            case 3: currentWordList = HARD_WORDS; currentDifficulty = "Hard"; break;
            case 4: currentWordList = EXPERT_WORDS; currentDifficulty = "Expert"; break;
            default: currentWordList = EASY_WORDS; currentDifficulty = "Easy";}
        shuffledWords = new ArrayList<String>(Arrays.asList(currentWordList));
        Collections.shuffle(shuffledWords);
        currentWordIndex = 0;
        correctWords = 0;
        totalTyped = 0;
        correctChars = 0;
        timeLeft = TEST_DURATION;
        testActive = true;
        difficultyLabel.setText("Difficulty: " + currentDifficulty);
        statusIconLabel.setText("Type the word!");
        statusIconLabel.setForeground(new Color(100, 200, 255));
        updateStats();
        inputField.setText("");
        inputField.setEnabled(true);
        inputField.requestFocus();
        timeProgressBar.setValue(TEST_DURATION);
        timeProgressBar.setString(TEST_DURATION + "s / " + TEST_DURATION + "s");
        timeProgressBar.setForeground(new Color(100, 255, 100));
        wordProgressBar.setValue(0);
        wordProgressBar.setString("0 words typed");
        showNextWord();
        cardLayout.show(mainPanel, "GAME");
        startTime = System.currentTimeMillis();
        if (countdownTimer != null) {
            countdownTimer.stop();}
        countdownTimer = new javax.swing.Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            updateTimer();}});
        countdownTimer.start();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                inputField.requestFocusInWindow();}});}
    private void renderWord(String targetWord, String userInput) {
        StyledDocument doc = wordDisplayPane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            for (int i = 0; i < targetWord.length(); i++) {
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setFontFamily(attrs, "Monospaced");
                StyleConstants.setFontSize(attrs, 56);
                StyleConstants.setBold(attrs, true);
                if (i < userInput.length()) {
                    if (userInput.charAt(i) == targetWord.charAt(i)) {
                        StyleConstants.setForeground(attrs, COLOR_CORRECT);
                        StyleConstants.setBackground(attrs, new Color(46, 204, 113, 40));
                    } else {
                        StyleConstants.setForeground(attrs, COLOR_INCORRECT);
                        StyleConstants.setBackground(attrs, new Color(231, 76, 60, 60));
                        StyleConstants.setUnderline(attrs, true);}
                } else if (i == userInput.length()) {
                    StyleConstants.setForeground(attrs, COLOR_CURRENT);
                    StyleConstants.setBackground(attrs, new Color(100, 150, 255, 80));
                    StyleConstants.setUnderline(attrs, true);
                } else {
                    StyleConstants.setForeground(attrs, COLOR_PENDING);}
                doc.insertString(doc.getLength(), String.valueOf(targetWord.charAt(i)), attrs);}
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
        } catch (BadLocationException e) {
            e.printStackTrace();}}
    private void showNextWord() {
        if (currentWordIndex < shuffledWords.size()) {
            renderWord(shuffledWords.get(currentWordIndex), "");
        } else {
            ArrayList<String> newWords = new ArrayList<String>(Arrays.asList(currentWordList));
            Collections.shuffle(newWords);
            shuffledWords.addAll(newWords);
            renderWord(shuffledWords.get(currentWordIndex), "");} }
    private void updateHighlighting() {
        if (currentWordIndex >= shuffledWords.size()) return;
        String currentWord = shuffledWords.get(currentWordIndex);
        String userInput = inputField.getText();
        if (userInput.length() > currentWord.length()) {
            userInput = userInput.substring(0, currentWord.length());}
        renderWord(currentWord, userInput);
        updateLiveStats(userInput, currentWord);}
    private void updateLiveStats(String userInput, String currentWord) {
        int currentCorrect = 0;
        int minLen = Math.min(userInput.length(), currentWord.length());
        for (int i = 0; i < minLen; i++) {
            if (userInput.charAt(i) == currentWord.charAt(i)) {
                currentCorrect++;}}
        int liveTotalChars = totalTyped + userInput.length();
        int liveCorrectChars = correctChars + currentCorrect;
        int accuracy = 0;
        if (liveTotalChars > 0) {
            accuracy = (liveCorrectChars * 100) / liveTotalChars;}
        long elapsed = System.currentTimeMillis() - startTime;
        int wpm = 0;
        if (elapsed > 500) {
            double minutes = elapsed / 60000.0;
            if (minutes > 0) {
            wpm = (int)(correctWords / minutes);}}
        updateStatLabel(accuracyLabel, "Accuracy", accuracy + "%", new Color(100, 255, 100));
        updateStatLabel(wpmLabel, "WPM", String.valueOf(wpm), new Color(100, 200, 255));}
    private void updateTimer() {
        if (!testActive) return;
        long elapsed = System.currentTimeMillis() - startTime;
        int secondsElapsed = (int)(elapsed / 1000);
        timeLeft = TEST_DURATION - secondsElapsed;
        if (timeLeft <= 0) {
            timeLeft = 0;
            endGame();
            return;}
        Color timerColor = timeLeft < 10 ? new Color(255, 50, 50) : new Color(255, 100, 100);
        updateStatLabel(timerLabel, "Time", timeLeft + "s", timerColor);
        timeProgressBar.setValue(timeLeft);
        timeProgressBar.setString(timeLeft + "s / " + TEST_DURATION + "s");
        if (timeLeft < 10) {
            timeProgressBar.setForeground(new Color(255, 50, 50));
        } else if (timeLeft < 20) {
            timeProgressBar.setForeground(new Color(255, 200, 50));}
        updateStats();}
    private void updateStats() {
        long elapsed = System.currentTimeMillis() - startTime;
        int wpm = 0;
        if (elapsed > 500) {
            double minutes = elapsed / 60000.0;
            if (minutes > 0) {
            wpm = (int)(correctWords / minutes);}}
        int accuracy = 0;
        if (totalTyped > 0) {
            accuracy = (correctChars * 100) / totalTyped;}
        updateStatLabel(wpmLabel, "WPM", String.valueOf(wpm), new Color(100, 200, 255));
        updateStatLabel(accuracyLabel, "Accuracy", accuracy + "%", new Color(100, 255, 100));
        updateStatLabel(correctWordsLabel, "Correct", String.valueOf(correctWords), new Color(100, 255, 150));
        int wrongWords = currentWordIndex - correctWords;
        updateStatLabel(wrongWordsLabel, "Wrong", String.valueOf(wrongWords), new Color(255, 100, 100));
        int wordProgress = Math.min(100, currentWordIndex);
        wordProgressBar.setValue(wordProgress);
        wordProgressBar.setString(currentWordIndex + " words typed");}
    private void processInput() {
        if (!testActive) return;
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;
        String currentWord = shuffledWords.get(currentWordIndex);
        totalTyped += userInput.length();
        int minLength = Math.min(userInput.length(), currentWord.length());
        for (int i = 0; i < minLength; i++) {
            if (userInput.charAt(i) == currentWord.charAt(i)) {
                correctChars++;}}
        boolean isCorrect = userInput.equals(currentWord);
        if (isCorrect) {
            correctWords++;
            wordDisplayPane.setBackground(new Color(46, 204, 113, 80));
            statusIconLabel.setText("Correct!");
            statusIconLabel.setForeground(COLOR_CORRECT);
        } else {
            wordDisplayPane.setBackground(new Color(231, 76, 60, 80));
            statusIconLabel.setText("Wrong! Expected: " + currentWord);
            statusIconLabel.setForeground(COLOR_INCORRECT);}
        updateStats();
        currentWordIndex++;
        inputField.setText("");
        javax.swing.Timer feedbackTimer = new javax.swing.Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                wordDisplayPane.setBackground(new Color(25, 25, 50));
                if (testActive) {
                    statusIconLabel.setText("Type the word!");
                    statusIconLabel.setForeground(new Color(100, 200, 255));
                    showNextWord();
                    inputField.requestFocusInWindow();}}});
        feedbackTimer.setRepeats(false);
        feedbackTimer.start();}
    private void endGame() {
        testActive = false;
        inputField.setEnabled(false);
        if (countdownTimer != null) {
            countdownTimer.stop();}
        long elapsed = System.currentTimeMillis() - startTime;
        int secondsElapsed = (int)(elapsed / 1000);
        int wpm = 0;
        if (secondsElapsed > 0) {
            wpm = (correctWords * 60) / secondsElapsed;}
        int accuracy = 0;
        if (totalTyped > 0) {
        accuracy = (correctChars * 100) / totalTyped;}
        resultDifficulty.setText(currentDifficulty);
        resultTotalWords.setText(String.valueOf(currentWordIndex));
        resultCorrectWords.setText(String.valueOf(correctWords));
        resultAccuracy.setText(accuracy + "%");
        resultWPM.setText(String.valueOf(wpm));
        String rating;
        Color ratingColor;
        if (wpm >= 60) {
            rating = "EXCELLENT! Professional level!";
            ratingColor = new Color(255, 215, 0);
        } else if (wpm >= 40) {
            rating = "GOOD! Above average!";
            ratingColor = new Color(100, 200, 255);
        } else if (wpm >= 20) {
            rating = "AVERAGE! Keep practicing!";
            ratingColor = new Color(255, 200, 100);
        } else {
            rating = "BEGINNER! Practice more!";
            ratingColor = new Color(255, 100, 100);}
        resultRating.setText(rating);
        resultRating.setForeground(ratingColor);
        cardLayout.show(mainPanel, "RESULT");}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            new TypingSpeedTestGame();}
        });
       }
      }