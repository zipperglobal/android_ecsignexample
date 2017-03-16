package com.zipperglobal.signexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.ethereum.crypto.*;
import org.spongycastle.util.encoders.Hex;

public class MainActivity extends AppCompatActivity {
    protected ECKey m_key;

    public static byte[] hexStringToByteArray(String s) {
       int len = s.length();
       byte[] data = new byte[len / 2];
       for (int i = 0; i < len; i += 2) {
           data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                + Character.digit(s.charAt(i+1), 16));
       }
       return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

/*        byte[] message = "SOME MESSAGE TO SIGN".getBytes();
        byte[] hash = HashUtil.sha3(message);
*/
        // READ THIS: hex format hash from tx generator on server
        byte[] hash = hexStringToByteArray("58dda56ae68fbb638fe96605943ed2f3af0ba427d695e4b14aee078ca3664454");

        // GENERATE A NEW KEY (only needed once, serialize and store it)
        m_key = new ECKey();
        byte[] addr = m_key.getAddress();
        byte[] priv = m_key.getPrivKeyBytes();

        String addrStr = Hex.toHexString(addr);
        String privStr = Hex.toHexString(priv);
        // THE ETHEREUM ADDRESS FOR THE KEY
        System.out.println("ADDRESS: " + addrStr);


        System.out.println("PRIVATE KEY: " + privStr);

        ECKey.ECDSASignature sig = m_key.sign(hash);
        
        System.out.println(" SIGNATURE in hex: " + sig.toHex());
        // SEND THIS TO PAY-MY-GAS
        System.out.println(" v: 0x" + Integer.toHexString(sig.v & 0xFF));
        System.out.println(" r: 0x" + sig.r.toString(16));
        System.out.println(" s: 0x" + sig.s.toString(16));

        if(!m_key.verify(hash, sig))
        {
            System.out.println("MESSAGE VALIDATION FAILED!");
            return;
        }

        System.out.println("MESSAGE VALIDATED SUCCESSFULLY!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
