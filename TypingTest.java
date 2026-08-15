package typingtest;

import java.text.DecimalFormat;

enum DifficultyLevel {
    EASY(1.0, 3, 5, "Easy"),
    MEDIUM(1.5, 6, 8, "Medium"),
    HARD(2.0, 9, 12, "Hard"),
    EXPERT(2.5, 13, 20, "Expert");
    private final double multiplier;
    private final int minWordLength;
    private final int maxWordLength;
    private final String displayName;
    DifficultyLevel(double multiplier, int minWordLength, int maxWordLength, String displayName) {
        this.multiplier = multiplier;
        this.minWordLength = minWordLength;
        this.maxWordLength = maxWordLength;
        this.displayName = displayName; }
    public double getMultiplier() { return multiplier; }
    public int getMinWordLength() { return minWordLength; }
    public int getMaxWordLength() { return maxWordLength; }
    public String getDisplayName() { return displayName; }
    public static DifficultyLevel fromWordLength(int length) {
        if (length <= 5) return EASY;
        else if (length <= 8) return MEDIUM;
        else if (length <= 12) return HARD;
        else return EXPERT; } }
class ScoringCalculator {
    private int correctKeystrokes = 0;
    private int incorrectKeystrokes = 0;
    private int wordsTyped = 0;
    private int correctWords = 0;
    private int totalChars = 0;
    private double accuracy = 0.0;
    private int baseScore = 0;
    private int bonusPoints = 0;
    private int finalScore = 0;
    private DifficultyLevel currentDifficulty = DifficultyLevel.EASY;
    private long startTime = 0;
    private long endTime = 0;
    private DecimalFormat df = new DecimalFormat("0.00");
    public void startSession() {
        correctKeystrokes = 0;
        incorrectKeystrokes = 0;
        wordsTyped = 0;
        correctWords = 0;
        totalChars = 0;
        accuracy = 0.0;
        baseScore = 0;
        bonusPoints = 0;
        finalScore = 0;
        startTime = System.currentTimeMillis(); }
    public void endSession() {
        endTime = System.currentTimeMillis();
        calculateFinalScore(); }
    public void recordKeystroke(boolean isCorrect) {
        if (isCorrect) {
        correctKeystrokes++;
        } else {
        incorrectKeystrokes++; }
        totalChars++;
        calculateAccuracy();
        calculateBaseScore();
        calculateBonusPoints();
        calculateFinalScore(); }
    public void recordWord(String word, boolean isCorrect) {
        wordsTyped++;
        if (isCorrect) {
        correctWords++; }
        currentDifficulty = DifficultyLevel.fromWordLength(word.length());
        calculateAccuracy();
        calculateBaseScore();
        calculateBonusPoints();
        calculateFinalScore(); }
    private void calculateAccuracy() {
        int total = correctKeystrokes + incorrectKeystrokes;
        accuracy = (total == 0) ? 0.0 : ((double) correctKeystrokes / total) * 100; }
    private void calculateBaseScore() {
        baseScore = (int) (correctKeystrokes * 10 * currentDifficulty.getMultiplier()); }
    private void calculateBonusPoints() {
        if (accuracy >= 95) bonusPoints = 50;
        else if (accuracy >= 90) bonusPoints = 45;
        else if (accuracy >= 80) bonusPoints = 35;
        else if (accuracy >= 70) bonusPoints = 20;
        else if (accuracy >= 60) bonusPoints = 10;
        else if (accuracy >= 50) bonusPoints = 5;
        else bonusPoints = 0; }
    private void calculateFinalScore() {
        finalScore = baseScore + bonusPoints; }
    public double getElapsedTime() {
        long end = (endTime == 0) ? System.currentTimeMillis() : endTime;
        return (end - startTime) / 1000.0; }
    public double getWPM() {
        double minutes = getElapsedTime() / 60.0;
        return (minutes == 0) ? 0 : wordsTyped / minutes; }
    public int getCorrectKeystrokes() { return correctKeystrokes; }
    public int getIncorrectKeystrokes() { return incorrectKeystrokes; }
    public int getTotalKeystrokes() { return correctKeystrokes + incorrectKeystrokes; }
    public int getWordsTyped() { return wordsTyped; }
    public int getCorrectWords() { return correctWords; }
    public double getAccuracy() { return accuracy; }
    public int getBaseScore() { return baseScore; }
    public int getBonusPoints() { return bonusPoints; }
    public int getFinalScore() { return finalScore; }
    public DifficultyLevel getCurrentDifficulty() { return currentDifficulty; }
    public String getFormattedAccuracy() { return df.format(accuracy) + "%"; }
    public String getFormattedWPM() { return df.format(getWPM()) + " WPM"; } }
