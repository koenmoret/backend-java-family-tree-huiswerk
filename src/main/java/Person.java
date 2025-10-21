import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Person {

    private String name;
    private String middleName;
    private String lastName;
    private String sex;
    private int age;
    private Person mother;
    private Person father;
    private List<Person> children = new ArrayList<>();
    private List<Pet> pets;

    // Gevraagde constructor: name, lastName, sex, sex
    public Person(String name, String lastName, String sex, int age) {
        this.name = name;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
    }

    // Reeds aanwezige variant (volgens eis met middleName)
    public Person(String name, String middleName, String lastName, String sex, int age) {
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
    }

    // Getters
    public String getName() { return name; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getSex() { return sex; }
    public int getAge() { return age; }
    public Person getMother() { return mother; }
    public Person getFather() { return father; }
    public List<Person> getChildren() { return children; }
    public List<Pet> getPets() { return pets; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setSex(String sex) { this.sex = sex; }
    public void setAge(int age) { this.age = age; }

    // addParents: zet ouders én zorgt dat dit kind bij de ouders geregistreerd wordt
    public void addParents(Person mother, Person father) {
        if (mother != null) {
            this.mother = mother;
            if (!mother.children.contains(this)) {
                mother.children.add(this);
            }
        }
        if (father != null) {
            this.father = father;
            if (!father.children.contains(this)) {
                father.children.add(this);
            }
        }
    }

    // addChild: zet kind bij deze persoon, en (indien logisch) zet ouderrol
    public void addChild(Person child) {
        Objects.requireNonNull(child, "child cannot be null");
        if (!children.contains(child)) {
            children.add(child);
        }
        // Als dit een moeder is, zet moeder; als vader, zet vader; anders overslaan
        if (this.equals(child.mother) || this.equals(child.father)) return;
        if ("female".equalsIgnoreCase(this.sex) || "vrouw".equalsIgnoreCase(this.sex)) {
            child.mother = this;
        } else if ("male".equalsIgnoreCase(this.sex) || "man".equalsIgnoreCase(this.sex)) {
            child.father = this;
        }
    }

    // addPet: simpel toevoegen aan lijst
    public void addPet(Pet pet) {
        Objects.requireNonNull(pet, "pet cannot be null");
        pets.add(pet);
    }

    // addSibling: zelfde ouders toekennen aan sibling (voor zover bekend)
    // En zorg dat de sibling ook als kind bij die ouders komt.
    public void addSibling(Person sibling) {
        Objects.requireNonNull(sibling, "sibling cannot be null");
        if (this.mother == null && this.father == null) {
            throw new IllegalStateException("Kan geen sibling toevoegen zonder bekende ouder(s).");
        }
        if (this.mother != null) {
            sibling.mother = this.mother;
            if (!this.mother.children.contains(sibling)) {
                this.mother.children.add(sibling);
            }
        }
        if (this.father != null) {
            sibling.father = this.father;
            if (!this.father.children.contains(sibling)) {
                this.father.children.add(sibling);
            }
        }
    }

    // getGrandChildren: alle kleinkinderen (children of children)
    public List<Person> getGrandChildren() {
        List<Person> result = new ArrayList<>();
        for (Person child : children) {
            result.addAll(child.children);
        }
        return result;
        // Eventueel uniek maken:
        // return result.stream().distinct().toList();
    }
}
