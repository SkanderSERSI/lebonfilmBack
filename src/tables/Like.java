package tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entite representant les likes de notre application.
 */
@Entity(name = "Likes") // Indique qu'il s'agit d'une classe que l'on souhaite persister
@Table(name = "Likes") // Indique le nom de la table dans la bdd
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter // Genere automatiquement les getters
@Setter // Genere automatiquement les setters
@ToString
public class Like {

  @Id // Cle primaire
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(unique = true) // Chaque films a un identifiant unique
  private Integer id; // Identifiant du like

  @NotEmpty // Ne doit pas etre vide
  private int tmdbId; // Identifiant du film (tmdbid) associe au like

  @NotEmpty // Ne doit pas etre vide
  private String username; // Pseudo de l'utilisateur qui a liker

  public Like(String username, Integer tmdbId) {
    this.username = username;
    this.tmdbId = tmdbId;
  }

}
