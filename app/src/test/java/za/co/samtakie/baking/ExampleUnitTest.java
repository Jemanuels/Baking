package za.co.samtakie.baking;

import android.provider.BaseColumns;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import za.co.samtakie.baking.data.BakingContract;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void inner_class_exists() throws Exception {
        Class[] innerClass = BakingContract.class.getDeclaredClasses();
        assertEquals("There should be 1 Inner class inside the contract class", 1, innerClass.length);
    }

    @Test
    public void inner_class_type_correct() throws Exception{
        Class[] innerClasses = BakingContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);

        Class entryClass = innerClasses[0];
        assertTrue("Inner class should be implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));
    }

    @Test
    public void inner_class_members_correct() throws Exception{
        Class[] innerClasses = BakingContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);

        Class entryClass = innerClasses[0];
        Field[] allFields = entryClass.getDeclaredFields();
        assertEquals("There should be exactly 4 String members in the inner class", 5, allFields.length);
        for(Field field : allFields){
            assertTrue("All members in the contract class should be a String", field.getType()==String.class);
            assertTrue("All members in the contract class should be a final", Modifier.isFinal(entryClass.getModifiers()));
            assertTrue("All members in the contract class should be a static", Modifier.isStatic(entryClass.getModifiers()));
        }
    }
}