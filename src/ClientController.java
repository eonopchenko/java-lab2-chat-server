import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController implements Runnable {
	
	Socket client;
	DataInputStream dis;
	DataOutputStream dos;
	
	@FXML
	private ListView lvUsers;
	
	@FXML
	private TextArea taChat;
	
	@FXML
	private TextField tfSend;

    @FXML
    private Button btnSend;
    
    @FXML
    void btnSendOnActionHandler(ActionEvent event) {
		try {
			dos.writeInt(ServerConstants.CHAT_MESSAGE);
			dos.writeUTF(tfSend.getText());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    
    public ClientController() {
		try {
			client = new Socket("localhost", 5000);
			dis = new DataInputStream(client.getInputStream());
			dos = new DataOutputStream(client.getOutputStream());
			
			Thread clientThread = new Thread(this);
			clientThread.start();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true)
		{
			try {
				int messageType = dis.readInt(); // receive a message from the server, determine message type based on an integer
				
				// decode message and process
				switch(messageType)
				{
					case ServerConstants.CHAT_BROADCAST:
						taChat.appendText(dis.readUTF()+"\n");
						break;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
