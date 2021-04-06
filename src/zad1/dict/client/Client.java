package zad1.dict.client;

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

public class Client extends JFrame {
    public final static int mainServerPort = 2628;
    private String server;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private PrintWriter responseOut;
    private BufferedReader responseIn;
    private String database = "*";  // info ze wszystkich baz

    JTextArea ta = new JTextArea(20, 40);
    Container cp = getContentPane();


    public Client(String server, int timeout) {

        try {
            clientSocket = new Socket(server, mainServerPort);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8),
                    true);

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 1500));

            String resp = in.readLine(); // połączenie nawiązane - info o tym
            System.out.println(resp);
            if (!resp.startsWith("220")) {
                cleanExit(1); // jeżeli dostęp niemożliwy
            }

            clientSocket.setSoTimeout(timeout);

        } catch (UnknownHostException exc) {
            System.err.println("Unknown host " + server);
            System.exit(2);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(3);
        }

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

            // Zlecenie dla serwera
            out.println("DEFINE " + database + " " + word);

            // Czytamy odpowiedź
            // Kod 250 na początku wiersza oznacza koniec definicji
            while (resp != null && !resp.startsWith("250")) {
                resp = in.readLine();
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
            out.close();
            in.close();
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

    private boolean makeRequest(String req) {
        try {
            System.out.println("Request: " + req);
            out.println(req);
            Socket responseConnection = serverSocket.accept();
            responseIn = new BufferedReader(
                    new InputStreamReader(responseConnection.getInputStream(), StandardCharsets.UTF_8));

            String resp = responseIn.readLine();
            System.out.println(resp);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}