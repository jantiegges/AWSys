package de.tub.ise.anwsys.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Sort;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.LinkedList;

@RestController
@RequestMapping("/channels")
public class ChatController {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private String token = "HelloWorld";

    public ChatController(ChannelRepository channelRepository, MessageRepository messageRepository){
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createChannel(@RequestBody Channel channel,
                                           @RequestHeader("X-Group-Token") String header){

        if(!header.equals(token)) return ResponseEntity.status(401).build();
        Channel c = new Channel(channel.getName(), channel.getTopic());
        channelRepository.save(c); // save new channel object in database
        return ResponseEntity.ok(c);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllChannels(Pageable pageable,
                                            PagedResourcesAssembler pagedResourcesAssembler,
                                            @RequestHeader("X-Group-Token") String header) {

        if(!header.equals(token)) return ResponseEntity.status(401).build();
        Page<Channel> channelsPage = channelRepository.findAll(pageable);
        PagedResources<?> pagedResources1 = pagedResourcesAssembler.toResource(channelsPage, linkTo(ChatController.class).withSelfRel());

        return ResponseEntity.ok(pagedResources1);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSpecificChannel (@PathVariable("id") long id,
                                                 @RequestHeader("X-Group-Token") String header) {

            if(!header.equals(token)) return ResponseEntity.status(401).build();
            Channel channel = channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id)); // throw 404 if channel is not found
            return ResponseEntity.ok(new Resource<>(channel));

}

    @RequestMapping(value = "/{id}/messages", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Message message,
                                         @PathVariable("id") long id,
                                         @RequestHeader("X-Group-Token") String header){
        if(!header.equals(token)) return ResponseEntity.status(401).build();
        Message m = new Message(message.getCreator(), message.getContent(), id, channelRepository.findById(id).get());
        messageRepository.save(m);
        return ResponseEntity.ok(m);
    }

    @RequestMapping(value = "/{id}/messages", method = RequestMethod.GET)
    public ResponseEntity<?> getMessages (@PathVariable("id") long id,
                                          @RequestHeader("X-Group-Token") String header,
                                          Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler,
                                          @RequestParam(value = "lastSeenTimestamp", required = false) Optional<Instant> timestamp
                                          /*@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<Instant> timestamp*/) {

        Page<Message> messagePage;
        //test
        if(!header.equals(token)) return ResponseEntity.status(401).build();
        if (timestamp.isPresent()) {
            Instant timestamp2 = timestamp.get();

            //timestamp2 = timestamp2.plusMinutes(10);
            messagePage = messageRepository.findMessagesByTimestamp(timestamp2, pageable, id);

        } else {
            messagePage = messageRepository.findAllMessagesByChannel(pageable,id);
            //messagePage = messageRepository.findAll(PageRequest.of(0,10, Sort.Direction.DESC, "id"));

        }
        PagedResources<?> pagedResources = pagedResourcesAssembler.toResource(messagePage, linkTo(ChatController.class).withSelfRel());
        return ResponseEntity.ok(pagedResources);
    }

    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(@PathVariable("id") long id,
                            @RequestHeader("X-Group-Token") String header){

        if(!header.equals(token)) return ResponseEntity.status(401).build();
        List<String> userList = messageRepository.findUniqueCreatorByChannelId(id);
        return ResponseEntity.ok(userList);

        //
    }
}


