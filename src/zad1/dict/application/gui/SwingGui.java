package zad1.dict.application.gui;

import zad1.dict.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SwingGui extends JFrame implements Gui {
    JTextArea ta = new JTextArea(20, 40);
    Container cp = getContentPane();

    private final Client client;

    public SwingGui(Client client) {
        this.client = client;
    }

    public void setWindowTitle() {
        setTitle(client.getThreadLabel() + " on port " + client.getLocalPort());
        pack();
        if (client.getLocalPort() > 1500) {
            // TODO: replace it, now it is for testing only
            setLocationRelativeTo(null);
        }
        setVisible(true);
    }

    public String getTranslation(String word, String targetLanguage) {
        try {
            return client.getTranslation(word, targetLanguage);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.closeResources();
    }

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
