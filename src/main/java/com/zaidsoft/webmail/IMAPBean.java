/*
 * Copyright 2015 ZAIDSOFT. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Initially Created on November 25, 2001, 11:37 AM
 */
package com.zaidsoft.webmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeMessage;
import javax.mail.search.MessageIDTerm;

/**
 *
 * @author DevTeam Zaidsoft <info@zaidsoft.com>
 *
 */
public class IMAPBean implements java.io.Serializable, JspTreeInfo {

    public boolean DEBUG = true;

    // POP3 Accound (Server) details
    private String mailServer;
    private String username;
    private String password;

    //public void msgCache = new Hashtable(h);
    char seperator;
    Folder defaultFolder = null, folder = null;

    // the location on local filesystem where 
    // the mails are stored
    String storeLocation = null;

    public static boolean Use_UID_as_MsgID = true;

    // this method method should be called just ones and 
    // before all method calls
    public void setStoreLocation(String storeLoc) {
        storeLocation = storeLoc;
    }

    public void setServerInfo(String mailServer, String username, String password) throws NoSuchProviderException, MessagingException {
        this.mailServer = mailServer;
        this.username = username;
        this.password = password;
        init();
    }

    ////////////////////////////////////////////////////////////////////////////    
    public String getFolderName() {
        return folder.getName();
    }

