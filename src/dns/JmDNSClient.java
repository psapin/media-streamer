package dns;

import enums.RunType;
import gui.PeerTable;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.ServiceInfoImpl;

public class JmDNSClient implements Runnable {

    public final static String SERVICE_TYPE = "_streamer._tcp.local.";
    public String SERVICE_NAME = "Streamer";
    
    //device info to publish
    private String deviceName;
    private String deviceType;
    //for update/remove callbacks
    private PeerTable table;
    
    private JmDNS jmdns;
    
    private boolean publish;
    private boolean discover;
    
    //constructor, set some instance variables
    public JmDNSClient(PeerTable pTable, String dName, String dType) {    	
    	table = pTable;
    	deviceName = dName;
    	deviceType = dType;
    	publish = true;
    	discover = true;
    }
    
    //constructor, set some instance variables
    public JmDNSClient(PeerTable pTable) {    	
    	table = pTable;
    	publish = false;
    	discover = true;
    }

    //constructor to only publish
    public JmDNSClient(String dName, String dType) {
    	deviceName = dName;
    	deviceType = dType;
    	publish = true;
    	discover = false;
	}

	/* main thread function
     * runs when parent thread calls start()
     * if publish is true, registers a new dns service with the given info
     * if discover is true, creates a new service listener for SERVICE_TYPE
     * gently dismantles everything if thread is interrupted
     */
    public void run() {
        try {
        	Random random = new Random();
        	int id = random.nextInt(100000);
            SERVICE_NAME += "" + id;
            System.out.println("Opening JmDNS...");
            jmdns = JmDNS.create(InetAddress.getLocalHost(), SERVICE_NAME);
            System.out.println("Opened JmDNS!");
            
            if(publish) {
            	final HashMap<String, String> values = new HashMap<String, String>();
                values.put("name", deviceName);
                values.put("type", deviceType);
                
                System.out.println("Registering Service: " + SERVICE_NAME);
                ServiceInfoImpl service = new ServiceInfoImpl(SERVICE_TYPE, SERVICE_NAME, "", 80, 0, 0, true, values);
                jmdns.registerService(service);

                System.out.println("\nRegistered Service as " + service);
            }
            if(discover)
            	jmdns.addServiceListener(SERVICE_TYPE, new StreamerListener());
            
            
            //wait for thread to be interrupted, then shut down
            while (!Thread.interrupted()) { }
            System.out.println("Closing JmDNS...");
            jmdns.unregisterAllServices();
            jmdns.close();
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * StreamerListener - class to act as the service listener
     * does basic actions for adding, resolving, and removing data
     */
    class StreamerListener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service type added: " + event.getType());
            jmdns.requestServiceInfo(SERVICE_TYPE, event.getName());
        }
        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service type resolved: " + event.getType());
            ServiceInfoImpl si = (ServiceInfoImpl) event.getInfo();
            System.out.println(si.getName()+' '+Arrays.toString(si.getInetAddresses())+' '+si.getNiceTextString());
            table.addData(si.getPropertyString("name"), si.getPropertyString("type"), si.getName(), si.getInetAddresses()[0]);
        }
		@Override
		public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service type removed: " + event.getType());
			ServiceInfoImpl si = (ServiceInfoImpl) event.getInfo();
			table.removeData(si.getName());
		}
    }
}