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
	String userName;
//	ArrayList<Users> users = new ArrayList<Users>();
	
//	private class Users {
//		ServerThread connectedClient;
//		String name;
//		public Users(ServerThread connectedClient, String name) {
//			this.connectedClient = connectedClient;
//			this.name = name;
//		}
//	}
	
	public ServerThread(Socket remoteClient, ServerController serverController, ArrayList<ServerThread> connectedClients)
	{
		this.userName = "";
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

						String name = dis.readUTF();
						serverController.getTextArea().appendText(name + " has joined the chat" + "\n");
						userName = name;


						for(ServerThread client: connectedClients)
						{
							for(ServerThread clientName: connectedClients) 
							{
							client.getDos().writeInt(ServerConstants.REGISTER_BROADCAST);
							client.getDos().writeUTF(clientName.getUserName());
							}
						}

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

	public String getUserName()
	{
		return userName;
	}
}
