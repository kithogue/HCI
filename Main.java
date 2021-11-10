import org.json.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final Color[] COLORS = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.PINK};
    private static int index = 0;
    private static long timer;
    private static long finalTime;

    public static void main(String[] args) throws Exception {

        JSONArray questionsList = new JSONArray(new String(Files.readAllBytes(Paths.get("questions.json"))));
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel contentPane = new JPanel();
        CardLayout cl = new CardLayout(20, 20);
        contentPane.setLayout(cl);
        JButton next = new JButton("Next");
        next.setEnabled(false);

        for (int i = 0; i < questionsList.length(); i++) {
            JSONArray questions = questionsList.getJSONArray(i);
            JPanel questionsPanel = new JPanel();
            questionsPanel.setLayout(new GridLayout(questions.length(), 1));
            ButtonGroup bg = new ButtonGroup();
            for (int j = 0; j < questions.length(); j++) {
                String question = questions.getString(j);
                JRadioButton questionButton = new JRadioButton(question);
                questionButton.setForeground(COLORS[j % COLORS.length]);
                questionButton.addActionListener(e -> next.setEnabled(true));
                questionsPanel.add(questionButton);
                bg.add(questionButton);
            }
            contentPane.add(questionsPanel, String.valueOf(i));
        }
        frame.add(contentPane, BorderLayout.CENTER);
        JLabel timeLabel = new JLabel();
        next.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == questionsList.length() - 1) {
                    finalTime = (System.currentTimeMillis() - timer) ;
                    String timeStr = "Previous question time is " + finalTime + " ms and we're done.";
                    System.out.println(timeStr);
                    timeLabel.setText(timeStr);
                    next.setVisible(false);
                    return;
                }
                long time = (System.currentTimeMillis() - timer);
                timer = System.currentTimeMillis();
                String timeStr = "Previous question time is " + time + " ms";
                System.out.println(timeStr);
                timeLabel.setText(timeStr);
                cl.show(contentPane, String.valueOf(++index));
                frame.add(timeLabel, BorderLayout.NORTH);
                next.setEnabled(false);
            }
        });
        frame.add(next, BorderLayout.SOUTH);
        frame.add(timeLabel, BorderLayout.NORTH);

        frame.setVisible(true);
        timer = System.currentTimeMillis();
    }

}
