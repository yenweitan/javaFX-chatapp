
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ChatServer extends Application {
    //the client number
    private int clientNo = 0;

    ArrayList<Socket> chatClients = new ArrayList<>();

    // Text area for displaying contents
    TextArea ta = new TextArea();

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Chat Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> ta.appendText("MultiThreadSever started at "+new Date() +"\n"));

                while (true) {

                    // Listen for a new connection request
                    Socket client = serverSocket.accept();

                    // Increment clientNo
                    clientNo++;

                    Platform.runLater( () -> {
                        // Display the client number
                        ta.appendText("Connection from Client " +clientNo+ ", Socket[addr=/" + client.getInetAddress().getHostAddress() +
                                " at "+ new Date() + '\n');
                    });

                    // Create and start a new thread for the connection (for the new client)
                    Thread t = new Thread(new HandleAClient(client,clientNo));
                    chatClients.add(client);
                    t.start();

                }
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }

    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private Socket client; // A connected socket
        private int clientNo;

        /** Construct a thread */
        public HandleAClient(Socket socket,int clientNo) {
            this.client = socket;
            this.clientNo = clientNo;
        }

        /** Run a thread */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        client.getInputStream());

                // Continuously serve the client
                while (true) {

                    String name = inputFromClient.readUTF();
                    String message = inputFromClient.readUTF();
                    String color = inputFromClient.readUTF();

                    //send the message to everyone in the chatClients list
                    for(Socket s: chatClients) {
                        DataOutputStream outputToClient = new DataOutputStream(
                                s.getOutputStream());
                        outputToClient.writeUTF(name + ": " + message);
                        outputToClient.writeUTF(color);
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }


}