package com.camelsoft.rayaserver.Repository.Country;

import com.camelsoft.rayaserver.Models.country.Translations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationsRepository extends JpaRepository<Translations,Long> {

}
