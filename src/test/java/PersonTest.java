import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    Person person1;
    String person1Name = "Koen";
    String person1LastName = "Moret";
    String person1sex = "male"; // addChild-logic verwacht "male"/"female"
    int person1Age = 45;

    Person mother;
    Person father;
    Person child1;
    Person child2;
    Person sibling1;

    Pet pet1;

    @BeforeEach
    void CreatePerson1() {
        // arrange
        person1 = new Person(person1Name, person1LastName, person1sex, person1Age);

        mother = new Person("Anna", "Moret", "female", 70);
        father = new Person("Jan", "Moret", "male", 72);

        child1 = new Person("Mart", "Moret", "male", 12);
        child2 = new Person("Lisa", "Moret", "female", 10);

        sibling1 = new Person("Joost", "Moret", "male", 40);

        pet1 = new Pet("Binky", "Dog", 5);
    }

    // Hulpmethode om private 'pets' te initialiseren
    private static void initPets(Person p) {
        try {
            Field f = Person.class.getDeclaredField("pets");
            f.setAccessible(true);
            if (f.get(p) == null) {
                f.set(p, new ArrayList<>());
            }
        } catch (Exception e) {
            fail("Kon 'pets' niet initialiseren via reflectie: " + e.getMessage());
        }
    }

    @Test
    void getName() {
        // arrange
        // (person1 is aangemaakt in @BeforeEach)

        // act
        String result = person1.getName();

        // assert
        assertEquals(person1Name, result);
    }

    @Test
    void getMiddleName() {
        // arrange
        // (standaard null)

        // act
        String initial = person1.getMiddleName();

        // assert
        assertNull(initial);

        // arrange
        person1.setMiddleName("van");

        // act
        String updated = person1.getMiddleName();

        // assert
        assertEquals("van", updated);
    }

    @Test
    void getLastName() {
        // arrange

        // act
        String result = person1.getLastName();

        // assert
        assertEquals(person1LastName, result);
    }

    @Test
    void getSex() {
        // arrange

        // act
        String result = person1.getSex();

        // assert
        assertEquals(person1sex, result);
    }

    @Test
    void getAge() {
        // arrange

        // act
        int result = person1.getAge();

        // assert
        assertEquals(person1Age, result);
    }

    @Test
    void getMother() {
        // arrange
        person1.addParents(mother, father);

        // act
        Person m = person1.getMother();

        // assert
        assertSame(mother, m);
    }

    @Test
    void getFather() {
        // arrange
        person1.addParents(mother, father);

        // act
        Person f = person1.getFather();

        // assert
        assertSame(father, f);
    }

    @Test
    void getChildren() {
        // arrange
        person1.addChild(child1);
        person1.addChild(child2);

        // act
        List<Person> kids = person1.getChildren();

        // assert
        assertEquals(2, kids.size());
        assertTrue(kids.contains(child1));
        assertTrue(kids.contains(child2));
    }

    @Test
    void getPets() {
        // arrange
        initPets(person1);

        // act
        List<Pet> pets = person1.getPets();

        // assert
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }

    @Test
    void setName() {
        // arrange
        String newName = "Coen";

        // act
        person1.setName(newName);

        // assert
        assertEquals(newName, person1.getName());
    }

    @Test
    void setMiddleName() {
        // arrange
        String mn = "van";

        // act
        person1.setMiddleName(mn);

        // assert
        assertEquals(mn, person1.getMiddleName());
    }

    @Test
    void setLastName() {
        // arrange
        String ln = "Jansen";

        // act
        person1.setLastName(ln);

        // assert
        assertEquals(ln, person1.getLastName());
    }

    @Test
    void setSex() {
        // arrange
        String s = "female";

        // act
        person1.setSex(s);

        // assert
        assertEquals(s, person1.getSex());
    }

    @Test
    void setAge() {
        // arrange
        int newAge = 46;

        // act
        person1.setAge(newAge);

        // assert
        assertEquals(newAge, person1.getAge());
    }

    @Test
    void addParents() {
        // arrange

        // act
        person1.addParents(mother, father);

        // assert
        assertSame(mother, person1.getMother());
        assertSame(father, person1.getFather());
        assertTrue(mother.getChildren().contains(person1));
        assertTrue(father.getChildren().contains(person1));
    }

    @Test
    void addChild() {
        // arrange

        // act
        person1.addChild(child1);

        // assert
        assertTrue(person1.getChildren().contains(child1));
        assertSame(person1, child1.getFather()); // person1 is "male"
        assertNull(child1.getMother());
    }

    @Test
    void addPet() {
        // arrange
        initPets(person1);

        // act
        person1.addPet(pet1);

        // assert
        assertEquals(1, person1.getPets().size());
        assertSame(pet1, person1.getPets().get(0));
    }

    @Test
    void addSibling() {
        // arrange
        // Zonder ouders verwacht je een exception
        // act + assert
        assertThrows(IllegalStateException.class, () -> person1.addSibling(sibling1));

        // arrange: ouders koppelen
        person1.addParents(mother, father);

        // act
        person1.addSibling(sibling1);

        // assert
        assertSame(mother, sibling1.getMother());
        assertSame(father, sibling1.getFather());
        assertTrue(mother.getChildren().contains(person1));
        assertTrue(mother.getChildren().contains(sibling1));
        assertTrue(father.getChildren().contains(person1));
        assertTrue(father.getChildren().contains(sibling1));
    }

    @Test
    void getGrandChildren() {
        // arrange
        person1.addChild(child1);
        person1.addChild(child2);
        Person grandChild1 = new Person("Noah", "Moret", "male", 1);
        Person grandChild2 = new Person("Saar", "Moret", "female", 2);
        child1.addChild(grandChild1);
        child2.addChild(grandChild2);

        // act
        List<Person> grandChildren = person1.getGrandChildren();

        // assert
        assertEquals(2, grandChildren.size());
        assertTrue(grandChildren.contains(grandChild1));
        assertTrue(grandChildren.contains(grandChild2));
    }
}
