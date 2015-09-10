package com.example.swsahu.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button btnEncrypt;
    private Button btnDecrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetControls();
    }

    public void SetControls()
    {
        btnEncrypt = (Button)findViewById(R.id.btnEncrypt);
        btnDecrypt = (Button)findViewById(R.id.btnDecrypt);

        btnEncrypt.setOnClickListener(this);
        btnDecrypt.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id)
        {
            case R.id.btnEncrypt :
                intent =new Intent(getApplicationContext(),encrypt.class);
                startActivity(intent);
                break;
            case R.id.btnDecrypt :
                intent =new Intent(getApplicationContext(),decrypt.class);
                startActivity(intent);
                break;
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
