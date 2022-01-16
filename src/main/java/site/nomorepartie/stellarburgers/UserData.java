package site.nomorepartie.stellarburgers;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserData {

    private static Faker faker = new Faker();
    private String email;
    private String password;
    private String name;

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserData() {
    }

    public static UserData getRandom() {
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password();
        final String name = faker.name().name();
        return new UserData(email, password, name);
    }

    public static UserData getWithoutPassword() {
        return new UserData().setEmail(faker.internet().emailAddress()).setName(faker.name().name());
    }

    public static UserData getWithoutName() {
        return new UserData().setEmail(faker.internet().emailAddress()).setPassword(faker.internet().password());
    }

    public static UserData getWithoutEmail() {
        return new UserData().setPassword(faker.internet().password()).setName(faker.name().name());
    }

    public static UserData login(UserData user) {
        return new UserData().setEmail(user.getEmail()).setPassword(user.getPassword());
    }

    public static UserData loginWithInvalidEmail(UserData user) {
        return new UserData().setEmail(faker.internet().emailAddress()).setPassword(user.getPassword());
    }

    public static UserData loginWithInvalidPassword(UserData user) {
        return new UserData().setEmail(user.getEmail()).setPassword(faker.internet().password());
    }

    public UserData changeEmail() {
        setEmail(faker.internet().emailAddress());
        return this;
    }

    public UserData changePassword() {
        setPassword(faker.internet().password());
        return this;
    }

    public UserData changeName() {
        setName(faker.name().name());
        return this;
    }
}
