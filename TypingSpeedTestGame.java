package typingspeedtestgame;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
public class TypingSpeedTestGame {
    private static final String[] EASY_WORDS = {
        "cat", "dog", "run", "sun", "hat", "car", "box", "red", "big", "hot",
        "cup", "map", "pen", "key", "fly", "sky", "sea", "one", "two", "six",
        "blue", "fast", "jump", "play", "read", "book", "tree", "star", "moon",
        "fish", "ball", "bird", "hand", "food", "game", "help", "kind", "life",
        "long", "mind"};
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
    private static final int TEST_DURATION = 60;
    private static final int TARGET_WORDS = 40;
    private static final String SAVE_FILE = "leaderboard.csv";
    private static final String STATS_FILE = "player_stats.dat";
    private static final String CSV_HEADER = "name,difficulty,score,wpm,accuracy,timeUsed,date";
    private static final long MAX_PAUSE_MS = 60000;
    private static final Object[][] RATING_TIERS = {
        { 55, 92, 40, "LEGEND"     },
        { 35, 85, 35, "CHALLENGER" },
        { 20, 75, 25, "RISING STAR"},
        {  0,  0,  0, "NOOB"       }};
    private static final Achievement[] ACHIEVEMENTS = {
        new Achievement("first_game",   "First Steps",      "Complete your first game",              50),
        new Achievement("first_win",    "Word Master",      "Complete all 40 words in a game",       100),
        new Achievement("wpm_30",       "Getting Fast",     "Reach 30 WPM",                          100),
        new Achievement("wpm_50",       "Speed Demon",      "Reach 50 WPM",                          250),
        new Achievement("wpm_80",       "Lightning",        "Reach 80 WPM",                          400),
        new Achievement("acc_95",       "Sharp Eye",        "Finish with 95%+ accuracy",             100),
        new Achievement("acc_99",       "Perfectionist",    "Finish with 99%+ accuracy",             300),
        new Achievement("complete_easy","Easy Rider",       "Complete an Easy game",                 100),
        new Achievement("complete_hard","Mountain Mover",   "Complete a Hard game",                  300),
        new Achievement("complete_all", "Champion",         "Complete all 4 levels",                 500),
        new Achievement("perfect_game", "Flawless",         "100% accuracy on any level",            500),
        new Achievement("streak_7",     "On Fire",          "Play 7 days in a row",                  200),
        new Achievement("coins_1000",   "Coin Hoarder",     "Accumulate 1000 coins",                 250),
        new Achievement("speedrun",     "Speedrunner",      "Beat 50 WPM for 30 words",              300)};
    private String currentTheme = "Dark";
    private Color COLOR_CORRECT;
    private Color COLOR_INCORRECT;
    private Color COLOR_PENDING;
    private Color COLOR_CURRENT;
    private Color COLOR_BG;
    private Color COLOR_MENU_BG;
    private Color COLOR_GOLD;
    private Color COLOR_TEXT;
    private void applyTheme(String theme) {
        currentTheme = theme;
        if ("Light".equals(theme)) {
            COLOR_CORRECT   = new Color(34, 139, 34);
            COLOR_INCORRECT = new Color(200, 40, 40);
            COLOR_PENDING   = new Color(120, 120, 120);
            COLOR_CURRENT   = new Color(0, 0, 0);
            COLOR_BG        = new Color(245, 245, 250);
            COLOR_MENU_BG   = new Color(230, 230, 240);
            COLOR_GOLD      = new Color(184, 134, 11);
            COLOR_TEXT      = new Color(30, 30, 30);
        } else if ("Cyberpunk".equals(theme)) {
            COLOR_CORRECT   = new Color(0, 255, 200);
            COLOR_INCORRECT = new Color(255, 0, 100);
            COLOR_PENDING   = new Color(120, 120, 180);
            COLOR_CURRENT   = new Color(255, 255, 0);
            COLOR_BG        = new Color(15, 10, 30);
            COLOR_MENU_BG   = new Color(10, 5, 25);
            COLOR_GOLD      = new Color(255, 200, 0);
            COLOR_TEXT      = new Color(0, 255, 255);
        } else { // Dark
            COLOR_CORRECT   = new Color(46, 204, 113);
            COLOR_INCORRECT = new Color(231, 76, 60);
            COLOR_PENDING   = new Color(180, 180, 180);
            COLOR_CURRENT   = new Color(255, 255, 255);
            COLOR_BG        = new Color(40, 40, 70);
            COLOR_MENU_BG   = new Color(30, 30, 60);
            COLOR_GOLD      = new Color(255, 215, 0);
            COLOR_TEXT      = Color.WHITE; }}
    private String[] currentWordList;
    private ArrayList<String> shuffledWords;
    private int currentWordIndex = 0;
    private int correctWords = 0;
    private int totalTyped = 0;
    private int correctChars = 0;
    private boolean testActive = false;
    private boolean paused = false;
    private long startTime = 0;
    private long endTime;
    private long pausedAt = 0;
    private long totalPausedTime = 0;
    private String currentDifficulty = "";
    private String gameMode = "TIMED";
    private javax.swing.Timer countdownTimer;
    private int timeLeft = TEST_DURATION;
    private boolean runCompleted = false;
    private int totalXP = 0;
    private int totalCoins = 0;
    private int totalGames = 0;
    private int bestWPM = 0;
    private int avgWPM = 0;
    private int currentStreak = 0;
    private String lastPlayedDate = "";
    private Set<String> unlockedAchievements = new HashSet<String>();
    private boolean easyDone = false;
    private boolean mediumDone = false;
    private boolean hardDone = false;
    private boolean expertDone = false;
    private List<GameRecord> gameHistory = new ArrayList<GameRecord>();
    private JFrame frame;
    private JLabel timerLabel, wpmLabel, accuracyLabel;
    private JLabel correctWordsLabel, wrongWordsLabel;
    private JLabel statusIconLabel;
    private JTextPane wordDisplayPane;
    private JTextField inputField;
    private JProgressBar timeProgressBar, wordProgressBar;
    private JLabel difficultyLabel;
    private JButton pauseBtn;
    private CardLayout cardLayout;
    private JPanel mainPanel, menuPanel, gamePanel, resultPanel, leaderboardPanel, achievementsPanel, statsPanel, settingsPanel;
    private JPanel pauseOverlay;
    private JLabel menuLevelLabel, menuXPLabel, menuCoinsLabel, menuStreakLabel;
    private JLabel resultWPM, resultAccuracy, resultCorrectWords;
    private JLabel resultTotalWords, resultDifficulty, resultRating, resultTimeUsed;
    private JLabel resultXPGain, resultMessage, resultConsistency;
    private JPanel leaderboardListPanel;
    private JLabel leaderboardStatusLabel;
    private JPanel achievementsListPanel;
    private JLabel achievementsStatusLabel;
    private JComboBox<String> themeCombo;
    private JLabel[] statsBoxLabels = new JLabel[9];
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TypingSpeedTestGame();}});}
    public TypingSpeedTestGame() {
        applyTheme("Dark");
        loadPlayerStats();
        applyTheme(currentTheme);
        createAndShowGUI(); }
    private void createAndShowGUI() {
        frame = new JFrame("Typing Speed Test - CSE 2216");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 820);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        createMenuPanel();
        createGamePanel();
        createResultPanel();
        createLeaderboardPanel();
        createAchievementsPanel();
        createStatsPanel();
        createSettingsPanel();
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(resultPanel, "RESULT");
        mainPanel.add(leaderboardPanel, "LEADERBOARD");
        mainPanel.add(achievementsPanel, "ACHIEVEMENTS");
        mainPanel.add(statsPanel, "STATS");
        mainPanel.add(settingsPanel, "SETTINGS");
        frame.add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
        frame.setVisible(true);}
    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(COLOR_MENU_BG);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("TYPING SPEED TEST", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 44));
        titleLabel.setForeground(COLOR_TEXT);
        JLabel subtitleLabel = new JLabel("CSE 2216 - Software Development I", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        JPanel hudPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        hudPanel.setOpaque(false);
        hudPanel.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));
        menuLevelLabel  = createHudTile("LEVEL", "1");
        menuXPLabel     = createHudTile("XP", "0");
        menuCoinsLabel  = createHudTile("COINS", "0");
        menuStreakLabel = createHudTile("STREAK", "0");
        hudPanel.add(menuLevelLabel);
        hudPanel.add(menuXPLabel);
        hudPanel.add(menuCoinsLabel);
        hudPanel.add(menuStreakLabel);
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 250, 5, 250));
        infoPanel.add(makeInfoRow("Easy",   "3-5 char words",   new Color(46, 204, 113)));
        infoPanel.add(makeInfoRow("Medium", "6-8 char words",   new Color(52, 152, 219)));
        infoPanel.add(makeInfoRow("Hard",   "9-12 char words",  new Color(241, 196, 15)));
        infoPanel.add(makeInfoRow("Expert", "13+ char words",   new Color(231, 76, 60)));
        JLabel commonLabel = new JLabel(TARGET_WORDS + " words * " + TEST_DURATION + " seconds per game",
            SwingConstants.CENTER);
        commonLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        commonLabel.setForeground(new Color(180, 180, 180));
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 8, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 180, 10, 180));
        String[] difficulties = {"Easy", "Medium", "Hard", "Expert"};
        Color[] colors = {
            new Color(46, 204, 113),
            new Color(52, 152, 219),
            new Color(241, 196, 15),
            new Color(231, 76, 60)};
        for (int i = 0; i < difficulties.length; i++) {
            final int difficulty = i + 1;
            JButton btn = createStyledButton(difficulties[i] + " Mode", colors[i]);
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { startGame(difficulty, "TIMED"); }});
            buttonPanel.add(btn);}
        JButton statsBtn = createStyledButton("Statistics", new Color(26, 188, 156));
        statsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showStats(); }});
        buttonPanel.add(statsBtn);
        JButton achievementsBtn = createStyledButton("Achievements", COLOR_GOLD);
        achievementsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showAchievements(); }});
        buttonPanel.add(achievementsBtn);
        JButton leaderboardBtn = createStyledButton("Leaderboard", new Color(155, 89, 182));
        leaderboardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showLeaderboard(); }});
        buttonPanel.add(leaderboardBtn);
        JButton settingsBtn = createStyledButton("Settings", new Color(149, 165, 166));
        settingsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showSettings(); }});
        buttonPanel.add(settingsBtn);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        JPanel midPanel = new JPanel(new BorderLayout(0, 5));
        midPanel.setOpaque(false);
        midPanel.add(hudPanel, BorderLayout.NORTH);
        midPanel.add(infoPanel, BorderLayout.CENTER);
        midPanel.add(commonLabel, BorderLayout.SOUTH);
        centerPanel.add(midPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        menuPanel.add(centerPanel, BorderLayout.CENTER);
        JLabel footerLabel = new JLabel(
            "Northern University of Business & Technology, Khulna",
            SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(150, 150, 150));
        menuPanel.add(footerLabel, BorderLayout.SOUTH);
        updateMenuHUD();}
    private JLabel createHudTile(String title, String value) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>" + title +
            "<br><font size='4' color='#FFD54F'><b>" + value + "</b></font></div></html>",
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setForeground(new Color(180, 180, 180));
        label.setOpaque(true);
        label.setBackground(new Color(20, 20, 45));
        label.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        return label;}
    private void updateMenuHUD() {
        int level = getPlayerLevel();
        menuLevelLabel.setText("<html><div style='text-align:center;'>LEVEL" +
            "<br><font size='4' color='#FFD54F'><b>" + level + "</b></font></div></html>");
        menuXPLabel.setText("<html><div style='text-align:center;'>XP" +
            "<br><font size='4' color='#FFD54F'><b>" + totalXP + "</b></font></div></html>");
        menuCoinsLabel.setText("<html><div style='text-align:center;'>COINS" +
            "<br><font size='4' color='#FFD54F'><b>" + totalCoins + "</b></font></div></html>");
        menuStreakLabel.setText("<html><div style='text-align:center;'>STREAK" +
            "<br><font size='4' color='#FFD54F'><b>" + currentStreak + "</b></font></div></html>");}
    private int getPlayerLevel() {
        return (totalXP / 500) + 1;}
    private JPanel makeInfoRow(String level, String words, Color color) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel left = new JLabel(level);
        left.setFont(new Font("Arial", Font.BOLD, 12));
        left.setForeground(color);
        JLabel right = new JLabel(words, SwingConstants.RIGHT);
        right.setFont(new Font("Arial", Font.PLAIN, 12));
        right.setForeground(new Color(200, 200, 200));
        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;}
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e)  { button.setBackground(bgColor); }});
        return button;}
    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBackground(COLOR_BG);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JPanel statsRow = new JPanel(new GridLayout(1, 5, 10, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        timerLabel        = createStatLabel("Time", TEST_DURATION + "s", new Color(255, 100, 100));
        wpmLabel          = createStatLabel("WPM", "0", new Color(100, 200, 255));
        accuracyLabel     = createStatLabel("Accuracy", "100%", new Color(100, 255, 100));
        correctWordsLabel = createStatLabel("Correct", "0", new Color(100, 255, 150));
        wrongWordsLabel   = createStatLabel("Wrong", "0", new Color(255, 100, 100));
        statsRow.add(timerLabel);
        statsRow.add(wpmLabel);
        statsRow.add(accuracyLabel);
        statsRow.add(correctWordsLabel);
        statsRow.add(wrongWordsLabel);
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        difficultyLabel = new JLabel("Easy", SwingConstants.LEFT);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        difficultyLabel.setForeground(COLOR_TEXT);
        statusIconLabel = new JLabel("Ready", SwingConstants.CENTER);
        statusIconLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusIconLabel.setForeground(COLOR_TEXT);
        JPanel gameControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        gameControls.setOpaque(false);
        pauseBtn = new JButton("Pause");
        pauseBtn.setFont(new Font("Arial", Font.BOLD, 12));
        pauseBtn.setForeground(Color.WHITE);
        pauseBtn.setBackground(new Color(241, 196, 15));
        pauseBtn.setFocusPainted(false);
        pauseBtn.setBorderPainted(false);
        pauseBtn.setOpaque(true);
        pauseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        pauseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { togglePause(); }});
        JButton exitGameBtn = new JButton("Exit");
        exitGameBtn.setFont(new Font("Arial", Font.BOLD, 12));
        exitGameBtn.setForeground(Color.WHITE);
        exitGameBtn.setBackground(new Color(231, 76, 60));
        exitGameBtn.setFocusPainted(false);
        exitGameBtn.setBorderPainted(false);
        exitGameBtn.setOpaque(true);
        exitGameBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitGameBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        exitGameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { exitToMenu(); }});
        gameControls.add(pauseBtn);
        gameControls.add(exitGameBtn);
        headerRow.add(difficultyLabel, BorderLayout.WEST);
        headerRow.add(statusIconLabel, BorderLayout.CENTER);
        headerRow.add(gameControls, BorderLayout.EAST);
        JPanel wordContainer = new JPanel();
        wordContainer.setLayout(new OverlayLayout(wordContainer));
        wordContainer.setOpaque(false);
        wordDisplayPane = new JTextPane();
        wordDisplayPane.setEditable(false);
        wordDisplayPane.setBackground(new Color(25, 25, 50));
        wordDisplayPane.setFont(new Font("Monospaced", Font.BOLD, 56));
        wordDisplayPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 150), 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        wordDisplayPane.setPreferredSize(new Dimension(800, 150));
        wordDisplayPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        pauseOverlay = new JPanel(new GridBagLayout());
        pauseOverlay.setBackground(new Color(20, 20, 45, 220));
        pauseOverlay.setVisible(false);
        JLabel pauseText = new JLabel("PAUSED");
        pauseText.setFont(new Font("Arial", Font.BOLD, 44));
        pauseText.setForeground(new Color(241, 196, 15));
        pauseOverlay.add(pauseText);
        wordContainer.add(pauseOverlay);
        wordContainer.add(wordDisplayPane);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setOpaque(false);
        centerPanel.add(headerRow, BorderLayout.NORTH);
        centerPanel.add(wordContainer, BorderLayout.CENTER);
        JPanel progressPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        progressPanel.setOpaque(false);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JPanel timeBarPanel = new JPanel(new BorderLayout(10, 0));
        timeBarPanel.setOpaque(false);
        JLabel timeBarLabel = new JLabel("Time:");
        timeBarLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timeBarLabel.setForeground(COLOR_TEXT);
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
        wordBarLabel.setForeground(COLOR_TEXT);
        wordBarLabel.setPreferredSize(new Dimension(60, 20));
        wordProgressBar = new JProgressBar(0, TARGET_WORDS);
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
        JLabel inputLabel = new JLabel("Type here and press ENTER:");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputLabel.setForeground(COLOR_TEXT);
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
                if (testActive && !paused) updateHighlighting();}});
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testActive && !paused) processInput();}});
        inputField.setEnabled(false);
        bottomPanel.add(inputLabel, BorderLayout.NORTH);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        gamePanel.add(statsRow, BorderLayout.NORTH);
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
        label.setForeground(COLOR_TEXT);
        return label;}
    private void updateStatLabel(JLabel label, String title, String value, Color color) {
        label.setText("<html><div style='text-align:center;'>" + title +
            "<br><font size='5' color='rgb(" + color.getRed() + "," + color.getGreen() +
            "," + color.getBlue() + ")'><b>" + value + "</b></font></div></html>"); }
    private void togglePause() {
        if (!testActive) return;

        if (!paused) {
            paused = true;
            pausedAt = System.currentTimeMillis();
            pauseOverlay.setVisible(true);
            inputField.setEnabled(false);
            pauseBtn.setText("Resume");
            pauseBtn.setBackground(new Color(46, 204, 113));
            statusIconLabel.setText("Paused");
            statusIconLabel.setForeground(new Color(241, 196, 15));
        } else {
            long pauseDur = System.currentTimeMillis() - pausedAt;
            if (pauseDur > MAX_PAUSE_MS) pauseDur = MAX_PAUSE_MS;
            totalPausedTime += pauseDur;
            paused = false;
            pauseOverlay.setVisible(false);
            inputField.setEnabled(true);
            inputField.requestFocusInWindow();
            pauseBtn.setText("Pause");
            pauseBtn.setBackground(new Color(241, 196, 15));
            statusIconLabel.setText("Type the word!");
            statusIconLabel.setForeground(new Color(100, 200, 255));}}
    private void exitToMenu() {
        int choice = JOptionPane.showConfirmDialog(frame,
            "Exit current game? Progress will be lost.",
            "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;
        testActive = false;
        paused = false;
        pauseOverlay.setVisible(false);
        if (countdownTimer != null) countdownTimer.stop();
        inputField.setEnabled(false);
        pauseBtn.setText("Pause");
        pauseBtn.setBackground(new Color(241, 196, 15));
        updateMenuHUD();
        cardLayout.show(mainPanel, "MENU");}
    private void createResultPanel() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(COLOR_MENU_BG);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));
        JLabel titleLabel = new JLabel("GAME OVER!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 44));
        titleLabel.setForeground(new Color(255, 200, 50));
        JPanel resultsGrid = new JPanel(new GridLayout(9, 2, 20, 8));
        resultsGrid.setOpaque(false);
        resultsGrid.setBorder(BorderFactory.createEmptyBorder(15, 120, 15, 120));
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
        resultsGrid.add(createResultLabel("Time Used:"));
        resultTimeUsed = createResultValueLabel("");
        resultsGrid.add(resultTimeUsed);
        resultsGrid.add(createResultLabel("Rating:"));
        resultRating = createResultValueLabel("");
        resultsGrid.add(resultRating);
        resultsGrid.add(createResultLabel("Consistency:"));
        resultConsistency = new JLabel("", SwingConstants.LEFT);
        resultConsistency.setFont(new Font("Arial", Font.BOLD, 15));
        resultConsistency.setForeground(new Color(100, 255, 100));
        resultsGrid.add(resultConsistency);
        resultsGrid.add(createResultLabel("Rewards:"));
        resultXPGain = new JLabel("+0 XP   +0 coins");
        resultXPGain.setFont(new Font("Arial", Font.BOLD, 15));
        resultXPGain.setForeground(new Color(100, 255, 100));
        resultsGrid.add(resultXPGain);
        resultMessage = new JLabel(" ", SwingConstants.CENTER);
        resultMessage.setFont(new Font("Arial", Font.ITALIC, 14));
        resultMessage.setForeground(new Color(200, 220, 255));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 20));
        buttonPanel.setOpaque(false);
        JButton saveBtn = createStyledButton("Save Score", new Color(52, 152, 219));
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { saveCurrentScore(); }});
        buttonPanel.add(saveBtn);
        JButton statsBtn = createStyledButton("View Stats", new Color(26, 188, 156));
        statsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMenuHUD();
                showStats();} });
        buttonPanel.add(statsBtn);
        JButton playAgainBtn = createStyledButton("Play Again", new Color(46, 204, 113));
        playAgainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMenuHUD();
                cardLayout.show(mainPanel, "MENU");}});
        buttonPanel.add(playAgainBtn);
        JButton exitBtn = createStyledButton("Exit", new Color(231, 76, 60));
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { System.exit(0); } });
        buttonPanel.add(exitBtn);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(resultsGrid, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout(0, 5));
        southPanel.setOpaque(false);
        southPanel.add(resultMessage, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.add(southPanel, BorderLayout.SOUTH);
        resultPanel.add(centerPanel, BorderLayout.CENTER);}
    private JLabel createResultLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(COLOR_TEXT);
        return label;}
    private JLabel createResultValueLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(new Color(100, 255, 100));
        return label; }
    private void createStatsPanel() {
        statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(COLOR_MENU_BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        JLabel titleLabel = new JLabel("YOUR STATISTICS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_GOLD);
        JPanel statsGrid = new JPanel(new GridLayout(3, 3, 15, 15));
        statsGrid.setOpaque(false);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        for (int i = 0; i < 9; i++) {
            statsBoxLabels[i] = createStatBoxWithValue("", "", Color.WHITE);}
        statsGrid.add(statsBoxLabels[0]);
        statsGrid.add(statsBoxLabels[1]);
        statsGrid.add(statsBoxLabels[2]);
        statsGrid.add(statsBoxLabels[3]);
        statsGrid.add(statsBoxLabels[4]);
        statsGrid.add(statsBoxLabels[5]);
        statsGrid.add(statsBoxLabels[6]);
        statsGrid.add(statsBoxLabels[7]);
        statsGrid.add(statsBoxLabels[8]);
        refreshStatsBoxes();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttonPanel.setOpaque(false);
        JButton backBtn = createStyledButton("Back to Menu", new Color(120, 120, 120));
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "MENU"); }});
        buttonPanel.add(backBtn);
        statsPanel.add(titleLabel, BorderLayout.NORTH);
        statsPanel.add(statsGrid, BorderLayout.CENTER);
        statsPanel.add(buttonPanel, BorderLayout.SOUTH); }
    private void refreshStatsBoxes() {
        if (gameHistory.size() > 0) {
            int sum = 0;
            for (GameRecord r : gameHistory) sum += r.wpm;
            avgWPM = sum / gameHistory.size();
        } else {
            avgWPM = 0;}
        statsBoxLabels[0].setText(makeStatBoxHtml("Total Games", String.valueOf(totalGames), new Color(52, 152, 219)));
        statsBoxLabels[1].setText(makeStatBoxHtml("Best WPM", String.valueOf(bestWPM), new Color(231, 76, 60)));
        statsBoxLabels[2].setText(makeStatBoxHtml("Average WPM", String.valueOf(avgWPM), new Color(241, 196, 15)));
        statsBoxLabels[3].setText(makeStatBoxHtml("Total XP", String.valueOf(totalXP), new Color(100, 255, 100)));
        statsBoxLabels[4].setText(makeStatBoxHtml("Total Coins", String.valueOf(totalCoins), COLOR_GOLD));
        statsBoxLabels[5].setText(makeStatBoxHtml("Current Level", String.valueOf(getPlayerLevel()), new Color(155, 89, 182)));
        statsBoxLabels[6].setText(makeStatBoxHtml("Play Streak", currentStreak + " days", new Color(230, 126, 34)));
        statsBoxLabels[7].setText(makeStatBoxHtml("Achievements", unlockedAchievements.size() + "/" + ACHIEVEMENTS.length, new Color(46, 204, 113)));
        statsBoxLabels[8].setText(makeStatBoxHtml("Last Played", lastPlayedDate.isEmpty() ? "Never" : lastPlayedDate, new Color(149, 165, 166)));}
    private String makeStatBoxHtml(String title, String value, Color color) {
        return "<html><div style='text-align:center;'><b>" + title +
            "</b><br><font size='4' color='rgb(" + color.getRed() + "," + color.getGreen() +
            "," + color.getBlue() + ")'>" + value + "</font></div></html>";}
    private JLabel createStatBoxWithValue(String title, String value, Color color) {
        JLabel box = new JLabel(makeStatBoxHtml(title, value, color));
        box.setOpaque(true);
        box.setBackground(new Color(25, 25, 50));
        box.setBorder(BorderFactory.createLineBorder(color, 2));
        box.setFont(new Font("Arial", Font.PLAIN, 12));
        box.setForeground(new Color(200, 200, 200));
        return box; }
    private void showStats() {
        refreshStatsBoxes();
        cardLayout.show(mainPanel, "STATS"); }
    private void createSettingsPanel() {
        settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBackground(COLOR_MENU_BG);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        JLabel titleLabel = new JLabel("SETTINGS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_GOLD);
        JPanel settingsGrid = new JPanel(new GridLayout(2, 1, 10, 15));
        settingsGrid.setOpaque(false);
        settingsGrid.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        themePanel.setOpaque(false);
        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        themeLabel.setForeground(COLOR_TEXT);
        themeCombo = new JComboBox<String>(new String[]{"Dark", "Light", "Cyberpunk"});
        themeCombo.setSelectedItem(currentTheme);
        themeCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        themeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String) themeCombo.getSelectedItem();
                if (selected != null && !selected.equals(currentTheme)) {
                    applyTheme(selected);
                    savePlayerStats();
                    rebuildUI();}}});
        themePanel.add(themeLabel);
        themePanel.add(themeCombo);
        settingsGrid.add(themePanel);
        JButton resetBtn = createStyledButton("Reset All Data", new Color(231, 76, 60));
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "This will delete all your stats and scores.\nAre you sure?",
                    "Reset All Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    resetAllData();
                    JOptionPane.showMessageDialog(frame, "Data reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshStatsBoxes();
                    updateMenuHUD();} }});
        settingsGrid.add(resetBtn);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttonPanel.setOpaque(false);
        JButton backBtn = createStyledButton("Back to Menu", new Color(120, 120, 120));
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePlayerStats();
                cardLayout.show(mainPanel, "MENU");} });
        buttonPanel.add(backBtn);
        settingsPanel.add(titleLabel, BorderLayout.NORTH);
        settingsPanel.add(settingsGrid, BorderLayout.CENTER);
        settingsPanel.add(buttonPanel, BorderLayout.SOUTH);}
    private void rebuildUI() {
        mainPanel.removeAll();
        createMenuPanel();
        createGamePanel();
        createResultPanel();
        createLeaderboardPanel();
        createAchievementsPanel();
        createStatsPanel();
        createSettingsPanel();
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(resultPanel, "RESULT");
        mainPanel.add(leaderboardPanel, "LEADERBOARD");
        mainPanel.add(achievementsPanel, "ACHIEVEMENTS");
        mainPanel.add(statsPanel, "STATS");
        mainPanel.add(settingsPanel, "SETTINGS");
        mainPanel.revalidate();
        mainPanel.repaint();
        cardLayout.show(mainPanel, "SETTINGS");}
    private void showSettings() {
        cardLayout.show(mainPanel, "SETTINGS");}
    private void resetAllData() {
        totalXP = 0;
        totalCoins = 0;
        totalGames = 0;
        bestWPM = 0;
        avgWPM = 0;
        currentStreak = 0;
        lastPlayedDate = "";
        unlockedAchievements.clear();
        gameHistory.clear();
        easyDone = mediumDone = hardDone = expertDone = false;
        savePlayerStats();}
    private void createAchievementsPanel() {
        achievementsPanel = new JPanel(new BorderLayout());
        achievementsPanel.setBackground(COLOR_MENU_BG);
        achievementsPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        JLabel titleLabel = new JLabel("ACHIEVEMENTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_GOLD);
        achievementsStatusLabel = new JLabel(" ", SwingConstants.CENTER);
        achievementsStatusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        achievementsStatusLabel.setForeground(new Color(200, 200, 200));
        achievementsListPanel = new JPanel();
        achievementsListPanel.setLayout(new BoxLayout(achievementsListPanel, BoxLayout.Y_AXIS));
        achievementsListPanel.setBackground(new Color(25, 25, 50));
        JScrollPane scrollPane = new JScrollPane(achievementsListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 150), 2));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttonPanel.setOpaque(false);
        JButton backBtn = createStyledButton("Back to Menu", new Color(120, 120, 120));
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "MENU"); }});
        buttonPanel.add(backBtn);
        JPanel topSection = new JPanel(new BorderLayout(0, 5));
        topSection.setOpaque(false);
        topSection.add(titleLabel, BorderLayout.NORTH);
        topSection.add(achievementsStatusLabel, BorderLayout.SOUTH);
        achievementsPanel.add(topSection, BorderLayout.NORTH);
        achievementsPanel.add(scrollPane, BorderLayout.CENTER);
        achievementsPanel.add(buttonPanel, BorderLayout.SOUTH);}
    private void showAchievements() {
        loadAchievements();
        cardLayout.show(mainPanel, "ACHIEVEMENTS");}
    private void loadAchievements() {
        achievementsListPanel.removeAll();
        int unlocked = 0;
        for (Achievement a : ACHIEVEMENTS) {
            boolean isUnlocked = unlockedAchievements.contains(a.id);
            if (isUnlocked) unlocked++;
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(isUnlocked ? new Color(30, 60, 40) : new Color(30, 30, 55));
            row.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            JLabel icon = new JLabel(isUnlocked ? "UNLOCKED" : "LOCKED");
            icon.setFont(new Font("Arial", Font.BOLD, 13));
            icon.setForeground(isUnlocked ? new Color(100, 255, 150) : new Color(140, 140, 140));
            icon.setPreferredSize(new Dimension(85, 40));
            icon.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setOpaque(false);
            JLabel name = new JLabel(a.name);
            name.setFont(new Font("Arial", Font.BOLD, 14));
            name.setForeground(isUnlocked ? new Color(100, 255, 150) : new Color(180, 180, 180));
            JLabel desc = new JLabel(a.description);
            desc.setFont(new Font("Arial", Font.PLAIN, 11));
            desc.setForeground(isUnlocked ? new Color(200, 200, 200) : new Color(140, 140, 140));
            textPanel.add(name);
            textPanel.add(desc);
            JLabel reward = new JLabel(isUnlocked ? "+" + a.rewardXP + " XP (earned)" : "+" + a.rewardXP + " XP");
            reward.setFont(new Font("Arial", Font.BOLD, 11));
            reward.setForeground(isUnlocked ? COLOR_GOLD : new Color(140, 140, 140));
            reward.setPreferredSize(new Dimension(130, 30));
            reward.setHorizontalAlignment(SwingConstants.RIGHT);
            row.add(icon, BorderLayout.WEST);
            row.add(textPanel, BorderLayout.CENTER);
            row.add(reward, BorderLayout.EAST);
            achievementsListPanel.add(row);
            achievementsListPanel.add(Box.createVerticalStrut(2));}
        achievementsStatusLabel.setText(unlocked + " / " + ACHIEVEMENTS.length
            + " unlocked  -  Total XP: " + totalXP);
        achievementsListPanel.revalidate();
        achievementsListPanel.repaint();}
    private void createLeaderboardPanel() {
        leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setBackground(COLOR_MENU_BG);
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        JLabel titleLabel = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_GOLD);
        JPanel headerRow = new JPanel(new GridLayout(1, 6, 5, 5));
        headerRow.setBackground(new Color(20, 20, 45));
        headerRow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] headers = {"#", "Name", "Difficulty", "Score", "WPM", "Acc %"};
        for (String h : headers) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(new Color(200, 220, 255));
            headerRow.add(lbl);}
        leaderboardListPanel = new JPanel();
        leaderboardListPanel.setLayout(new BoxLayout(leaderboardListPanel, BoxLayout.Y_AXIS));
        leaderboardListPanel.setBackground(new Color(25, 25, 50));
        JScrollPane scrollPane = new JScrollPane(leaderboardListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 150), 2));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leaderboardStatusLabel = new JLabel(" ", SwingConstants.CENTER);
        leaderboardStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        leaderboardStatusLabel.setForeground(new Color(180, 180, 180));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttonPanel.setOpaque(false);
        JButton refreshBtn = createStyledButton("Refresh", new Color(52, 152, 219));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { loadLeaderboard(); }});
        buttonPanel.add(refreshBtn);
        JButton clearBtn = createStyledButton("Clear All", new Color(231, 76, 60));
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearLeaderboard(); } });
        buttonPanel.add(clearBtn);
        JButton backBtn = createStyledButton("Back to Menu", new Color(120, 120, 120));
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "MENU"); }});
        buttonPanel.add(backBtn);
        JPanel topSection = new JPanel(new BorderLayout(0, 5));
        topSection.setOpaque(false);
        topSection.add(titleLabel, BorderLayout.NORTH);
        topSection.add(headerRow, BorderLayout.SOUTH);
        leaderboardPanel.add(topSection, BorderLayout.NORTH);
        leaderboardPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.setOpaque(false);
        bottomSection.add(leaderboardStatusLabel, BorderLayout.NORTH);
        bottomSection.add(buttonPanel, BorderLayout.CENTER);
        leaderboardPanel.add(bottomSection, BorderLayout.SOUTH);}
    private void showLeaderboard() {
        loadLeaderboard();
        cardLayout.show(mainPanel, "LEADERBOARD");}
    private File getSaveFile() {
        return new File(System.getProperty("user.dir"), SAVE_FILE);
    }
    private List<ScoreEntry> readAllEntries() {
        List<ScoreEntry> entries = new ArrayList<ScoreEntry>();
        File f = getSaveFile();
        if (!f.exists()) return entries;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                try {
                    ScoreEntry entry = new ScoreEntry();
                    entry.name       = parts[0];
                    entry.difficulty = parts[1];
                    entry.score      = Integer.parseInt(parts[2]);
                    entry.wpm        = Integer.parseInt(parts[3]);
                    entry.accuracy   = Integer.parseInt(parts[4]);
                    entry.timeUsed   = Integer.parseInt(parts[5]);
                    entry.date       = parts[6];
                    entries.add(entry);
                } catch (NumberFormatException nfe) { }}
        } catch (IOException e) {
            System.err.println("Error reading leaderboard: " + e.getMessage());
        } finally {
            if (br != null) try { br.close(); } catch (IOException ignored) { }}
        return entries;}
    private void sortEntries(List<ScoreEntry> entries) {
        Collections.sort(entries, new Comparator<ScoreEntry>() {
            public int compare(ScoreEntry a, ScoreEntry b) {
                if (b.score != a.score) return b.score - a.score;
                if (b.wpm != a.wpm) return b.wpm - a.wpm;
                return b.accuracy - a.accuracy;}});}
    private boolean appendEntry(ScoreEntry entry) {
        File f = getSaveFile();
        boolean writeHeader = !f.exists() || f.length() == 0;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f, true));
            if (writeHeader) {
                bw.write(CSV_HEADER);
                bw.newLine();}
            bw.write(entry.toCsvLine());
            bw.newLine();
            bw.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
            return false;
        } finally {
            if (bw != null) try { bw.close(); } catch (IOException ignored) { }}}
    private void loadLeaderboard() {
        leaderboardListPanel.removeAll();
        List<ScoreEntry> entries = readAllEntries();
        sortEntries(entries);
        if (entries.isEmpty()) {
            leaderboardStatusLabel.setText("No scores yet. Play a game and save your score!");
            JLabel empty = new JLabel("No scores saved yet", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.ITALIC, 16));
            empty.setForeground(new Color(150, 150, 150));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(BorderFactory.createEmptyBorder(60, 0, 60, 0));
            leaderboardListPanel.add(empty);
        } else {
            leaderboardStatusLabel.setText(
                "Showing top " + Math.min(entries.size(), 50) + " of " + entries.size() + " scores");
            int rank = 0;
            for (ScoreEntry e : entries) {
                rank++;
                if (rank > 50) break;
                leaderboardListPanel.add(buildLeaderboardRow(rank, e)); }}
        leaderboardListPanel.revalidate();
        leaderboardListPanel.repaint();}
    private JPanel buildLeaderboardRow(int rank, ScoreEntry e) {
        JPanel row = new JPanel(new GridLayout(1, 6, 5, 5));
        row.setBackground(rank % 2 == 0 ? new Color(30, 30, 60) : new Color(35, 35, 70));
        row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        Color rowColor = new Color(220, 220, 220);
        if (rank == 1) rowColor = COLOR_GOLD;
        else if (rank == 2) rowColor = new Color(200, 200, 200);
        else if (rank == 3) rowColor = new Color(205, 127, 50);
        String prefix = "";
        if (rank == 1) prefix = "1st ";
        else if (rank == 2) prefix = "2nd ";
        else if (rank == 3) prefix = "3rd ";
        String[] fields = {
            prefix + "#" + rank,
            e.name,
            e.difficulty,
            String.valueOf(e.score),
            String.valueOf(e.wpm),
            String.valueOf(e.accuracy)};
        for (int i = 0; i < fields.length; i++) {
            JLabel lbl = new JLabel(fields[i],
                i == 0 ? SwingConstants.LEFT : SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", (rank <= 3 ? Font.BOLD : Font.PLAIN), 13));
            lbl.setForeground(rowColor);
            row.add(lbl); }
        return row;}
    private void clearLeaderboard() {
        int choice = JOptionPane.showConfirmDialog(frame,
            "Delete all scores? Cannot be undone.",
            "Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;
        File f = getSaveFile();
        if (f.exists() && f.delete()) {
            leaderboardStatusLabel.setText("All scores deleted.");}
        loadLeaderboard();}
    private void saveCurrentScore() {
        String name = JOptionPane.showInputDialog(frame, "Enter your name:", "Save Score",
            JOptionPane.PLAIN_MESSAGE);
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) name = "Anonymous";
        if (name.contains(",")) name = name.replace(",", ";");
        if (name.length() > 20) name = name.substring(0, 20);
        int timeUsed = getEffectiveElapsedSeconds();
        if (timeUsed <= 0) timeUsed = 1;
        ScoreEntry entry = new ScoreEntry();
        entry.name       = name;
        entry.difficulty = currentDifficulty;
        entry.score      = calculateFinalScore();
        entry.wpm        = calculateFinalWPM();
        entry.accuracy   = calculateFinalAccuracy();
        entry.timeUsed   = timeUsed;
        entry.date       = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        if (appendEntry(entry)) {
            JOptionPane.showMessageDialog(frame,
                "Score saved!\n\nWPM: " + entry.wpm + "\nAccuracy: " + entry.accuracy + "%",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to save score.", "Error", JOptionPane.ERROR_MESSAGE);}}
    private File getStatsFile() {
        return new File(System.getProperty("user.dir"), STATS_FILE);}
    private void loadPlayerStats() {
        File f = getStatsFile();
        if (!f.exists()) return;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length != 2) continue;
                String key = parts[0].trim();
                String val = parts[1].trim();
                if ("xp".equals(key))           totalXP = parseIntSafe(val, 0);
                else if ("coins".equals(key))   totalCoins = parseIntSafe(val, 0);
                else if ("games".equals(key))   totalGames = parseIntSafe(val, 0);
                else if ("bestWPM".equals(key)) bestWPM = parseIntSafe(val, 0);
                else if ("avgWPM".equals(key))  avgWPM = parseIntSafe(val, 0);
                else if ("streak".equals(key))  currentStreak = parseIntSafe(val, 0);
                else if ("lastDate".equals(key)) lastPlayedDate = val;
                else if ("easyDone".equals(key))   easyDone = "true".equals(val);
                else if ("mediumDone".equals(key)) mediumDone = "true".equals(val);
                else if ("hardDone".equals(key))   hardDone = "true".equals(val);
                else if ("expertDone".equals(key)) expertDone = "true".equals(val);
                else if ("theme".equals(key))      currentTheme = val;
                else if ("achievements".equals(key)) {
                    unlockedAchievements.clear();
                    if (!val.isEmpty()) {
                        for (String id : val.split("\\|")) {
                            if (!id.isEmpty()) unlockedAchievements.add(id);}}}}
        } catch (IOException e) {
            System.err.println("Error loading stats: " + e.getMessage());
        } finally {
            if (br != null) try { br.close(); } catch (IOException ignored) { }}}
    private void savePlayerStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("xp=").append(totalXP).append("\n");
        sb.append("coins=").append(totalCoins).append("\n");
        sb.append("games=").append(totalGames).append("\n");
        sb.append("bestWPM=").append(bestWPM).append("\n");
        sb.append("avgWPM=").append(avgWPM).append("\n");
        sb.append("streak=").append(currentStreak).append("\n");
        sb.append("lastDate=").append(lastPlayedDate).append("\n");
        sb.append("easyDone=").append(easyDone).append("\n");
        sb.append("mediumDone=").append(mediumDone).append("\n");
        sb.append("hardDone=").append(hardDone).append("\n");
        sb.append("expertDone=").append(expertDone).append("\n");
        sb.append("theme=").append(currentTheme).append("\n");
        StringBuilder ach = new StringBuilder();
        for (String id : unlockedAchievements) {
            if (ach.length() > 0) ach.append("|");
            ach.append(id); }
        sb.append("achievements=").append(ach.toString()).append("\n");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(getStatsFile()));
            bw.write(sb.toString());
            bw.flush();
        } catch (IOException e) {
            System.err.println("Error saving stats: " + e.getMessage());
        } finally {
            if (bw != null) try { bw.close(); } catch (IOException ignored) { }}}
    private int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; } }
    private long getEffectiveElapsedMs() {
        if (startTime == 0) return 0;
        long end = testActive ? System.currentTimeMillis() : endTime;
        long elapsed = (end - startTime) - totalPausedTime;
        return elapsed > 0 ? elapsed : 0;}
    private int getEffectiveElapsedSeconds() {
        int s = (int)(getEffectiveElapsedMs() / 1000);
        return s > 0 ? s : 1;}
    private int calculateFinalWPM() {
        int seconds = getEffectiveElapsedSeconds();
        if (seconds <= 0) return 0;
        return (int) Math.round((correctChars / 5.0) / (seconds / 60.0));}
    private int calculateFinalAccuracy() {
        if (totalTyped <= 0) return 0;
        return (correctChars * 100) / totalTyped;}
    private int calculateFinalScore() {
        int wpm = calculateFinalWPM();
        int acc = calculateFinalAccuracy();
        int diffMult = difficultyMultiplier(currentDifficulty);
        int raw = (wpm * acc * diffMult) / 100;
        if (runCompleted) raw += 500;
        return raw;}
    private int difficultyMultiplier(String difficulty) {
        if ("Easy".equals(difficulty))   return 10;
        if ("Medium".equals(difficulty)) return 15;
        if ("Hard".equals(difficulty))   return 20;
        if ("Expert".equals(difficulty)) return 25;
        return 10;}
    private String getRatingLabel(int wpm, int accuracy) {
        int wordsDone = currentWordIndex;
        int streakBump = currentStreak >= 3 ? 1 : 0;
        for (int i = 0; i < RATING_TIERS.length; i++) {
            int minWpm   = (Integer) RATING_TIERS[i][0];
            int minAcc   = (Integer) RATING_TIERS[i][1];
            int minWords = (Integer) RATING_TIERS[i][2];
            boolean wpmAccOK = (minWpm > 0 && wpm >= minWpm && accuracy >= minAcc);
            boolean wordsOK  = (minWords > 0 && wordsDone >= minWords);
            if (wpmAccOK || wordsOK) {
                int finalIndex = i - streakBump;
                if (finalIndex < 0) finalIndex = 0;
                return (String) RATING_TIERS[finalIndex][3]; }}
        return "NOOB";}
    private Color getRatingColor(int wpm) {
        if (wpm >= 55) return new Color(255, 100, 255);
        if (wpm >= 35) return COLOR_GOLD;
        if (wpm >= 20) return new Color(100, 200, 255);
        return new Color(200, 200, 200);}
    private String calculateConsistency() {
        int accuracy = calculateFinalAccuracy();
        if (accuracy >= 98) return "Excellent";
        if (accuracy >= 90) return "Good";
        if (accuracy >= 75) return "Average";
        return "Needs Practice";}
    private int calculateXPReward(int wpm, int accuracy) {
        int base = 20;
        int wpmBonus = wpm * 2;
        int accBonus = accuracy / 2;
        int diffBonus = difficultyMultiplier(currentDifficulty);
        int xp = base + wpmBonus + accBonus + diffBonus;
        if (runCompleted) xp += 100;
        return xp;}
    private int calculateCoinReward(int wpm, int accuracy) {
        int base = 10;
        int wpmBonus = wpm / 2;
        int accBonus = accuracy / 10;
        int coins = base + wpmBonus + accBonus;
        if (runCompleted) coins += 50;
        return coins;}
    private void checkAchievements(int wpm, int accuracy) {
        List<Achievement> newlyUnlocked = new ArrayList<Achievement>();
        Map<String, Boolean> conditions = new HashMap<String, Boolean>();
        conditions.put("first_game",     totalGames >= 1);
        conditions.put("first_win",      runCompleted);
        conditions.put("wpm_30",         wpm >= 30);
        conditions.put("wpm_50",         wpm >= 50);
        conditions.put("wpm_80",         wpm >= 80);
        conditions.put("acc_95",         accuracy >= 95);
        conditions.put("acc_99",         accuracy >= 99);
        conditions.put("complete_easy",  "Easy".equals(currentDifficulty)   && runCompleted);
        conditions.put("complete_hard",  "Hard".equals(currentDifficulty)   && runCompleted);
        conditions.put("complete_all",   easyDone && mediumDone && hardDone && expertDone);
        conditions.put("perfect_game",   accuracy == 100);
        conditions.put("streak_7",       currentStreak >= 7);
        conditions.put("coins_1000",     totalCoins >= 1000);
        conditions.put("speedrun",       wpm >= 50 && currentWordIndex >= 30);
        for (Achievement a : ACHIEVEMENTS) {
            if (unlockedAchievements.contains(a.id)) continue;
            Boolean met = conditions.get(a.id);
            if (met != null && met.booleanValue()) {
                unlockedAchievements.add(a.id);
                totalXP += a.rewardXP;
                newlyUnlocked.add(a); } }
        for (Achievement a : newlyUnlocked) {
            showAchievementPopup(a); }}
    private void showAchievementPopup(Achievement a) {
        JDialog dlg = new JDialog(frame, "Achievement Unlocked!", true);
        dlg.setSize(400, 180);
        dlg.setLocationRelativeTo(frame);
        dlg.setResizable(false);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        JLabel header = new JLabel("ACHIEVEMENT UNLOCKED", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(COLOR_GOLD);
        panel.add(header, BorderLayout.NORTH);
        JLabel name = new JLabel(a.name, SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.BOLD, 16));
        name.setForeground(new Color(100, 255, 150));
        panel.add(name, BorderLayout.CENTER);
        JLabel reward = new JLabel("+" + a.rewardXP + " XP", SwingConstants.CENTER);
        reward.setFont(new Font("Arial", Font.BOLD, 14));
        reward.setForeground(COLOR_GOLD);
        panel.add(reward, BorderLayout.SOUTH);
        dlg.add(panel);
        dlg.setVisible(true);}
    private String getMotivationalMessage(int wpm, int accuracy, String rating) {
        if ("LEGEND".equals(rating)) {
            return "You're absolutely incredible!";}
        if ("CHALLENGER".equals(rating)) {
            return "Challenger status! Keep pushing!";}
        if ("RISING STAR".equals(rating)) {
            return "Rising Star! Consistent improvement!";}
        if (accuracy == 100) {
            return "PERFECT! 100% accuracy!";}
        if (wpm >= 50) {
            return "Excellent speed! Great job!"; }
        if (accuracy >= 95) {
            return "Amazing accuracy!";}
        return "Keep practicing! You'll get there!";}
    private void startGame(int difficulty, String mode) {
        switch (difficulty) {
            case 1: currentWordList = EASY_WORDS;   currentDifficulty = "Easy";   break;
            case 2: currentWordList = MEDIUM_WORDS; currentDifficulty = "Medium"; break;
            case 3: currentWordList = HARD_WORDS;   currentDifficulty = "Hard";   break;
            case 4: currentWordList = EXPERT_WORDS; currentDifficulty = "Expert"; break;
            default: currentWordList = EASY_WORDS;  currentDifficulty = "Easy";}
        gameMode = mode;
        shuffledWords = new ArrayList<String>(Arrays.asList(currentWordList));
        Collections.shuffle(shuffledWords);
        if (shuffledWords.size() > TARGET_WORDS) {
            shuffledWords = new ArrayList<String>(shuffledWords.subList(0, TARGET_WORDS));}
        currentWordIndex = 0;
        correctWords = 0;
        totalTyped = 0;
        correctChars = 0;
        timeLeft = TEST_DURATION;
        testActive = true;
        paused = false;
        pausedAt = 0;
        totalPausedTime = 0;
        runCompleted = false;
        startTime = 0;
        difficultyLabel.setText(currentDifficulty + "  -  " + TARGET_WORDS + " words");
        statusIconLabel.setText("Type the word!");
        statusIconLabel.setForeground(new Color(100, 200, 255));
        pauseOverlay.setVisible(false);
        pauseBtn.setText("Pause");
        pauseBtn.setBackground(new Color(241, 196, 15));
        updateStats();
        inputField.setText("");
        inputField.setEnabled(true);
        inputField.requestFocus();
        timeProgressBar.setValue(TEST_DURATION);
        timeProgressBar.setString(TEST_DURATION + "s");
        timeProgressBar.setForeground(new Color(100, 255, 100));
        wordProgressBar.setMaximum(TARGET_WORDS);
        wordProgressBar.setValue(0);
        wordProgressBar.setString("0 / " + TARGET_WORDS);
        wordProgressBar.setForeground(new Color(100, 200, 255));
        showNextWord();
        cardLayout.show(mainPanel, "GAME");
        if (countdownTimer != null) countdownTimer.stop();
        countdownTimer = new javax.swing.Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateTimer(); }});
        countdownTimer.start();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { inputField.requestFocusInWindow(); }});}
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
                        StyleConstants.setUnderline(attrs, true); }
                } else if (i == userInput.length()) {
                    StyleConstants.setForeground(attrs, COLOR_CURRENT);
                    StyleConstants.setBackground(attrs, new Color(100, 150, 255, 80));
                    StyleConstants.setUnderline(attrs, true);
                } else {
                    StyleConstants.setForeground(attrs, COLOR_PENDING);}
                doc.insertString(doc.getLength(), String.valueOf(targetWord.charAt(i)), attrs);}
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
        } catch (BadLocationException e) {
            e.printStackTrace(); }}
    private String getCurrentWord() {
        if (shuffledWords.isEmpty()) return "";
        int idx = currentWordIndex % shuffledWords.size();
        return shuffledWords.get(idx);}
    private void showNextWord() {
        renderWord(getCurrentWord(), "");}
    private void updateHighlighting() {
        String currentWord = getCurrentWord();
        if (currentWord.isEmpty()) return;
        String userInput = inputField.getText();
        if (userInput.length() > currentWord.length()) {
            userInput = userInput.substring(0, currentWord.length());}
        renderWord(currentWord, userInput);
        updateLiveStats(userInput, currentWord);}
    private void updateLiveStats(String userInput, String currentWord) {
        int currentCorrect = 0;
        int minLen = Math.min(userInput.length(), currentWord.length());
        for (int i = 0; i < minLen; i++) {
            if (userInput.charAt(i) == currentWord.charAt(i)) currentCorrect++;}
        int liveTotalChars = totalTyped + userInput.length();
        int liveCorrectChars = correctChars + currentCorrect;
        int accuracy = liveTotalChars > 0 ? (liveCorrectChars * 100) / liveTotalChars : 0;
        long elapsed = getEffectiveElapsedMs();
        int wpm = 0;
        if (elapsed > 500) {
            double minutes = elapsed / 60000.0;
            if (minutes > 0) {
                wpm = (int) Math.round((liveCorrectChars / 5.0) / minutes); }}
        updateStatLabel(accuracyLabel, "Accuracy", accuracy + "%", new Color(100, 255, 100));
        updateStatLabel(wpmLabel, "WPM", String.valueOf(wpm), new Color(100, 200, 255));}
    private void updateTimer() {
        if (!testActive) return;
        if (startTime == 0) {
            startTime = System.currentTimeMillis();}
        if (paused) return;
        long elapsed = getEffectiveElapsedMs();
        int secondsElapsed = (int)(elapsed / 1000);
        timeLeft = TEST_DURATION - secondsElapsed;
        if (timeLeft <= 0) {
            timeLeft = 0;
            endGame();
            return;}
        Color timerColor = timeLeft < 10 ? new Color(255, 50, 50) : new Color(255, 100, 100);
        updateStatLabel(timerLabel, "Time", timeLeft + "s", timerColor);
        timeProgressBar.setValue(timeLeft);
        timeProgressBar.setString(timeLeft + "s");
        if (timeLeft < 10)      timeProgressBar.setForeground(new Color(255, 50, 50));
        else if (timeLeft < 20) timeProgressBar.setForeground(new Color(255, 200, 50));
        updateStats(); }
    private void updateStats() {
        long elapsed = getEffectiveElapsedMs();
        int wpm = 0;
        if (elapsed > 500) {
            double minutes = elapsed / 60000.0;
            if (minutes > 0) {
                wpm = (int) Math.round((correctChars / 5.0) / minutes);}}
        int accuracy = totalTyped > 0 ? (correctChars * 100) / totalTyped : 0;
        updateStatLabel(wpmLabel, "WPM", String.valueOf(wpm), new Color(100, 200, 255));
        updateStatLabel(accuracyLabel, "Accuracy", accuracy + "%", new Color(100, 255, 100));
        updateStatLabel(correctWordsLabel, "Correct", String.valueOf(correctWords), new Color(100, 255, 150));
        int wrongWords = currentWordIndex - correctWords;
        updateStatLabel(wrongWordsLabel, "Wrong", String.valueOf(wrongWords), new Color(255, 100, 100));
        int shownProgress = Math.min(TARGET_WORDS, currentWordIndex);
        wordProgressBar.setValue(shownProgress);
        wordProgressBar.setString(currentWordIndex + " / " + TARGET_WORDS);}
    private void processInput() {
        if (!testActive || paused) return;
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;
        String currentWord = getCurrentWord();
        if (currentWord.isEmpty()) return;
        totalTyped += userInput.length();
        int minLength = Math.min(userInput.length(), currentWord.length());
        for (int i = 0; i < minLength; i++) {
            if (userInput.charAt(i) == currentWord.charAt(i)) {
                correctChars++; }}
        if (userInput.equals(currentWord)) {
            correctWords++;
            wordDisplayPane.setBackground(new Color(46, 204, 113, 80));
            statusIconLabel.setText("Correct!");
            statusIconLabel.setForeground(COLOR_CORRECT);
        } else {
            wordDisplayPane.setBackground(new Color(231, 76, 60, 80));
            statusIconLabel.setText("Wrong! Expected: " + currentWord);
            statusIconLabel.setForeground(COLOR_INCORRECT); }
        updateStats();
        currentWordIndex++;
        if (currentWordIndex == TARGET_WORDS) {
            runCompleted = true; }
        inputField.setText("");
        javax.swing.Timer feedbackTimer = new javax.swing.Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                wordDisplayPane.setBackground(new Color(25, 25, 50));
                if (testActive && !paused) {
                    statusIconLabel.setText("Type the word!");
                    statusIconLabel.setForeground(new Color(100, 200, 255));
                    showNextWord();
                    inputField.requestFocusInWindow(); }}});
        feedbackTimer.setRepeats(false);
        feedbackTimer.start();}
    private void endGame() {
        testActive = false;
        endTime = System.currentTimeMillis();
        inputField.setEnabled(false);
        pauseOverlay.setVisible(false);
        if (countdownTimer != null) countdownTimer.stop();
        int wpm = calculateFinalWPM();
        int accuracy = calculateFinalAccuracy();
        int timeUsed = getEffectiveElapsedSeconds();
        String rating = getRatingLabel(wpm, accuracy);
        int xpEarned   = calculateXPReward(wpm, accuracy);
        int coinsEarned = calculateCoinReward(wpm, accuracy);
        totalGames++;
        totalXP += xpEarned;
        totalCoins += coinsEarned;
        GameRecord record = new GameRecord();
        record.difficulty = currentDifficulty;
        record.score = calculateFinalScore();
        record.wpm = wpm;
        record.accuracy = accuracy;
        record.date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        gameHistory.add(record);
        if (gameHistory.size() > 0) {
            int sum = 0;
            for (GameRecord r : gameHistory) sum += r.wpm;
            avgWPM = sum / gameHistory.size();}
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!today.equals(lastPlayedDate)) {
            currentStreak++;
            lastPlayedDate = today;}
        if (wpm > bestWPM) bestWPM = wpm;
        if (runCompleted) {
            if ("Easy".equals(currentDifficulty))   easyDone = true;
            if ("Medium".equals(currentDifficulty)) mediumDone = true;
            if ("Hard".equals(currentDifficulty))   hardDone = true;
            if ("Expert".equals(currentDifficulty)) expertDone = true;}
        resultDifficulty.setText(currentDifficulty);
        resultTotalWords.setText(currentWordIndex + " / " + TARGET_WORDS);
        resultCorrectWords.setText(String.valueOf(correctWords));
        resultAccuracy.setText(accuracy + "%");
        resultWPM.setText(String.valueOf(wpm));
        resultTimeUsed.setText(timeUsed + "s");
        resultRating.setText(rating);
        resultRating.setForeground(getRatingColor(wpm));
        resultConsistency.setText(calculateConsistency());
        // NEW: single-line reward text
        resultXPGain.setText("+" + xpEarned + " XP   +" + coinsEarned + " coins");
        resultMessage.setText(getMotivationalMessage(wpm, accuracy, rating));
        updateMenuHUD();
        refreshStatsBoxes();
        cardLayout.show(mainPanel, "RESULT");
        checkAchievements(wpm, accuracy);
        savePlayerStats();}
    private static class ScoreEntry {
        String name;
        String difficulty;
        int score;
        int wpm;
        int accuracy;
        int timeUsed;
        String date;
        String toCsvLine() {
            return name + "," + difficulty + "," + score + "," + wpm + "," + accuracy + "," + timeUsed + "," + date; } }
    private static class GameRecord {
        String difficulty;
        int score;
        int wpm;
        int accuracy;
        String date;}
    private static class Achievement {
        String id;
        String name;
        String description;
        int rewardXP;
        Achievement(String id, String name, String description, int rewardXP) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.rewardXP = rewardXP; }
    }
}