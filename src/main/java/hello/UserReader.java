package hello;

import org.springframework.batch.item.ItemReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sakshi.verma on 8/21/2017.
 */
public class UserReader implements ItemReader<User> {

    private int nextUserIndex;
    private List<User> userData;

    UserReader() {
        initialize();
    }

    private void initialize() {
        User a = new User();
        a.setEmail("user_a@gmail.com");
        a.setName("User A");

        User b = new User();
        b.setEmail("user_b@gmail.com");
        b.setName("User B");

        User c = new User();
        c.setEmail("user_c@gmail.com");
        c.setName("User C");

        userData = Collections.unmodifiableList(Arrays.asList(a, b, c));
        nextUserIndex = 0;
    }

    @Override
    public User read() throws Exception {
        User nextUser = null;

        if (nextUserIndex < userData.size()) {
            nextUser = userData.get(nextUserIndex);
            nextUserIndex++;
        }

        return nextUser;
    }
}
