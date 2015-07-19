package ua.com.it_st.ordersmanagers.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class GetFileList {

    static Properties props;

    public static void main(String[] args) {

        GetFileList getMyFiles = new GetFileList();
        if (args.length < 1) {
            System.err.println("Usage: java " + getMyFiles.getClass().getName() +
                    " Properties_file");
            System.exit(1);
        }

        String propertiesFile = args[0].trim();
        getMyFiles.startFTP(propertiesFile);

    }

    public boolean startFTP(String propertiesFile) {

        props = new Properties();

        try {

            props.load(new FileInputStream("properties/" + propertiesFile));

            String serverAddress = props.getProperty("serverAddress").trim();
            String userId = props.getProperty("userId").trim();
            String password = props.getProperty("password").trim();
            String remoteDirectory = props.getProperty("remoteDirectory").trim();
            String localDirectory = props.getProperty("localDirectory").trim();

            //new ftp client
            FTPClient ftp = new FTPClient();
            //try to connect
            ftp.connect(serverAddress);
            //login to server
            if (!ftp.login(userId, password)) {
                ftp.logout();
                return false;
            }
            int reply = ftp.getReplyCode();
            //FTPReply stores a set of constants for FTP reply codes.
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            }

            //enter passive mode
            ftp.enterLocalPassiveMode();
            //get system name
            System.out.println("Remote system is " + ftp.getSystemType());
            //change current directory
            ftp.changeWorkingDirectory(remoteDirectory);
            System.out.println("Current directory is " + ftp.printWorkingDirectory());

            //get list of filenames
            FTPFile[] ftpFiles = ftp.listFiles();

            if (ftpFiles != null && ftpFiles.length > 0) {
                //loop thru files
                for (FTPFile file : ftpFiles) {
                    if (!file.isFile()) {
                        continue;
                    }
                    System.out.println("File is " + file.getName());


                    //get output stream
                    OutputStream output;
                    output = new FileOutputStream(localDirectory + "/" + file.getName());
                    //get the file from the remote system
                    ftp.retrieveFile(file.getName(), output);
                    //close output stream
                    output.close();

                    //delete the file
                    ftp.deleteFile(file.getName());

                }
            }

            ftp.logout();
            ftp.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}