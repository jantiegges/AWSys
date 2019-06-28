package de.tub.ise.anwsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.*;
import javax.persistence.CascadeType;
import java.util.LinkedList;
import javax.persistence.*;

@Entity // The Channel class is annotated with @Entity, indicating that it is a JPA entity. For lack of a @Table annotation, it is assumed that this entity will be mapped to a table named Channel.
public class Channel {
//  ************* Attribute ******************

    @Id
    @GeneratedValue
    private long id; // ID will be generated automatically

    // Unannotated properties: it is assumed that they’ll be mapped to columns that share the same name as the properties themselves:
    private String name;
    private String topic;


//  ************* Konstruktor *****************

    protected Channel(){} // "The default constructor only exists for the sake of JPA"

    public Channel (String name, String topic) {
        this.name = name;
        this.topic = topic;
    }


//  ************* GETTER & SETTER *************

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

    @Override
    public String toString() {
        return String.format("Channel(id=%d, Name='%s', Topic='%s')", id, name, topic);
    }
}
