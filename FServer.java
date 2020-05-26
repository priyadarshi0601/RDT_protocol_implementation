import java.net.*;
import java.io.*;
import java.util.*;
 
public class FServer {
 
	public static void main(String[] args) {
 
		DatagramSocket ss = null;
		FileInputStream fis = null;
		DatagramPacket rp, sp=null;
		byte[] rd, sd,sd1,last;

		InetAddress ip;
		int port;
		int count=0;
		
		try {
			ss = new DatagramSocket(Integer.parseInt(args[0]));
			System.out.println("Server is up....");
			ss.setSoTimeout(9*1000);


			// read file into buffer
			//fis = new FileInputStream("demoText.html");

			int consignment;
			String strConsignment;
			int result = 0; // number of bytes read
			String filename="";
	 
			while(true && result!=-1){
				count++;
	 
				rd=new byte[100];
				sd=new byte[512];
				sd1=new byte[520];
				last=new byte[20];
				 
				rp = new DatagramPacket(rd,rd.length);
				try {
				 
					ss.receive(rp);
					ip = rp.getAddress(); 
					port =rp.getPort();	
					strConsignment = new String(rp.getData());					
					if(count==1) {
						strConsignment=strConsignment.substring(7).split("\r")[0];
						System.out.println(strConsignment);
						filename=strConsignment;
						fis = new FileInputStream(strConsignment.trim());
						consignment=0;
					}
					else {
						//System.out.println(strConsignment);
						strConsignment=strConsignment.substring(3).split("\r")[0];
						consignment = Integer.parseInt(strConsignment.trim());
					}
				}
				catch(Exception e) {
					System.out.println("Resending the consignment #"+(count-2));
					ss.send(sp);
					ss.receive(rp);
					ip = rp.getAddress(); 
					port =rp.getPort();	
					strConsignment = new String(rp.getData());
					strConsignment=strConsignment.substring(3).split("\r")[0];
					consignment = Integer.parseInt(strConsignment.trim());
				}
				if(consignment!=0)
					System.out.println("Received ACK = " + consignment);

				// prepare data
				result = fis.read(sd);
				if (result == -1) {
					byte f1[]=new String("RDT").getBytes();
					consignment = -1;
					byte f4[]="-1".getBytes();
					byte f3[]=new String(" END").getBytes();
					byte f5[]=new String("/r/f").getBytes();
					last=concat(f1,f4,f3,f5);
					sp=new DatagramPacket(last,last.length,ip,port);
					
				}
				else {
					byte f1[]=new String("RDT ").getBytes();
					String f2=Integer.toString(consignment)+" ";
					byte f3[]=new String(" \r\f").getBytes();
					byte f4[]=f2.getBytes();
					sd1=concat(f1,f4,sd,f3);
					sp=new DatagramPacket(sd1,sd1.length,ip,port);
			
				}
				
				 
				
				
				if(count==3) {
					System.out.println("Forgot Consignment");
					continue;
					
				}
					
				 
				ss.send(sp);
				 
				rp=null;
				//sp = null;
				 
				System.out.println(" Send consignment #" + consignment+"\r\f");
				
	 
			}
			
		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
		
	}
	public static byte[] concat(byte[] a, byte[] b, byte[] c, byte[] d) {
        byte[] result = new byte[a.length + b.length + c.length + d.length]; 
        System.arraycopy(a, 0, result, 0, a.length); 
        System.arraycopy(b, 0, result, a.length, b.length);
        System.arraycopy(c, 0, result, a.length+b.length, c.length);
        System.arraycopy(d, 0, result, a.length+b.length+c.length, d.length);
        return result;
    }
	/*public static byte[] concat1(byte[] a, byte[] b, byte[] c, byte[] d, byte[] e) {
        byte[] result = new byte[a.length + b.length + c.length + d.length + e.length]; 
        System.arraycopy(a, 0, result, 0, a.length); 
        System.arraycopy(b, 0, result, a.length, b.length);
        System.arraycopy(c, 0, result, a.length+b.length, c.length);
        System.arraycopy(d, 0, result, a.length+b.length+c.length, d.length);
        System.arraycopy(e, 0, result, a.length+b.length+c.length+d.length, e.length);
        return result;
    }*/
}

