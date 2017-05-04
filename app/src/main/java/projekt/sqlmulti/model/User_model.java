package projekt.sqlmulti.model;

/**
 * Created by Darek on 2016-12-02.
 */

public class User_model {

    long id;
    String name;
    String email;
    String password;
    String created_at;

    // constructors
    public User_model() {
    }

    public User_model(String name, String email, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
    }


    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {this.email = email; }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() { return this.email; }

    public String getCreated_at() {return  this.created_at; }
}
