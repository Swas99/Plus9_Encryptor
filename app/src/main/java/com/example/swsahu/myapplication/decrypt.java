package com.example.swsahu.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.view.animation.AnimationUtils.loadAnimation;


public class decrypt extends ActionBarActivity implements View.OnClickListener {

    private Button btnDecrypt;
    private EditText tb_input;
    private EditText tb_key;
    private EditText tb_output;
    private Button btnPaste;
    private ImageView imgGear1;
    private ImageView imgGear2;
    private Animation rotation;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        SetControls();
    }

    public void SetControls()
    {
        btnDecrypt = (Button)findViewById(R.id.btnDecrypt);
        tb_input = (EditText)findViewById(R.id.input);
        tb_key = (EditText)findViewById(R.id.Key);
        tb_output = (EditText)findViewById(R.id.Output);
        btnPaste = (Button) findViewById(R.id.btnPaste);
        imgGear1 = (ImageView) findViewById(R.id.imgGear1);
        imgGear2 = (ImageView) findViewById(R.id.imgGear2);

        btnDecrypt.setOnClickListener(this);
        tb_input.setOnClickListener(this);
        tb_key.setOnClickListener(this);
        tb_output.setOnClickListener(this);
        btnPaste.setOnClickListener(this);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.decrypt);

        rotation = loadAnimation(getApplicationContext(), R.anim.rotate);
        rotation.setFillBefore(true);
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mp.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgGear1.setVisibility(View.INVISIBLE);
                imgGear2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        try {
            int id = view.getId();

            switch (id) {
                case R.id.btnDecrypt:
                //region Start Animation
                    imgGear1.setVisibility(View.VISIBLE);
                    imgGear1.startAnimation(rotation);
                    imgGear2.setVisibility(View.VISIBLE);
                    imgGear2.startAnimation(rotation);
                //endregion

                //region Decrypting text
                    String input = tb_input.getText().toString();
                    if(input == null || input.isEmpty())//Return if input is empty
                        return;

                    String key = tb_key.getText().toString().trim();
                    String decrypted_text;
                    int indexOfDelimiter;
                    String encrypted_text = EncryptionLogic.UnShuffleText(tb_input.getText().toString(),key);

                    int delimiter_code = EncryptionLogic.GetDelimiterCode(key); //Assuming it's real key
                    String delimiter = EncryptionLogic.DELIMITER[delimiter_code]; //Assuming it's real key

                    //Check if it's a fake key
                    if(!encrypted_text.contains(delimiter))
                    {
                        delimiter_code = EncryptionLogic.GetDelimiterCode(key+EncryptionLogic.NINE);
                        delimiter = EncryptionLogic.DELIMITER[delimiter_code];
                        indexOfDelimiter = encrypted_text.indexOf(delimiter);

                        if(indexOfDelimiter == -1) //It's an incorrect key or input data. Abort decryption
                        {
                            Toast.makeText(getApplicationContext(),"Operation failed!",Toast.LENGTH_SHORT).show();
                            tb_output.setText("");
                            return;
                        }

                        //Get encrypted text for fake data
                        encrypted_text = encrypted_text.substring(indexOfDelimiter+delimiter.length());
                    }
                    else //Get encrypted text for real data
                    {
                        indexOfDelimiter = encrypted_text.indexOf(delimiter);
                        encrypted_text = encrypted_text.substring(0,indexOfDelimiter);
                    }

                    try
                    {
                        decrypted_text = EncryptionLogic.decrypt_text(encrypted_text, key);
                        tb_output.setText(decrypted_text.trim());
                    } catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Operation Failed!\n"+e.toString(), Toast.LENGTH_LONG).show();
                        tb_output.setText("");
                    }
                //endregion

                    break;
                case R.id.btnPaste:
                //region GetDataFromClipBoard
                    String pasteData;
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        pasteData = clipboard.getText().toString();
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                        // Gets the clipboard as text.
                        pasteData = item.getText().toString();
                    }
                    tb_input.setText(pasteData);
                //endregion
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }



}
