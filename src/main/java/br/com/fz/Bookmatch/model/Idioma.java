package br.com.fz.Bookmatch.model;


public enum Idioma {
    es("Español"),
    en("Inglés"),
    fr("Francés"),
    pt("Portugués"),
    nd("none"),
    ;

    public class idioma{

    }

    private String Idiomas;

    Idioma(String idiomas){
        this.Idiomas=idiomas;
    }

    public static Idioma stringToEnum(String idioma){
        for(Idioma item:Idioma.values()){
            if(item.name().equalsIgnoreCase(idioma)){
                return item;
            }
        }
        return nd;
    }

    public static void listarIdiomas(){
        for (Idioma idioma:Idioma.values()){
            System.out.println(idioma.name()+" - "+idioma.getIdiomas());
        }
    }

    public String getIdiomas() {
        return Idiomas;
    }
}
