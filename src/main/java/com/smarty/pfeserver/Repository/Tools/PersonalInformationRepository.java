package com.smarty.pfeserver.Repository.Tools;

import com.smarty.pfeserver.Models.Tools.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation,Long> {
}
