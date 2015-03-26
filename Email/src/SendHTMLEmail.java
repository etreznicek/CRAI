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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SendHTMLEmail
{
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

            public static void main(String args[]) throws AddressException, MessagingException, IOException {
                SendHTMLEmail obj = new SendHTMLEmail();
                String[] username = obj.run();
                generateAndSendEmail(username);
                System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
            }

            public static String [] run() throws IOException {

                String csvFile = "/Users/hudl/Desktop/usertest.csv";
                BufferedReader br = null;
                String line = null;
                String cvsSplitBy = ",";

                CSVReader reader = new CSVReader(new FileReader(csvFile));
                List username = reader.readAll();


                br.close();
                return username;
            }

            public static void generateAndSendEmail(String[] username) throws AddressException, MessagingException {

//Step1
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

                //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("anthony.duren@hudl.com"));
                generateMailMessage.setSubject("Hudl Support | Not Enough Disk Space");
                String emailBody = "<table cellpadding=\"20\" style=\"background-color: #38434F; width: 100%;\" width=\"100%\">\n" +
                        "    <tr>\n" +
                        "        <td>\n" +
                        "            <img src=\"http://hudl-content.s3.amazonaws.com/mkt/hudl-light2.png\" align=\"none\" style=\"height:50px;\">\n" +
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

                int count = 0;
                System.out.println(username.length);
                do {

                    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(username[count]));
//Step3
                    //System.out.println("\n\n 3rd ===> Get Session and Send mail");
                    Transport transport = getMailSession.getTransport("smtp");

                    // Enter your correct gmail UserID and Password (XXXApp Shah@gmail.com)
                    transport.connect("smtp.gmail.com", "etreznicek@gmail.com", "ASUN15119");
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    transport.close();
                    System.out.println(username[count]);
                    count++;
                } while (count < username.length);

            }

    }