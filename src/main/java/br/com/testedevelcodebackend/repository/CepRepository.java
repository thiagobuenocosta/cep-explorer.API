package br.com.testedevelcodebackend.repository;

import br.com.testedevelcodebackend.model.Cep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CepRepository extends JpaRepository<Cep, Long> {

}
