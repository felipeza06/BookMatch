package br.com.fz.Bookmatch.repository;

import br.com.fz.Bookmatch.model.Idioma;
import br.com.fz.Bookmatch.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma(Idioma idioma);
}
