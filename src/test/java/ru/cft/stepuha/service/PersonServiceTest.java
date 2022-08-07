package ru.cft.stepuha.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.PersonEntity;
import ru.cft.stepuha.service.exceptions.*;
import ru.cft.stepuha.service.impl.PersonServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Test
    public void createPerson_ShouldCreateNewPerson () {
        given(personRepository.loginIsUsed(eq("ecgoat"))).willReturn(true);
        given(personRepository.loginIsUsed(eq("ecgoat1"))).willReturn(false);
        given(personRepository.loginIsUsed(eq("ecgoat34"))).willReturn(false);
        given(personRepository.loginIsUsed(eq("ec,goat"))).willReturn(false);
        assertThrows(WrongNamePartFormatException.class, () -> {
            personService.createPerson("Eg0r", "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(WrongNamePartFormatException.class, () -> {
            personService.createPerson("eGor", "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(WrongNamePartFormatException.class, () -> {
            personService.createPerson("Egor", "Chup1n1", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(WrongNamePartFormatException.class, () -> {
            personService.createPerson("Egor", "ChupAn", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(WrongNamePartFormatException.class, () -> {
            personService.createPerson("Egor", "Chupin", Optional.of("Vlad1mir0vich"),
                    BigDecimal.TEN, "ecgoat1");
        });


        assertThrows(WrongNamePartFormatException.class, () -> {
            personService.createPerson("Egor", "Chupin", Optional.of("vLadimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(WrongLoginFormatException.class, () -> {
            personService.createPerson("Egor", "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ec,goat");
        });

        assertThrows(NamePartTooLongException.class, () -> {
            personService.createPerson("Egor".repeat(300), "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });
        assertThrows(NamePartTooLongException.class, () -> {
            personService.createPerson("Egor", "Chupin".repeat(1020), Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(NamePartTooLongException.class, () -> {
            personService.createPerson("Egor", "Chupin", Optional.of("Vladimirovich".repeat(100000)),
                    BigDecimal.TEN, "ecgoat1");
        });

        assertThrows(LoginTooLongException.class, () -> {
            personService.createPerson("Egor", "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat".repeat(10));
        });


        assertThrows(LoginIsUsedException.class, () -> {
            personService.createPerson("Egor", "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat");
        });

        assertDoesNotThrow(() -> {
            personService.createPerson("Egor", "Chupin", Optional.of("Vladimirovich"),
                    BigDecimal.TEN, "ecgoat1");
        });

       assertDoesNotThrow(() -> {
            personService.createPerson("Egor", "Chupin", Optional.empty(), BigDecimal.TEN,
                    "ecgoat34");
        });
    }

    @Test
    public void addMoneyToPerson_ShouldUpdateMoneyOfPersonById () {
        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(eq(existingPersonId))).willReturn(true);
        given(personRepository.personExists(eq(nonexistentPersonId))).willReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            personService.addMoneyToPerson(nonexistentPersonId, BigDecimal.TEN);
        });
        assertDoesNotThrow(() -> {
            personService.addMoneyToPerson(existingPersonId, BigDecimal.TEN);
        });
        verify(personRepository, times(1)).addMoneyToPersonById(anyLong(), any(BigDecimal.class));
        verify(personRepository, times(1)).addMoneyToPersonById(eq(existingPersonId), any(BigDecimal.class));
    }

    @Test
    public void takeMoneyFromPerson_ShouldUpdateMoneyOfPersonById () {
        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(eq(existingPersonId))).willReturn(true);
        given(personRepository.personExists(eq(nonexistentPersonId))).willReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            personService.takeMoneyFromPerson(nonexistentPersonId, BigDecimal.TEN);
        });

        PersonEntity mockPerson = Mockito.mock(PersonEntity.class);
        given(personRepository.getPersonById(existingPersonId)).willReturn(mockPerson);

        Mockito.when(mockPerson.getMoney()).thenReturn(BigDecimal.valueOf(9));
        assertThrows(NotEnoughMoneyException.class, () -> {
            personService.takeMoneyFromPerson(existingPersonId, BigDecimal.TEN);
        });

        assertDoesNotThrow(() -> {
            personService.takeMoneyFromPerson(existingPersonId, BigDecimal.valueOf(9));
        });
        verify(personRepository, times(1)).takeMoneyFromPersonById(anyLong(), any(BigDecimal.class));
        verify(personRepository, times(1)).takeMoneyFromPersonById(existingPersonId, BigDecimal.valueOf(9));
    }

    @Test
    public void getPersonByLogin_ShouldReturnPerson_OrThrowException () {
        String existingLogin = "egusator";
        String nonexistentLogin = "egusato";
        given(personRepository.personWithThisLoginExists(nonexistentLogin)).willReturn(false);
        given(personRepository.personWithThisLoginExists(existingLogin)).willReturn(true);

        assertThrows(UserNotFoundException.class, () -> {
            personService.getPersonByLogin(nonexistentLogin);
        });

        assertDoesNotThrow(() -> {
            personService.getPersonByLogin(existingLogin);
        });
    }
}
