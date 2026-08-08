public class AccuracyCalculator {
    private int correctChars;
    private int totalChars;
    private double timeInSeconds;
    public AccuracyCalculator(int correctChars, int totalChars, double timeInSeconds) {
    this.correctChars = correctChars;
    this.totalChars = totalChars;
    this.timeInSeconds = timeInSeconds;
    }
    public double calculateWPM() {
    double timeInMinutes = timeInSeconds / 60.0;
    double wordsTyped = totalChars / 5.0;
    return wordsTyped / timeInMinutes;
    }
    public double calculateAccuracyPercent() {
    if (totalChars == 0) return 0;
    return (correctChars / (double) totalChars) * 100;
    }
    public void printResults() {
    System.out.println("=== Typing Test Results ===");
    System.out.printf("WPM: %.2f\n", calculateWPM());
    System.out.printf("Accuracy: %.2f%%\n", calculateAccuracyPercent());
    }
    public static void main(String[] args) {
    System.out.println("=== AccuracyCalculator Test ===\n");
    System.out.println("Test 1: Perfect Typing");
    AccuracyCalculator test1 = new AccuracyCalculator(50, 50, 30);
    System.out.println("Correct chars: 50, Total chars: 50, Time: 30 seconds");
    test1.printResults();
    System.out.println();
    System.out.println("Test 2: Good Typing (90% Accuracy)");
    AccuracyCalculator test2 = new AccuracyCalculator(45, 50, 30);
    System.out.println("Correct chars: 45, Total chars: 50, Time: 30 seconds");
    test2.printResults();
    System.out.println();
    System.out.println("Test 3: Fast Typing");
    AccuracyCalculator test3 = new AccuracyCalculator(80, 85, 15);
    System.out.println("Correct chars: 80, Total chars: 85, Time: 15 seconds");
    test3.printResults();
    System.out.println();
    System.out.println("Test 4: Slow Typing");
    AccuracyCalculator test4 = new AccuracyCalculator(30, 35, 60);
    System.out.println("Correct chars: 30, Total chars: 35, Time: 60 seconds");
    test4.printResults();
    }
}