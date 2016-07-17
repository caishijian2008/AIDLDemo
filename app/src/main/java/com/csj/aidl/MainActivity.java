package com.csj.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNum;
    private Button btnQuery;
    private TextView tvShow;
    private IPerson iPerson;
    private PersonConnection conn = new PersonConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //绑定远程Service
        Intent intent = new Intent("android.intent.action.AIDLService");
        intent.setPackage("com.csj.aidlserverdemo");
        bindService(intent, conn, BIND_AUTO_CREATE);

        btnQuery.setOnClickListener(this);
    }

    private void initView() {
        etNum = (EditText) findViewById(R.id.etNum);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        tvShow = (TextView) findViewById(R.id.tvShow);
    }

    @Override
    public void onClick(View view) {
        String number = etNum.getText().toString();
        int num = Integer.valueOf(number);
        try {
            tvShow.setText(iPerson.queryPerson(num));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        etNum.setText("");
    }

    private final class PersonConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iPerson = IPerson.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iPerson = null;
        }
    }
}
