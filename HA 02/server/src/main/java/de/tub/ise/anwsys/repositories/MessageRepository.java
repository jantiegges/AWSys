package de.tub.ise.anwsys.repositories;

import de.tub.ise.anwsys.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{

    @Query("SELECT DISTINCT m.creator FROM Message AS m WHERE m.channel.id = :channelId ")
    List<String> findUniqueCreatorByChannelId(@Param("channelId") long channelId);

    @Query("SELECT m FROM Message AS m WHERE m.timestamp >= :lastSeenTimestamp AND m.channel.id = :channelId ORDER BY m.id DESC ")
    Page<Message> findMessagesByTimestamp(@Param("lastSeenTimestamp") Instant lastSeenTimestamp, Pageable pageable, long channelId);

    @Query("SELECT m FROM Message  AS m WHERE m.channel.id = :channelId")
    Page<Message> findAllMessagesByChannel(Pageable pageable, long channelId);



}
