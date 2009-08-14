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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestRobot {

	/**
	 * @param args
	 */
	
	
	public static final byte MOVE = 1;
	public static final byte LEFT_CLICK_PRESSED = 2;
	public static final byte LEFT_CLICK_RELEASE = 3;
	
	public static final byte RIGHT_CLICK_PRESSED = 4;
	public static final byte RIGHT_CLICK_RELEASE = 5;
	public static final byte SCROLL_UP = 6;
	public static final byte SCROLL_DOWN = 7;
	
	public static void main(String[] args) {
	    Robot robot = null;
	    ServerSocket providerSocket = null;
		
	    try {
			robot = new Robot();
			providerSocket = new ServerSocket( 9000	, 20 );
	    } catch (AWTException e1) {
			
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while( true){


			try{
			
				providerSocket.setSoTimeout(30000);
				Socket connection = providerSocket.accept();
				System.out.println("Connected");
				ObjectInputStream in = new ObjectInputStream( connection.getInputStream());
				
				int last_x = 0;
				int last_y = 0;
				boolean first_ref = true;
				byte instruction;
				while( (instruction = in.readByte()) != 10 ){
			
					switch(instruction){
						case MOVE:
							short side_to_side = in.readShort();
							short up_down = in.readShort();
							//System.out.println(" s: " + side_to_side + " u: " + up_down );
							
						
							if( first_ref ){
								last_x = side_to_side;
								last_y =  up_down;
								first_ref = false;
							}else{
								
								int diff_x = side_to_side - last_x;  // for + sts ++ last, 
								int diff_y = up_down - last_y;
								long start = System.currentTimeMillis();
								
								while( diff_x  != 0 || diff_y != 0){
								
									//System.out.println( "x: " + last_x + "dx: " +  diff_x + " sts: " + side_to_side 
									//		+ "\n y: " + last_y + " df: " + diff_y + " up: " + up_down );
									
									if( diff_x > 0 ){
										last_x ++;
									}else if( diff_x < 0){
										last_x --;
									}
									if( diff_y > 0){
										last_y++;
									}else if( diff_y < 0){
										last_y--;
									}
									
									
									
									robot.mouseMove(last_x, last_y);
									
									
									
									diff_x = side_to_side - last_x;  // for + sts ++ last, 
									diff_y = up_down - last_y;
									
									long time = System.currentTimeMillis() - start;
									if( time > 30){
										break;
									}
									
								}
							}
							
							//robot.mouseMove(side_to_side, up_down);
							
							
						break;
						case LEFT_CLICK_PRESSED:
							robot.mousePress(InputEvent.BUTTON1_MASK);
							
						break;
						case LEFT_CLICK_RELEASE:
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
							break;
						case RIGHT_CLICK_PRESSED:
							robot.mousePress(InputEvent.BUTTON3_MASK);
							
						break;
						case RIGHT_CLICK_RELEASE:
							robot.mouseRelease(InputEvent.BUTTON3_MASK);
							break;
						case SCROLL_UP:
							robot.mouseWheel(-1);
						break;
						case SCROLL_DOWN:
							robot.mouseWheel(1);
						break;
					}
				}
				System.out.println("G1 disconnected mouse");
			
			} catch (IOException e) {
				System.out.println("Not Connected");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} 
		
		
		}
			
			
		
	   
		
		

	}

}













