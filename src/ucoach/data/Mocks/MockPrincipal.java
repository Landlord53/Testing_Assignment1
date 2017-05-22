package ucoach.data.Mocks;

/**
 * Created by nikolai on 5/7/17.
 */
import java.security.Principal;

public class MockPrincipal implements Principal {

    private final String name;

    public MockPrincipal(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MockPrincipal) {
            final MockPrincipal other = (MockPrincipal) obj;
            return name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}