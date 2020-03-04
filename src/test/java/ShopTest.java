import ShopifyProj.Item;
import ShopifyProj.Shop;
import ShopifyProj.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShopTest {
    private Shop testShop;

    @Before
    public void setUp() {
        testShop = new Shop();
    }

    @After
    public void teardown() {
        testShop = null;
    }

    @Test
    public void testAddItem(){
        Item newItem = new Item("ITEM_1");

        testShop.addItem(newItem);

        assertEquals(testShop.getItem(newItem.getId()), newItem);
        assertEquals(testShop.getItems().size(), 1);
    }

    @Test
    public void testRemoveItem(){
        Item newItem = new Item("ITEM_1");
        Item newItem2 = new Item("ITEM_2");

        testShop.addItem(newItem);
        testShop.addItem(newItem2);
        testShop.removeItemWithId(newItem.getId());

        assertEquals(testShop.getItem(newItem2.getId()), newItem2);
    }

    @Test
    public void testClearItems(){
        Item buddy1 = new Item("ITEM_1");
        Item buddy2 = new Item("ITEM_2");
        Item buddy3 = new Item("ITEM_3");

        testShop.addItem(buddy1);
        testShop.addItem(buddy2);
        testShop.addItem(buddy3);

        assertEquals(testShop.getItems().size(), 3);

        testShop.clearItems();

        assertEquals(testShop.getItems().size(), 0);
    }

    @Test
    public void testAddTag(){
        Tag newTag = new Tag("TAG_1");

        testShop.addTag(newTag);

        assertEquals(testShop.getTag(newTag.getId()), newTag);
        assertEquals(testShop.getTags().size(), 1);
    }

    @Test
    public void testRemoveTag(){
        Tag newTag = new Tag("TAG_1");
        Tag newTag2 = new Tag("TAG_2");

        testShop.addTag(newTag);
        testShop.addTag(newTag2);
        testShop.removeTagWithId(newTag.getId());

        assertEquals(testShop.getTag(newTag2.getId()), newTag2);
    }

    @Test
    public void testClearTags(){
        Tag newTag = new Tag("TAG_1");
        Tag newTag2 = new Tag("TAG_2");
        Tag newTag3 = new Tag("TAG_3");

        testShop.addTag(newTag);
        testShop.addTag(newTag2);
        testShop.addTag(newTag3);

        assertEquals(testShop.getTags().size(), 3);

        testShop.clearTags();

        assertEquals(testShop.getTags().size(), 0);
    }
}