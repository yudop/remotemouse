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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
/**
 * Test application to multicast to G1.  It does not work.  The bug has being
 * documented on the android forums
 * @author jfvillal
 *
 */
public class BCastMyIP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 InetAddress group;
			
			try {
				group = InetAddress.getByName( "228.5.6.7");
			
		
		
				 MulticastSocket s = new MulticastSocket(6789);
				 s.joinGroup(group);
				 
				 for(int i = 0; i < 100; i++){
					 	String msg = "hell";
						 DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
						                             group, 6789);
						 s.send(hi);
						 System.out.println("sending one");
						 try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Thread.sleep(1000);
				 }
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
