package nl.han.devops.service.priem;

import org.springframework.stereotype.Service;

@Service
public class FlawedPriemTester implements PriemTester {

    @Override
    public Boolean IsPriemgetal(int kandidaat) {
        if (kandidaat <= 2) {
            return false;
        }
        return true;
    }
}

