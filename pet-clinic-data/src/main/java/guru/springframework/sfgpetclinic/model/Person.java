package guru.springframework.sfgpetclinic.model;

public class Person extends BaseEntity{
    private String firstName;
    private String lastNamel;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNamel() {
        return lastNamel;
    }

    public void setLastNamel(String lastNamel) {
        this.lastNamel = lastNamel;
    }
}