public class TypingTest {
    public static void main(String[] args) {
    System.out.println("=== TYPING SPEED TEST - SCORING CALCULATOR ===\n");
        ScoringCalculator sc = new ScoringCalculator();
        sc.startSession();
        System.out.println("Test 1: Recording keystrokes");
        sc.recordKeystroke(true);
        sc.recordKeystroke(true);
        sc.recordKeystroke(false);
        System.out.println("Correct: " + sc.getCorrectKeystrokes() + " (Expected: 2)");
        System.out.println("Incorrect: " + sc.getIncorrectKeystrokes() + " (Expected: 1)");
        System.out.println("Pass: " + (sc.getCorrectKeystrokes() == 2 && sc.getIncorrectKeystrokes() == 1) + "\n");
        System.out.println("Test 2: Recording words");
        sc.recordWord("java", true);
        sc.recordWord("programming", true);
        sc.recordWord("test", false);
        System.out.println("Words Typed: " + sc.getWordsTyped() + " (Expected: 3)");
        System.out.println("Correct Words: " + sc.getCorrectWords() + " (Expected: 2)");
        System.out.println("Pass: " + (sc.getWordsTyped() == 3 && sc.getCorrectWords() == 2) + "\n");
        System.out.println("Test 3: Accuracy calculation");
        sc.recordKeystroke(true);
        sc.recordKeystroke(true);
        sc.recordKeystroke(true);
        sc.recordKeystroke(false);
        double acc = sc.getAccuracy();
        System.out.println("Accuracy: " + acc + "% (Expected: 75.0%)");
        System.out.println("Pass: " + (acc == 75.0) + "\n");
        System.out.println("Test 4: Difficulty detection");
        sc.recordWord("cat", true);
        System.out.println("'cat' → " + sc.getCurrentDifficulty().getDisplayName() + " (Expected: Easy)");
        sc.recordWord("elephant", true);
        System.out.println("'elephant' → " + sc.getCurrentDifficulty().getDisplayName() + " (Expected: Medium)");
        sc.recordWord("butterfly", true);
        System.out.println("'butterfly' → " + sc.getCurrentDifficulty().getDisplayName() + " (Expected: Hard)");
        sc.recordWord("internationalization", true);
        System.out.println("'internationalization' → " + sc.getCurrentDifficulty().getDisplayName() + " (Expected: Expert)");
        sc.endSession();
        System.out.println("\n=== FINAL SCORE ===");
        System.out.println("Correct Keystrokes: " + sc.getCorrectKeystrokes());
        System.out.println("Incorrect Keystrokes: " + sc.getIncorrectKeystrokes());
        System.out.println("Words Typed: " + sc.getWordsTyped());
        System.out.println("Correct Words: " + sc.getCorrectWords());
        System.out.println("Difficulty: " + sc.getCurrentDifficulty().getDisplayName());
        System.out.println("Multiplier: x" + sc.getCurrentDifficulty().getMultiplier());
        System.out.println("Accuracy: " + sc.getFormattedAccuracy());
        System.out.println("Base Score: " + sc.getBaseScore());
        System.out.println("Bonus Points: " + sc.getBonusPoints());
        System.out.println("FINAL SCORE: " + sc.getFinalScore());
        System.out.println("Time: " + sc.getElapsedTime() + "s");
        System.out.println("Speed: " + sc.getFormattedWPM());
        System.out.println("\n=== ALL TESTS COMPLETED! ===");
    }
}