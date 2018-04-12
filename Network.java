import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class Network {

	public void recieve(int port) {
		try {
			@SuppressWarnings("resource")
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[65507];

			System.out.printf("Listening on udp:%s:%d%n",
			InetAddress.getLocalHost().getHostAddress(), port);     
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			while(true){
				serverSocket.receive(receivePacket);
				String sentence = new String( receivePacket.getData(), 0,
						receivePacket.getLength() );
				System.out.println("RECEIVED:" + sentence);   
				System.out.println(sentence.length());
				InetAddress IPAddress = receivePacket.getAddress();
				System.out.println("Address: " + IPAddress);
			}
		} catch (IOException e) {
			System.out.println(e);
		}   
	}

	public static List<InetAddress> availableInterfaces() {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			System.out.println(e);
		}
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			try {
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}
			} catch (SocketException e) {
				System.out.println(e);
			}
			networkInterface.getInterfaceAddresses().stream() 
			.map(a -> a.getBroadcast())
			.filter(Objects::nonNull)
			.forEach(broadcastList::add);
		}
		return broadcastList;
	}
}
