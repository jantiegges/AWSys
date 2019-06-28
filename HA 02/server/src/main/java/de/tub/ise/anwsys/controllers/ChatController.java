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

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public ChatController(ChannelRepository channelRepository, MessageRepository messageRepository){
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createChannel(@RequestBody Channel channel){
        channelRepository.save(channel); // save new channel object in database
        return ResponseEntity.ok(channel);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resources<Resource<Channel>> getAllChannels() {
        Iterator<Channel> iteratorC = channelRepository.findAll().iterator();
        List<Resource<Channel>> channelList = new LinkedList<>();
        while(iteratorC.hasNext()){
//            Channel tmp = iteratorC.next();
//            channelList.add(new Resource<>(tmp, linkTo(methodOn(ChatController.class).getSpecificChannel(tmp.getId())).withSelfRel())); // adding links to each resource is not required in the api
//            channelList.add(new Resource<>(tmp));
            channelList.add(new Resource<>(iteratorC.next()));
        }
        return new Resources<>(channelList,linkTo(methodOn(ChatController.class).getAllChannels()).withSelfRel());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource<Channel> getSpecificChannel (@PathVariable("id") long id) {
            Channel channel = channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id)); // throw 404 if channel is not found
            return new Resource<>(channel);
            // this is also not as in the requirements API:
//            Channel channel = channelRepository.findById(id).get();
//            return new Resource<>(channel, linkTo(methodOn(ChatController.class).getSpecificChannel(id)).withSelfRel());
}

    @RequestMapping(value = "/{id}/message", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Message message, @PathVariable("id") long id){
        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }
    @RequestMapping(value = "/{id}/message", method = RequestMethod.GET)
    public ResponseEntity<?> getMessages (@PathVariable("id") long id){
        return ResponseEntity.ok(messageRepository.findAll());
    }
}


