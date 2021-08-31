package exampleModel;

import java.io.Serializable;

public class Person implements Serializable
{
    private String name;
    private int age;

    public Person(String name, int age)
    {
        this.name = name;
        this.age = age;
    }


    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(o == null || getClass() != o.getClass())
        {
            return false;
        }

        Person person = (Person) o;
        return age == person.age && name.equals(person.name);
    }
}
