package br.com.fz.Bookmatch.principal;

import br.com.fz.Bookmatch.model.*;
import br.com.fz.Bookmatch.repository.AutorRepository;
import br.com.fz.Bookmatch.repository.LivroRepository;
import br.com.fz.Bookmatch.service.ConsumoApi;
import br.com.fz.Bookmatch.service.ConverteDados;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class Principal {

    private final LivroRepository repositorio;
    private final AutorRepository autorRepository;
    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumo = new ConsumoApi();
    private final String url = "https://gutendex.com/books/?search=";

    public Principal(LivroRepository repositorio, AutorRepository autorRepository) {
        this.repositorio = repositorio;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {

        var opcao = -1;

        while (opcao != 6) {
            var menu = """                   
                    
                    1- Buscar livros
                    2- Listar livros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos em um determinado ano
                    5- Listar livros em um determinado idioma
                    
                    6 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivros();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosIdioma();
                    break;
                case 6:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void listarLivrosIdioma() {
        System.out.println("Informe o código do idioma (ex: en para inglês, pt para português): ");
        String codigoIdioma = leitura.nextLine().trim().toLowerCase();

        Idioma idiomaEnum;
        try {
            idiomaEnum = Idioma.valueOf(codigoIdioma);
        } catch (IllegalArgumentException e) {
            System.out.println("Nenhum idioma encontrado com o código: " + codigoIdioma);
            return;
        }

        var livros = repositorio.findByIdioma(idiomaEnum);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + codigoIdioma);
            return;
        }

        System.out.println("Livros encontrados no idioma " + codigoIdioma + ":");
        for (Livro livro : livros) {
            System.out.println("----------------------------");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Autor desconhecido"));
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("Número de Downloads: " + livro.getNumeroDeDownloads());
            System.out.println("----------------------------");
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Informe o ano desejado para verificar autores vivos: ");
        int ano = leitura.nextInt();
        leitura.nextLine();

        var autores = autorRepository.findAll();

        var autoresVivosNoAno = autores.stream()
                .filter(autor -> {
                    var nascimento = autor.getAnoDeNascimento();
                    var falecimento = autor.getAnoDeMorte();

                    boolean vivoNesseAno = (nascimento != null && autor.getAnoDeNascimento() <= ano) &&
                            (falecimento == null || autor.getAnoDeMorte() > ano);

                    return vivoNesseAno;
                })
                .toList();

        if (autoresVivosNoAno.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano " + ano + ".");
            return;
        }

        System.out.println("Autores vivos no ano " + ano + ":");
        for (Autor autor : autoresVivosNoAno) {
            System.out.println("----------------------------");
            System.out.println("Nome: " + autor.getNome());
            System.out.println("Data de Nascimento: " + autor.getAnoDeNascimento());
            System.out.println("Data de Falecimento: " + (autor.getAnoDeMorte() != null ? autor.getAnoDeMorte() : "Ainda vivo"));
            System.out.println("----------------------------");
        }
    }

    private void listarAutores() {
        var autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado no banco de dados.");
            return;
        }

        System.out.println("Lista de Autores Registrados:");
        for (Autor autor : autores) {
            System.out.println("----------------------------");
            System.out.println("Nome: " + autor.getNome());
            System.out.println("Data de Nascimento: " + (autor.getAnoDeNascimento() != null ? autor.getAnoDeNascimento() : "Desconhecida"));
            System.out.println("Data de Falecimento: " + (autor.getAnoDeMorte() != null ? autor.getAnoDeMorte() : "Ainda vivo"));
            System.out.println("----------------------------");
        }
    }

    private void listarLivros() {
        var livros = repositorio.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no banco de dados.");
            return;
        }

        System.out.println("Lista de Livros Registrados:");
        for (Livro livro : livros) {
            System.out.println("----------------------------");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Autor desconhecido"));
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("Número de Downloads: " + livro.getNumeroDeDownloads());
            System.out.println("----------------------------");
        }
    }

    private void buscarLivros() {
        System.out.println("Informe o nome do livro: ");
        var nomeLivro = leitura.nextLine();

        var json = consumo.obterDados(url + nomeLivro.replace(" ", "+"));
        var wrapper = conversor.obterDados(json, ResultWrapper.class);

        if (wrapper.results() != null && !wrapper.results().isEmpty()) {
            for (LivroRecord livroRecord : wrapper.results()) {
                Livro livro = new Livro(livroRecord);
                Autor autor = livro.getAutor();

                if (autor != null) {
                    Optional<Autor> autorExistente = autorRepository.findByNome(autor.getNome());

                    try {
                        if (autorExistente.isPresent()) {
                            livro.setAutor(autorExistente.orElse(null));
                        } else {
                           autorRepository.save(autor);
                        }
                        repositorio.save(livro);
                    } catch (NullPointerException e) {
                        System.out.println("Não foi possivel salvar, verifique o nome do livro");
                    }
                }
                System.out.println("Livro salvo: ");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor(es): " + autor.getNome());
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Número de Downloads: " + livro.getNumeroDeDownloads());
                System.out.println("----------------------------");
            }
        }
    }
}
