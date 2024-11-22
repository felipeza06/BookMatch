package br.com.fz.Bookmatch.repository;

import br.com.fz.Bookmatch.model.Autor;
import br.com.fz.Bookmatch.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNome(String nome);
}
