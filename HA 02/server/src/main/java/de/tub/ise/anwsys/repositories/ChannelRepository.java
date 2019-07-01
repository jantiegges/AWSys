package de.tub.ise.anwsys.repositories;

import de.tub.ise.anwsys.model.Channel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ChannelRepository extends PagingAndSortingRepository<Channel, Long> {


}
