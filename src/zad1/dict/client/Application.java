package zad1.dict.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Application extends JFrame {
    private final Client client;

    JTextArea ta = new JTextArea(20, 40);
    Container cp = getContentPane();

    public Application(Client client) {
        this.client = client;
        initClientGui();
    }

    private void initClientGui() {
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
        tf.addActionListener(e -> translate(tf));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setTitle(client.getThreadLabel() + " on port " + client.getLocalPort());
        pack();
        if (client.getLocalPort() > 1500) {
            // TODO: replace it, now it is for testing only
            setLocationRelativeTo(null);
        }
        setVisible(true);

        // Ustalenie fokusu na polu wprowadzania szukanych słów
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tf.requestFocus();
            }
        });
    }

    private void translate(JTextField tf) {
        try {
            String word = tf.getText();
            String translation = client.getTranslation(word, "EN");
            ta.setText(translation);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.closeResources();
    }

    public static void startLocally(int numberOfClients) {
        int firstLocalPort = 1500;

        for (int i = 0; i < numberOfClients; i++) {
            startInstanceLocally(firstLocalPort + i);
        }
    }

    public static void startInstanceLocally(int applicationPort) {
        String applicationHost = "localhost";
        String proxyHost = "localhost";
        int translatorConnectionTimeout = 1000;

        boolean isInitialized = false;
        Client client = null;

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(applicationHost, applicationPort));

            client = new Client(proxyHost, serverSocket, translatorConnectionTimeout);
            isInitialized = client.initializeIfValid();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (isInitialized) {
            new Application(client);
        } else {
            System.out.println("Application cannot start.");
        }
    }

}