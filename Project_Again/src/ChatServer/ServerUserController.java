/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

public class ServerUserController extends Thread {
    private final Server server;
    private final Socket clientSocket;
    private OutputStream outputStream;
    private ServerReader sr;
    private String login = null;
    private String register = null;
    
    public ServerUserController(Server server, Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException ex) {
            Logger.getLogger(ServerUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String clientData; // "login " + username + " " + password + "\n";
        
        while ( (clientData = reader.readLine()) != null) {
            this.sr = new ServerReader();
            String[] tokens = StringUtils.split(clientData, null, 3);
            
            if(tokens != null && tokens.length > 0){
                String cmd = tokens[0];
                sr.setCommand(cmd);
                
                if("logoff".equalsIgnoreCase(sr.getCommand()) || "quit".equalsIgnoreCase(sr.getCommand())){
                    handleLogoff();
                    break;
                } else if("register".equalsIgnoreCase(sr.getCommand())){
                    handleRegister(outputStream, tokens);
                } else if("login".equalsIgnoreCase(sr.getCommand())){
                    handleLogin(outputStream, tokens);
                } else if("msg".equalsIgnoreCase(sr.getCommand())){
                    handleMessage(tokens);
                } else {
                    String msg = "error command: " + sr.getCommand() + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        
        clientSocket.close();
    }

    private void handleLogoff() throws IOException {
        server.removeUser(this);
        List<ServerUserController> usersList = server.getUsersList();

        // gửi đến users online khác trạng thái user hiện tại 
        String offlineMsg = "offline " + login + "\n";
        for(ServerUserController user : usersList) {
            if (!login.equals(user.getLogin())) {
                user.send(offlineMsg);
            }
        }
        clientSocket.close();
    }

    private String getLogin() {
        return login;
    }

    private void send(String msg) {
        if (login != null) {
            try {
                outputStream.write(msg.getBytes());
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException { 
        String username = tokens[1];
        String password = tokens[2];
        
        sr.setFlag1(username);
        sr.setFlag2(password);
        
        ServerStatement serverStatement = new ServerStatement();
        
        if(serverStatement.loginRecord(sr.getFlag1(), sr.getFlag2())){
            String msg = "Login success\n";
            outputStream.write(msg.getBytes());
            this.login = sr.getFlag1();
            System.out.println("User logged in succesfully: " + login);

            List<ServerUserController> usersList = server.getUsersList();

            // gửi đến user hiện tại các users online khác
            for(ServerUserController user : usersList) {
                if (user.getLogin() != null) {
                    if (!login.equals(user.getLogin())) {
                        String msg2 = "online " + user.getLogin() + "\n";
                        send(msg2);
                    }
                }
            }

            // gửi đến các users online khác trạng thái user hiện tại 
            String onlineMsg = "online " + login + "\n";
            for(ServerUserController user : usersList) {
                if (!login.equals(user.getLogin())) {
                    user.send(onlineMsg);
                }
            }
        } else {
            String msg = "Error login\n";
            outputStream.write(msg.getBytes());
            System.err.println("Login failed for " + sr.getFlag1());
        }
    }

    private void handleMessage(String[] tokens) {
        String sendTo = tokens[1];
        String body = tokens[2];
        
        sr.setFlag1(sendTo);
        sr.setFlag2(body);

        List<ServerUserController> usersList = server.getUsersList();
        
        if((sr.getFlag1()).equals("all")){
            for(ServerUserController user : usersList) {
                if (user.getLogin() != null) {
                    if (!login.equals(user.getLogin())) {
                        String outMsg = "Message from " + login + ": " + sr.getFlag2() + "\n";
                        user.send(outMsg);
                    }
                }
            }
        } else {
            for(ServerUserController user : usersList) {
                if (sendTo.equalsIgnoreCase(user.getLogin())) {
                    String outMsg = "Message from " + login + ": " + sr.getFlag2() + "\n";
                    user.send(outMsg);
                }
            }
        }
    }

    private void handleRegister(OutputStream outputStream, String[] tokens) throws IOException {
        String username = tokens[1];
        String password = tokens[2];
        
        sr.setFlag1(username);
        sr.setFlag2(password);
        
        ServerStatement serverStatement = new ServerStatement();
        
        if(serverStatement.registerRecord(sr.getFlag1(), sr.getFlag2()) == 1){
            String msg = "Register success\n";
            outputStream.write(msg.getBytes());
            this.register = sr.getFlag1();
            System.out.println("1 user with username " + register + " has been added to the database");
        } else {
            String msg = "Register failed, username already exists\n";
            outputStream.write(msg.getBytes());
        }
    }
    
}
