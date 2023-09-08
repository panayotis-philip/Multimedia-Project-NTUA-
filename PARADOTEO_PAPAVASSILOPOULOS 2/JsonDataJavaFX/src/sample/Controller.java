package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.swing.interop.SwingInterOpUtils;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.print.DocFlavor;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.*;
public class Controller {


    @FXML
    ImageView myImageView;

    @FXML
    Label wordsAtDict;

    @FXML
    Label pointsLabel;

    @FXML
    Label soutLabel;

    @FXML
    Label wordLabel;

    @FXML
    Label soutLabelprob;

    Image myImage0 = new Image(getClass().getResourceAsStream("mistakes0.png"));
    Image myImage1 = new Image(getClass().getResourceAsStream("mistakes1.png"));
    Image myImage2 = new Image(getClass().getResourceAsStream("mistakes2.png"));
    Image myImage3 = new Image(getClass().getResourceAsStream("mistakes3.png"));
    Image myImage4 = new Image(getClass().getResourceAsStream("mistakes4.png"));
    Image myImage5 = new Image(getClass().getResourceAsStream("mistakes5.png"));
    Image myImage6 = new Image(getClass().getResourceAsStream("mistakes6.png"));

    TextField posTextField;

    TextField letterTextField;

    String bookId;

    String[] words;

    String position;
    String let;

    boolean giveUp=false;

    ArrayList<ArrayList<String>> result= new ArrayList<ArrayList<String>>();

