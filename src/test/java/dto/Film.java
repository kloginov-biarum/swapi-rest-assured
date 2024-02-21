package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class Film {
    private String title;
    private int episode_id;
    private String opening_crawl;
    private String director;
    private String producer;
    private String release_date;
    private ArrayList<String> characters;
    public ArrayList<String> planets;
    private ArrayList<String> starships;
    private ArrayList<String> vehicles;
    private ArrayList<String> species;
    private String created;
    private String edited;
    private String url;
}
