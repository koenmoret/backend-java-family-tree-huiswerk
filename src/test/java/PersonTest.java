import org.junit.jupiter.api.BeforeEach;

public class PersonTest {

    Person person1;
    String person1Name = "Koen";
    String person1MiddleName = "";
    String person1LastName = "Moret";
    String person1sex = "M";
    int person1Age = 45;

    @BeforeEach
    void CreatePerson1() {
        person1 = new Person(person1Name, person1LastName, person1sex, person1Age);
    }
}
