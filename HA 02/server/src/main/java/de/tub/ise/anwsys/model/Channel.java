package de.tub.ise.anwsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.*;
import javax.persistence.CascadeType;
import java.util.LinkedList;
import javax.persistence.*;

@Entity
public class Channel {
//************* Attribute ******************
    @Id
    @GeneratedValue
    private long id; // ID will be generated automatically

    private String name;

    private String topic;

//    private List <Message> messages = new LinkedList<>();
//************* Konstruktor *****************
    public Channel(){
    }
//************* GETTER & SETTER *************

    public long getId(){ return id;}

    public String getName(){ return name; }

    public String getTopic(){ return topic;}

//    @OneToMany(targetEntity = de.tub.ise.anwsys.model.Message.class, cascade = CascadeType.ALL, mappedBy = "channelId")
//    @JoinColumn(name="Channel_Identifier")
//    public List<Message> getMessages(){
//        if(messages == null) return null;
//        else return messages;
//    }

    public void setId (long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTopic(String topic){
        this.topic = topic;
    }

//    public void setMessages(List<Message> messages) {
//        this.messages = messages;
//    }
}
