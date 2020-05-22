/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;


public class ServerReader {
    private String command, flag1, flag2;

    public ServerReader() {
    }

    public ServerReader(String command, String flag1, String flag2) {
        this.command = command;
        this.flag1 = flag1;
        this.flag2 = flag2;
    }
    
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFlag1() {
        return flag1;
    }

    public void setFlag1(String flag1) {
        this.flag1 = flag1;
    }

    public String getFlag2() {
        return flag2;
    }

    public void setFlag2(String flag2) {
        this.flag2 = flag2;
    }
    
//    public JSONObject toJson() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("command", this.getCommand());
//        jsonObject.put("flag1", this.getFlag1());
//        jsonObject.put("flag2", this.getFlag2());
//        return jsonObject;
//    }
    
//    public static void main(String[] args) {
//        ServerReader serverReader = new ServerReader("message", "admin", "hi how are you");
//        
//        JSONObject jsonObject = (JSONObject) serverReader.toJson();
//        
//        System.out.println(jsonObject);
// 
//        String cmd = (String) jsonObject.get("command");
//        String flag1 = (String) jsonObject.get("flag1");
//        String flag2 = (String) jsonObject.get("flag2");
//        System.out.println("command: " + cmd);
//        System.out.println("flag1: " + flag1);
//        System.out.println("flag2: " + flag2);
//    }
}
