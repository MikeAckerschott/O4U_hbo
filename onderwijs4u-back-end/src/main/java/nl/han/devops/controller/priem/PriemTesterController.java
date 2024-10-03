package nl.han.devops.controller.priem;

import nl.han.devops.dto.Priem;
import nl.han.devops.repositories.PriemRepository;
import nl.han.devops.service.priem.ExternalPrimeServiceOpenNumeric;
import nl.han.devops.service.priem.PriemTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/priem")
public class PriemTesterController {

    private final PriemTester priemTester;

    private final PriemRepository priemRepository;


    PriemTesterController(@Autowired ExternalPrimeServiceOpenNumeric priemTester, @Autowired PriemRepository priemRepository) {
        this.priemTester = priemTester;
        this.priemRepository = priemRepository;
    }

    @GetMapping("/{number}")
    public boolean priem(@PathVariable int number) {
        boolean exists = priemRepository.findById(number).isPresent();

        if (exists) {
            return priemRepository.findById(number)
                    .map(Priem::isPriem)
                    .orElse(false);
        }

        Priem newPriem = new Priem();
        newPriem.setGetal(number);
        newPriem.setPriem(priemTester.IsPriemgetal(number));
        priemRepository.save(newPriem);

        return Boolean.TRUE.equals(priemTester.IsPriemgetal(number));
    }

    @GetMapping("/all")
    public Iterable<Priem> all() {
        return priemRepository.findAll();
    }

}
