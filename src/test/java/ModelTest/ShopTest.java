package ModelTest;

import ShopifyProj.Model.Image;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);

        testShop.addItem(newItem);

        assertEquals(testShop.getItem(newItem.getId()), newItem);
        assertEquals(testShop.getItems().size(), 1);
    }

    @Test
    public void testRemoveItem(){
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        Item newItem2 = new Item("ITEM_2", new ArrayList<Image>(), 0.0, 0);


        testShop.addItem(newItem);
        testShop.addItem(newItem2);
        assertEquals(testShop.getItem(newItem.getId()), newItem);
        assertEquals(testShop.getItem(newItem2.getId()), newItem2);
        assertEquals(testShop.getItems().size(), 2);

        String idToRem = newItem.getId();

        testShop.removeItemWithId(idToRem);

        assertEquals(testShop.getItem(idToRem), null);
        assertEquals(testShop.getItem(newItem2.getId()), newItem2);
        assertEquals(testShop.getItems().size(), 1);
    }

    @Test
    public void testClearItems(){
        Item item1 = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        Item item2 = new Item("ITEM_2", new ArrayList<Image>(), 0.0, 0);
        Item item3 = new Item("ITEM_3", new ArrayList<Image>(), 0.0, 0);

        testShop.addItem(item1);
        testShop.addItem(item2);
        testShop.addItem(item3);

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

        assertEquals(testShop.getTag(newTag.getId()), newTag);
        assertEquals(testShop.getTag(newTag2.getId()), newTag2);
        assertEquals(testShop.getTags().size(), 2);

        String idToRem = newTag.getId();

        testShop.removeTagWithId(newTag.getId());

        assertEquals(testShop.getTag(idToRem), null);
        assertEquals(testShop.getTag(newTag2.getId()), newTag2);
        assertEquals(testShop.getTags().size(), 1);

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