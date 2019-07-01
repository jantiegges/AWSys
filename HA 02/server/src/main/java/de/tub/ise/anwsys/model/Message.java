package de.tub.ise.anwsys.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Timestamp;


@Entity
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime timestamp;
    private String creator;
    private String content;
    //@Column(name = "Channel_Id")
    //private long channelId;
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "channel_id")
    private Channel channel;


    public Message(){} // for internal use only

    public Message(String creator, String content, long channelId, Channel channel) {
        Date date = new Date();
        this.timestamp = timestamp.now();
        this.creator = creator;
        this.content = content;
        this.channel = channel;
        //this.channelId = channelId;
    }

    //*************************************
    // GETTER & SETTER METHODS

    public long getId(){ return id; }
    public LocalDateTime getLocalDateTime(){ return timestamp; }
    public String getContent(){ return content; }
    public String getCreator(){ return creator; }
    //public long getChannelId(){ return channelId; }

    public Channel getChannel() {
        return channel;
    }

    public void setTimestamp(){
        Date date = new Date();
        this.timestamp = LocalDateTime.now();
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