    public void play_game(){
        if(!wordLabel.getText().equals("-")){
            giveUp=false;
            myImageView.setImage(myImage0);
            submitText(pointsLabel,"0");
            String key_word = getKeyWord(words);

            LinkedList<String> listWord = new LinkedList<String>();

            for(int i =0;i<words.length;i++) {
                if (words[i].length() == key_word.length()) {
                    listWord.add(words[i]);
                }
            }
            String [] keyWord = createKeyWord(key_word);
            String [] missWord = createMissWord(key_word);



            int mistakes=0;

            int win=0;
            int points=0;

            while (mistakes<=5||!Arrays.equals(keyWord,missWord)){
                for(int i=0;i<listWord.size();i++){
                    System.out.println(listWord.get(i));
                }
                System.out.println(Arrays.toString(keyWord));
                System.out.println(Arrays.toString(missWord));
                String wordLabelString="";
                for(int i=0;i<missWord.length;i++){
                    wordLabelString+=missWord[i];
                    wordLabelString+="      ";
                }
                wordLabel.setText(wordLabelString);

                //Scanner scanner1 = new Scanner(System.in);
                System.out.println("select position (Stating from 0)");
                soutLabel.setText("Select position (stating from 0)");
                //String res = scanner1.nextLine();
                displayPos();
                if(giveUp==true) {
                    System.out.println("You lost! The word was: "+Arrays.toString(keyWord));
                    String wordLabelStringWin="";
                    for(int i=0;i<key_word.length();i++){
                        wordLabelStringWin+=keyWord[i];
                        wordLabelStringWin+="";
                    }
                    soutLabel.setText("You lost! The word was:  "+wordLabelStringWin);
                    myImageView.setImage(myImage6);
                    win=0;
                    break;
                }
                String res = position;
                if (!isInt(res,10)){
                    continue;
                }
                int pos = Integer.parseInt(res);
                if(pos>=keyWord.length||pos<0){
                    System.out.println("position not valid");
                    soutLabel.setText("position not valid");
                    continue;
                }
                if(missWord[pos]!="_"){
                    System.out.println("already found the letter in this position");
                    soutLabel.setText("already found the letter in this position");
                    continue;
                }

                //showProb(listWord,pos);
                LinkedList<Character> charsWord = new LinkedList<Character>();

                for(int i=0;i<listWord.size();i++){

                    charsWord.add(listWord.get(i).charAt(pos));
                }

                LinkedList <String> charStrings = new LinkedList<String>();

                LinkedList<Double> probabilities = new LinkedList<Double>();

                for (int i= 0;i<charsWord.size();i++){

                    charStrings.add(Character.toString(charsWord.get(i)));

                    //String probString = String.valueOf(Collections.frequency(charsWord,charsWord.get(i))*1.0/charsWord.size()*1.0);
                /*if(probString.length()>5){
                    probabilities.add(probString.substring(0,5));
                }else{
                    probabilities.add(probString);
                }*/
                    probabilities.add(Collections.frequency(charsWord,charsWord.get(i))*1.0/charsWord.size()*1.0);


                }

                for(int i =0;i<charStrings.size();i++) {
                    if (probabilities.get(i).toString().length()<= 5) {
                        System.out.println(charStrings.get(i) + " " + (probabilities.get(i).toString()));
                    }else {
                        System.out.println(charStrings.get(i) + " " + (probabilities.get(i).toString()).substring(0, 5));
                    }
                }

                String soutLabelprobString="";
                for(int i =0;i<charStrings.size();i++) {
                    if (probabilities.get(i).toString().length()<= 5) {
                        soutLabelprobString+=charStrings.get(i)+ ":      "+probabilities.get(i).toString()+"\n";
                    }else {
                        soutLabelprobString+=charStrings.get(i)+ ":     " +probabilities.get(i).toString().substring(0, 5)+"\n";
                    }
                }

                soutLabelprob.setText(soutLabelprobString);

                // FUNCTION ABOVE

                System.out.println("enter letter");
                soutLabel.setText("Enter letter");
                //String char_try = scanner1.nextLine();
                displayLetter();
                String char_try = let;
                soutLabelprob.setText("");
                if(!isLetter(char_try)){
                    System.out.println("that is not a letter");
                    soutLabel.setText("That is not a letter");
                    continue;
                }
                if(char_try.toUpperCase().equals(keyWord[pos])){
                    missWord[pos]=char_try.toUpperCase();


                    int index = charStrings.indexOf(char_try.toUpperCase());
                    double probi = probabilities.get(index);
                    if(probi<0.25){
                        points+=30;
                    }else if(probi>=0.25&&probi<0.4){
                        points+=15;
                    }else if(probi>=0.4&&probi<0.6){
                        points+=10;
                    }else {
                        points+=5;
                    }

                    //update the listWord
                    for(int i =0;i<listWord.size();i++){
                        if(listWord.get(i).charAt(pos)!=char_try.charAt(0)){
                            listWord.remove(i);
                            i--;
                        }
                    }
                }else {
                    mistakes++;
                    if(points>0)
                        points-=15;
                }
                System.out.println("Points gathered: "+points);
                submitText(pointsLabel,String.valueOf(points));
                System.out.println("Mistakes: "+mistakes);
                if(Arrays.equals(keyWord,missWord)){
                    System.out.println("You won!");
                    soutLabel.setText("You won!");
                    win=1;
                    wordLabel.setText("");
                    String wordLabelStringWin="";
                    for(int i=0;i<keyWord.length;i++){
                        wordLabelStringWin+=keyWord[i];
                        wordLabelStringWin+="      ";
                    }
                    wordLabel.setText(wordLabelStringWin);
                    break;
                }
                if(mistakes==1){
                    setImage(myImage1);
                }else if(mistakes==2){
                    setImage(myImage2);
                }else if(mistakes==3){
                    setImage(myImage3);
                }else if(mistakes==4){
                    setImage(myImage4);
                }else if(mistakes==5){
                    setImage(myImage5);
                } else if(mistakes==6) {
                    System.out.println("You lost! The word was: "+Arrays.toString(keyWord));
                    String wordLabelStringWin="";
                    for(int i=0;i<key_word.length();i++){
                        wordLabelStringWin+=keyWord[i];
                        wordLabelStringWin+="";
                    }
                    soutLabel.setText("You lost! The word was:  "+wordLabelStringWin);
                    myImageView.setImage(myImage6);
                    win=0;
                    break;
                }

            }

            ArrayList<String> gameRes = new ArrayList<String>();
            gameRes.add(key_word);
            gameRes.add(String.valueOf(mistakes));
            if(win==0){
                gameRes.add("computer");
            }else {
                gameRes.add("player");
            }

            //show result
            if(result.size()<5){
                result.add(gameRes);
            }
            else {
                result.remove(0);
                result.add(gameRes);
            }
            return;
        }
    }

    public void lastFiveGames(){
        String s = new String();

        for(int i =result.size()-1;i>=0;i--){
            //System.out.println();
            s+="\n";
            s+= "Word: "+result.get(i).get(0)+"\n"+"Number of mistakes: "+result.get(i).get(1)+"\n"+"The winner was: "+result.get(i).get(2)+"\n";
            //System.out.println("Word: "+result.get(i).get(0)+"\n"+"Number of mistakes: "+result.get(i).get(1)+"\n"+"The winner was: "+result.get(i).get(2));
        }
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle("Last Five Games");
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e-> {
            window.close();
        });
        Label gamesLabel = new Label();
        gamesLabel.setText(s);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(gamesLabel,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


    }

