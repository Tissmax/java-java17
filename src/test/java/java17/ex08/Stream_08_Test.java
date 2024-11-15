package java17.ex08;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Exercice 5 - Files
 */
public class Stream_08_Test {

    // Chemin vers un fichier de données des naissances
    private static final String NAISSANCES_DEPUIS_1900_CSV = "/home/tiss/IdeaProjects/java-java17/src/main/resources/naissances_depuis_1900.csv";

    // Structure modélisant les informations d'une ligne du fichier
    class Naissance {
        String annee;
        String jour;
        Integer nombre;

        public Naissance(String annee, String jour, Integer nombre) {
            this.annee = annee;
            this.jour = jour;
            this.nombre = nombre;
        }

        public String getAnnee() {
            return annee;
        }

        public void setAnnee(String annee) {
            this.annee = annee;
        }

        public String getJour() {
            return jour;
        }

        public void setJour(String jour) {
            this.jour = jour;
        }

        public Integer getNombre() {
            return nombre;
        }

        public void setNombre(Integer nombre) {
            this.nombre = nombre;
        }
    }


    @Test
    public void test_group() throws IOException {

        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        Path path = Path.of(NAISSANCES_DEPUIS_1900_CSV);
        try (Stream<String> lines = Files.lines(path)) {

            // TODO construire une MAP (clé = année de naissance, valeur = somme des nombres de naissance de l'année)
            Map<String, Integer> result = lines.skip(1).map(line -> {
                String[] split = line.split(";");
                return new Naissance(split[1], split[2], Integer.valueOf(split[3]));
            }).collect(Collectors.groupingBy(Naissance::getAnnee, Collectors.summingInt(Naissance::getNombre)));

            assertThat(result.get("2015"), is(8097));
            assertThat(result.get("1900"), is(5130));
        }
    }

    @Test
    public void test_max() throws IOException {

        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(Path.of(NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO trouver l'année où il va eu le plus de nombre de naissance
            Optional<Naissance> result = lines.skip(1).map(line -> {
                String[] split = line.split(";");
                return new Naissance(split[1], split[2], Integer.valueOf(split[3]));
            }).max(Comparator.comparingInt(Naissance::getNombre));


            assertThat(result.get().getNombre(), is(48));
            assertThat(result.get().getJour(), is("19640228"));
            assertThat(result.get().getAnnee(), is("1964"));
        }
    }

    @Test
    public void test_collectingAndThen() throws IOException {
        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(Path.of(NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO construire une MAP (clé = année de naissance, valeur = maximum de nombre de naissances)
            // TODO utiliser la méthode "collectingAndThen" à la suite d'un "grouping"
            Map<String, Naissance> result = lines.map(line -> {
                String[] split = line.split(";");
                return new Naissance(split[1], split[2], Integer.valueOf(split[3]));
            }).collect(Collectors.groupingBy(Naissance::getAnnee,
                    Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Naissance::getNombre)), Optional::get)));

            assertThat(result.get("2015").getNombre(), is(38));
            assertThat(result.get("2015").getJour(), is("20150909"));
            assertThat(result.get("2015").getAnnee(), is("2015"));

            assertThat(result.get("1900").getNombre(), is(31));
            assertThat(result.get("1900").getJour(), is("19000123"));
            assertThat(result.get("1900").getAnnee(), is("1900"));
        }
    }

}