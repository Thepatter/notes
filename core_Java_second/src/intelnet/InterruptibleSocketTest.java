package intelnet;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zyw
 */
public class InterruptibleSocketTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new InterruptibleSocketFrame();
            frame.setTitle("InterruptibleSocketTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
    static class InterruptibleSocketFrame extends JFrame
    {
        private Scanner in;
        private JButton interruptibleButton;
        private JButton blockingButton;
        private JButton cancelButton;
        private JTextArea messages;
        private TestServer server;
        private Thread connectThread;
        private static final int PORT = 8189;

        InterruptibleSocketFrame()
        {
            JPanel northPanel = new JPanel();
            add(northPanel, BorderLayout.NORTH);

            final int TEXT_ROWS = 20;
            final int TEXT_COLUMNS = 60;
            messages = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
            add(new JScrollPane(messages));

            interruptibleButton = new JButton("Interruptible");
            blockingButton = new JButton("Blocking");

            northPanel.add(interruptibleButton);
            northPanel.add(blockingButton);

            interruptibleButton.addActionListener(event -> {
                interruptibleButton.setEnabled(false);
                blockingButton.setEnabled(false);
                cancelButton.setEnabled(true);
                connectThread = new Thread(() -> {
                    try {
                        connectInterruptibly();
                    } catch (IOException e) {
                        messages.append("\nInterruptibleSocketTest.connectInterruptibl: " + e);
                    }
                });
                connectThread.start();
            });

            blockingButton.addActionListener(event -> {
                interruptibleButton.setEnabled(false);
                blockingButton.setEnabled(false);
                cancelButton.setEnabled(true);
                connectThread = new Thread(() -> {
                    try {
                        connectBlocking();
                    } catch (IOException e) {
                        messages.append("\nInterruptibleSocketTest.connectBlocking: " + e);
                    }
                });
                connectThread.start();
            });

            cancelButton = new JButton("Cancel");
            cancelButton.setEnabled(false);
            northPanel.add(cancelButton);
            cancelButton.addActionListener(event -> {
                connectThread.interrupt();
                cancelButton.setEnabled(false);
            });
            server = new TestServer();
            new Thread(server).start();
            pack();
        }

        void connectInterruptibly() throws IOException
        {
            messages.append("Interruptible:\n");
            try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", PORT))) {
                in = new Scanner(channel, StandardCharsets.UTF_8);
                while (!Thread.currentThread().isInterrupted()) {
                    messages.append("Reading ");
                    if (in.hasNextLine()) {
                        String line = in.nextLine();
                        messages.append(line);
                        messages.append("\n");
                    }
                }
            } finally {
                EventQueue.invokeLater(() -> {
                    messages.append("Channel closed\n");
                    interruptibleButton.setEnabled(true);
                    blockingButton.setEnabled(true);
                });
            }
        }

        void connectBlocking() throws IOException {
            messages.append("Blocking: \n");
            try (Socket socket = new Socket("localhost", PORT)) {
                in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
                while (!Thread.currentThread().isInterrupted()) {
                    messages.append("Reading ");
                    if (in.hasNextLine()) {
                        String line = in.nextLine();
                        messages.append(line);
                        messages.append("\n");
                    }
                }
            } finally {
                EventQueue.invokeLater(() -> {
                    messages.append("Socket closed\n");
                    interruptibleButton.setEnabled(true);
                    blockingButton.setEnabled(true);
                });
            }
        }

        class TestServer implements Runnable
        {
            @Override
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                    while (true) {
                        Socket incoming = serverSocket.accept();
                        Runnable r = new TestServerHandler(incoming);
                        Thread t = new Thread(r);
                        t.start();
                    }
                } catch (IOException e) {
                    messages.append("\nTestServer.run: " + e);
                }
            }
        }

        class TestServerHandler implements Runnable
        {
            private Socket incoming;
            private int counter;

            TestServerHandler(Socket i) {
                incoming = i;
            }

            @Override
            public void run()
            {
                try {
                    try {
                        OutputStream outputStream = incoming.getOutputStream();
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
                        while (counter < 100) {
                            counter++;
                            if (counter <= 10) {
                                out.println(counter);
                            }
                            Thread.sleep(100);
                        }
                    } finally {
                        incoming.close();
                        messages.append("Closing server\n");
                    }
                } catch (Exception e) {
                    messages.append("\nTestServerHandler.run: " + e);
                }
            }
        }
    }
}