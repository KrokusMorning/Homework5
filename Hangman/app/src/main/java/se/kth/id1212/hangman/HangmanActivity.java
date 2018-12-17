package se.kth.id1212.hangman;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import se.kth.id1212.hangman.net.Connection;
import se.kth.id1212.hangman.net.OutputHandler;

public class HangmanActivity extends Activity {

    private Connection tcpClient;
    private HangmanHandler hangmanHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        hangmanHandler = new HangmanHandler();
        setupActivity();
    }
    private void setupActivity()
    {
        new HangmanActivity.ConnectTask().execute("");
        final Button guessButton = (Button) findViewById(R.id.guessBtn);
        guessButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                new msgToServer().execute("GUESS " + editText.getText().toString());
            }
        });
        final Button newGameButton = (Button) findViewById(R.id.newGameBtn);
        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                new msgToServer().execute("START");
            }
        });
        final Button quitButton = (Button) findViewById(R.id.quitBtn);
        quitButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                new msgToServer().execute("DISCONNECT");
                finish();
            }
        });
        final Button infoButton = (Button) findViewById(R.id.infoBtn);
        infoButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Toast infoToast = Toast.makeText(getApplicationContext(), hangmanHandler.getInfo(), Toast.LENGTH_LONG);
                infoToast.show();
            }
        });
    }

    private void handleServerResponse(String msg){
        if(msg == null){
            return;
        }
        String inputArray[] = msg.split(" ");
        String result[] = new String[4];
        TextView progress = (TextView) findViewById(R.id.progress);
        TextView guessesLeft = (TextView) findViewById(R.id.guessesLeft);
        TextView score = (TextView) findViewById(R.id.score);
        if(inputArray[0].equals("START")){
            result = hangmanHandler.newGame(msg);
        }
        else if(inputArray[0].equals("GUESS")){
            result = hangmanHandler.guess(msg);
        }

        if(result[3] != null){
            Toast toast = Toast.makeText(getApplicationContext(), result[3], Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if(result[0] != null){
            progress.setText(   "Progress:      " + result[0]);
            guessesLeft.setText("Guesses left:  " + result[1]);
            score.setText(      "Score:         " + result[2]);
        }
    }

    public class msgToServer extends AsyncTask<String, String, Connection> {

        @Override
        protected Connection doInBackground(String... parameters) {
            tcpClient.sendMessage(parameters[0]);
            return null;
        }
    }


    public class ConnectTask extends AsyncTask<String, String, Connection> {

        @Override
        protected Connection doInBackground(String... message) {

            tcpClient = new Connection(new OutputHandler() {
                @Override
                public void handleMsg(String msg) {
                    publishProgress(msg);
                }
            });
            tcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... strings) {
            super.onProgressUpdate(strings);
            handleServerResponse(strings[0]);
        }
    }



}
