package ShopifyProj.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Image {
    private int id;
    private String url;
    private String altText;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public Image() { this("", ""); }

    public Image(String url, String altText){
        this.id = Math.toIntExact(counter.incrementAndGet());
        this.url = url;
        this.altText = altText;
    }

    public void setId(int id) { this.id = id; }

    @Id
    public int getId() { return this.id; }

    public void setUrl(String url){ this.url = url; }

    public String getUrl() { return this.url; }

    public void setAltText(String altText) { this.altText = altText; }

    public String getAltText() { return this.altText; }

    @Override
    public String toString() {
        String toRet = "";

        toRet += "URL: " + this.url + "\n";
        toRet += "ALT: " + this.altText + "\n";

        return(toRet);
    }
}
