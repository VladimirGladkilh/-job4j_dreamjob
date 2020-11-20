package DataStore;

import java.util.Date;
import java.util.Objects;

public class Candidate {
    private String login;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private Date birthDate;
    private String resume;

    public Candidate() {
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", firstName, middleName, lastName);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return login.equals(candidate.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    public String getResume() {
        return resume;
    }
}
