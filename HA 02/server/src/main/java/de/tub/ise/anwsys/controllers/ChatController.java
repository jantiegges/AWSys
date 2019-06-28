package de.tub.ise.anwsys.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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
    public PagedResources<?> getAllChannels(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {

        Page<Channel> channelsPage = channelRepository.findAll(pageable);
        PagedResources<?> pagedResources1 = pagedResourcesAssembler.toResource(channelsPage, linkTo(ChatController.class).withSelfRel());

        return pagedResources1;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource<?> getSpecificChannel (@PathVariable("id") long id) {

            Channel channel = channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id)); // throw 404 if channel is not found
            return new Resource<>(channel);

}

    @RequestMapping(value = "/{id}/message", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Message message, @PathVariable("id") long id){
        Message m = new Message();
        m.setTimestamp();
        m.setCreator(message.getCreator());
        m.setContent(message.getContent());
        m.setChannelId(id);
        messageRepository.save(m);
        return ResponseEntity.ok(m);
    }
    @RequestMapping(value = "/{id}/message", method = RequestMethod.GET)
    public ResponseEntity<?> getMessages (@PathVariable("id") long id){

        return ResponseEntity.ok(messageRepository.findAll());
    }
    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public List<?> getUsers(@PathVariable("id") long id){
        List<String> userList = messageRepository.findUniqueCreatorByChannelId(id);
        return userList;
    }
}


