import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ServerMain extends Application {
	
	private TextArea messages = new TextArea();
	ServerSocket serverSocket;
	ArrayList <ServerThread> connectedClients = new ArrayList<ServerThread>();
	ConnectionThread connThread = new ConnectionThread();
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {		

		Pane root = new Pane(messages);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Server");
		stage.show();
		connThread.start();
	}

	private class ConnectionThread extends Thread {
		
		@Override
		public void run() 
		{			
			try {
				serverSocket = new ServerSocket(5000);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				while(true)
				{
					Socket remoteClient = serverSocket.accept(); 
									
					ServerThread st = new ServerThread(remoteClient, connectedClients);
					st.start();
					
					connectedClients.add(st);

				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	public void appendMessage(String text) {
		messages.appendText(text);
	}
	public void setSystemLog(TextArea systemLog) {
		systemLog = messages;
	}
}
