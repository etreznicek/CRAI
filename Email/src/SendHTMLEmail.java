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
        generateAndSendMacMercEmail();
        //deleteOldUserEmailsFromStaticSourceURIFile();
        //deleteOldUserEmailsFromStaticDiskSpaceFile();
        //System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
    }

    public static void generateAndSendSourceURIEmail() throws AddressException, MessagingException, IOException {
        //Step1
        String csvFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\NewSourceURIUsers.csv";
        String staticFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\StaticSourceURIUsers.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";
        String line2 = "";
        //String unformattedEmailAddress = "";
        String emailAddress = "";
        String csvSplitBy = ",";

        System.out.println("1st ===> Setup Mail Server Properties..");

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        System.out.println("1st ===> Mail Server Properties have been setup successfully..");

        //Step2
        System.out.println("2nd ===> get Mail Session..");

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
                "            Following the steps in this <a href=\"http://goo.gl/euzfDL\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">tutorial</a> should resolve the problem.\n" +
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
        generateMailMessage.setHeader("X-MC-TRACK", "opens, clicks_all");

        br = new BufferedReader(new FileReader(csvFile));

        while ((line = br.readLine()) != null) {
            String[] username = line.split(csvSplitBy);
            int x = 0;

            //unformattedEmailAddress = username[0];
            //emailAddress = unformattedEmailAddress.substring(1,(unformattedEmailAddress.length() - 1));
            emailAddress = username[0];

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
                    transport.connect("smtp.mandrillapp.com", "eric.reznicek@hudl.com", "i39DyhJSdEQYWO9NktcEpQ");
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    transport.close();

                    //Step4
                    FileWriter fw = new FileWriter(staticFile, true);
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

        System.out.println("Email Batch Complete, MuthaFucka.");
    }

    public static void generateAndSendDiskSpaceEmail() throws AddressException, MessagingException, IOException {
        //Step1
        String csvFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\NewDiskSpaceUsers.csv";
        String staticFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\StaticDiskSpaceUsers.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";
        String line2 = "";
        //String unformattedEmailAddress = "";
        String emailAddress = "";
        String csvSplitBy = ",";

        System.out.println("1st ===> Setup Mail Server Properties..");

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        System.out.println("1st ===> Mail Server Properties have been setup successfully..");

        //Step2
        System.out.println("2nd ===> get Mail Session..");

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
                "            Following the steps in this <a href=\"http://goo.gl/JP5sHw\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">tutorial</a> should resolve the problem.\n" +
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

            //unformattedEmailAddress = username[0];
            //emailAddress = unformattedEmailAddress.substring(1,(unformattedEmailAddress.length() - 1));
            emailAddress = username[0];

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
                    transport.connect("smtp.office365.com", "support@hudl.com", "Hudd1ePassw0rd");
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    transport.close();

                    //Step4
                    FileWriter fw = new FileWriter(staticFile, true);
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

        System.out.println("Email Batch Complete, MuthaFucka.");
    }

    public static void deleteOldUserEmailsFromStaticSourceURIFile() throws IOException{
        Scanner scanner = null;
        FileWriter fw = null;
        String string = "";
        String username = "";
        String staticFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\StaticSourceURIUsers.csv";
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date date = null;
        Date oneWeekAgo = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");

        fw = new FileWriter(staticFile,true);
        scanner = new Scanner(new File(staticFile));
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

    public static void generateAndSendMacMercEmail() throws AddressException, MessagingException, IOException {
        //Step1
        String csvFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\NewMacMercUsers.csv";
        String staticFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\StaticMacMercUsers.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";
        String line2 = "";
        //String unformattedEmailAddress = "";
        String emailAddress = "";
        String csvSplitBy = ",";

        System.out.println("1st ===> Setup Mail Server Properties..");

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        System.out.println("1st ===> Mail Server Properties have been setup successfully..");

        //Step2
        System.out.println("2nd ===> get Mail Session..");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);

        generateMailMessage.setSubject("Hudl Support | Outdated Mac Mercury");

        String emailBody = "<table cellpadding=\"15\" style=\"background-color: #414d58; width: 100%;\" width=\"100%\">" +
        "<tr>" +
        "<td>" +
        "<img src=\"https://gallery.mailchimp.com/ede0e578e4e69ca463770569b/images/2f4b6766-26b7-4706-af08-ee8d8248f007.jpg\" align=\"none\" style=\"height:58px;\">" +
        "</td>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: bold;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
                "Hey Coach," +
        "</td>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
                "We noticed that you may be uploading on an <span style=\"color: #F87620; font-weight: bold;\">outdated version of Hudl Mercury</span> on your Mac computer." +
                "</td>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
                "If your Mac is on version 10.7.5 or lower, please disregard this email. Don't know how to check what version you are on? Check out this tutorial <a href=\"https://support.apple.com/en-us/HT201260\" style=\"color:#F87620; font-weight: bold;\">Here.</a>" +
                "</td>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
                "If you are on a version of Mac OSX later than 10.7.5, you can install the new version of Mac Mercury by first going to your Applications folder on your computer, and moving Hudl Mercury to your trash can. Make sure to empty your trash when this is done!" +
                "</td>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
                "To install the new version of Hudl Mercury, you can follow <a href=\"http://public.hudl.com/support/getting-video-online/mercury-for-mac/getting-started-with-mercury-for-mac/\" style=\"color:#F87620; font-weight: bold;\">This Tutorial</a> to get the job done!" +
                "</td>" +
        "</tr>" +
        "<tr>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
                "If you ever need any help, contact us at <a href=\"mailto:support@hudl.com\" style=\"color: #F87620; font-weight: bold; transition: 0.2s;\">support@hudl.com</a>." +
        "</td>" +
        "</tr>" +
        "<tr>" +
        "<td style=\"font-family:'arial','helvetica neue','helvetica',sans-serif;font-weight: normal;line-height: 1.6;color: #E8E8E8;font-size: 15px;text-align: left;\">" +
        "<span style=\"color: #F87620; font-weight: bold\">-</span> The Hudl Team" +
        "</td>" +
        "</tr>" +
        "</table>";

        generateMailMessage.setContent(emailBody, "text/html");
        generateMailMessage.setHeader("X-MC-TRACK", "opens, clicks_all");

        br = new BufferedReader(new FileReader(csvFile));

        while ((line = br.readLine()) != null) {
            String[] username = line.split(csvSplitBy);
            int x = 0;

            //unformattedEmailAddress = username[0];
            //emailAddress = unformattedEmailAddress.substring(1,(unformattedEmailAddress.length() - 1));
            emailAddress = username[0];

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
                    transport.connect("smtp.mandrillapp.com", "eric.reznicek@hudl.com", "7Blsv_Y-lK1rT_tia-zxhA");
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    transport.close();

                    //Step4
                    FileWriter fw = new FileWriter(staticFile, true);
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

        System.out.println("Email Batch Complete, MuthaFucka.");
    }

    public static void deleteOldUserEmailsFromStaticDiskSpaceFile() throws IOException{
        Scanner scanner = null;
        FileWriter fw = null;
        String string = "";
        String username = "";
        String staticFile = "C:\\Users\\eric.reznicek\\Documents\\Support_Projects\\PROJECT_CRAI\\StaticDiskSpaceUsers.csv";
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date date = null;
        Date oneWeekAgo = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        DateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");

        fw = new FileWriter(staticFile,true);
        scanner = new Scanner(new File(staticFile));
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