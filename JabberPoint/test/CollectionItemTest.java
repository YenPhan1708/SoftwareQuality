import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class CollectionItemTest {
    private CollectionItem collectionItem;

    @BeforeEach
    public void setup() {
        collectionItem = new CollectionItem(2);
    }

    @Test
    public void testConstructorAndLevel() {
        assertEquals(2, collectionItem.getLevel());
    }

    @Test
    public void testToStringEmpty() {
        String result = collectionItem.toString();
        assertNotNull(result);
        assertTrue(result.contains("CollectionItem"));
    }

    @Test
    public void testToStringAfterAdd() {
        String result = collectionItem.toString();
        assertTrue(result.contains("CollectionItem"));
        assertTrue(result.contains("Hello"));
    }
}
