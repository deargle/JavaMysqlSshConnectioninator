package dbconnect;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Deargle
 */
public class PortForwarder {

    static String connectionString = ""; // username@hostname
    static String portsString = ""; // port:host:hostport
    static String password = ""; // ssh password here for 'username' here
    
    static Session session = null;

    public static int openConnection() {
        
        int assigned_port = -1;
        
        JSch jsch = new JSch();
        
        try {

            if (connectionString.isEmpty() || portsString.isEmpty() || password.isEmpty()) {
                throw new Exception("Set your connection settings in the PortForwarder class");
            }
            
            String user = connectionString.substring(0, connectionString.indexOf('@'));
            String host = connectionString.substring(connectionString.indexOf('@') + 1);

            int lport;
            String rhost;
            int rport;

            String[] portParams;


            portParams = portsString.split(":");

            lport = Integer.parseInt(portParams[0]);
            rhost = portParams[1];
            rport = Integer.parseInt(portParams[2]);

            session = jsch.getSession(user, host, 22);

            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            
            //UserInfo ui = new MyUserInfo();
            //session.setUserInfo(ui);
            
            session.connect();

            assigned_port = session.setPortForwardingL(lport, rhost, rport);
            
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        
        return assigned_port;
    }
    
    public static void closeConnection() {
        if (session != null) {
            session.disconnect();
        }
    }
    
    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        public String getPassword() {
            return passwd;
        }

        public boolean promptYesNo(String str) {
            Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null,
                    str,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo == 0;
        }
        String passwd;
        JTextField passwordField = (JTextField) new JPasswordField(20);

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            Object[] ob = {passwordField};
            int result =
                    JOptionPane.showConfirmDialog(null, ob, message,
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                passwd = passwordField.getText();
                return true;
            } else {
                return false;
            }
        }

        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);
        }
        final GridBagConstraints gbc =
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        public String[] promptKeyboardInteractive(String destination,
                String name,
                String instruction,
                String[] prompt,
                boolean[] echo) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }
    }
}
