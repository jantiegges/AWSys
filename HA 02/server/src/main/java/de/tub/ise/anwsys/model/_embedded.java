package de.tub.ise.anwsys.model;

import javax.persistence.Entity;
import java.util.LinkedList;

@Entity
public class _embedded {

    private LinkedList<Message> messageList;

    public _embedded (){
        messageList = new LinkedList<Message>();
    }
    //*********************************
    // GETTER & SETTER METHODS
    public LinkedList<Message> getMessages(){
        return messageList;
    }

    public void addMessage(Message message){
        messageList.add(message);
    }
    public Message getMessage(int index){
        return messageList.get(index);
    }
    //**********************************

}
