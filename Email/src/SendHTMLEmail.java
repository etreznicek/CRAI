import sun.org.mozilla.javascript.internal.ast.WhileLoop;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;

public class SendHTMLEmail {
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    public static void main(String args[]) throws AddressException, MessagingException, IOException {
        SendHTMLEmail obj = new SendHTMLEmail();
        generateAndSendSourceURIEmail();
        generateAndSendDiskSpaceEmail();
        deleteOldUserEmailsFromStaticSourceURIFile();
        deleteOldUserEmailsFromStaticDiskSpaceFile();
        System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
    }

    public static void generateAndSendSourceURIEmail() throws AddressException, MessagingException, IOException {
        //Step1
        String csvFile = "/Users/hudl/Desktop/NewSourceURIUsers.csv";
        String staticFile = "/Users/hudl/Desktop/StaticSourceURIUsers.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";
        String line2 = "";
        String unformattedEmailAddress = "";
        String emailAddress = "";
        String csvSplitBy = ",";

        System.out.println("\n 1st ===> setup Mail Server Properties..");

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        System.out.println("Mail Server Properties have been setup successfully..");

        //Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);

        generateMailMessage.setSubject("Hudl Support | Source URI Error");

        String emailBody = "<table cellpadding=\"20\" style=\"background-color: #38434F; width: 100%;\" width=\"100%\">\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <img src=\"http://hudl-content.s3.amazonaws.com/mkt/hudl-light2.png\" height=\"50px;\" align=\"none\" style=\"height:50px;\">\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: bold;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            Hey Coach,\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            We noticed that you might be experiencing <span style=\"color: #F87620;\">difficulties in uploading film</span> with Hudl Mercury. You may see this eror in step two of Hudl Mercury when attempting to review the video.\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            Following the steps in this <a href=\"http://public.hudl.com/support/troubleshooting/source-uri-error/\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">tutorial</a> should resolve the problem.\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            If you ever need any help, contact us at <a href=\"mailto:support@hudl.com\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">support@hudl.com</a>.\n" +
                "            <br>\n" +
                "            <br>\n" +
                "            <span style=\"color: #F87620; font-weight: bold\">&ndash;</span> The Hudl Team\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>";

        generateMailMessage.setContent(emailBody, "text/html");

        br = new BufferedReader(new FileReader(csvFile));

        while ((line = br.readLine()) != null) {
            String[] username = line.split(csvSplitBy);
            int x = 0;

            unformattedEmailAddress = username[0];
            emailAddress = unformattedEmailAddress.substring(1,(unformattedEmailAddress.length() - 1));

            br2 = new BufferedReader(new FileReader(staticFile));

            while ((line2 = br2.readLine()) != null) {
                String[] staticUsername = line2.split(csvSplitBy);

                if (emailAddress.equals(staticUsername[0])) {
                    x = 1;
                    break;
                }
            }

            br2.close();

            if (x == 0) {
                System.out.println(emailAddress);

                if (!(emailAddress.equals("sernam"))) {
                    generateMailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
                    generateMailMessage.setSender(new InternetAddress("support@hudl.com"));
                    generateMailMessage.setFrom(new InternetAddress("support@hudl.com"));

                    //Step3
                    //System.out.println("\n\n 3rd ===> Get Session and Send mail");
                    Transport transport = getMailSession.getTransport("smtp");

                    // Enter your correct gmail UserID and Password (XXXApp Shah@gmail.com)
                    transport.connect("smtp.office365.com", "support@hudl.com", "");
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    transport.close();

                    //Step4
                    FileWriter fw = new FileWriter("/Users/anthonyduren/Documents/GitHub/CRAI/Email/src/StaticSourceURIUsers.csv", true);
                    Date date = new Date();
                    DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");

                    fw.append(emailAddress + "," + formatDate.format(date) + "," + "\n");

                    System.out.println("Wrote " + emailAddress + " to file on " + date);

                    fw.flush();
                    fw.close();
                }
            }
        }

        br.close();

        System.out.println("Done muthafucka");
    }

