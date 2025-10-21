public class Pet {

    private String name;
    private int age;
    private String species;
    private Person owner;

    public Pet(String name, String species, int age) {
        this.name = name;
        this.species = species;
        this.age = age;
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSpecies() { return species; }
    public Person getOwner() { return owner; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setSpecies(String species) { this.species = species; }
    public void setOwner(Person owner) { this.owner = owner; }

}
