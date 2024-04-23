package com.camelsoft.rayaserver.Repository.Tools;

import com.camelsoft.rayaserver.Models.Tools.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation,Long> {
}
