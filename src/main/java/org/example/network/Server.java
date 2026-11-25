package org.example.network;

import org.example.model.GameResult;
import org.example.storage.ResultStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import  java.util.concurrent.atomic.AtomicBoolean;

public final class Server implements AutoCloseable {
    private final int requestedPort;
    private final ResultStore resultStore;
    private final AtomicBoolean running = new AtomicBoolean();
    private ServerSocket serverSocket;
    private Thread acceptThread;

    public Server(int port, ResultStore resultStore) {
        this.requestedPort = port;
        this.resultStore = resultStore;
    }

    public synchronized void start() throws IOException {
        if (running.get()) {
            return;
        }
        serverSocket = new ServerSocket(requestedPort, 50, InetAddress.getLoopbackAddress());
        running.set(true);
        acceptThread = new Thread(this::acceptClients, "battle-arena-server");
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    public int getPort() {
        if (serverSocket == null) {
            throw new IllegalStateException("Servern har inte startats.");
        }
        return serverSocket.getLocalPort();
    }

    private void acceptClients() {
        while (running.get()) {
            try {
                Socket socket = serverSocket.accept();
                Thread clientThread = new Thread(() -> handleClient(socket), "battle-arena-client");
                clientThread.setDaemon(true);
                clientThread.start();
            } catch (IOException exception) {
                if (running.get()) {
                    System.err.println("Servern kunde inte ta emot en anslutning: " + exception.getMessage());
                }
                return;
            }
        }
    }

    private void handleClient(Socket socket) {
        try (socket;
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
            socket.setSoTimeout(5000);
            String request = input.readLine();
            if (request == null) {
                output.println("ERROR\tIngen begäran mottagen.");
                return;
            }
            processRequest(request, output);
        } catch (IOException exception) {
            System.err.println("Fel vid hantering av klient: " + exception.getMessage());
        }
    }

    private void processRequest(String request, PrintWriter output) {
        try {
            if ("HISTORY".equals(request)) {
                List<GameResult> results = resultStore.readAll();
                output.println("HISTORY");
                for (GameResult result : results) {
                    output.println(ResultStore.encode(result));
                }
                return;
            }
            if (request.startsWith("SAVE\t")) {
                GameResult result = ResultStore.decode(request.substring("SAVE\t".length()));
                resultStore.save(result);
                output.println("OK");
                return;
            }

            output.println("ERROR\tOgiltig begäran: ");
        } catch (IllegalArgumentException | IOException exception) {
            String message = exception.getMessage() == null ? "Okänt fel." : exception.getMessage();
            output.println("ERROR\t" + message.replace('\t', ' '));
        }
    }

    @Override
    public synchronized void close() throws IOException {
        running.set(false);
        if (serverSocket != null) {
            serverSocket.close();
        }
        if (acceptThread != null) {
            try {
                acceptThread.join(1000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
