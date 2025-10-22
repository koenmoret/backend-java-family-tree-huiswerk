import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void getName() {
        String name = person1.getName();
        assertEquals(person1Name, name); //gelijk aan de verwachte waarde
    }

    @Test
    void getMiddleName() {
    }

    @Test
    void getLastName() {
    }

    @Test
    void getSex() {
    }

    @Test
    void getAge() {
    }

    @Test
    void getMother() {
    }

    @Test
    void getFather() {
    }

    @Test
    void getChildren() {
    }

    @Test
    void getPets() {
    }

    @Test
    void setName() {
    }

    @Test
    void setMiddleName() {
    }

    @Test
    void setLastName() {
    }

    @Test
    void setSex() {
    }

    @Test
    void setAge() {
    }

    @Test
    void addParents() {
    }

    @Test
    void addChild() {
    }

    @Test
    void addPet() {
    }

    @Test
    void addSibling() {
    }

    @Test
    void getGrandChildren() {
    }
}
