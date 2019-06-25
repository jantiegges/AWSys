package de.tub.ise.anwsys.model;

import de.tub.ise.anwsys.model.Channels;

import javax.persistence.*;
import java.util.*;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


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
    //@JsonUnwrapped
    //private final Resources<EmbeddedWrapper> embeddeds;

    public Message(){
    }
    //*************************************
    // GETTER & SETTER METHODS
    public long getId(){return id;}
    public Timestamp getTimestamp(){return timestamp;}
    public String getContent(){return content;}
    public String getCreator(){return creator;}
    public long getChannelId(){
        return channelId;
    }

    public void setTimestamp(){
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
    public void setContent(String content){this.content = content;}
    public void setCreator(String creator){this.creator = creator;}
    public void setChannelId(long channelId){this.channelId = channelId;}
    //**************************************
}
