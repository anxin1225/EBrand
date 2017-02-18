package com.sam.ebrand.application.sms;

import java.util.LinkedList;

/**
 * Created by sam on 2016/12/5.
 */
public class UnReadMessageQueue {
    private static UnReadMessageQueue instance;
    private LinkedList<ShortMessage> mMessageQueue;

    static {
        UnReadMessageQueue.instance = null;
    }

    private UnReadMessageQueue() {
        this.mMessageQueue = new LinkedList<ShortMessage>();
    }

    public static UnReadMessageQueue getInstance() {
        if (UnReadMessageQueue.instance == null) {
            UnReadMessageQueue.instance = new UnReadMessageQueue();
        }
        return UnReadMessageQueue.instance;
    }

    public void add(final ShortMessage shortMessage) {
        this.mMessageQueue.add(shortMessage);
    }

    public void clear() {
        this.mMessageQueue.clear();
    }

    public ShortMessage poll() {
        return this.mMessageQueue.poll();
    }

    public static class ShortMessage
    {
        public String mMsgContent;
        public String mSender;
        public boolean mbAdmin;

        public ShortMessage() {
            this.mSender = "";
            this.mMsgContent = "";
            this.mbAdmin = false;
        }

        public ShortMessage(final boolean mbAdmin) {
            this.mbAdmin = mbAdmin;
            this.mSender = "";
            this.mMsgContent = "";
        }
    }
}
