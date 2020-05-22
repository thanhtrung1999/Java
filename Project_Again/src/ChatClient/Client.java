/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread{
    
    private final String host;
    private final int port;
    private Socket socket;
    private OutputStream serverOut;
    private InputStream serverIn;
    private BufferedReader bufferedIn;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean acceptConnect(){
        try {
            this.socket = new Socket(this.getHost(), this.getPort());
            this.serverOut = socket.getOutputStream(); 
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean checkRegister(String username, String password) throws IOException{
        String cmd = "register " + username + " " + password + "\n";
        serverOut.write(cmd.getBytes());
        
        String response = bufferedIn.readLine();
        System.out.println("Response: " + response);
        
        if("Register success".equalsIgnoreCase(response)){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean checkLogin(String username, String password) throws IOException{
        String cmd = "login " + username + " " + password + "\n"; 
        serverOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response: " + response);

        if("Login success".equalsIgnoreCase(response)){
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }
    
    public void logoff() throws IOException {
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    private void startMessageReader() {
        Thread readMessage = new Thread() {
            @Override
            public void run() {
                String line;
                try {
                    while((line = bufferedIn.readLine()) != null){
                        
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        readMessage.start();
    }
}