    public void setFolder(String folderName) throws MessagingException {
        if (defaultFolder == null) {
            init();
        }

        // close the prev folder
        if (null != folder && folder.isOpen()) {
            folder.close(false);
        }
        folder = defaultFolder.getFolder(folderName);
        if (!folder.exists()) {
            throw new MessagingException("Folder doesn't exists.");
        }
        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }
    }

    public void refresh() throws MessagingException {
        if (folder.getName().equals("INBOX")) {
            folder.close(true);
            setFolder("INBOX");
        }
        folder.hasNewMessages();

    }

    public void createNewFolder(String folderName) {

    }

    public void deleteFolder(String folderName) throws MessagingException {
        //defaultFolder.getFolder(folderName).delete();
    }

    public void renameFolder(String oldName, String newName) throws MessagingException {
        //defaultFolder.getFolder(oldName).renameTo(newName);
    }

    ////////////////////////////////////////////////////////////////////////////
    public int getMessageCount() throws MessagingException {
        return folder.getMessageCount();
    }

    // index from 1
    public MimeMessage getMessage(int index) throws MessagingException {
        return (MimeMessage) folder.getMessage(index);
    }

    public MimeMessage getMessage(String msgID) throws MessagingException {
        if (Use_UID_as_MsgID) {
            long uid = Long.parseLong(msgID);
            return (MimeMessage) ((UIDFolder) folder).getMessageByUID(uid);
        } else {
            return (MimeMessage) folder.search(new MessageIDTerm(msgID))[0];
        }
    }

    public void deleteMessage(int index) throws MessagingException {
        if (DEBUG) {
            System.out.println("WebmailBean: Message " + index + " deleted");
        }
        getMsg(index);
        message.setFlag(Flags.Flag.DELETED, true);
        folder.expunge();
    }

    public void deleteMessages(int[] m) throws MessagingException {
        if (DEBUG) {
            System.out.println("WebmailBean: Message " + m + " deleted");
        }
        folder.setFlags(m, new Flags(Flags.Flag.DELETED), true);
        folder.expunge();
    }

    Message message = null;
    int lastNumber = 0;

    private void getMsg(int no) throws MessagingException {
        if (lastNumber == 0 || lastNumber != no) {
            message = (MimeMessage) folder.getMessage(no);
        }
    }

    // connects to POP3 Server to
    // download Mail 
    private void init() throws NoSuchProviderException, MessagingException {
        String provider = null;
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = null;

        if (PropertiesHelper.getBooleanProperty("useLocalStore")) {
            // In csae POP3 is to be used as per configuration (unusual)
            if (storeLocation != null) {
                props.put("jp.gr.java_conf.roadster.net.pop.rootDirectory", storeLocation);
            }
            session = Session.getInstance(props);
        } else {
            // Use IMAP 
            provider = "imaps"; // we need it instead of imap for ssl to work
            session = Session.getDefaultInstance(props, null);
        }

        //session.setDebug(true);
        Store store = session.getStore(provider);
        store.connect(mailServer, username, password);

        defaultFolder = store.getDefaultFolder();
        seperator = defaultFolder.getSeparator();
        setFolder("INBOX");

        boolean dumpMsg = false;
        // For debug Only
        if (DEBUG && dumpMsg) {
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                System.out.println("------------ Message " + (i + 1)
                        + " ------------");
                try {
                    messages[i].writeTo(System.out);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        //Close the connection 
        //but don't remove the messages from the server
        //inbox.close(false);
        //store.close();
    }

    private void buildSentContacts() throws MessagingException {
        List<String> contacts = new ArrayList<String>();
        folder = defaultFolder.getFolder("Sent");
        if (!folder.exists()) {
            folder = defaultFolder.getFolder("Sent Mail");
        }
        Message[] messages = folder.getMessages();
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add("To");
        folder.fetch(messages, fp);
        for (Message m : messages) {
            Address[] as = m.getRecipients(Message.RecipientType.TO);
            for (Address a : as) {
                String s = a.toString().intern();
                if (!contacts.contains(s)) {
                    contacts.add(s);
                }
            }
            as = m.getRecipients(Message.RecipientType.CC);
            for (Address a : as) {
                String s = a.toString().intern();
                if (!contacts.contains(s)) {
                    contacts.add(s);
                }
            }
            as = m.getRecipients(Message.RecipientType.BCC);
            for (Address a : as) {
                String s = a.toString().intern();
                if (!contacts.contains(s)) {
                    contacts.add(s);
                }
            }
        }
    }

    public List<ListRow> buildPageSummary(int page) throws MessagingException {
    	 System.out.println("No page is ======================== "+page);
        int count = folder.getMessageCount();
        System.out.println("No of total msgs is ======================== "+count);
        int max = 5;
        int local_count = count-(page * max -max);
        max = count < max ? count : max;
        System.out.println("No of max is ======================== "+max);
       // int start = (count - (page) * max)+1;
        int start = page ;
        if(page==1)
        	start=page;
        else start=(page*max -max)+1;
        System.out.println("No of start is ======================== "+start);
        //int end = start + max-1;
        int end =max;
        if (page==1 && count<=max)
        	end = max;
        else 
        {
        	if(local_count>max)
        		end = start + max;
        	else
        		end = local_count-start;
        }
        	//end = start + local_count - max;
        
        System.out.println("No of end is ======================== "+end);
        List<ListRow> rows = new ArrayList<ListRow>();
        System.out.println("No of rows is ======================== "+rows);
        Message[] messages = folder.getMessages(start, end);
        System.out.println("messages ======================== "+messages);
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add("Subject");
        fp.add("From");
        fp.add("To");
        fp.add("Date");
        folder.fetch(messages, fp);
        for (Message m : messages) {
            ListRow row = new ListRow();
            row.date = m.getReceivedDate();
            row.from = m.getFrom()[0].toString();
            Address[] a = m.getRecipients(Message.RecipientType.TO);
            if (a != null) {
                row.to = a[0].toString();
            }
            row.subject = m.getSubject();
            if (Use_UID_as_MsgID) {
                row.messageID = String.valueOf(((UIDFolder) folder).getUID(m));
            } else {
                row.messageID = ((javax.mail.internet.MimeMessage) m).getMessageID();
            }
            row.size = m.getSize();
            rows.add(row);
        }
        return rows;
    }

    public String getMessageID(Message msg) throws MessagingException {
        if (Use_UID_as_MsgID) {
            return String.valueOf(((UIDFolder) folder).getUID(msg));
        } else {
            return ((javax.mail.internet.MimeMessage) msg).getMessageID();
        }
    }

    public class ListRow {

        String messageID;
        String subject;
        String from;
        String to;
        boolean attachment;
        Date date;
        long size;

        public String getSizeK() throws MessagingException {
            if (size == -1) {
                return "0";
            }
            return String.valueOf(size / 1024);
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public boolean isAttachment() {
            return attachment;
        }

        public void setAttachment(boolean attachment) {
            this.attachment = attachment;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getMessageID() {
            return messageID;
        }

        public void setMessageID(String messageID) {
            this.messageID = messageID;
        }

    }

    /**
     * The String which is to be displayed for Root Node.
     */
    public String rootName() {
        return "Folders";
    }

    /**
     * The String ( usually of just one char) used as seperator while describing
     * a node.
     */
    public char getSeperator() {
        return seperator;
    }

    /**
     * When called this method returns the list of the nodes under given node.
     * Only nodes one level deep are listed. may be called with separator to get
     * children under root.
     */
    public String[] list(String nodeFullName) {
        Folder fol = null;
        try {
            fol = defaultFolder.getFolder(nodeFullName);
            if (!fol.exists()) {
                return null;
            }
            Folder[] f = fol.list();
            return folNames(f);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] folNames(Folder[] f) {
        String[] s = new String[f.length];
        for (int i = 0; i < f.length; i++) {
            s[i] = f[i].getName();
        }
        return s;
    }

}
