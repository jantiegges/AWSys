package de.tub.ise.anwsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedList;

@Entity
public class Channels {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String topic;
    @OneToMany(mappedBy = "channel")
    private LinkedList<Message> messages;


    public Channels(){
    }

    public String getName(){ return name; }
    public String getTopic(){ return topic;}
    public long getId(){ return id; }

    public void setName(String name){
        this.name = name;
    }

    public void setTopic(String topic){
        this.topic = topic;
    }





}
