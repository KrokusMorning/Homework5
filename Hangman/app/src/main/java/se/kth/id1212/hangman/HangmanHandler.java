package se.kth.id1212.hangman;

public class HangmanHandler {

    public String[] newGame(String msg){
        String inputArray[] = msg.split(" ");

        String wordProgress = inputArray[2];
        String remainingGuesses = inputArray[3];
        String wordLength = inputArray[4];
        String totalScore = inputArray[6];

        String result[] = new String[4];
        result[0] = wordProgress;
        result[1] = remainingGuesses;
        result[2] = totalScore;

        return result;

    }

    public String[] guess (String msg){
        String inputArray[] = msg.split(" ");
        String result[] = new String[4];

        String word = inputArray[1];
        String wordProgress = inputArray[2];
        String remainingGuesses = inputArray[3];
        String wordLength = inputArray[4];
        String foundLetters = inputArray[5];
        String totalScore = inputArray[6];

        result[0] = wordProgress;
        result[1] = remainingGuesses;
        result[2] = totalScore;

        if(wordLength.equals(foundLetters)){
            result[3] = word + " IS THE WORD, YOU WIN! TOTAL SCORE: " + totalScore;
        }
        else if(remainingGuesses.equals("0")){
            result[0] = word;
            result[3] =  "\"" + word + "\"" + " WAS THE WORD, THE MAN GOT HANGED! " + "TOTAL SCORE: " + totalScore;
        }
        if(remainingGuesses.equals("-1")){
            result[0] = "";
            result[1] = "0";
            result[2] = totalScore;
            result[3] = "press \"NEW GAME\" to start new game";
        }

        return result;
    }
    public String getInfo() {
        return "Guess the letters of a word one by one or the complete word. \n" +
                "Start new game:        press \"NEW GAME\"      \n" +
                "Guess the letter a:    type \"a\" in the text field and press \"GUESS\"\n" +
                "Guess the word hello:  type \"hello\" in the text field and press \"GUESS\" \n" +
                "Quit:   press \"QUIT\"\n";
    }
}
