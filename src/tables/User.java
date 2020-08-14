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
 * Entite representant un utilisateur de notre application.
 */
@Entity(name = "Users") // Indique qu'il s'agit d'une classe que l'on souhaite persister
@Table(name = "Users") // Indique le nom de la table dans la bdd
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter // Genere automatiquement les getters
@Setter // Genere automatiquement les setters
@ToString
public class User {

  @Id // Cle primaire
  @NotEmpty // Ne doit pas etre vide
  @Column(unique = true) // Chaque utilisateurs a un pseudo unique
  private String username; // Pseudo de l'utilisateur

  @NotEmpty // Ne doit pas etre vide
  private String lastname; // Nom de l'utilisateur

  @NotEmpty // Ne doit pas etre vide
  private String firstname; // Prenom de l'utilisateur

  @NotEmpty // Ne doit pas etre vide
  private String email; // Email de l'utilisateur

  @NotEmpty // Ne doit pas etre vide
  private String password; // Mot de passe de l'utilisateur

  @Column(length = 280) // Nombre de caracteres est limite a 280
  private String profilBio; // Bio de l'utilisateur

  @Column(length = 500) // Nombre de caracteres est limite a 500
  private String profilImageUrl; // Url de l'image de profile de l'utilisateur

  @NotEmpty // Ne doit pas etre vide
  private String salt; // Cle de salage de l'utilisateur

  public User(String username, String lastname, String firstname, String email, String password) {
    this.username = username;
    this.lastname = lastname;
    this.firstname = firstname;
    this.email = email;
    this.password = password;
    this.profilBio = null;
    this.profilImageUrl = null;
    this.salt = null;
  }

}
