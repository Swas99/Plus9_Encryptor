package com.example.swsahu.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.view.animation.AnimationUtils.loadAnimation;


public class encrypt extends ActionBarActivity implements View.OnClickListener  {

    private Button btnEncrypt;
    private EditText tb_real_input;
    private EditText tb_fake_input;
    private EditText tb_key;
    private EditText tb_output;
    private CheckBox cb_fake;
    private Button btnCopy;
    private ImageView imgGear1;
    private ImageView imgGear2;
    private Animation rotation;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        SetControls();
    }

    public void SetControls()
    {
        btnEncrypt = (Button)findViewById(R.id.btnEncrypt);
        tb_real_input = (EditText)findViewById(R.id.real_input);
        tb_fake_input = (EditText)findViewById(R.id.FakeInput);
        tb_key = (EditText)findViewById(R.id.Key);
        tb_output = (EditText)findViewById(R.id.Output);
        cb_fake = (CheckBox)findViewById(R.id.cbFake);
        btnCopy = (Button) findViewById(R.id.btnCopy);
        imgGear1 = (ImageView) findViewById(R.id.imgGear1);
        imgGear2 = (ImageView) findViewById(R.id.imgGear2);

        btnEncrypt.setOnClickListener(this);
        tb_real_input.setOnClickListener(this);
        tb_fake_input.setOnClickListener(this);
        tb_key.setOnClickListener(this);
        tb_output.setOnClickListener(this);
        cb_fake.setOnClickListener(this);
        btnCopy.setOnClickListener(this);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.encrypt);

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

        tb_fake_input.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        try {
            int id = view.getId();
            switch (id) {
                case R.id.btnEncrypt:
                //region Start Animation
                    imgGear1.setVisibility(View.VISIBLE);
                    imgGear1.startAnimation(rotation);
                    imgGear2.setVisibility(View.VISIBLE);
                    imgGear2.startAnimation(rotation);
                //endregion

                //region Encrypting text
                    String real = tb_real_input.getText().toString().trim();
                    String fake = tb_fake_input.getText().toString().trim();
                    String key = tb_key.getText().toString().trim();
                    String encrypted_real_text="";
                    String encrypted_fake_text="";
                    try
                    {
                        encrypted_real_text = EncryptionLogic.encrypt_text(real, key+EncryptionLogic.NINE);
                        encrypted_fake_text = EncryptionLogic.encrypt_text(fake,key);
                    } catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Encryption Failed!\n" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    int delimiter_code = EncryptionLogic.GetDelimiterCode(key+EncryptionLogic.NINE);
                    String combined_text = encrypted_real_text + EncryptionLogic.DELIMITER[delimiter_code] +encrypted_fake_text;
                    String final_encrypted_text = EncryptionLogic.ShuffleText(combined_text, key);
                    tb_output.setText(final_encrypted_text);
                //endregion
                    break;

                case R.id.cbFake:
                 //region Set Real/Fake input textBox visibility
                    if (cb_fake.isChecked()) {
                        tb_fake_input.setVisibility(View.VISIBLE);
                        tb_real_input.setVisibility(View.GONE);
                    } else {
                        tb_fake_input.setVisibility(View.GONE);
                        tb_real_input.setVisibility(View.VISIBLE);
                    }
                  //endregion
                    break;
                case R.id.btnCopy:
                //region Copy data to clipBoard
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB)
                    {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(tb_output.getText().toString());
                    }
                    else
                    {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Encrypted String", tb_output.getText().toString()));
                    }
                //endregion
                    break;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }




}
