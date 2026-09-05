package typingspeedtest;
import java.util.*;
import java.io.*;
public class TypingSpeedTest {
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
    private static final int TEST_DURATION = 30;
    private static int currentWordIndex = 0;
    private static int correctWords = 0;
    private static int totalTyped = 0;
    private static int correctChars = 0;
    private static boolean testActive = true;
    private static long startTime;
    private static String[] currentWordList;
    private static List<String> shuffledWords;
    private static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║               TYPING SPEED TEST                         ║");
        System.out.println("║            Software Development I - CSE 2216            ║");
        System.out.println("║              Week 5: Timer & Feedback System            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        int difficulty = selectDifficulty();
        switch(difficulty) {
            case 1: currentWordList = EASY_WORDS; break;
            case 2: currentWordList = MEDIUM_WORDS; break;
            case 3: currentWordList = HARD_WORDS; break;
            case 4: currentWordList = EXPERT_WORDS; break;
            default: currentWordList = EASY_WORDS;}
        shuffledWords = new ArrayList<>(Arrays.asList(currentWordList));
        Collections.shuffle(shuffledWords);
        startTest();
        scanner.close();}
    private static int selectDifficulty() {
        System.out.println("Select Difficulty Level:");
        System.out.println("┌────────────────────────────────────────────────────────────┐");
        System.out.println("│ 1. Easy   (3-5 characters)                                 │");
        System.out.println("│ 2. Medium (6-8 characters)                                 │");
        System.out.println("│ 3. Hard   (9-12 characters)                                │");
        System.out.println("│ 4. Expert (13+ characters)                                 │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("\nEnter your choice (1-4): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        String[] levelNames = {"", "Easy", "Medium", "Hard", "Expert"};
        System.out.println("\n✅ Selected: " + levelNames[choice] + " mode");
        return choice;}
    private static void startTest() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    TEST STARTING                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\n📌 You have " + TEST_DURATION + " seconds to type as many words as possible.");
        System.out.println("📌 Type the word shown and press ENTER to submit.");
        System.out.println("📌 Press ENTER when ready to begin...");
        scanner.nextLine();
        for (int i = 0; i < 30; i++) {
            System.out.println();}
        startTime = System.currentTimeMillis();
        testActive = true;
        currentWordIndex = 0;
        correctWords = 0;
        totalTyped = 0;
        correctChars = 0;
        Thread timerThread = new Thread(() -> runTimer());
        timerThread.setDaemon(true);
        timerThread.start();
        while(testActive && currentWordIndex < shuffledWords.size()) {
            displayStats();
            String currentWord = shuffledWords.get(currentWordIndex);
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║  📝 Type this word: " + currentWord + "  ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.print("\n✏️  Your input: ");
            String userInput = scanner.nextLine().trim();
            if (!testActive) break;
            processInput(userInput, currentWord);
            currentWordIndex++;}
        showResults();}
    private static void runTimer() {
        try {
        long endTime = startTime + (TEST_DURATION * 1000); 
        while(System.currentTimeMillis() < endTime && testActive) {
        Thread.sleep(100); }
            
            if (testActive) {
                testActive = false;
                System.out.println("\n\n⏰ ════════════════════════════════════════════════════════");
                System.out.println("⏰                    TIME'S UP!");
                System.out.println("⏰ ════════════════════════════════════════════════════════");}
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();}}
    private static void displayStats() {
        long elapsed = System.currentTimeMillis() - startTime;
        int secondsElapsed = (int)(elapsed / 1000);
        int timeLeft = Math.max(0, TEST_DURATION - secondsElapsed);
        int wpm = calculateWPM();
        int accuracy = calculateAccuracy();
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.printf("│ ⏱  Time Left: %2d seconds  📝 WPM: %3d  🎯 Accuracy: %3d%% │%n", 
                          timeLeft, wpm, accuracy);
        System.out.printf("│ ✅ Correct Words: %3d  📊 Total Words: %3d                │%n",
                          correctWords, currentWordIndex);
        System.out.println("└────────────────────────────────────────────────────────────┘");}
    private static int calculateWPM() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 1000) return 0;
        double minutes = elapsed / 60000.0;
        if (minutes == 0) return 0;
        return (int)((correctWords) / minutes);}
    private static int calculateAccuracy() {
        if (totalTyped == 0) return 0;
        return (correctChars * 100) / totalTyped;}
    private static void processInput(String userInput, String currentWord) {
        totalTyped += userInput.length();
        int minLength = Math.min(userInput.length(), currentWord.length());
        for (int i = 0; i < minLength; i++) {
            if (userInput.charAt(i) == currentWord.charAt(i)) {
                correctChars++; } }
        if (userInput.equals(currentWord)) {
            correctWords++;
            System.out.println("✅ CORRECT! 🎉");
        } else {
            System.out.println("❌ INCORRECT. Expected: '" + currentWord + "'"); }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); }}
    private static void showResults() {
        long elapsed = System.currentTimeMillis() - startTime;
        int secondsElapsed = (int)(elapsed / 1000);
        System.out.println("\n\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    GAME OVER!                           ║");
        System.out.println("║              FINAL RESULTS                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.printf("│ ⏱  Time Taken:      %d seconds                              │%n", secondsElapsed);
        System.out.printf("│ 📝 Words Typed:      %d                                    │%n", currentWordIndex);
        System.out.printf("│ ✅ Correct Words:    %d                                    │%n", correctWords);
        System.out.printf("│ 🎯 Accuracy:         %d%%                                  │%n", calculateAccuracy());
        System.out.printf("│ 🚀 Net WPM:          %d                                    │%n", calculateWPM());
        int grossWPM = (currentWordIndex * 60) / Math.max(1, secondsElapsed);
        System.out.printf("│ 💨 Gross WPM:        %d                                    │%n", grossWPM);
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("\n⭐ Performance Rating: ");
        int wpm = calculateWPM();
        if (wpm >= 60) {
            System.out.println("⭐⭐⭐ EXCELLENT! Professional typist level! 🏆");
        } else if (wpm >= 40) {
            System.out.println("⭐⭐ GOOD! Above average! 👍");
        } else if (wpm >= 20) {
            System.out.println("⭐ AVERAGE! Keep practicing! 💪");
        } else {
            System.out.println("BEGINNER! Practice more to improve! 📚"); }
        int accuracy = calculateAccuracy();
        if (accuracy < 70 && wpm > 20) {
            System.out.println("💡 Tip: Focus on accuracy first, speed will follow!");
        } else if (accuracy > 90 && wpm < 30) {
            System.out.println("💡 Tip: Try to type a bit faster, your accuracy is good!"); }
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("📋 Week 5 Deliverables Completed:");
        System.out.println("   ✓ Working countdown timer");
        System.out.println("   ✓ Live WPM calculation");
        System.out.println("   ✓ Live accuracy calculation");
        System.out.println("   ✓ Auto-stop when timer reaches zero");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("\nPress ENTER to exit...");
        scanner.nextLine(); }
}