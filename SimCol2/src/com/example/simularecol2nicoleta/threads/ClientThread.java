package com.example.simularecol2nicoleta.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.simularecol2nicoleta.general.Constants;
import com.example.simularecol2nicoleta.general.Utilities;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {

	private String address;
	private int port;
	private String city;
	private String informationType;
	
	 private TextView weatherForecastTextView;
	 
	 private Socket socket;

	    public ClientThread(
	            String address,
	            int port,
	            String city,
	            String informationType,
	            TextView weatherForecastTextView) {
	        this.address = address;
	        this.port = port;
	        this.city = city;
	        this.informationType = informationType;
	        this.weatherForecastTextView = weatherForecastTextView;
	    }
	    
	    
	    @Override
	    public void run() {
	        try {
	            socket = new Socket(address, port);
	            if (socket == null) {
	                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
	            }

	            BufferedReader bufferedReader = Utilities.getReader(socket);
	            PrintWriter printWriter = Utilities.getWriter(socket);
	            if (bufferedReader != null && printWriter != null) {
	                printWriter.println(city);
	                //trimit datele
	                printWriter.flush();
	                printWriter.println(informationType);
	                printWriter.flush();
	                //din nou trimit datele
	                String weatherInformation;
	                while ((weatherInformation = bufferedReader.readLine()) != null) {
	                    final String finalizedWeatherInformation = weatherInformation;
	                    weatherForecastTextView.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            weatherForecastTextView.append(finalizedWeatherInformation + "\n");
	                        }
	                    });
	                }
	            } else {
	                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
	            }
	            socket.close();
	        } catch (IOException ioException) {
	            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
	            if (Constants.DEBUG) {
	                ioException.printStackTrace();
	            }
	        }
	    }

}
