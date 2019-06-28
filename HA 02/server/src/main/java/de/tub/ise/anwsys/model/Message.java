package de.tub.ise.anwsys.model;

import javax.persistence.*;
import java.util.*;
import java.sql.Timestamp;


@Entity
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private Timestamp timestamp;
    private String creator;
    private String content;
    @Column(name = "Channel_Id")
    private long channelId;


    protected Message(){} // for internal use only

    public Message(String creator, String content, long channelId) {
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
        this.creator = creator;
        this.content = content;
        this.channelId = channelId;
    }

    //*************************************
    // GETTER & SETTER METHODS

    public long getId(){ return id; }
    public Timestamp getTimestamp(){ return timestamp; }
    public String getContent(){ return content; }
    public String getCreator(){ return creator; }
    public long getChannelId(){ return channelId; }

    public void setTimestamp(){
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
    public void setContent(String content){this.content = content;}
    public void setCreator(String creator){this.creator = creator;}
    public void setChannelId(long channelId){this.channelId = channelId;}

    @Override
    public String toString() {
        return String.format("Message(channelId=%d, timestamp=%s, creator='%s', content='%s')", channelId, timestamp.toString(), creator, content);
    }

    //**************************************
}
