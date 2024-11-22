package br.com.fz.Bookmatch.model;

import br.com.fz.Bookmatch.model.LivroRecord;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultWrapper(
        @JsonAlias("results") List<LivroRecord> results
) {}