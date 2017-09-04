import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread
{
	DataInputStream dis;
	DataOutputStream dos;
	Socket remoteClient;	
	ServerController serverController;
	ArrayList<ServerThread> connectedClients;

	
	public ServerThread(Socket remoteClient, ServerController serverController, ArrayList<ServerThread> connectedClients)
	{
		this.remoteClient = remoteClient;
		this.connectedClients = connectedClients;
		try {
			this.dis = new DataInputStream(remoteClient.getInputStream());
			this.dos = new DataOutputStream(remoteClient.getOutputStream());
			this.serverController = serverController;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		while(true)
		{
			try {
				int mesgType = dis.readInt();
				System.err.println(mesgType);
				
				switch(mesgType)
				{
					case ServerConstants.CHAT_MESSAGE:
						String data = dis.readUTF();
						System.err.println(data);
						serverController.getTextArea().appendText(remoteClient.getInetAddress()+":"+remoteClient.getPort()+">"+data+"\n");
						
						for(ServerThread otherClient: connectedClients)
						{
							if(!otherClient.equals(this)) // don't send the message to the client that sent the message in the first place
							{
								otherClient.getDos().writeInt(ServerConstants.CHAT_BROADCAST);
								otherClient.getDos().writeUTF(data);
							}
						}
						
						break;
					case ServerConstants.REGISTER_CLIENT:
						// TODO develop code to handle new client registrations

						// TODO broadcast this registration to all other clients connected to the server (similar to the CHAT_BROADCAST message sent to each client above)
						
						break;	
					case ServerConstants.PRIVATE_MESSAGE:
						// TODO develop code to handle private messages sent by the client
						break;
				}				
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
		}
	}

	public DataOutputStream getDos() {
		return dos;
	}
}
