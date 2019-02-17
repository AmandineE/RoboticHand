package com.example.android.robotichand;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.R.id.closeButton;
import static android.R.id.list;

public class Main2Activity extends AppCompatActivity {
    private final String DEVICE_NAME="HC-05";
    private final String DEVICE_ADDRESS="98:D3:31:F6:1B:84";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button openButton, closeButton, startButton;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        startButton = (Button) findViewById(R.id.button_start);
        openButton = (Button) findViewById(R.id.button_open);
        closeButton = (Button) findViewById(R.id.button_close);

        setUiEnabled(false);
        Toast.makeText(Main2Activity.this, "Click on Begin connection to be connected", Toast.LENGTH_SHORT).show();

    }

    public void setUiEnabled(boolean bool)
    {
        startButton.setEnabled(!bool);
        openButton.setEnabled(bool);
        closeButton.setEnabled(bool);

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

    public void onClickStart(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
                Toast.makeText(Main2Activity.this, "Click on Open or Close to control the hand", Toast.LENGTH_SHORT).show();
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

    public void onClickOpen(View view) {
                String string = "open";
                string.concat("\n");
                try {
                    outputStream.write(string.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    public void onClickClose(View view) {
        String string = "close";
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onClickAngles(View view) {
        Intent intent =new Intent(Main2Activity.this, Main3Activity.class);
        startActivity(intent);

    }

}
