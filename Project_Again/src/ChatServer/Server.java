/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{
    private int port;
    private List<ServerUserController> usersList = new ArrayList<ServerUserController>();

    public Server(int port) {
        this.port = port;
    }
    
    public List<ServerUserController> getUsersList(){
        return usersList;
    }
    
    public void removeUser(ServerUserController user) {
        usersList.remove(user);
    }
    
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            while(true){
                System.out.println("Server is ready...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection form " + clientSocket);
                
                ServerUserController user = new ServerUserController(this, clientSocket);
                usersList.add(user);
                user.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Server server = new Server(7777);
        server.start();
    }
}
