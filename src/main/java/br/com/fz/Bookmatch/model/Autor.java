package br.com.fz.Bookmatch.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String nome;
    @Column
    private Integer anoDeNascimento;
    @Column
    private Integer anoDeMorte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    public void addLivro(Livro livro) {


        livro.setAutor(this);
        this.livros.add(livro);
    }

    public void removeLivro(Livro livro) {
        livro.setAutor(null);
        this.livros.remove(livro);
    }

    public Autor(AutorRecord autor){
        this.nome=autor.nome();
        this.anoDeNascimento= autor.anoNascimento();
        this.anoDeMorte= autor.anoFalecimento();
    }

    public Autor(Autor autor) {
        this.nome = autor.getNome();
        this.anoDeNascimento = autor.getAnoDeNascimento();
        this.anoDeMorte = autor.getAnoDeMorte();
    }

    public Autor() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnoDeNascimento() {
        return anoDeNascimento;
    }

    public void setAnoDeNascimento(Integer anoDeNascimento) {
        this.anoDeNascimento = anoDeNascimento;
    }

    public Integer getAnoDeMorte() {
        return anoDeMorte;
    }

    public void setAnoDeMorte(Integer anoDeMorte) {
        this.anoDeMorte = anoDeMorte;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        return
                ", nome='" + nome + '\'' +
                ", anoDeNascimento=" + anoDeNascimento +
                ", anoDeMorte=" + anoDeMorte +
                ", livros=" + livros +
                '}';
    }
}
