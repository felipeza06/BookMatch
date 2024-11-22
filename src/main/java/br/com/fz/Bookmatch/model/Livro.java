package br.com.fz.Bookmatch.model;


import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "Livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer numeroDeDownloads;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Integer getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    public void setNumeroDeDownloads(Integer numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }

    public Livro(LivroRecord livroRecord) {
        this.titulo = livroRecord.titulo();
        this.numeroDeDownloads = livroRecord.numeroDeDownloads();

        livroRecord.autor().stream().findFirst().ifPresent(autorRecord -> {
            this.autor = new Autor(autorRecord);
            this.autor.getLivros().add(this);
        });

        this.idioma = (livroRecord.idiomas() != null && !livroRecord.idiomas().isEmpty())
                ? Idioma.stringToEnum(livroRecord.idiomas().get(0))
                : Idioma.nd;
    }

    public Livro() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma +
                ", autor=" + autor +
                '}';
    }
}
