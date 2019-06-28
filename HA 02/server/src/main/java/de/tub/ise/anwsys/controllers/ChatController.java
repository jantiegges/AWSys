package de.tub.ise.anwsys.controllers;

import org.springframework.hateoas.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import de.tub.ise.anwsys.model.Message;
import de.tub.ise.anwsys.repositories.ChannelRepository;
import de.tub.ise.anwsys.repositories.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.tub.ise.anwsys.model.Channel;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.*;
import java.util.LinkedList;

@RestController
@RequestMapping("/channels")
public class ChatController {

    ChannelRepository channelsRepository;
    MessageRepository messageRepository;

    public ChatController(ChannelRepository channelsRepository, MessageRepository messageRepository){

        this.channelsRepository = channelsRepository;
        this.messageRepository = messageRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createChannel(@RequestBody Channel channel){
        Channel c = new Channel();
        c.setName(channel.getName());
        c.setTopic(channel.getTopic());
        channelsRepository.save(c);
        return ResponseEntity.ok(c);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resources<Resource<Channel>> getAllChannels() {
        Iterator<Channel> iteratorC= channelsRepository.findAll().iterator();
        List<Resource<Channel>> channelList = new LinkedList<>();
        while(iteratorC.hasNext()){

            Channel tmp = iteratorC.next();
            channelList.add(new Resource<Channel>(tmp,
            linkTo(methodOn(ChatController.class).getSpecificChannel(tmp.getId())).withSelfRel()));
        }

        return new Resources<>(channelList,linkTo(methodOn(ChatController.class).getAllChannels()).withSelfRel());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource<Channel> getSpecificChannel (@PathVariable("id") long id) {
        Channel channel = channelsRepository.findById(id).get();
        return new Resource<Channel>(channel,linkTo(methodOn(ChatController.class).getSpecificChannel(id)).withSelfRel());



}

    @RequestMapping(value = "/{id}/message", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Message message, @PathVariable("id") long id){
        Message m = new Message();
        m.setTimestamp();
        m.setCreator(message.getCreator());
        m.setContent(message.getContent());
        m.setChannelId(id);
        messageRepository.save(m);


        //return ResponseEntity.ok(m);
        return ResponseEntity.ok(m);
    }
    @RequestMapping(value = "/{id}/message", method = RequestMethod.GET)
    public ResponseEntity<?> getMessages (@PathVariable("id") long id){
        return ResponseEntity.ok(messageRepository.findAll());
    }



}


