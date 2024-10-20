package com.smarty.pfeserver.Repository.Country;

import com.smarty.pfeserver.Models.country.Translations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationsRepository extends JpaRepository<Translations,Long> {

}
