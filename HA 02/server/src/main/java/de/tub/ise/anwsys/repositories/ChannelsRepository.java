package de.tub.ise.anwsys.repositories;

import de.tub.ise.anwsys.model.Channels;
import org.springframework.data.repository.CrudRepository;
import javax.persistence.Query;
import javax.persistence.EntityManager;

public interface ChannelsRepository extends CrudRepository<Channels, Long> {
    /*
    @Override
    public Optional<Channels> findById(long id) {
        String hql = "select * from Channels where e.id ="+id;
        Query query =
        List<Users> users = query.getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of( users.get(0) );
    }

     */
}
