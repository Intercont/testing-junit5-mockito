package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceBDDTest {

    @InjectMocks
    private VisitSDJpaService service;

    @Mock
    private VisitRepository visitRepository;

    @DisplayName("Find All Test")
    @Test
    void findAll() {
        //GIVEN
        Set<Visit> visits = new HashSet<>();
        visits.add(new Visit());
        given(visitRepository.findAll()).willReturn(visits);

        //WHEN
        Set<Visit> result = service.findAll();

        //THEN
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        then(visitRepository).should().findAll();
    }

    @DisplayName("Find by ID Test")
    @Test
    void findById() {
        //GIVEN
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(mock(Visit.class)));

        //WHEN
        Visit result = service.findById(anyLong());

        //THEN
        then(visitRepository).should().findById(anyLong());
        assertNotNull(result);
    }

    @DisplayName("Save Test")
    @Test
    void save() {
        //GIVEN
        String testValidation = "Saved";
        Visit savedVisit = new Visit();
        savedVisit.setDescription(testValidation);
        given(visitRepository.save(savedVisit)).willReturn(savedVisit);

        //WHEN
        Visit resultSaved = service.save(savedVisit);

        //THEN
        then(visitRepository).should().save(any(Visit.class));
        assertNotNull(resultSaved);
        assertEquals(testValidation, resultSaved.getDescription());
    }

    @DisplayName("Delete Test")
    @Test
    void delete() {
        //WHEN
        service.delete(any(Visit.class));
        //THEN
        then(visitRepository).should(atLeastOnce()).delete(any());
    }

    @DisplayName("Delete by ID Test")
    @Test
    void deleteById() {
        //WHEN
        service.deleteById(anyLong());
        //THEN
        then(visitRepository).should().deleteById(any());
    }
}