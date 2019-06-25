package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.model.Message;
import de.tub.ise.anwsys.repositories.ChannelsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.tub.ise.anwsys.model.Channels;
import org.springframework.web.bind.annotation.PathVariable;
import javax.persistence.TypedQuery;
import java.util.LinkedList;

@RestController
@RequestMapping("/channels")
public class HelloController {

    ChannelsRepository channelsRepository;

    public HelloController(ChannelsRepository channelsRepository){
        this.channelsRepository = channelsRepository;
    }
/*
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public ResponseEntity<?> getHelloWorld(@RequestParam(name = "me", required = false) String me) {
        if ((me == null) || me.isEmpty()) {
            return ResponseEntity.ok("Hello, world!");
        }
        return ResponseEntity.ok(String.format("Hello, %s!", me));
    }
*/
    @RequestMapping(value = "/leo", method = RequestMethod.GET, produces = "text/html")
    public String mapToMyName (){
        return "Leonard Kinzinger";
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createChannel(@RequestBody Channels channel){
        Channels c = new Channels();
        c.setName(channel.getName());
        c.setTopic(channel.getTopic());
        c = channelsRepository.save(channel);
        return ResponseEntity.ok(c);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllChannels() {
        return ResponseEntity.ok(channelsRepository.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSpecificChannel (@PathVariable("id") long id) {
            return ResponseEntity.ok(channelsRepository.findById(id));

    }

    @RequestMapping(value = "/{id}/message", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Message message, @PathVariable("id") long id){
        Message m = new Message();
        m.setTimestamp();
        m.setContent(message.getContent());
        m.setCreator(message.getCreator());
        Channels c = new Channels();
        if(id >0) {
            c = channelsRepository.findById(id).get();
            m.setChannel(c);
        }

        return ResponseEntity.ok(m);
    }


}


