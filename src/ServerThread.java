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
	static ArrayList<ServerThread> connectedClients;
	
	ArrayList<Users> users = new ArrayList<Users>();
	
	private class Users {
		ServerThread connectedClient;
		String name;
		public Users(ServerThread connectedClient, String name) {
			this.connectedClient = connectedClient;
			this.name = name;
		}
	}

	
	public ServerThread(Socket remoteClient, ServerController serverController, ArrayList<ServerThread> connectedClients)
	{
		this.remoteClient = remoteClient;
		ServerThread.connectedClients = connectedClients;
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
						String name = dis.readUTF();
						System.err.println(name);
						serverController.getTextArea().appendText(name + " has joined the chat" + "\n");
						users.add(new Users(this, name));

						// TODO broadcast this registration to all other clients connected to the server (similar to the CHAT_BROADCAST message sent to each client above)
						for(ServerThread otherClient: connectedClients)
						{
							if(!otherClient.equals(this)) // don't send the message to the client that sent the message in the first place
							{
								otherClient.getDos().writeInt(ServerConstants.REGISTER_BROADCAST);
								otherClient.getDos().writeUTF(name);
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
}
