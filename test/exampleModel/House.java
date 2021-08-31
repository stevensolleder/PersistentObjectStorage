package exampleModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class House implements Serializable
{
    private List<Person> persons;

    public House(List<Person> persons)
    {
        this.persons=persons;
    }

    public Iterator<Person> getPersonIterator()
    {
        return persons.iterator();
    }
}
