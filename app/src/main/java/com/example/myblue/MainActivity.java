package com.example.myblue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    BluetoothAdapter bluetoothAdapter;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView = findViewById(R.id.listViewId);
        arrayList = new ArrayList();

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

//        findPairedDevices();
    }

    private void findPairedDevices() {

        int index = 0;
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        String[] str = new String[bluetoothDevices.size()];

        if(bluetoothDevices.size()>0){

            for(BluetoothDevice device: bluetoothDevices){

                str[index] = device.getName();
                index++;

            }

            arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, str);
            listView.setAdapter(arrayAdapter);

        }
    }

    public void discoverDevices(View v) {

        bluetoothAdapter.startDiscovery();

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();

            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    public void onClick(View view) {



        switch (view.getId()){

            case R.id.onButtonId:
                if(bluetoothAdapter == null){

                    Toast.makeText(getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();

                }
                else {

                    if(!bluetoothAdapter.isEnabled()){

                        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(i, 1);

                    }

                }

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == 1) {
            if(requestCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Blutooth is enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
