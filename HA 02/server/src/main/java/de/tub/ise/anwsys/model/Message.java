package de.tub.ise.anwsys.model;

import de.tub.ise.anwsys.model.Channels;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private Timestamp timestamp;
    private String content;
    private String creator;
    @ManyToOne
    @JoinColumn(name = "id", nullable=false)
    private Channels channel;

    public Message(){}
    //*************************************
    // GETTER & SETTER METHODS
    public long getId(){return id;}
    public Timestamp getTimestamp(){return timestamp;}
    public String getContent(){return content;}
    public String getCreator(){return creator;}
    public Channels getChannels(){ return channel;}

    public void setTimestamp(){
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
    public void setContent(String content){this.content = content;}
    public void setCreator(String creator){this.creator = creator;}
    public void setChannel(Channels channel){this.channel = channel;}
    //**************************************
}
