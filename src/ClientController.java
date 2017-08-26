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
			dos.writeInt(0);
			dos.writeUTF(tfSend.getText());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	Socket client;
	DataInputStream dis;
	DataOutputStream dos;
    
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
		while(true) {
		}
	}
}
