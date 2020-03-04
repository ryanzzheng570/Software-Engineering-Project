package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Tag {
    private int id;

    private String tagName;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public Tag() {
        this("");
    }

    public Tag(String name) {
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.tagName = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId() {
        return (this.id);
    }

    public void setTagName(String newName) {
        this.tagName = newName;
    }

    public String getTagName() {
        return (this.tagName);
    }

    @Override
    public String toString() {
        String toRet = "";

        toRet += this.tagName;

        return (toRet);
    }
}
