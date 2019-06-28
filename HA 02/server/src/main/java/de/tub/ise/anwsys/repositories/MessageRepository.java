package de.tub.ise.anwsys.repositories;

import de.tub.ise.anwsys.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{

    @Query("SELECT m.creator from Message As m WHERE channelId = :channelId")
    List<String> findUniqueCreatorByChannelId(@Param("channelId") long channelId);

}
