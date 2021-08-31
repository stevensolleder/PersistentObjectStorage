import java.util.ArrayList;
import java.util.List;

import de.stevensolleder.persistentobjectstorage.PersistentObjectStorage;
import exampleModel.*;

import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersistentObjectStorageTest
{
    private final PersistentObjectStorage persistentObjectStorage;
    private final House house;



    PersistentObjectStorageTest()
    {
        persistentObjectStorage=new PersistentObjectStorage("Test");

        List<Person> people=new ArrayList<>();
        people.add(new Person("Max Meier", 40));
        people.add(new Person("Tim Taler", 21));
        people.add(new Person("Anna Aiber", 60));
        house=new House(people);
    }


    @BeforeEach
    void resetData()
    {
        persistentObjectStorage.resetAllData();
    }


    @Test //This test depends on the username and operating system
    void testGetStoragePath()
    {
        Assertions.assertEquals("/Users/stevensolleder/Test", persistentObjectStorage.getStoragePath());
    }


    @Test
    void testIsFirstStart()
    {
        Assertions.assertTrue(persistentObjectStorage.isFirstStart());
    }

    @Test
    void testFirstStartFinished() throws IOException
    {
        persistentObjectStorage.firstStartFinished();
        Assertions.assertFalse(persistentObjectStorage.isFirstStart());
    }

    @Test
    void testResetFirstStart() throws IOException
    {
        persistentObjectStorage.firstStartFinished();
        persistentObjectStorage.resetFirstStart();
        Assertions.assertTrue(persistentObjectStorage.isFirstStart());
    }


    @Test
    void testProperlySetUpReadAndWrite() throws IOException, ClassNotFoundException
    {
        persistentObjectStorage.write(house, "house");

        final House readHouse=persistentObjectStorage.<House>read("house");

        Iterator<Person> readHousePersonIterator=readHouse.getPersonIterator();
        Assertions.assertEquals(readHousePersonIterator.next(), new Person("Max Meier", 40));
        Assertions.assertEquals(readHousePersonIterator.next(), new Person("Tim Taler", 21));
        Assertions.assertEquals(readHousePersonIterator.next(), new Person("Anna Aiber", 60));
    }


    @Test
    void testWriteWithFileNameNull()
    {
        Assertions.assertThrows(NullPointerException.class, ()->persistentObjectStorage.<House>write(house, null));
    }

    @Test
    void testReadWithFileNameNull()
    {
        Assertions.assertThrows(NullPointerException.class, ()->persistentObjectStorage.<String>read(null));
    }

    @Test
    void testReadWithUnavailableFile()
    {
        Assertions.assertThrows(FileNotFoundException.class, ()->persistentObjectStorage.<House>read("xxx"));
    }

    @Test
    void testReadWithFalseGivenType() throws IOException
    {
        persistentObjectStorage.write(house, "house");
        Assertions.assertThrows(ClassCastException.class, ()->persistentObjectStorage.<String>read("house"));
    }


    @Test
    void testResetAllData() throws IOException
    {
        persistentObjectStorage.write(house, "house");
        persistentObjectStorage.resetAllData();
        Assertions.assertThrows(FileNotFoundException.class, ()->persistentObjectStorage.<House>read("house"));
    }
}
