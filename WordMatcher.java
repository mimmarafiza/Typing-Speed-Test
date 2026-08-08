public class WordMatcher {
    private String correctWord;
    private String userInput;
    public WordMatcher(String correctWord, String userInput) {
    this.correctWord = correctWord;
    this.userInput = userInput;
    }
    public boolean isExactMatch() {
    return correctWord.equals(userInput);
    }
    public int getCorrectChars() {
        int correctCount = 0;
        int minLength = Math.min(correctWord.length(), userInput.length());
        for (int i = 0; i < minLength; i++) {
        if (correctWord.charAt(i) == userInput.charAt(i)) {
        correctCount++;
        }
    }
    return correctCount;
    }
    public int getIncorrectChars() {
    return userInput.length() - getCorrectChars();
    }
    public static void main(String[] args) {
    System.out.println("=== WordMatcher Test ===\n");
        WordMatcher test1 = new WordMatcher("hello", "hello");
        System.out.println("Test 1: Exact Match");
        System.out.println("Correct word: hello");
        System.out.println("User input: hello");
        System.out.println("Is exact match: " + test1.isExactMatch());
        System.out.println("Correct chars: " + test1.getCorrectChars());
        System.out.println("Incorrect chars: " + test1.getIncorrectChars());
        System.out.println();

        WordMatcher test2 = new WordMatcher("hello", "hallo");
        System.out.println("Test 2: Partial Match");
        System.out.println("Correct word: hello");
        System.out.println("User input: hallo");
        System.out.println("Is exact match: " + test2.isExactMatch());
        System.out.println("Correct chars: " + test2.getCorrectChars());
        System.out.println("Incorrect chars: " + test2.getIncorrectChars());
        System.out.println();
        
        WordMatcher test3 = new WordMatcher("computer", "compter");
        System.out.println("Test 3: Multiple Errors");
        System.out.println("Correct word: computer");
        System.out.println("User input: compter");
        System.out.println("Is exact match: " + test3.isExactMatch());
        System.out.println("Correct chars: " + test3.getCorrectChars());
        System.out.println("Incorrect chars: " + test3.getIncorrectChars());
    }
}