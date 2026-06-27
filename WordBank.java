package wordbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class WordBank {
    private List<String> easyWords;
    private List<String> mediumWords;
    private List<String> hardWords;
    private List<String> expertWords;
    private Random random;
    public WordBank() {
      random = new Random();
      initializeWordLists();
    }private void initializeWordLists() {
    easyWords = new ArrayList<>(Arrays.asList(
    "cat", "dog", "run", "sun", "hat", "car", "box", "red", "big", "hot",
    "cup", "map", "pen", "key", "fly", "sky", "sea", "one", "two", "six",
    "blue", "fast", "jump", "play", "read", "book", "tree", "star", "moon", 
    "fish", "ball", "bird", "hand", "food", "game", "help", "kind", "life", 
    "long", "mind"));
    mediumWords = new ArrayList<>(Arrays.asList(
    "animal", "beauty", "castle", "danger", "energy", "forest", "garden", 
    "health", "island", "jungle", "market", "nature", "orange", "planet", 
    "quick", "river", "silver", "travel", "unique", "valley", "window", 
    "yellow", "zebra", "button", "common", "dream", "eagle", "flight", 
    "golden", "humble", "inside", "jacket", "kingdom", "little", "motion", 
    "noble", "oxygen", "pencil", "random", "summer"));
    hardWords = new ArrayList<>(Arrays.asList(
    "adventure", "beautiful", "challenge", "dangerous", "education", 
    "freedom", "generous", "happiness", "imagine", "knowledge", 
    "laughter", "magazine", "national", "ocean", "personal", 
    "question", "remember", "strength", "together", "umbrella", 
    "volunteer", "wonderful", "yesterday", "celebrate", "delicious", 
    "elephant", "familiar", "government", "hospital", "important", 
    "keyboard", "language", "mountain", "nervous", "ordinary", 
    "position", "recorder", "sentence", "telephone", "universe"));
    expertWords = new ArrayList<>(Arrays.asList(
    "accommodation", "basketball", "communication", "development", 
    "environment", "fascinating", "generally", "happiness", 
    "independent", "journalism", "kilometer", "laboratory", 
    "mathematics", "northeastern", "opportunity", "philosophy", 
    "qualification", "recommendation", "satisfaction", "technology", 
    "uncomfortable", "vocabulary", "whether", "xylophone", 
    "yesterday", "alternative", "beneficial", "celebration", 
    "demonstration", "electricity", "forever", "gratitude", 
    "illustration", "management", "notebook", "organization", 
    "particular", "recognition", "sustainable", "transformation"));}
    public String getRandomWord(String level) {
    List<String> selectedList = getWordList(level);
    if (selectedList.isEmpty()) {
    return "No words available";}
    int index = random.nextInt(selectedList.size());
    return selectedList.get(index);}
    public List<String> getWordsForLevel(String level) {
    return new ArrayList<>(getWordList(level));}
    public int getWordCount(String level) {
    return getWordList(level).size();}
    private List<String> getWordList(String level) {
    switch (level.toLowerCase()) {
    case "easy":
    return easyWords;
    case "medium":
    return mediumWords;
    case "hard":
    return hardWords;
    case "expert":
    return expertWords;
    default:
    return new ArrayList<>();}}
    public List<String> getRandomWords(String level, int count) {
    List<String> selectedList = getWordList(level);
    List<String> result = new ArrayList<>();
    if (selectedList.isEmpty()) {
    return result;}
    for (int i = 0; i < count; i++) {
    int index = random.nextInt(selectedList.size());
    result.add(selectedList.get(index));}
    return result;}
    public static void main(String[] args) {
    WordBank wordBank = new WordBank();
    System.out.println("=== Typing Speed Test - Word Bank Test ===\n");
    System.out.println("Word Counts:");
    System.out.println("Easy: " + wordBank.getWordCount("easy") + " words");
    System.out.println("Medium: " + wordBank.getWordCount("medium") + " words");
    System.out.println("Hard: " + wordBank.getWordCount("hard") + " words");
    System.out.println("Expert: " + wordBank.getWordCount("expert") + " words");
    System.out.println("\n=== Random Words from each level ===\n");
    System.out.println("Easy word: " + wordBank.getRandomWord("easy"));
    System.out.println("Medium word: " + wordBank.getRandomWord("medium"));
    System.out.println("Hard word: " + wordBank.getRandomWord("hard"));
    System.out.println("Expert word: " + wordBank.getRandomWord("expert"));   
    System.out.println("\n=== Multiple random words (Easy) ===");
    List<String> words = wordBank.getRandomWords("easy", 5);
    for (String word : words) {
    System.out.println("- " + word);}}
}