    public static void generateAndSendDiskSpaceEmail() throws AddressException, MessagingException, IOException {
        //Step1
        String csvFile = "/Users/hudl/Desktop/NewDiskSpaceUsers.csv";
        String staticFile = "/Users/hudl/Desktop/StaticDiskSpaceUsers.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";
        String line2 = "";
        String unformattedEmailAddress = "";
        String emailAddress = "";
        String csvSplitBy = ",";

        System.out.println("\n 1st ===> setup Mail Server Properties..");

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        System.out.println("Mail Server Properties have been setup successfully..");

        //Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);

        generateMailMessage.setSubject("Hudl Support | Not Enough Disk Space");

        String emailBody = "<table cellpadding=\"20\" style=\"background-color: #38434F; width: 100%;\" width=\"100%\">\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <img src=\"http://hudl-content.s3.amazonaws.com/mkt/hudl-light2.png\" height=\"50px;\" align=\"none\" style=\"height:50px;\">\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: bold;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            Hey Coach,\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            We noticed that you might be experiencing difficulties in uploading film related to <span style=\"color: #F87620;\">not enough space</span> on your hard-drive. Files should be deleted automatically after an upload is complete, but you can delete old video files if you encounter an error.\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            Following the steps in this <a href=\"http://public.hudl.com/support/troubleshooting/delete-mercury-temporary-project-files/\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">tutorial</a> should resolve the problem.\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.25;color: #E8E8E8;font-size: 15px;text-align: left;\">\n" +
                "            If you ever need any help, contact us at <a href=\"mailto:support@hudl.com\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">support@hudl.com</a>.\n" +
                "            <br>\n" +
                "            <br>\n" +
                "            <span style=\"color: #F87620; font-weight: bold\">&ndash;</span> The Hudl Team\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>";

        generateMailMessage.setContent(emailBody, "text/html");

        br = new BufferedReader(new FileReader(csvFile));

        while ((line = br.readLine()) != null) {
            String[] username = line.split(csvSplitBy);
            int x = 0;

            unformattedEmailAddress = username[0];
            emailAddress = unformattedEmailAddress.substring(1,(unformattedEmailAddress.length() - 1));

            br2 = new BufferedReader(new FileReader(staticFile));

            while ((line2 = br2.readLine()) != null) {
                String[] staticUsername = line2.split(csvSplitBy);

                if (emailAddress.equals(staticUsername[0])) {
                    x = 1;
                    break;
                }
            }

            br2.close();

            if (x == 0) {
                System.out.println(emailAddress);

                if(!(emailAddress.equals("sernam"))) {
                    generateMailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
                    generateMailMessage.setSender(new InternetAddress("support@hudl.com"));
                    generateMailMessage.setFrom(new InternetAddress("support@hudl.com"));

                    //Step3
                    //System.out.println("\n\n 3rd ===> Get Session and Send mail");
                    Transport transport = getMailSession.getTransport("smtp");

                    // Enter your correct gmail UserID and Password (XXXApp Shah@gmail.com)
                    transport.connect("smtp.office365.com", "support@hudl.com", "");
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    transport.close();

                    //Step4
                    FileWriter fw = new FileWriter("/Users/hudl/Desktop/StaticDiskSpaceUsers.csv", true);
                    Date date = new Date();
                    DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");

                    fw.append(emailAddress + "," + formatDate.format(date) + "," + "\n");

                    System.out.println("Wrote " + emailAddress + " to file on " + date);

                    fw.flush();
                    fw.close();
                }
            }
        }

        br.close();

        System.out.println("Done muthafucka");
    }

    public static void deleteOldUserEmailsFromStaticSourceURIFile() throws IOException{
        Scanner scanner = new Scanner(new File("/Users/hudl/Desktop/StaticSourceURIUsers.csv"));
        String string = "";
        String username = "";
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date date = null;
        Date oneWeekAgo = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");

        FileWriter fw = new FileWriter("/Users/hudl/Desktop/StaticSourceURIUsers.csv",true);

        scanner.useDelimiter(",");

        while(scanner.hasNext()){
            string = scanner.next();

            try{
                date = formatDate.parse(string);

                if(date.after(oneWeekAgo)){
                    fw.append(username + "," + string + ",");
                }
            }catch (java.text.ParseException e){
                username = string;
            }catch (NullPointerException e){

            }
        }

        fw.flush();
        fw.close();

        scanner.close();
    }

    public static void deleteOldUserEmailsFromStaticDiskSpaceFile() throws IOException{
        Scanner scanner = new Scanner(new File("/Users/hudl/Desktop/StaticDiskSpaceUsers.csv"));
        String string = "";
        String username = "";
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date date = null;
        Date oneWeekAgo = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");

        FileWriter fw = new FileWriter("/Users/hudl/Desktop/StaticDiskSpaceUsers.csv",true);

        scanner.useDelimiter(",");

        while(scanner.hasNext()){
            string = scanner.next();

            try{
                date = formatDate.parse(string);

                if(date.after(oneWeekAgo)){
                    fw.append(username + "," + string + ",");
                }
            }catch (java.text.ParseException e){
                username = string;
            }catch (NullPointerException e){

            }
        }

        fw.flush();
        fw.close();

        scanner.close();
    }
}