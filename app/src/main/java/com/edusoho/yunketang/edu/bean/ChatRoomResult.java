package com.edusoho.yunketang.edu.bean;


import java.util.List;

public class ChatRoomResult {
    public List<ChatRoom> resources;
    public int total;

    public static class ChatRoom{
        public String type;
        public int id;
        public String title;
        public String picture;
    }
}
