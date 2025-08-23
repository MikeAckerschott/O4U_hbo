package nl.han.devops.service.priem;

import org.apache.commons.numbers.primes.Primes;
import org.springframework.stereotype.Service;

@Service
public class ExternalPrimeServiceOpenNumeric implements PriemTester {

    @Override
    public Boolean IsPriemgetal(int kandidaat) {
        return Primes.isPrime(kandidaat);
    }
}
