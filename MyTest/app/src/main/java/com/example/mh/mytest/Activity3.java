package com.example.mh.mytest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;

import net.sourceforge.zbar.Symbol;


public class Activity3 extends Activity implements View.OnClickListener {
    //public class Activity3 extends Activity  {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private static final int BIN_REQUEST = 2;
    private String theBin;
    private Integer theCheck;
    private Integer theBinNumber;
    private View mView;
    private View mView2;
    //private Integer suite;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        launchScanner();
    }

    public void launchScanner(){
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
    }

    public void launchScannerBin(){
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        startActivityForResult(intent, BIN_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*switch (requestCode) {
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK) {
                    //Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_LONG).show();
                    theBin = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                    showResult();
                } else if (resultCode == RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if (!TextUtils.isEmpty(error)) {
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    }
                }
        }*/
        if (resultCode == RESULT_CANCELED && data != null) {
            String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == ZBAR_QR_SCANNER_REQUEST) {
            theBin = data.getStringExtra(ZBarConstants.SCAN_RESULT);
            theBinNumber = Integer.parseInt(theBin);
            showResult(0);
        }
        else if (requestCode == BIN_REQUEST) {
            theCheck = Integer.parseInt(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            if (theCheck == theBinNumber){
                showResult2();
            }
            else {
                showResult(1);
            }
            //Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_LONG).show();
        }
    }



    private void showResult(int i){
        //suite = 1;
        //getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        CardBuilder card = new CardBuilder(this,CardBuilder.Layout.TEXT);
        /*if (i==0){
            card.setText("Click to scan the bin N°" + theBinNumber );
            card.addImage(R.drawable.bin2);
        }
        else{
            card.setText("Wrong Bin, Please click to scan the bin N°" + theBinNumber );
        }*/
        card.setText("Put the item in the bin N°"+ theBinNumber);
        card.setFootnote("Click to scan a new item");
        //card.setText("Click to scan a new item");
        mView = card.getView();
        //mView.setTag("premier");
        mView.setOnClickListener(this);
        mView.setFocusable(true);
        mView.setFocusableInTouchMode(true);
        setContentView(mView);

    }

    private void showResult2(){
        //suite = 2;
        //getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        CardBuilder card = new CardBuilder(this,CardBuilder.Layout.CAPTION);
        card.setText("OK! Put the item in this bin and click to continue");
        //card.addImage(R.drawable.bin);
        mView2 = card.getView();
        //mView2.setTag("deuxieme");
        mView2.setOnClickListener(this);
        mView2.setFocusable(true);
        mView2.setFocusableInTouchMode(true);
        setContentView(mView2);
        

    }




   /* @Override
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
    }*/

    /*public boolean onCreatePanelMenu(int featureId, Menu menu){
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId ==  Window.FEATURE_OPTIONS_PANEL) {
            getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
            return true;

        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId ==  Window.FEATURE_OPTIONS_PANEL) {
            switch (item.getItemId()) {
                case R.id.next:
                    if (suite == 1){
                        launchScannerBin();
                    }
                    else{
                    launchScanner();
                }
                    break;
                case R.id.go_back:
                    Intent resultsIntent = new Intent(this, MainActivity.class);
                    startActivity(resultsIntent);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
*/


    @Override
    public void onClick(View v) {
        // perform desired action
        if (v == mView) {

            //launchScannerBin();
            launchScanner();
        }
        else if (v == mView2) {

            launchScanner();
        }
    }

}
