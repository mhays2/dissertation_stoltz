package com.example.mh.mytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;

import net.sourceforge.zbar.Symbol;

import jp.nyatla.nyartoolkit.and.SimpleLiteActivity;


public class MainActivity2Activity extends Activity implements View.OnClickListener {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int MON_AR_REQUEST = 2;
    private String theBin;
    private Integer theBinNumber;
    private Integer checking;
    private View mView;
    private View mView2;
    private static int TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        launchScanner();

    }

    public void launchScanner(){
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED && data != null) {
            String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        }
        else {
                switch (requestCode) {
                    case ZBAR_SCANNER_REQUEST:
                        //Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_LONG).show();
                        theBin = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                        theBinNumber = Integer.parseInt(theBin);
                        showResult();
                    break;
                    case MON_AR_REQUEST:
                        CardBuilder card = new CardBuilder(this,CardBuilder.Layout.TEXT);
                        checking = data.getIntExtra("checking",0);
                        if(checking == 0){
                            card.setText("Wrong bin! Please look at the correct bin and put the item in it");
                            mView = card.getView();
                            mView.setOnClickListener(this);
                            mView.setFocusable(true);
                            mView.setFocusableInTouchMode(true);
                            setContentView(mView);
                        }
                        else {
                            card.setText("Wait to scan a new item");
                            mView2 = card.getView();
                            //mView2.setOnClickListener(this);
                            //mView2.setFocusable(true);
                            //mView2.setFocusableInTouchMode(true);
                            setContentView(mView2);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    launchScanner();
                                }
                            }, TIME_OUT);
                        }

                    break;
                }
        }

    }

    private void showResult(){
       Intent intent = new Intent(this, SimpleLiteActivity.class);
        intent.putExtra("mon_choix", theBinNumber);
        startActivityForResult(intent, MON_AR_REQUEST);
        /*CardBuilder card = new CardBuilder(this,CardBuilder.Layout.CAPTION);
        card.setText(theBin);
        card.addImage(R.drawable.bin);
        View mView = card.getView();
        mView.setOnClickListener(this);
        mView.setFocusable(true);
        mView.setFocusableInTouchMode(true);
        setContentView(mView);*/

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // perform desired action
       if (v == mView2){
            launchScanner();
        }
        else if (v == mView){
            showResult();
        }
    }

}
