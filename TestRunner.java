public class TestRunner {
    static class Timer {
    private long startTime;
    private long elapsedTime = 0;
    private boolean isRunning = false;
    public void start() {
    startTime = System.currentTimeMillis();
    isRunning = true;
    }public void stop() {
    if (isRunning) {
    elapsedTime = System.currentTimeMillis() - startTime;
    isRunning = false;}
    }public long getElapsedTime() {
    return isRunning ? System.currentTimeMillis() - startTime : elapsedTime;}
    public double getElapsedSeconds() {
    return getElapsedTime() / 1000.0;}}
    static class WordMatcher {
    private String correctWord;
    private String userInput;
    public WordMatcher(String correctWord, String userInput) {
    this.correctWord = correctWord;
    this.userInput = userInput;}
    public boolean isExactMatch() {
    return correctWord.equals(userInput);}
    public int getCorrectChars() {
    int correctCount = 0;
    int minLength = Math.min(correctWord.length(), userInput.length());
    for (int i = 0; i < minLength; i++) {
    if (correctWord.charAt(i) == userInput.charAt(i)) {
    correctCount++;}}
    return correctCount; }
    public int getIncorrectChars() {
    return userInput.length() - getCorrectChars();}}
    static class AccuracyCalculator {
    private int correctChars;
    private int totalChars;
    private double timeInSeconds;
    public AccuracyCalculator(int correctChars, int totalChars, double timeInSeconds) {
    this.correctChars = correctChars;
    this.totalChars = totalChars;
    this.timeInSeconds = timeInSeconds;}
    public double calculateWPM() {
    double timeInMinutes = timeInSeconds / 60.0;
    double wordsTyped = totalChars / 5.0;
    return wordsTyped / timeInMinutes; }
    public double calculateAccuracyPercent() {
    if (totalChars == 0) return 0;
    return (correctChars / (double) totalChars) * 100; }
    public void printResults() {
    System.out.println("=== Typing Test Results ===");
    System.out.printf("WPM: %.2f\n", calculateWPM());
    System.out.printf("Accuracy: %.2f%%\n", calculateAccuracyPercent());}}
    public static void main(String[] args) {
    System.out.println("=== Typing Speed Test - Core Logic CLI ===\n");
    System.out.println("--- Timer Test ---");
    Timer timer = new Timer();
    timer.start();
    try { 
    Thread.sleep(2000); 
    } catch (InterruptedException e) {
    e.printStackTrace(); }
    timer.stop();
    System.out.println("Elapsed time: " + timer.getElapsedSeconds() + " seconds\n");
    System.out.println("--- WordMatcher Test ---");
    WordMatcher matcher = new WordMatcher("hello", "hallo");
    System.out.println("Correct word: hello");
    System.out.println("User input: hallo");
    System.out.println("Correct chars: " + matcher.getCorrectChars());
    System.out.println("Incorrect chars: " + matcher.getIncorrectChars());
    System.out.println("Is exact match: " + matcher.isExactMatch() + "\n");
    System.out.println("--- AccuracyCalculator Test ---");
    AccuracyCalculator calc = new AccuracyCalculator(45, 50, 30);
    System.out.println("Correct chars: 45, Total chars: 50, Time: 30 seconds");
    calc.printResults();
    }
}