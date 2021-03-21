package com.ivan.rc.lab4.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BusinessLogic {
    private static final BusinessLogic instance = new BusinessLogic();
    /**
     * pattern "username: message"
     */
    private final List<String> messages = new LinkedList<>();

    private BusinessLogic() {
    }

    public static BusinessLogic getInstance() {
        return instance;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void processAction(String action, String data, String userId, PrintWriter pw, String aesKey) {
        switch (action) {
        case "SET_MESSAGE":
            setMessageEndpoint(userId, data, pw, aesKey);
            getMessages(pw, aesKey);
            break;
        case "GET_MESSAGES":
            getMessages(pw, aesKey);
            break;
        default:
            return;
        }
    }

    private void setMessageEndpoint(String userName, String message, PrintWriter pw, String aesKey) {
        String msg = String.format("%s: %s\n", String.valueOf(userName.hashCode()), message);
        this.messages.add(msg);

    }

    private void getMessages(PrintWriter pw, String aesKey) {
        String dataField = this.messages.stream().reduce("", (a, b) -> {
            if ("".equals(a)) {
                return a + b;
            }

            return a + "<separator>" + b;
        });
        AES256 aes = new AES256();

        pw.write(String.format("Data: %s\n", aes.encrypt(dataField, aesKey)));
    }
}
