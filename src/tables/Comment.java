package tables;

import java.util.Date;
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
 * Entite representant les commentaires de notre application.
 */
@Entity(name = "Comments") // Indique qu'il s'agit d'une classe que l'on souhaite persister
@Table(name = "Comments") // Indique le nom de la table dans la bdd
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter // Genere automatiquement les getters
@Setter // Genere automatiquement les setters
@ToString
public class Comment {

  @Id // Cle primaire
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(unique = true) // Chaque commentaires a un identifiant unique
  private Integer id; // Identifiant du commentaire

  @NotEmpty // Ne doit pas etre vide
  @Column(length = 280) // Nombre de caracteres est limite a 280
  private String content; // Contenu du commentaire

  @NotEmpty // Ne doit pas etre vide
  private int tmdbId; // Identifiant du film (tmdbid) associe au commentaire

  @NotEmpty // Ne doit pas etre vide
  private String username; // Pseudo de l'utilisateur qui a ecrit le commentaire

  @NotEmpty // Ne doit pas etre vide
  private Date date; // Date de publication du commentaire

  public Comment(String username, int tmdbId, String content) {
    this.username = username;
    this.tmdbId = tmdbId;
    this.content = content;
    this.date = new Date();
  }

}
