package de.tub.ise.anwsys.repositories;

import de.tub.ise.anwsys.model.Channels;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Channels, Long>{
}
