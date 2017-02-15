package info.bhtours.bhtours.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
import java.util.Properties;

/**
 * Created by mcerkic on 25.2.2016..
 */
public class MailSender {
    private String mailhost="";
    private String username="";
    private String password="";
    private Session session;

    static {
        Security.addProvider(new info.bhtours.bhtours.util.JSSEProvider());
    }

    public MailSender(String _mailhost, String _username, String _password) {
        this.username = _username;
        this.password = _password;
        this.mailhost = _mailhost;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        Authenticator auth = new SMTPAuthenticator();
        session = Session.getDefaultInstance(props, auth);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        AsyncCallSendMail taskSendMail = new AsyncCallSendMail();
        taskSendMail.execute(subject, body, sender, recipients);

        /*try{
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        }catch(Exception e){
            Log.d("err", e.getMessage());
        }*/
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String _username = username;
            String _password = password;
            return new PasswordAuthentication(_username, _password);
        }
    }

    private class AsyncCallSendMail extends AsyncTask<String, Void, String> {
        private boolean hasErrors = false;

        @Override
        protected String doInBackground(String... params) {
            String subject = params[0];
            String body = params[1];
            String sender = params[2];
            String recipients = params[3];

            //readJSON(URL, action, pageid);
            return sendMail(subject, body, sender, recipients);
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public String sendMail(String subject, String body, String sender, String recipients)
        {
            String retVal="";
            try{
                MimeMessage message = new MimeMessage(session);
                DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
                message.setSender(new InternetAddress(sender));
                message.setSubject(subject);
                message.setDataHandler(handler);
                if (recipients.indexOf(',') > 0)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                else
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
                Transport.send(message);
            }catch(Exception e){
                Log.d("err", e.getMessage());
            }
            return retVal;
        }
    }
}
