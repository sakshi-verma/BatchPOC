package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public User process(final User user) throws Exception {
        final String name = user.getName().toUpperCase();
        final String email = user.getEmail().toUpperCase();

        final User transformedUser = new User(name, email);

        log.info("Converting (" + user + ") into (" + transformedUser + ")");

        return transformedUser;
    }

}
