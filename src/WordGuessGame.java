import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class WordGuessGame extends Application {

    static int idx = 0;

    //20 words with length 3 - 20
    static String[]  word ={"ace", "mill", "cramp", "muzzle", "swindle", "gracious", "meticulous",
            "symmetry", "souvenirs", "stochastic", "sovereignty", "supernatural",
            "spontaneously", "stratification", "sympathetically", "sentimentalising",
            "spectrophotometry", "sentimentalization", "straightforwardness", "internationalization"};

    int score = 0;
    int numGuess = 1;
    TextField userGuess = new TextField();
    Text scrambledWord = new Text();
    Text enterText = new Text("Enter word: ");
    Text scoreText = new Text();
    Button guessButton = new Button("Guess");
    Button nextLevel = new Button("Next Level");
    Button startButton = new Button("Start Game");
    Text correctGuessMessage = new Text("Correct Guess. Next level.");
    BorderPane gameDisplayPane = new BorderPane();
    HBox correctGuessBox = new HBox();
    HBox totalScore = new HBox();
    BorderPane mainPane = new BorderPane();
    HBox errorMessageBox = new HBox();


    static Stage gameStage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //VBox for the start button and the gameDisplayPane
        VBox startButtonBox = new VBox();
        startButtonBox.setAlignment(Pos.CENTER);
        startButtonBox.getChildren().add(startButton);
        gameDisplayPane = gamePane();
        startButtonBox.getChildren().add(gameDisplayPane);
        startButtonBox.setPadding(new Insets(10,0,0,0));
        gameDisplayPane.setVisible(false);

        //HBox for the congratulation message for correct guess
        correctGuessBox.setAlignment(Pos.CENTER);
        correctGuessBox.setPadding(new Insets(5,20,40,20));
        correctGuessBox.setStyle("-fx-background-color: white");
        correctGuessMessage.getStyleClass().add("correctMessage");
        correctGuessBox.getChildren().add(correctGuessMessage);
        correctGuessBox.setVisible(false);

        //overall contents in the scene
        mainPane.setTop(getTitle());
        mainPane.setCenter(startButtonBox);
        mainPane.setBottom(correctGuessBox);

        // Process events
        startButton.setOnAction(e -> startGuess());

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 500, 340);
        scene.getStylesheets().add("CSSstyle/WordGuessGame.css"); //add style sheet for the scene
        primaryStage.setTitle("Word Guess Game"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        gameStage = primaryStage;
    }
    public BorderPane gamePane(){
        BorderPane game = new BorderPane();

        //HBox for the total score
        HBox gameScore = new HBox();
        gameScore.setAlignment(Pos.CENTER);
        gameScore.setPadding(new Insets(0, 20, 10, 20));
        scoreText.setText("Score : "+ score);
        scoreText.setStyle("-fx-fill: white");
        gameScore.getChildren().add(scoreText);

        //HBox for scrambled word
        HBox scrambledWordBox = new HBox();
        scrambledWordBox.setAlignment(Pos.CENTER);
        scrambledWordBox.setPadding(new Insets(0, 20, 10, 20));
        scrambledWord.setStyle("-fx-fill: white");
        scrambledWordBox.getChildren().add(scrambledWord);

        //HBox for the user guess input
        HBox userInputBox = new HBox();
        userInputBox.setSpacing(10);
        userInputBox.setAlignment(Pos.CENTER);
        userInputBox.setPadding(new Insets(0, 20, 10, 20));
        enterText.setStyle("-fx-fill:white");
        userInputBox.getChildren().add(enterText);
        userInputBox.getChildren().add(userGuess);

        //HBox for the guess button
        HBox guessButtonBox = new HBox();
        guessButtonBox.setAlignment(Pos.CENTER);
        guessButtonBox.setPadding(new Insets(0, 20, 10, 20));
        guessButtonBox.getChildren().add(guessButton);

        //VBox for the input text field and buttons
        VBox inputButton = new VBox();
        inputButton.setAlignment(Pos.CENTER);
        inputButton.getChildren().add(userInputBox);
        inputButton.getChildren().add(guessButtonBox);
        inputButton.getChildren().add(nextLevel);
        inputButton.setPadding(new Insets(0,20,0,20));

        //set the contents for game Pane
        game.setTop(gameScore);
        game.setCenter(scrambledWordBox);
        game.setBottom(inputButton);

        return game;

    }
    private HBox getTitle() { //HBox for the title
        HBox titleBox = new HBox(5);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(50, 20, 10, 20));
        Text title = new Text("Word Guess Game");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
        title.setStyle("-fx-fill: #a0f764");
        titleBox.getChildren().add(title);

        return titleBox;
    }
    public void wordGenerator() {
        String randomWord;
        do{
            randomWord = "";
            ArrayList<Integer> prev = new ArrayList<>();

            //scramble the words
            for(int i = 0; i<word[idx].length(); i++){
                int x = 0;
                do {
                     x = (int)Math.floor((Math.random()* word[idx].length()));
                }while(prev.contains(x)); //prevent generate the same index at the original word

                randomWord =  randomWord+word[idx].charAt(x); //create a random word with scrambled letters
                prev.add(x); //store the index of the original word that's already been used.
            }
        }while(randomWord.equals(word[idx])); //repeat if the scrambled word is exactly the same as the original word

        String result = "";
        for (int i = 0; i < randomWord.length(); i++) {
            if (i > 0) {
                result+="    ";
            }

            result+=randomWord.charAt(i);
        }

        scrambledWord.setText(result);
    }

    //trigger this function when the start button is clicked
    public void startGuess() {
        // reset the textfield
        userGuess.clear();

        //hide the start button, and display the gameDisplayPane
        startButton.setVisible(false);
        gameDisplayPane.setVisible(true);

        //display the scrambled word
        wordGenerator();

        //enable the guessButton
        guessButton.setVisible(true);

        //hide the next level button
        nextLevel.setVisible(false);

        //hide the correctGuessBox and errorMessageBox
        correctGuessBox.setVisible(false);
        errorMessageBox.setVisible(false);

        //trigger function checkGuess when the guessButton is clicked
        guessButton.setOnAction(e -> checkGuess());
    }

    public void checkGuess(){

        //get input
        String text = userGuess.getText();

        //reset the errorMessageBox
        errorMessageBox.setVisible(false);



        if(text.equals("")){ //if the user has an empty input
            startGuess();
        }
        else {
            //when less than 3 attempts
            if (numGuess <= 3) {
                if (text.equals(word[idx])) { //guess the correct word

                    //display congratulation message
                    mainPane.setBottom(correctGuessBox);
                    correctGuessBox.setVisible(true);

                    //calculate the score
                    score += word[idx].length() / (numGuess) * 10;

                    //display the score
                    scoreText.setText("Score: "+score);


                    //increment idx to move on to the next word
                    ++idx;

                    //disable the guess button
                    guessButton.setVisible(false);


                    //enable the next level button
                    nextLevel.setVisible(true);


                    //play until the end
                    if (idx >= word.length) {

                        //display the total score
                        Label endGameMessage = new Label("That's the end of the game. \n Total Score: "+score);
                        totalScore.setAlignment(Pos.CENTER);
                        endGameMessage.setStyle("-fx-text-fill: white");
                        totalScore.getChildren().add(endGameMessage);
                        mainPane.setCenter(totalScore);

                        //hide the gamDisplay pane
                        gameDisplayPane.setVisible(false);
                        correctGuessBox.setVisible(false);

                    }

                    //when user clicks on the nextLevel button
                    nextLevel.setOnAction(e -> startGuess());

                    numGuess = 0;

                } else { //if the user makes the wrong guess


                    //display wrong guess message
                    errorMessageBox.setVisible(true);
                    String tries = (3 - numGuess > 1) ? "tries" : "try";
                    Text errorMessage = new Text("Incorrect Guess. Guess again. "+ (3-numGuess)+" "+tries+" left.");
                    errorMessage.getStyleClass().add("errorMessage");
                    errorMessageBox.setAlignment(Pos.CENTER);
                    errorMessageBox.setPadding(new Insets(5,20,40,20));
                    errorMessageBox.setStyle("-fx-background-color: white");
                    //replace the existing error message
                    if(errorMessageBox.getChildren().size()== 0)
                        errorMessageBox.getChildren().add(errorMessage);
                    else
                        errorMessageBox.getChildren().set(0,errorMessage);
                    mainPane.setBottom(errorMessageBox);

                    // reset the textfield
                    userGuess.clear();

                    //if the guessButton is clicked
                    guessButton.setOnAction(e -> checkGuess());
                }
                ++numGuess;
            }
            if (numGuess > 3) {//user makes more than 3 attempts

                Label gameOverMessage = new Label("Game Over.  Score: "+score);
                gameOverMessage.setStyle("-fx-text-fill: white");
                totalScore.setAlignment(Pos.CENTER);
                totalScore.getChildren().add(gameOverMessage);
                mainPane.setCenter(totalScore);

                //hide the gameDisplayPane and errorMessageBox
                gameDisplayPane.setVisible(false);
                errorMessageBox.setVisible(false);
            }
        }
    }

}
