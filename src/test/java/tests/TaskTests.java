package tests;

import dto.Film;
import dto.Person;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tests.BaseTest.getRequest;

public class TaskTests {

    @Test
    public void getVaderInfo(){
        List<Person> people = getRequest("https://swapi.dev/api/people/?search=Vader", 200)
                .body().jsonPath().getList("results", Person.class);
        for (Person person: people) {
            assertTrue(person.getName().contains("Vader"));
        }
    }

    @Test
    public void lessPlanets(){
        List<Person> people = getRequest("https://swapi.dev/api/people/?search=Vader", 200)
                .body().jsonPath().getList("results", Person.class);
        List<String> films = people.get(0).getFilms();
    Map<String, Integer> filmPlanetsQuantity = new HashMap<>();
    for (String film: films) {
        Film filmInfo = getRequest(film, 200)
                .body().jsonPath().getObject("", Film.class);
     filmPlanetsQuantity.put(filmInfo.getTitle(), filmInfo.getPlanets().size());
    }
    String minFilm = Collections.min(filmPlanetsQuantity.entrySet(), Map.Entry.comparingByValue()).getKey();
    System.out.println(minFilm);
    }

    @Test
    public void isStarshipOnTheFilm(){
        List<Person> people = getRequest("https://swapi.dev/api/people/?search=Vader", 200)
                .body().jsonPath().getList("results", Person.class);
        List<String> films = people.get(0).getFilms();
       String vaderStarship = people.get(0).getStarships().get(0);
        for (String film: films) {
            Film filmInfo = getRequest(film, 200)
                    .body().jsonPath().getObject("", Film.class);
            if (filmInfo.getStarships().contains(vaderStarship)) {
                System.out.println("Vader's starship is on film " + filmInfo.getTitle());
            }
            else {
                System.out.println("Vader's starship is NOT on film " + filmInfo.getTitle());
            }
        }
    }
    @Test
    public void oldestPerson(){
        List<Person> allCharacters = new ArrayList<>();
        for (int pageNumber = 1; pageNumber<=9; pageNumber++) {
            List<Person> charactersFromPage = getRequest("https://swapi.dev/api/people/?page="+pageNumber, 200)
                    .body().jsonPath().getList("results", Person.class);
            for (Person character: charactersFromPage) {
                allCharacters.add(character);
            }
            String oldestCharacterName = "";
            int oldestCharacterBirthYear = Integer.MIN_VALUE;

            for (Person character: allCharacters) {
                String birthYear = character.getBirth_year();
                if (!birthYear.equals("unknown") && !birthYear.isEmpty()&&!birthYear.contains("ABY")){
                    int year = Integer.parseInt(birthYear.replaceAll("[^0-9]", ""));
                    if (year > oldestCharacterBirthYear) {
                        oldestCharacterName = character.getName();
                        oldestCharacterBirthYear = year;
                    }
                }
            }
            System.out.println(oldestCharacterName);
            System.out.println(oldestCharacterBirthYear);
        }
    }
    @Test
    public void peopleSchemaValidation(){
        String schemaFile = "schemas/PeopleSchema.json";
        Response response = getRequest("https://swapi.dev/api/people", 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaFile));
    }

}



