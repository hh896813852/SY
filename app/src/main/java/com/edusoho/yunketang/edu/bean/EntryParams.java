package com.edusoho.yunketang.edu.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntryParams implements Serializable {

    private String      appId;
    private String      roomNo;
    private int         roomStatus;
    private String      clientId;
    private String      avatar;
    private String      httpToken;
    private String      httpUrl;
    private String      sslUrl;
    private String      otherHttpUrl;
    private String      otherSslUrl;
    private SocketToken socketToken;
    private String      conversationNo;
    private String      chatConversationNo;
    private String      normalConversationNo;
    private String      clientName;
    private String      originalRole;
    private String      currentRole;
    private String      title;
    private String      versionUrl;
    private String      playServer;
    private String      env;
    private String      logUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHttpToken() {
        return httpToken;
    }

    public void setHttpToken(String httpToken) {
        this.httpToken = httpToken;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getSslUrl() {
        return sslUrl;
    }

    public void setSslUrl(String sslUrl) {
        this.sslUrl = sslUrl;
    }

    public String getOtherHttpUrl() {
        return otherHttpUrl;
    }

    public void setOtherHttpUrl(String otherHttpUrl) {
        this.otherHttpUrl = otherHttpUrl;
    }

    public String getOtherSslUrl() {
        return otherSslUrl;
    }

    public void setOtherSslUrl(String otherSslUrl) {
        this.otherSslUrl = otherSslUrl;
    }

    public SocketToken getSocketToken() {
        if (socketToken == null) {
            return new SocketToken();
        }
        return socketToken;
    }

    public void setSocketToken(SocketToken socketToken) {
        this.socketToken = socketToken;
    }

    public String getConversationNo() {
        return conversationNo;
    }

    public void setConversationNo(String conversationNo) {
        this.conversationNo = conversationNo;
    }

    public String getChatConversationNo() {
        return chatConversationNo;
    }

    public void setChatConversationNo(String chatConversationNo) {
        this.chatConversationNo = chatConversationNo;
    }

    public String getNormalConversationNo() {
        return normalConversationNo;
    }

    public void setNormalConversationNo(String normalConversationNo) {
        this.normalConversationNo = normalConversationNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOriginalRole() {
        return originalRole;
    }

    public void setOriginalRole(String originalRole) {
        this.originalRole = originalRole;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getPlayServer() {
        return playServer;
    }

    public void setPlayServer(String playServer) {
        this.playServer = playServer;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public static class SocketToken implements Serializable {

        private String       token;
        private List<String> servers;
        private List<String> sslServers;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<String> getServers() {
            if (servers == null) {
                return new ArrayList<>();
            }
            return servers;
        }

        public void setServers(List<String> servers) {
            this.servers = servers;
        }

        public List<String> getSslServers() {
            return sslServers;
        }

        public void setSslServers(List<String> sslServers) {
            this.sslServers = sslServers;
        }
    }

    public boolean isNull() {
        return socketToken == null;
    }
}
