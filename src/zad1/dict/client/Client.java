package zad1.dict.client;

import zad1.dict.LoggableSocketThread;
import zad1.dict.client.parser.TranslatorServerResponse;
import zad1.dict.client.parser.TranslatorServerResponseParser;
import zad1.dict.server.MainServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client extends JFrame implements LoggableSocketThread {
    private Socket clientSocket;
    private PrintWriter clientSocketWriter;
    private BufferedReader clientSocketReader;

    private ServerSocket serverSocket;
    private BufferedReader serverSocketReader;

    JTextArea ta = new JTextArea(20, 40);
    Container cp = getContentPane();

    public String getConnectionLabel() {
        return "Connection with MainServer";
    }

    public Client(String server, int timeout) {
        initClientBackend(server, timeout);
        initClientGui();
    }

    private void initClientBackend(String clientHost, int timeout) {
        try {
            clientSocket = new Socket(clientHost, MainServer.port);
            clientSocketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            clientSocketWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 1500));
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

        tf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeRequest(tf.getText());
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                cleanExit(0);
            }
        });

        pack();
        show();

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

    public static void main(String[] args) {

        int timeout = 0;
        String server = "dict.org";
        try {
            timeout = Integer.parseInt(args[0]);
            server = args[1];
        } catch (NumberFormatException exc) {
            server = args[0];
        } catch (ArrayIndexOutOfBoundsException exc) {
            exc.printStackTrace();
        }

        new Client(server, timeout);
    }

    private void makeRequest(String request) {
        try {
            logThreadCustomText("Sent " + request);
            clientSocketWriter.println(request);

            Socket responseConnection = serverSocket.accept();
            responseConnection.setSoTimeout(1000);
            serverSocketReader = new BufferedReader(new InputStreamReader(responseConnection.getInputStream(), StandardCharsets.UTF_8));

            String response = serverSocketReader.readLine();
            logThreadCustomText("Received " + response);

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
}