/**************************************************************************
 * Copyright (c) Jeremy Villalobos
 * RemoteMousePoiter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*****************************************************************************/
package com.fitandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AndroidMouse extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    Socket requestSocket;
	/** Called when the activity is first created. */
    
    float SideToSideRef;
    float UpDownRef;
    boolean Calibrate;
    
    //mouse action report constants
	public static final byte MOVE = 1;
	public static final byte LEFT_CLICK_PRESSED = 2;
	public static final byte LEFT_CLICK_RELEASE = 3;
	
	public static final byte RIGHT_CLICK_PRESSED = 4;
	public static final byte RIGHT_CLICK_RELEASE = 5;
	public static final byte SCROLL_UP = 6;
	public static final byte SCROLL_DOWN = 7;
	public static final byte DISCONNECT_SOCKET = 10;
	
	//menu constants
    static final int CALIBRATE = 1;
    static final int CONNECT = 2;
    static final int DISCONNECT = 3;
	
	long start_time;
	ObjectOutputStream out_s;
	
	public void getHostAddress(){
		TextView MultiOut = (TextView) this.findViewById(R.id.multi_out);
		try {
			InetAddress group;
			
			group = InetAddress.getByName("228.5.6.7");
			
			MulticastSocket s = new MulticastSocket(6789);
			s.joinGroup(group);
			
			byte[] buf = new byte[1000];
			 DatagramPacket recv = new DatagramPacket(buf, buf.length);
			 s.receive(recv);
			 String str = new String( buf);
			
			
			MultiOut.setText("got this: " + str);
				 
		} catch (UnknownHostException e) {
			MultiOut.setText( this.getStringFromErrorStack(e) );
			e.printStackTrace();
		} catch (IOException e) {
			MultiOut.setText( this.getStringFromErrorStack(e) );
			e.printStackTrace();
		}
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        TextView out = (TextView) findViewById(R.id.out);
        
        
        out.setText("test");
        
        //getHostAddress();
        
        start_time = System.currentTimeMillis();
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        
        List<Sensor> orientation =  mSensorManager.getSensorList( Sensor.TYPE_ORIENTATION);
        
        if( orientation.size() >= 1){
        
        	mSensorManager.registerListener(this, orientation.get(0), SensorManager.SENSOR_DELAY_FASTEST);
        }else{
        	out.setText("No Orientation Sensor");
        }
        //r.registerListener( this, SensorManager.SENSOR_ACCELEROMETER | SensorManager.SENSOR_DELAY_FASTEST);
        
        requestSocket = null;
        out_s = null;
        Calibrate = true;
        try {
			connect();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        /*
        Button calibrate = (Button) this.findViewById(R.id.calibrate);
        calibrate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Calibrate = true;
            }
        });
        
        Button MinusButton = (Button) this.findViewById(R.id.retry);
        MinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
                
                TextView out2 = (TextView) findViewById(R.id.out2);
        		
                try {
        				 
        			 
        			requestSocket = new Socket( "192.168.0.100", 9000);
        		
        			out_s = new ObjectOutputStream(requestSocket.getOutputStream());
        			
        
        			out2.setText("Connected");
        		
        		} catch (UnknownHostException e) {
        			out2.append("ERR: unknown host");
        			e.printStackTrace();
        		} catch (IOException e) {
        			out2.append("ERR: did not connect" + AndroidMouse.getStringFromErrorStack(e));
        			e.printStackTrace();
        		}
            }
        } );
        */
        Button LeftClick = (Button) this.findViewById(R.id.left_click);
        LeftClick.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try{
					if( out_s != null){
						 switch (event.getAction()) {
			                case MotionEvent.ACTION_DOWN:
			                    out_s.writeByte( AndroidMouse.LEFT_CLICK_PRESSED);
	        					out_s.flush();
			                break;
			                case MotionEvent.ACTION_UP:
			                    out_s.writeByte(AndroidMouse.LEFT_CLICK_RELEASE);
			                    out_s.flush();
			                break;
				         }
						
						
					}
				}catch( IOException e){
					
				}
				return false;
			}
		});
        /*;.setOnClickListener.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if( out_s != null){
					try {
						out_s.writeByte(LEFT_CLICK);
						out_s.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			
		});*/
        
        Button RightClick = (Button) this.findViewById(R.id.right_click);
        RightClick.setOnTouchListener(new View.OnTouchListener() {
			
        	
        	

			/*@Override
			public void onClick(View v) {
				if( out_s != null){
					try {
						out_s.writeByte(RIGHT_CLICK);
						out_s.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}*/

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try{
					if( out_s != null){
						 switch (event.getAction()) {
			                case MotionEvent.ACTION_DOWN:
			                    out_s.writeByte( AndroidMouse.RIGHT_CLICK_PRESSED);
		   					    out_s.flush();
			                break;
			                case MotionEvent.ACTION_UP:
			                    out_s.writeByte(AndroidMouse.RIGHT_CLICK_RELEASE);
			                    out_s.flush();
			                break;
				         }
					}
				}catch( IOException e){
					
				}
				return false;
			}
			
			
		});
        
    
        
     
        TextView scroll = (TextView) this.findViewById(R.id.scroll);
        scroll.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x = event.getX();
	            float y = event.getY();
	            
	            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ScrollYRef = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                	 TextView out2 = (TextView) findViewById(R.id.out2);
                    if( y < ScrollYRef - scroll_tol ){
                    	if(out_s != null){
                    		try {
                    			/*backwards make sence for internet browsing*/
								out_s.writeByte(AndroidMouse.SCROLL_DOWN);
								out_s.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                    		
                    		out2.setText("UP");
                    	}
                    }else if( y > ScrollYRef + scroll_tol){
                    	try {
							out_s.writeByte(AndroidMouse.SCROLL_UP);
							out_s.flush();
						} catch (IOException e) {
							
						}
                    	out2.setText("DOWN");
                    }
                    ScrollYRef = y;
                    break;
                case MotionEvent.ACTION_UP:
                    //nothing
                    break;
	            }
	            return true;
				
			}
			
		});
        
    }
    static final float scroll_tol = 3;
    float ScrollYRef ;
    
    

    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		TextView out2 = (TextView) findViewById(R.id.out2);
		switch( item.getItemId()){
		case CONNECT:
             try {
     			connect();
     		} catch (UnknownHostException e) {
     			out2.append("ERR: unknown host");
     			e.printStackTrace();
     		} catch (IOException e) {
     			out2.append("ERR: did not connect" + AndroidMouse.getStringFromErrorStack(e));
     			e.printStackTrace();
     		}
			break;
		case DISCONNECT:
			if( !disconnect()){
				out2.append("ERR rough disconnect");
			}
			break;
		case CALIBRATE:
			Calibrate = true;
			break;
		}
		return true;
	}

	public void connect() throws UnknownHostException, IOException{
			requestSocket = new Socket( "192.168.0.100", 9000);
			out_s = new ObjectOutputStream(requestSocket.getOutputStream());
	}
	
	@Override
	protected void onRestart() {
		requestSocket = null;
		out_s = null;
		Calibrate = true;
		
		 mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	        
	        
        List<Sensor> orientation =  mSensorManager.getSensorList( Sensor.TYPE_ORIENTATION);
        
        if( orientation.size() >= 1){
        
        	mSensorManager.registerListener(this, orientation.get(0), SensorManager.SENSOR_DELAY_FASTEST);
        }
		
		try {
			connect();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onRestart();
	}

	public boolean disconnect(){
		try {
			if( out_s != null){
				out_s.writeByte(this.DISCONNECT_SOCKET);
			
				out_s.flush();
				
				out_s.close();
				requestSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			this.requestSocket = null;
			out_s = null;
		}
		return true;
	}

	@Override
	protected void onStop() {
		
		this.mSensorManager.unregisterListener((SensorEventListener)this);
		disconnect();
		
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, CONNECT, 1, R.string.connect);
		menu.add(0, DISCONNECT, 1, R.string.disconnect);
		menu.add(0, CALIBRATE, 1, R.string.calibrate);
		return true;
	}

	public static String getStringFromErrorStack(Exception e){
		StackTraceElement[] error_stack = e.getStackTrace();
		String err =  e.getMessage() + " Exception: " + e.getClass().getName() ; 
				
		for(int i = 0; i < error_stack.length; i++){
			err += error_stack[i] +"\n";
		}
		return err;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
		
	}

	int screen_x = 900;
	int screen_y = 600;
	public final float tolerance = 2.8f;
	int bias_ref = 0;
	@Override
	public void onSensorChanged(SensorEvent event) {
		//Sensor s = event.sensor;
		//setContentView(R.layout.main);
	        
		TextView out = (TextView) findViewById(R.id.out);
		TextView out2 = (TextView) findViewById(R.id.out2);
		String str = "hello";
		
		float[] nums = event.values;
		//first number is compas 
		//second number is vertical up and down
		//third number is side to side
		for( int i = 0; i < nums.length; i++){
			str += " " + i  + " val: "  + nums[i] ;
		}
		
		if( Calibrate ){
			this.SideToSideRef = (short)(nums[0]);
			this.UpDownRef = (short)nums[1];
			Calibrate = false;
		}
		bias_ref++;
		long check_time = System.currentTimeMillis();
		float t = (float) bias_ref / ( (float)( check_time- start_time) / 1000.0f);
		out2.setText( "read/sec: " + t );
		if( out_s != null ){
			
			try {
				int M = 3;
				boolean sleep_v = false;
				boolean sleep_h = false;
				out_s.writeByte(MOVE);
				short side_to_side = (short)((nums[0]));
				short up_down = (short)( (nums[1] ));
				
				 
				if( side_to_side < SideToSideRef - tolerance){
					float diff = Math.abs(side_to_side - SideToSideRef)/ tolerance;
					if(Math.round(diff * M) < 2.0){
						sleep_h = true;
					}
					screen_x -= Math.round(diff );
					
					if(screen_x < -2000){
						screen_x = -2000;
					}
					/*if(sleep_h){
						if( bias_ref % precision_friction == 0){
							SideToSideRef --;
						}
					}else if(bias_ref % friction == 0){
						SideToSideRef --;
					}*/
				}else
				if( side_to_side > SideToSideRef + tolerance){
					float diff = Math.abs(side_to_side - SideToSideRef)/ tolerance;
					if(Math.round(diff * M) < 2.0){
						sleep_h = true;
					}
					screen_x += Math.round(diff);
					if(screen_x > 2000){
						screen_x = 2000;
					}
					/*if(sleep_h){
						if( bias_ref % precision_friction == 0){
							SideToSideRef ++;
						}
					}else if(bias_ref % friction ==0){
						SideToSideRef ++;
					}*/
				}
				if( up_down < UpDownRef - tolerance){
					float diff = Math.abs(up_down - UpDownRef) / tolerance;
					screen_y -= Math.round(diff);
					if(Math.round(diff * M) < 2.0){
						sleep_v = true;
					}
					if(screen_y < 0 ){
						screen_y = 0;
					}
					
				}else
				if( up_down > UpDownRef + tolerance){
					float diff = Math.abs(up_down - UpDownRef) / tolerance;
					screen_y += Math.round(diff);
					if(Math.round(diff * M) < 2.0){
						sleep_v = true;
					}
					if( screen_y > 1600){
						screen_y = 1600;
					}
					/*if(sleep_v){
						if( bias_ref % precision_friction == 0){
							UpDownRef ++;
						}
					}else if(bias_ref % friction ==0){
						UpDownRef ++;
					}*/
				}
				
				
				out_s.writeShort(screen_x);
				out_s.writeShort(screen_y);
				out_s.flush();
				/*try {
					if(sleep_v || sleep_h){
					//	Thread.sleep(200);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			} catch (IOException e) {
				out2.setText(this.getStringFromErrorStack(e));
				out_s = null;
				e.printStackTrace();
			}
			
			
		}
		
		
		out.setText(str);
	}

	
}