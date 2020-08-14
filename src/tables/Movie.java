package tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entite representant les films de notre application.
 */
@Entity(name = "Movies") // Indique qu'il s'agit d'une classe que l'on souhaite persister
@Table(name = "Movies") // Indique le nom de la table dans la bdd
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter // Genere automatiquement les getters
@Setter // Genere automatiquement les setters
@ToString
public class Movie {

  @Id // Cle primaire
  @NotEmpty // Ne doit pas etre vide
  @Column(unique = true) // Chaque films a un identifiant unique
  private int id; // Identifiant du film (tmdbid)

  private String title; // Titre du film

  private double voteAverage; // Moyenne des votes que le film a obtenu

  private String releaseDate; // Date de sortie du film

  private String category; // Genres associes au film

  private String backdropUrl; // Url de l'image de fond du film

  @Column(length = 1000) // Nombre de caracteres est limite a 1000
  private String overview; // Synopsis du film

  private String posterUrl; // Url du poster du film

  private String trailerUrl; // Url de la bande d'annonce du film

  @NotEmpty // Ne doit pas etre vide
  private boolean trending; // Indique si le film est tendance ou pas

  private String homepageUrl; // Url de la page officiel du film

  private String status; // Status du film

  private String runtime; // Duree du film

  @Column(length = 1000) // Nombre de caracteres est limite a 1000
  private String keywords; // Mots cles associes au film

  private String recommendations; // Ids des films recommandées associes au film

  public Movie(int id, String title, double voteAverage, String releaseDate, String category,
      String backdropUrl, String overview, String posterUrl, String trailerUrl, boolean trending,
      String homepageUrl, String status, String runtime, String keywords, String recommendations) {
    this.id = id;
    this.title = title;
    this.voteAverage = voteAverage;
    this.releaseDate = releaseDate;
    this.category = category;
    this.backdropUrl = backdropUrl;
    this.overview = overview;
    this.posterUrl = posterUrl;
    this.trailerUrl = trailerUrl;
    this.trending = trending;
    this.homepageUrl = homepageUrl;
    this.status = status;
    this.runtime = runtime;
    this.keywords = keywords;
    this.recommendations = recommendations;
  }

}
