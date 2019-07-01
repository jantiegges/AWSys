package de.tub.ise.anwsys.model;

import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Timestamp;


@Entity
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private Instant timestamp;
    private String creator;
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    private Channel channel;


    public Message(){} // for internal use only

    public Message(String creator, String content, long id, Channel channel) {
        Date date = new Date();
        this.timestamp = Instant.now();
        System.out.println(timestamp.toString());
        this.creator = creator;
        this.content = content;
        this.channel = channel;
    }

    //*************************************
    // GETTER & SETTER METHODS

    public long getId(){ return id; }
    public Instant getTimestamp(){ return timestamp; }
    public String getContent(){ return content; }
    public String getCreator(){ return creator; }
    //public long getChannelId(){ return channelId; }

    public Channel getChannel() {
        return channel;
    }

    public void setTimestamp(Instant timestamp){
        this.timestamp = timestamp;
    }
    public void setContent(String content){this.content = content;}
    public void setCreator(String creator){this.creator = creator;}
    //public void setChannelId(long channelId){this.channelId = channelId;}

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    /*
    @Override
    public String toString() {
        return String.format("Message(channelId=%d, timestamp=%s, creator='%s', content='%s')", timestamp.toString(), creator, content);
    }
    */


    //**************************************
}
