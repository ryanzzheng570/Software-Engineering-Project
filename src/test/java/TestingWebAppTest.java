package AddressBookProj;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebAppTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addAddressBook() throws Exception {
        this.mockMvc.perform(post("/addAddressBook"));
        this.mockMvc.perform(get("/viewAddressBook?id=-1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Address Book")));
    }

    @Test
    public void addBuddyInfo() throws Exception {
        String name = "Frodo";
        String phoneNumber = "2";
        String address = "1";
        String bookId = "-1";

        String addBuddyQuery = String.format("/addBuddy?name=%s&phoneNumber=%s&address=%s&bookId=%s", name, phoneNumber, address, bookId);
        String expectedResult =  String.format("Name: %s", name);

        this.mockMvc.perform(post("/addAddressBook"));
        this.mockMvc.perform(post(addBuddyQuery));
        this.mockMvc.perform(get("/viewAddressBook?id=-1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedResult)));
    }

    @Test
    public void removeBuddyInfo() throws Exception {
        String name = "Frodo";
        String phoneNumber = "2";
        String address = "1";
        String bookId = "-1";
        String buddyId = "-1";

        String addBuddyQuery = String.format("/addBuddy?name=%s&phoneNumber=%s&address=%s&bookId=%s", name, phoneNumber, address, bookId);
        String removeBuddyQuery = String.format("/removeBuddy?buddyId=%s&bookId=%s", buddyId, bookId);
        String expectedResult =  String.format("Name: %s", name);

        this.mockMvc.perform(post("/addAddressBook"));
        this.mockMvc.perform(post(addBuddyQuery));
        this.mockMvc.perform(get("/viewAddressBook?id=-1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedResult)));
        this.mockMvc.perform(post(removeBuddyQuery));
        this.mockMvc.perform(get("/viewAddressBook?id=-1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString(expectedResult))));

        this.mockMvc.perform(post(addBuddyQuery));
    }
}