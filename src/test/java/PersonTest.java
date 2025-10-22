import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonCoverageTest {

    private Person male;     // "male" triggert vader-logica in addChild
    private Person female;   // "female" triggert moeder-logica in addChild

    @BeforeEach
    void setUp() {
        // arrange
        male = new Person("Koen", "Moret", "male", 45);
        female = new Person("Anna", "Jansen", "female", 44);
    }

    // ---------- helpers ----------
    private static void setPrivate(Object target, String field, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            fail("Kon veld '" + field + "' niet zetten: " + e.getMessage());
        }
    }
    private static Object getPrivate(Object target, String field) {
        try {
            Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            fail("Kon veld '" + field + "' niet lezen: " + e.getMessage());
            return null;
        }
    }

    // ---------- constructors ----------
    @Test
    void ctor_withoutMiddleName_setsAllFields() {
        // act
        Person p = new Person("Mart", "Moret", "male", 12);
        // assert
        assertEquals("Mart", p.getName());
        assertNull(p.getMiddleName());
        assertEquals("Moret", p.getLastName());
        assertEquals("male", p.getSex());
        assertEquals(12, p.getAge());
        assertNotNull(p.getChildren());
    }

    @Test
    void ctor_withMiddleName_setsAllFields() {
        // act
        Person p = new Person("Lisa", "van", "Moret", "female", 10);
        // assert
        assertEquals("Lisa", p.getName());
        assertEquals("van", p.getMiddleName());
        assertEquals("Moret", p.getLastName());
        assertEquals("female", p.getSex());
        assertEquals(10, p.getAge());
    }

    // ---------- getters / setters ----------
    @Test
    void gettersAndSetters_coverAllSimpleFields() {
        // arrange
        male.setName("Coen");
        male.setMiddleName("van");
        male.setLastName("Jansen");
        male.setSex("other");
        male.setAge(46);

        // act + assert
        assertEquals("Coen", male.getName());
        assertEquals("van", male.getMiddleName());
        assertEquals("Jansen", male.getLastName());
        assertEquals("other", male.getSex());
        assertEquals(46, male.getAge());
    }

    @Test
    void getMotherFather_defaultNull_thenSetViaAddParentsAndReflection() {
        // arrange & act & assert defaults
        assertNull(male.getMother());
        assertNull(male.getFather());

        // arrange: add only mother
        Person mom = new Person("Mia", "Bos", "female", 70);
        male.addParents(mom, null);

        // assert
        assertSame(mom, male.getMother());
        assertNull(male.getFather());
        assertTrue(mom.getChildren().contains(male));

        // arrange: add only father (on a new person to cover branch)
        Person m2 = new Person("Jan", "Bos", "male", 72);
        Person child = new Person("Noah", "Bos", "male", 5);

        // act
        child.addParents(null, m2);

        // assert
        assertNull(child.getMother());
        assertSame(m2, child.getFather());
        assertTrue(m2.getChildren().contains(child));

        // arrange: set by reflection too to cover getter path
        Person mom2 = new Person("Ine", "Bos", "female", 68);
        setPrivate(child, "mother", mom2);

        // act
        Person resMom = child.getMother();

        // assert
        assertSame(mom2, resMom);
    }

    @Test
    void getChildren_exposesLiveList() {
        // arrange
        assertNotNull(male.getChildren());
        assertTrue(male.getChildren().isEmpty());

        Person c = new Person("Kid", "Moret", "male", 7);

        // act (mutate via returned list)
        male.getChildren().add(c);

        // assert
        assertEquals(1, male.getChildren().size());
        assertSame(c, male.getChildren().get(0));
    }

    @Test
    void getPets_defaultNull_thenSetWithReflection() {
        // act + assert default
        assertNull(male.getPets());

        // arrange
        ArrayList<Pet> list = new ArrayList<>();
        setPrivate(male, "pets", list);

        // act
        List<Pet> result = male.getPets();

        // assert
        assertSame(list, result);
        assertTrue(result.isEmpty());
    }

    // ---------- addChild ----------
    @Test
    void addChild_addsOnce_andSetsFatherForMale() {
        // arrange
        Person child = new Person("Mart", "Moret", "male", 12);

        // act
        male.addChild(child);
        male.addChild(child); // tweede keer zou niet nog eens toevoegen (contains-check)

        // assert
        assertEquals(1, male.getChildren().size());
        assertSame(child, male.getChildren().get(0));
        assertSame(male, child.getFather());
        assertNull(child.getMother());
    }

    @Test
    void addChild_setsMotherForFemale() {
        // arrange
        Person child = new Person("Lisa", "Jansen", "female", 10);

        // act
        female.addChild(child);

        // assert
        assertSame(female, child.getMother());
        assertNull(child.getFather());
    }

    @Test
    void addChild_doesNotReassign_whenParentAlreadySet() {
        // arrange: child already has mother= female
        Person child = new Person("Puck", "Jansen", "female", 8);
        setPrivate(child, "mother", female);

        // act: calling again with same female should early-return the assignment
        female.addChild(child);

        // assert (still same mother, no father set)
        assertSame(female, child.getMother());
        assertNull(child.getFather());
        assertTrue(female.getChildren().contains(child));
    }

    @Test
    void addChild_nullChild_throwsNPE() {
        // act + assert
        assertThrows(NullPointerException.class, () -> male.addChild(null));
    }

    // ---------- addPet ----------
    @Test
    void addPet_withNullPetsList_throwsNPE_thenSuccessAfterInit() {
        // arrange
        Pet p = new Pet("Binky", "Dog", 5);

        // act + assert: pets == null → NPE op pets.add(...)
        assertThrows(NullPointerException.class, () -> male.addPet(p));

        // arrange: init pets via reflectie
        setPrivate(male, "pets", new ArrayList<Pet>());

        // act
        male.addPet(p);

        // assert
        assertEquals(1, male.getPets().size());
        assertSame(p, male.getPets().get(0));
    }

    @Test
    void addPet_nullPet_throwsNPE() {
        // arrange: init pets list
        setPrivate(male, "pets", new ArrayList<Pet>());

        // act + assert
        assertThrows(NullPointerException.class, () -> male.addPet(null));
    }

    // ---------- addSibling ----------
    @Test
    void addSibling_withoutKnownParents_throwsISE() {
        // arrange
        Person sib = new Person("Bro", "Moret", "male", 40);

        // act + assert
        assertThrows(IllegalStateException.class, () -> male.addSibling(sib));
    }

    @Test
    void addSibling_withOnlyMother_setsSameMother_andAddsToMotherChildren() {
        // arrange
        Person mom = new Person("Mia", "Bos", "female", 70);
        male.addParents(mom, null);

        Person sib = new Person("Sis", "Bos", "female", 38);

        // act
        male.addSibling(sib);

        // assert
        assertSame(mom, sib.getMother());
        assertTrue(mom.getChildren().contains(male));
        assertTrue(mom.getChildren().contains(sib));
        assertNull(sib.getFather());
    }

    @Test
    void addSibling_withBothParents_setsBoth_andAddsToBothLists() {
        // arrange
        Person mom = new Person("Mia", "Bos", "female", 70);
        Person dad = new Person("Jan", "Bos", "male", 72);
        male.addParents(mom, dad);

        Person sib = new Person("Joost", "Bos", "male", 40);

        // act
        male.addSibling(sib);

        // assert
        assertSame(mom, sib.getMother());
        assertSame(dad, sib.getFather());
        assertTrue(mom.getChildren().contains(sib));
        assertTrue(dad.getChildren().contains(sib));
    }

    // ---------- getGrandChildren ----------
    @Test
    void getGrandChildren_emptyAndFilled() {
        // arrange: no grandchildren yet
        assertTrue(male.getGrandChildren().isEmpty());

        // arrange: child1 + child2
        Person child1 = new Person("Noah", "Moret", "male", 20);
        Person child2 = new Person("Saar", "Moret", "female", 18);
        male.addChild(child1);
        male.addChild(child2);

        // arrange: grandchildren
        Person gc1 = new Person("Finn", "Moret", "male", 1);
        Person gc2 = new Person("Luna", "Moret", "female", 2);
        child1.addChild(gc1);
        child2.addChild(gc2);

        // act
        List<Person> gcs = male.getGrandChildren();

        // assert
        assertEquals(2, gcs.size());
        assertTrue(gcs.contains(gc1));
        assertTrue(gcs.contains(gc2));
    }
}
