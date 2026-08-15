public enum DifficultyLevel {
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
        this.displayName = displayName;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    public int getMinWordLength() {
        return minWordLength;
    }
    
    public int getMaxWordLength() {
        return maxWordLength;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static DifficultyLevel fromWordLength(int length) {
        if (length <= 5) return EASY;
        else if (length <= 8) return MEDIUM;
        else if (length <= 12) return HARD;
        else return EXPERT;
    }
    
    public static void main(String[] args) {
        System.out.println("STARTING DIFFICULTY LEVEL TEST...");
        System.out.println("====================================\n");
        
        System.out.println("1. DISPLAY ALL LEVELS:");
        System.out.println("------------------------------------");
        for (DifficultyLevel level : DifficultyLevel.values()) {
            System.out.println(level.getDisplayName().toUpperCase() + ":");
            System.out.println("   Multiplier: x" + level.getMultiplier());
            System.out.println("   Word Length: " + level.getMinWordLength() + " - " + level.getMaxWordLength() + " chars");
            System.out.println();
        }
        
        System.out.println("2. TEST WORD LENGTH DETECTION:");
        System.out.println("------------------------------------");
        String[] words = {"cat", "elephant", "butterfly", "internationalization", "hello", "java"};
        
        for (String word : words) {
            DifficultyLevel level = DifficultyLevel.fromWordLength(word.length());
            System.out.println("Word: '" + word + "' (length " + word.length() + ") → " + level.getDisplayName() + " (x" + level.getMultiplier() + ")");
        }
        
        System.out.println("\n3. ACCESS INDIVIDUAL LEVELS:");
        System.out.println("------------------------------------");
        System.out.println("EASY.getMultiplier() = " + DifficultyLevel.EASY.getMultiplier());
        System.out.println("MEDIUM.getMultiplier() = " + DifficultyLevel.MEDIUM.getMultiplier());
        System.out.println("HARD.getMultiplier() = " + DifficultyLevel.HARD.getMultiplier());
        System.out.println("EXPERT.getMultiplier() = " + DifficultyLevel.EXPERT.getMultiplier());
        
        System.out.println("\n====================================");
        System.out.println("TEST COMPLETED SUCCESSFULLY!");
    }
}