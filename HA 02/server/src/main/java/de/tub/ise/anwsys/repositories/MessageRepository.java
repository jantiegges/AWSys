package de.tub.ise.anwsys.repositories;

import de.tub.ise.anwsys.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{

    @Query("SELECT m.creator FROM Message AS m WHERE channelId = :channelId ORDER BY channelId DESC")
    List<String> findUniqueCreatorByChannelId(@Param("channelId") long channelId);

    @Query("SELECT m FROM Message AS m WHERE timestamp >= :lastSeenTimestamp")
    Page<Message> findMessagesByTimestamp(@Param("lastSeenTimestamp")LocalDateTime lastSeenTimestamp, Pageable pageable);

}
