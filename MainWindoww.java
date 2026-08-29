package mainwindoww;

import javax.swing.*;
import java.awt.*;

public class MainWindoww extends JFrame {

    private JTextArea typingTextArea;
    private JTextArea inputTextArea;
    private JComboBox<String> difficultyBox;
    private JButton startButton;
    private JButton resetButton;
    private JLabel resultLabel;

    public MainWindoww() {

        // Window settings
        setTitle("Typing Speed Test");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Title
        JLabel titleLabel = new JLabel(
                "TYPING SPEED TEST",
                JLabel.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        // Difficulty
        JLabel difficultyLabel = new JLabel("Difficulty:");

        String[] levels = {
            "Easy",
            "Medium",
            "Hard"
        };

        difficultyBox = new JComboBox<>(levels);

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(difficultyBox);

        // Typing text label
        JLabel textLabel = new JLabel(
                "Type the following text:"
        );

        // Typing text area
        typingTextArea = new JTextArea();

        typingTextArea.setEditable(false);
        typingTextArea.setLineWrap(true);
        typingTextArea.setWrapStyleWord(true);
        typingTextArea.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        JScrollPane typingScrollPane =
                new JScrollPane(typingTextArea);

        // User input label
        JLabel inputLabel = new JLabel(
                "Your typing:"
        );

        // User input area
        inputTextArea = new JTextArea();

        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        JScrollPane inputScrollPane =
                new JScrollPane(inputTextArea);

        // Text panel
        JPanel textPanel = new JPanel(
                new GridLayout(4, 1, 5, 5)
        );

        textPanel.add(textLabel);
        textPanel.add(typingScrollPane);
        textPanel.add(inputLabel);
        textPanel.add(inputScrollPane);

        // Buttons
        startButton = new JButton("START");
        resetButton = new JButton("RESET");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);

        // Result message
        resultLabel = new JLabel(
                "Select difficulty and press START",
                JLabel.CENTER
        );

        // START button
        startButton.addActionListener(e -> {

            String difficulty =
                    (String) difficultyBox.getSelectedItem();

            String text;

            if (difficulty.equals("Easy")) {

                text = "The sun is bright and the sky is blue.";

            } else if (difficulty.equals("Medium")) {

                text = "The quick brown fox jumps over the lazy dog.";

            } else {

                text = "Programming requires practice, "
                        + "patience, and problem solving skills.";
            }

            typingTextArea.setText(text);
            inputTextArea.setText("");

            resultLabel.setText(
                    "Start typing the text above."
            );

            inputTextArea.requestFocus();
        });

        // RESET button
        resetButton.addActionListener(e -> {

            typingTextArea.setText("");
            inputTextArea.setText("");

            difficultyBox.setSelectedIndex(0);

            resultLabel.setText(
                    "Select difficulty and press START"
            );
        });

        // Top area
        JPanel topArea = new JPanel(
                new BorderLayout()
        );

        topArea.add(
                titleLabel,
                BorderLayout.NORTH
        );

        topArea.add(
                difficultyPanel,
                BorderLayout.SOUTH
        );

        // Bottom area
        JPanel bottomArea = new JPanel(
                new BorderLayout()
        );

        bottomArea.add(
                resultLabel,
                BorderLayout.NORTH
        );

        bottomArea.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // Add everything to main panel
        mainPanel.add(
                topArea,
                BorderLayout.NORTH
        );

        mainPanel.add(
                textPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomArea,
                BorderLayout.SOUTH
        );

        // Add main panel to window
        add(mainPanel);
    }

    // IMPORTANT: This launches the GUI
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainWindoww window = new MainWindoww();

            window.setVisible(true);
        });
    }
}