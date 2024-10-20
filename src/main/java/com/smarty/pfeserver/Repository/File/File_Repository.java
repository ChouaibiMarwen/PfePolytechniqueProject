package com.smarty.pfeserver.Repository.File;

import com.smarty.pfeserver.Models.File.MediaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface File_Repository extends JpaRepository<MediaModel,Long> {
    boolean existsById(Long id);
    MediaModel findByName(String name);


}