    public void setImage(Image image) {
        myImageView.setImage(image);
    }

    public void submitText(Label label,String s){
        label.setText(s);
    }


    public void displayPos(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle("Position");
        Label label = new Label();
        label.setText("Enter the letter's position");
        TextField textField = new TextField();

        Button closeButton = new Button("Done");
        Button giveUpButton = new Button("Give Up");
        HBox hBox = new HBox(5);
        closeButton.setOnAction(e-> {
            position=textField.getText();
            window.close();
        });
        giveUpButton.setOnAction(e->{
            giveUp=true;
            window.close();
        });
        VBox layout = new VBox(10);
        hBox.getChildren().addAll(closeButton,giveUpButton);
        hBox.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label,textField,hBox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }



    public void displayLetter(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle("Letter");
        Label label = new Label();
        label.setText("Enter the letter for that position");
        TextField textField = new TextField();

        Button closeButton = new Button("Done");
        Button giveUpButton = new Button("Give Up");
        HBox hBox = new HBox(5);
        giveUpButton.setOnAction(e->{
            giveUp=true;
            window.close();
        });

        closeButton.setOnAction(e-> {
            let=textField.getText();
            window.close();
        });
        hBox.getChildren().addAll(closeButton,giveUpButton);
        hBox.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,textField,hBox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void display(){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle("Create Dictionary");
        Label label = new Label();
        label.setText("Enter book code");
        TextField textField = new TextField();

        Button closeButton = new Button("Done");
        closeButton.setOnAction(e-> {
            bookId=textField.getText();
            window.close();
            try {
                addDict(bookId);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,textField,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        System.out.println("Book id : "+bookId);
    }

    public void display_load(){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle("Load Dictionary");
        Label label = new Label();
        label.setText("Enter book code/dictionary Id");
        TextField textField = new TextField();
        Button closeButton = new Button("Done");
        closeButton.setOnAction(e-> {
            bookId=textField.getText();
            window.close();

            words=hasMoreThanSix(noDuppl(createCharArrFromFile(bookId)));
            submitText(wordsAtDict,String.valueOf(words.length));
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,textField,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        System.out.println("Book id : "+bookId);
        System.out.println(Arrays.toString(words));
    }


    public void displayDictWords(String s1,String s2,String s3){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setTitle("Dictionary");
        Label label1 = new Label();
        label1.setText("Percentage of words of length 6: "+s1);
        Label label2 = new Label();
        label2.setText("Percentage of words of length 7-9: "+s2);
        Label label3 = new Label();
        label3.setText("Percentage of words of length 10+: "+s3);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e-> {
            window.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1,label2,label3,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void display_Dict(){
        if(!wordsAtDict.getText().equals("-")){
            int count6=0,count7_9=0,count10more=0;

            for (int i=0;i<words.length;i++) {
                if (words[i].length() == 6) {
                    count6++;
                } else if (words[i].length() >= 7 && words.length <= 9) {
                    count7_9++;
                } else {
                    count10more++;
                }
            }



            String string6=String.valueOf(count6*1.0/words.length*1.0),string7_9=String.valueOf(count7_9*1.0/ words.length*1.0),string10=String.valueOf(count10more*1.0/ words.length*1.0);
            System.out.println(string6+" "+string7_9+" "+string10);
            String string6final="",string7_9final="",string10final="";
            if (string6.length()>5){
                string6final=string6.substring(0,5);
            }else{
                string6final = string6;
            }
            if (string7_9.length()>5){
                string7_9final=string7_9.substring(0,5);
            }else{
                string7_9final = string7_9;
            }
            if (string10.length()>5){
                string10final=string10.substring(0,5);
            }else{
                string10final = string10;
            }
            displayDictWords(string6final,string7_9final,string10final);
        }

    }


    public void getPos(ActionEvent event) throws IOException{
        position = posTextField.getText();
        System.out.println("Position is: "+position);

    }

    public void getLetter(ActionEvent event) throws IOException{
        let = letterTextField.getText();
        System.out.println("Letter is: "+let);
    }


    //FOR PLAYING THE GAME

    //check if string is a letter
    public static boolean isLetter(String s){
        if (s == null) // checks if the String is null {
            return false;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if ((!Character.isLetter(s.charAt(i)))) {
                return false;
            }
        }
        return len <= 1;

    }

    //check if string is int
    public static boolean isInt(String s,int radix){
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;

    }

    public static String[] createMissWord(String key_word){
        String[] missWord = new String[key_word.length()];
        for(int i=0;i<key_word.length();i++){
            missWord[i]="_";
        }
        return missWord;
    }
    public static String[] createKeyWord(String key_word){
        String[] res = new String[key_word.length()];
        for (int i = 0; i < key_word.length(); i++) {
            res[i] = Character.toString(key_word.charAt(i));
        }
        return res;
    }

    public static String getKeyWord(String[] word_input){
        if(isDictGood(word_input)){
            return getRandWord(word_input);
        }else {
            System.out.println("something went wrong");
            return null;
        }
    }

    public static String getRandWord(String[] word_input){
        Random r=new Random();
        int randomNumber=r.nextInt(word_input.length);
        return word_input[randomNumber];
    }



    //FOR LOADING THE DICT

    /**
     * This method constructs an array of words from the .txt file from a specific book id
     * @param bookId the book id that is part of the name of the .txt file that we want to access
     * @return
     */

    public static String[] createCharArrFromFile(String bookId){
        //Scanner myScanner = new Scanner(System.in);
        String book_ID = bookId.trim();


        //read string from file
        File f = new File("hangman_"+book_ID+".txt");
        try {
            Scanner myReader = new Scanner(f);
            String s = "";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                s+=data;
            }
            myReader.close();
            //System.out.println("The string is: "+s);

            return s.split("[ .()1234567890@?'-:/_!,\t\n\"]+");

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

        }
        return null;
    }

    /**
     * This method checks if the array of words has a good percentage of words more than 9 letters
     * and also has more than 20 words
     * @param word_input the array of words that need to be checked
     * @return returns boolean true: the dictionary is good for the game or false: the dictionary is not good for the game
     */

    public static boolean isDictGood(String[] word_input){
        return hasGoodPercentage(word_input) && word_input.length >= 20;
    }

    /**
     * This method checks if the percentage of words with more than 9 letters in the dictionary is more than 20%
     * @param word_input an array of words that need to be checked
     * @return a boolean statement. True: percentage>20%. False: percentage<20%.
     */
    public static boolean hasGoodPercentage(String[] word_input){
        int counter=0;
        for (String s : word_input) {
            if (s.length() >= 9) {
                counter++;
            }
        }
        //System.out.println(counter);
        //System.out.println(word_input.length);
        double perc = counter*1.0/word_input.length;
        System.out.println("the percentage is : "+perc);
        return perc > 0.2;
    }

    /**
     * This method takes an array of words and returns a new array only with the words that have 6 letters or more
     * @param word_input an array of words
     * @return an array of words with 6 or more letters
     */

    public static String[] hasMoreThanSix(String[] word_input){
        LinkedHashSet<String> lhSetwords = new LinkedHashSet<>();

        for (String s : word_input) {
            if (s.length() >= 6) {
                lhSetwords.add(s);
            }
        }
        return lhSetwords.toArray(new String[ lhSetwords.size() ]);
    }

    /**
     * This method takes and array of words and returns a new array with no duplicate words
     * @param words_input an array of words with or without duplicate words
     * @return a new array with no duplicates
     */

    public static String[] noDuppl (String[] words_input){
        LinkedHashSet<String> lhSetwords = new LinkedHashSet<>(Arrays.asList(words_input));
        return lhSetwords.toArray(new String[ lhSetwords.size() ]);

    }


    //FOR CREATING THE DICT


    public static void addDict(String bookId) throws IOException {
        //Scanner myObj = new Scanner(System.in);
        //String book_ID = myObj.nextLine();
        String book_ID=bookId.trim();

        String s = getData(book_ID);
        createFile(book_ID,s);
    }

    public static void createFile(String book_id,String data){
        try {
            File myObj = new File("hangman_"+book_id+".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                try {
                    FileWriter myWriter = new FileWriter("hangman_"+book_id+".txt");
                    myWriter.write(data.toUpperCase());
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static String getData (String bookID) throws IOException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://openlibrary.org/works/"+bookID+".json")).build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Controller::parse)
                .join();
    }

    public static String parse(String responseBody){
        JSONObject data = new JSONObject(responseBody);
        String descr = data.getString("description");
        //String descr=data.getJSONObject("description").getString("value");
        return descr;
    }

}
