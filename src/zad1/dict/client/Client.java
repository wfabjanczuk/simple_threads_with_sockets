package zad1.dict.client;

import zad1.dict.LoggableSocketThread;
import zad1.dict.client.parser.TranslatorServerResponse;
import zad1.dict.client.parser.TranslatorServerResponseParser;
import zad1.dict.server.proxy.Proxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client extends JFrame implements LoggableSocketThread {
    private int localPort;

    private Socket clientSocket;
    private PrintWriter clientSocketWriter;
    private BufferedReader clientSocketReader;

    private ServerSocket serverSocket;

    JTextArea ta = new JTextArea(20, 40);
    Container cp = getContentPane();


    public String getDefaultConnectionLabel() {
        return "Proxy connection";
    }

    public Client(String server, int timeout, int localPort) {
        initClientBackend(server, timeout, localPort);
        initClientGui();
    }

    private void initClientBackend(String clientHost, int timeout, int localPort) {
        this.localPort = localPort;

        try {
            clientSocket = new Socket(clientHost, Proxy.port);
            clientSocketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            clientSocketWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", localPort));
            serverSocket.setSoTimeout(1000);

            String resp = clientSocketReader.readLine(); // połączenie nawiązane - info o tym
            System.out.println(resp);
            if (!resp.startsWith("220")) {
                cleanExit(1); // jeżeli dostęp niemożliwy
            }
            logThreadConnectionEstablished();

            clientSocket.setSoTimeout(timeout);

        } catch (UnknownHostException exc) {
            System.err.println("Unknown host " + clientHost);
            System.exit(2);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(3);
        }
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
                cleanExit(0);
            }
        });

        setTitle(getThreadLabel() + " on port " + localPort);
        pack();
        if (localPort > 1500) {
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

    // Wyszukiwanie

    public void doSearch(String word) {
        try {
            String resp = "",
                    defin = "Uzyskano następujące definicje:\\n";

            // Czytamy odpowiedź
            // Kod 250 na początku wiersza oznacza koniec definicji
            while (resp != null && !resp.startsWith("250")) {
                resp = clientSocketReader.readLine();
                defin += resp + "\\n";
                if (resp.startsWith("552")) break;  // słowo nie znalezione
            }
            ta.setText(defin);
        } catch (SocketTimeoutException exc) {
            ta.setText("Za długie oczekiwanie na odpowiedź");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void cleanExit(int code) {
        try {
            clientSocketWriter.close();
            clientSocketReader.close();
            clientSocket.close();
        } catch (Exception exc) {
        }
        System.exit(code);
    }

    private void translate(JTextField tf) {
        try {
            String request = prepareTranslationRequest(tf);
            logThreadSent(request);
            clientSocketWriter.println(request);

            Socket responseConnection = serverSocket.accept();
            responseConnection.setSoTimeout(1000);
            BufferedReader serverSocketReader = new BufferedReader(new InputStreamReader(responseConnection.getInputStream(), StandardCharsets.UTF_8));

            // TODO: Close responseConnection and dispose resources

            String response = serverSocketReader.readLine();
            logThreadReceived(response);

            TranslatorServerResponse translatorServerResponse = TranslatorServerResponseParser.parse(response);
            if (translatorServerResponse.isValid()) {
                ta.setText(translatorServerResponse.getTranslation());
            } else {
                ta.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String prepareTranslationRequest(JTextField tf) {
        String word = tf.getText();
        return "{\"" + word + "\",\"EN\"," + localPort + "}";
    }

    public static void main(String[] args) {
        // TODO: tworzenie pojedynczego klienta z np. portem

        int timeout = 0;
        String server = "localhost";
        for (int i = 0; i < 2; i++) {
            new Client(server, timeout, 1500 + i);
        }
    }
}