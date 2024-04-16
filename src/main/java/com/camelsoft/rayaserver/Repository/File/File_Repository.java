package com.camelsoft.rayaserver.Repository.File;

import com.camelsoft.rayaserver.Models.File.File_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface File_Repository extends JpaRepository<File_model,Long> {
    boolean existsById(Long id);
    File_model findByName(String name);


}
