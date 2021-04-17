package zad1.dict.application.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class SwingGui extends JFrame implements Gui {
    JTextArea ta = new JTextArea(20, 40);
    Container cp = getContentPane();

    public void initialize() {
        // wszystko poszło dobrze - tworzymy i pokazujemy okno wyszukiwania

        Font f = new Font("Dialog", Font.BOLD, 14);
        ta.setFont(f);
        cp.add(new JScrollPane(ta));
        final JTextField tf = new JTextField();
        tf.setFont(f);
        tf.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
        cp.add(tf, "South");

        ta.setText("Prototyp GUI na podstawie wykładu\n"
                + "Tłumaczone słowa: dom, szkoła, nauczyciel, droga, krzesło.\n"
                + "Tymczasowo ustawiono na sztywno język angielski."
        );
        tf.addActionListener(e -> {
            String word = tf.getText();
            String translation = getTranslation(word, "EN");
            ta.setText(translation);
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setWindowTitle();

        // Ustalenie fokusu na polu wprowadzania szukanych słów
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tf.requestFocus();
            }
        });
    }
}
