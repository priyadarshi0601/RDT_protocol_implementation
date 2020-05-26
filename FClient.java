import java.net.*;
import java.io.*;
import java.util.*;
 
public class FClient {
 
	public static void main(String[] args) {
	 
	    DatagramSocket cs = null;
		FileOutputStream fos = null;

		try {

	    	cs = new DatagramSocket();
	 
			byte[] rd, sd;
			String reply="";
			DatagramPacket sp,rp=null;
			int count=0;
			boolean end = false;
			int flag=0;
			
			// write received data into demoText1.html
			fos = new FileOutputStream("demoText1.html");
			int ff=1;

			while(!end)
			{
			    String ack = "ACK" + count+"\r\f";
			    if(count==0 ) {
			    	ack="REQUESTdemoText.html\r\f";
			    }
			    
			    	  
				// send ACK  
			    rd=new byte[520];
				rp=new DatagramPacket(rd,rd.length); 
			   sd=ack.getBytes();	
			    sp=new DatagramPacket(sd,sd.length, 
									  InetAddress.getByName(args[0]),
  									  Integer.parseInt(args[1]));
			    
			   
			    
			    System.out.println("Request: "+count);
			    if(count==4 &&flag==0) {
			    	flag=1;
			    	count-=1;
			    	cs.receive(rp);
				}
			    else {
			    	cs.send(sp);
			    	cs.receive(rp);
			    }

				// get next consignment
				
				
				//cs.receive(rp);
			    String reply1=new String(rp.getData());
			    if(reply1.equals(reply)&&flag==1) {
			    	System.out.println("Dulpicate packet discarded");
			    	
			    	/*cs.send(sp);
			    	cs.receive(rp);*/			    	
			    }
			    else {
				// concat consignment 
				    reply=new String(rp.getData());
				    
				    //System.out.println(reply);
					fos.write(rp.getData());
			    }

				if (reply.trim().substring(0,5).equals("RDT-1")) // if last consignment
					end = true;

				count++;
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		} finally {

			try {
				if (fos != null)
					fos.close();
				if (cs != null)
					cs.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}