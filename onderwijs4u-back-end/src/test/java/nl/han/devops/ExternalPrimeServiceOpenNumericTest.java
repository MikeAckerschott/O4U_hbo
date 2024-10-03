package nl.han.devops;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalPrimeServiceOpenNumericTest {

    @Autowired
    private nl.han.devops.service.priem.ExternalPrimeServiceOpenNumeric ExternalPrimeServiceOpenNumeric;

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 99, 100})
    void isPriemgetalExternal(int value) {
        boolean result = ExternalPrimeServiceOpenNumeric.IsPriemgetal(value);

        Assertions.assertFalse(result, value + " zou geen priemgetal moeten zijn");
    }
}