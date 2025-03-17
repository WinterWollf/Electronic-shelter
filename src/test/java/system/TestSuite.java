package system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AnimalShelterTest.class,
        AnimalTest.class,
        ShelterManagerTest.class
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSuite {
}
