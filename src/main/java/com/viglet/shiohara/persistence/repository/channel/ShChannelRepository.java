package com.viglet.shiohara.persistence.repository.channel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.channel.ShChannel;

public interface ShChannelRepository extends JpaRepository<ShChannel, Integer> {

	List<ShChannel> findAll();

	ShChannel findById(int id);

	ShChannel findByTitle(String title);

	ShChannel save(ShChannel ShChannel);

	@Modifying
	@Query("delete from ShChannel p where p.id = ?1")
	void delete(int shChannelId);
}
