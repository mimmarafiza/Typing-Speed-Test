package scoringcalculator;

import java.text.DecimalFormat;
public class ScoringCalculator {
    
    public enum DifficultyLevel {
        EASY(1.0), MEDIUM(1.5), HARD(2.0), EXPERT(2.5);
        private final double multiplier;
        DifficultyLevel(double multiplier) {
        this.multiplier = multiplier;}
        public double getMultiplier() {
        return multiplier;} }
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
        calculateFinalScore();  }
    public void recordWord(String word, boolean isCorrect) {
        wordsTyped++;
        if (isCorrect) {
        correctWords++; }
        currentDifficulty = getDifficultyFromWordLength(word.length());
        calculateAccuracy();
        calculateBaseScore();
        calculateBonusPoints();
        calculateFinalScore(); }
    private DifficultyLevel getDifficultyFromWordLength(int length) {
        if (length <= 5) return DifficultyLevel.EASY;
        else if (length <= 8) return DifficultyLevel.MEDIUM;
        else if (length <= 12) return DifficultyLevel.HARD;
        else return DifficultyLevel.EXPERT; }
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
    public String getFormattedWPM() { return df.format(getWPM()) + " WPM"; }
    public static void main(String[] args) {
        ScoringCalculator sc = new ScoringCalculator();
        sc.startSession();
        String[] words = {"java", "programming", "test", "speed", "expert"};
        String[] typed = {"java", "programming", "tst", "speed", "expert"};
        for (int i = 0; i < words.length; i++) {
            boolean correct = words[i].equals(typed[i]);
            sc.recordWord(words[i], correct);
            char[] expected = words[i].toCharArray();
            char[] actual = typed[i].toCharArray();
            int max = Math.max(expected.length, actual.length);
            for (int j = 0; j < max; j++) {
            char e = (j < expected.length) ? expected[j] : ' ';
            char a = (j < actual.length) ? actual[j] : ' ';
            sc.recordKeystroke(e == a); } }
        sc.endSession();
        System.out.println("=== SCORING RESULTS ===");
        System.out.println("Difficulty: " + sc.getCurrentDifficulty());
        System.out.println("Multiplier: x" + sc.getCurrentDifficulty().getMultiplier());
        System.out.println("Correct Keystrokes: " + sc.getCorrectKeystrokes());
        System.out.println("Incorrect Keystrokes: " + sc.getIncorrectKeystrokes());
        System.out.println("Words Typed: " + sc.getWordsTyped());
        System.out.println("Correct Words: " + sc.getCorrectWords());
        System.out.println("Accuracy: " + sc.getFormattedAccuracy());
        System.out.println("Base Score: " + sc.getBaseScore());
        System.out.println("Bonus: " + sc.getBonusPoints());
        System.out.println("FINAL SCORE: " + sc.getFinalScore());
        System.out.println("Time: " + sc.getElapsedTime() + "s");
        System.out.println("Speed: " + sc.getFormattedWPM());
    }
}