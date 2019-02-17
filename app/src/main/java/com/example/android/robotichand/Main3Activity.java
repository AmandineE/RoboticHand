package com.example.android.robotichand;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.R.id.closeButton;

public class Main3Activity extends AppCompatActivity {
    ListView mListView;
    ArrayList<Servo> servos;

    private final String DEVICE_NAME="HC-05";
    private final String DEVICE_ADDRESS="98:D3:31:F6:1B:84";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, beginButton;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        servos = new ArrayList<>();

        servos.add(new Servo("Servo1", 1, Color.parseColor("#FF80AB"), 20, 90));
        servos.add(new Servo("Servo2", 2, Color.parseColor("#90CAF9"), 15, 30));
        servos.add(new Servo("Servo3", 3, Color.parseColor("#FF80AB"), 30, 180));
        servos.add(new Servo("Servo4", 4, Color.parseColor("#90CAF9"), 12, 90));
        servos.add(new Servo("Servo5", 5, Color.parseColor("#FF80AB"), 58, 90));
        servos.add(new Servo("Servo6", 6, Color.parseColor("#90CAF9"), 86, 90));
        servos.add(new Servo("Servo7", 7, Color.parseColor("#FF80AB"), 15, 90));
        servos.add(new Servo("Servo8", 8, Color.parseColor("#90CAF9"), 45, 90));
        servos.add(new Servo("Servo9", 9, Color.parseColor("#FF80AB"), 20, 90));

        mListView = (ListView) findViewById(R.id.list_view);
        ListAdapter adapter = new ListAdapter(Main3Activity.this, servos);
        mListView.setAdapter(adapter);

        startButton = (Button) findViewById(R.id.button_start);
        beginButton = (Button) findViewById(R.id.button_begin);

        setUiEnabled(false);
        Toast.makeText(Main3Activity.this, "Cliquer sur Begin pour se connecter", Toast.LENGTH_SHORT).show();

    }

    public void setUiEnabled(boolean bool)
    {
        beginButton.setEnabled(!bool);
        startButton.setEnabled(bool);
    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                //if(iterator.getName().equals(DEVICE_NAME))
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return connected;
    }

    public void onClickBegin(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
                Toast.makeText(Main3Activity.this, "Changer les angles puis cliquer sur Start", Toast.LENGTH_SHORT).show();
            }

        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(),"Appuyer sur STOP pour continuer",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }

            }
        });

        thread.start();

    }

    public void onClickStart(View view) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i=0; i<9; i++){
            if (servos.get(i).getProgress() < 10){
                strings.add("" + i + "00" + servos.get(i).getProgress()+'\n');
                strings.get(i).concat("\n");
            }
            else if (servos.get(i).getProgress() < 100){
                strings.add("" + i + "0" + servos.get(i).getProgress()+'\n');
                strings.get(i).concat("\n");
            }
            else {
                strings.add("" + i + servos.get(i).getProgress()+'\n');
                strings.get(i).concat("\n");
            }
            try {
                outputStream.write(strings.get(i).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*public void onClickStart(View view) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i=0; i<2; i++){
            strings.add("" + i + servos.get(i).getProgress());
            Toast.makeText(Main3Activity.this, strings.get(i), Toast.LENGTH_SHORT).show();
        }
    }*/
}
