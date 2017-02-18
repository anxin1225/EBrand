package com.sam.ebrand.meetingNetwork.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 2016/11/23.
 */

public class MessageManager
{
    private static MessageManager mInstance;
    private static int mRefCount;
    private static ArrayList<SmsGroundBean> mSessionList;
    private static Map<String, List<SmsBean>> mSessionMap;

    static {
        MessageManager.mInstance = null;
        MessageManager.mRefCount = 0;
    }

    public MessageManager() {
        MessageManager.mSessionMap = new HashMap<String, List<SmsBean>>();
        MessageManager.mSessionList = new ArrayList<SmsGroundBean>();
    }

    public static void cacheSession(final String s, final ArrayList<SmsBean> list) {
        if (MessageManager.mSessionMap != null && !MessageManager.mSessionMap.containsKey(s)) {
            MessageManager.mSessionMap.put(s, list);
        }
    }

    public static List<SmsBean> getCacheSession(final String s) {
        if (MessageManager.mSessionMap != null && MessageManager.mSessionMap.containsKey(s)) {
            return MessageManager.mSessionMap.get(s);
        }
        return null;
    }

    public static MessageManager getInstance() {
        synchronized (MessageManager.class) {
            if (MessageManager.mInstance == null) {
                MessageManager.mInstance = new MessageManager();
            }
            ++MessageManager.mRefCount;
            return MessageManager.mInstance;
        }
    }

    public static ArrayList<SmsGroundBean> getSessionList() {
        return MessageManager.mSessionList;
    }

    public static ArrayList<SmsGroundBean> groupingMessagesToMap(final List<SmsBean> list) {
        MessageManager.mSessionMap.clear();
        MessageManager.mSessionList.clear();
        if (MessageManager.mSessionMap == null || list == null || list.size() == 0) {
            return null;
        }
        for (final SmsBean smsBean : list) {
            final String sendername = smsBean.getSendername();
            final String sendername2 = smsBean.getSendername();
            if (sendername2.equals(sendername)) {
                if (MessageManager.mSessionMap.containsKey(sendername2)) {
                    MessageManager.mSessionMap.get(sendername2).add(smsBean);
                }
                else {
                    final ArrayList<SmsBean> list2 = new ArrayList<SmsBean>();
                    list2.add(smsBean);
                    MessageManager.mSessionMap.put(sendername2, list2);
                    final SmsGroundBean smsGroundBean = new SmsGroundBean();
                    smsGroundBean.setSendername(sendername);
                    smsGroundBean.setAdd_time(smsBean.getAdd_time());
                    smsGroundBean.setContent(smsBean.getContent());
                    smsGroundBean.setAvatarUrl(smsBean.getAvatarUrl());
                    smsGroundBean.setUserId(smsBean.getUserId());
                    MessageManager.mSessionList.add(smsGroundBean);
                }
            }
            else if (MessageManager.mSessionMap.containsKey(sendername)) {
                MessageManager.mSessionMap.get(sendername).add(smsBean);
            }
            else {
                final ArrayList<SmsBean> list3 = new ArrayList<SmsBean>();
                list3.add(smsBean);
                MessageManager.mSessionMap.put(sendername, list3);
                final SmsGroundBean smsGroundBean2 = new SmsGroundBean();
                smsGroundBean2.setSendername(sendername);
                smsGroundBean2.setAdd_time(smsBean.getAdd_time());
                smsGroundBean2.setContent(smsBean.getContent());
                smsGroundBean2.setAvatarUrl(smsBean.getAvatarUrl());
                smsGroundBean2.setAvatarUrl(smsBean.getUserId());
                MessageManager.mSessionList.add(smsGroundBean2);
            }
        }
        return MessageManager.mSessionList;
    }

    public static boolean hasCacheSession(final long n) {
        return MessageManager.mSessionMap != null && MessageManager.mSessionMap.containsKey(n);
    }

    public static void release() {
        --MessageManager.mRefCount;
        if (MessageManager.mRefCount == 0) {
            MessageManager.mSessionMap.clear();
            MessageManager.mInstance = null;
        }
    }
}