
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient3
        extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws IOException {

        // Panel p to hold the label and text field
        GridPane paneForTextField = new GridPane();
        paneForTextField.setHgap(5);
        paneForTextField.setVgap(5);
        paneForTextField.setPadding(new Insets(10, 10, 10, 10));
        paneForTextField.setStyle("-fx-border-color: rgba(47,96,47,0.95)");
        paneForTextField.add(new Label("Name: "), 0,0);
        paneForTextField.add(new Label("Enter Text: "),0,1);

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        tf.setPrefWidth(480);
        paneForTextField.add(tf, 1,0);

        TextField tf2 = new TextField();
        tf2.setAlignment(Pos.BOTTOM_RIGHT);
        tf2.setPrefWidth(480);
        paneForTextField.add(tf2, 1, 1);

        VBox wordGuessGameBox = new VBox();
        wordGuessGameBox.setAlignment(Pos.CENTER);
        wordGuessGameBox.setPadding(new Insets(10,10,10,10));
        wordGuessGameBox.setStyle("-fx-border-color: rgba(47,96,47,0.95)");
        Button wordGuessGame = new Button("Word Guess Game");
        wordGuessGame.setCursor(Cursor.HAND);
        wordGuessGameBox.getChildren().add(wordGuessGame);

        // Text area to display contents
        TextArea ta = new TextArea();
        ta.setPrefRowCount(21);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);
        mainPane.setRight(wordGuessGameBox);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 600, 400);
        scene.getStylesheets().add("CSSstyle/ChatClient3Style.css"); //add style sheet for the scene
        primaryStage.setTitle("Chat Client 3"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);


            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }

        new Thread(() ->{
            try{
                while(true){

                    tf2.setOnAction(e -> {
                        try {
                            // Get the radius from the text field
                            String name = tf.getText().trim();
                            String text = tf2.getText().trim();
                            String color = "green";

                            // Send the radius to the server
                            toServer.writeUTF(name);
                            toServer.writeUTF(text);
                            toServer.writeUTF(color);

                            toServer.flush();
                            tf2.setText("");
                            tf.setEditable(false);
                        }
                        catch (IOException ex) {
                            System.err.println(ex);
                        }
                    });
                    String message = fromServer.readUTF();
                    String colorMessage = fromServer.readUTF();

                    Platform.runLater( () -> {
                        // Display to the text area
                        ta.setStyle(" -fx-text-fill: " + colorMessage);
                        ta.appendText(message + '\n');
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            wordGuessGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //create an object of the class you wish to invoke its
                    //start() method:

                    WordGuessGame ctc = new WordGuessGame();

                    // Then call its start() method in the following way:

                    ctc.start(WordGuessGame.gameStage);

                }// End handle(ActionEvent event)
            });
        }).start();
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
