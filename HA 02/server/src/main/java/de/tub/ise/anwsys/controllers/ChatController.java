package de.tub.ise.anwsys.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import de.tub.ise.anwsys.model.Message;
import de.tub.ise.anwsys.repositories.ChannelRepository;
import de.tub.ise.anwsys.repositories.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.tub.ise.anwsys.model.Channel;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
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
        Channel c = new Channel(channel.getName(), channel.getTopic());
        channelRepository.save(c); // save new channel object in database
        return ResponseEntity.ok(c);
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

    @RequestMapping(value = "/{id}/messages", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Message message, @PathVariable("id") long id){
        Message m = new Message(message.getCreator(), message.getContent(), id);
        messageRepository.save(m);
        return ResponseEntity.ok(m);
    }

    @RequestMapping(value = "/{id}/messages", method = RequestMethod.GET)
    public PagedResources<?> getMessages (@PathVariable("id") long id,
                                          Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler,
                                          @RequestParam(value = "lastSeenTimestamp", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> timestamp) {

        Page<Message> messagePage;
        //test

        if (timestamp.isPresent()) {
            LocalDateTime timestamp2 = timestamp.get();
            //timestamp2 = timestamp2.plusMinutes(10);
            messagePage = messageRepository.findMessagesByTimestamp(timestamp2, pageable);

        } else {
            messagePage = messageRepository.findAll(pageable);
        }
        PagedResources<?> pagedResources = pagedResourcesAssembler.toResource(messagePage, linkTo(ChatController.class).withSelfRel());
        return pagedResources;
    }

    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public List<?> getUsers(@PathVariable("id") long id){
        List<String> userList = messageRepository.findUniqueCreatorByChannelId(id);
        return userList;
    }
}


