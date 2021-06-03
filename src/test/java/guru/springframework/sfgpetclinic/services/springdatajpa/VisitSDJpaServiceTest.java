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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @InjectMocks
    private VisitSDJpaService service;

    @Mock
    private VisitRepository visitRepository;

    @DisplayName("Find All Test")
    @Test
    void findAll() {
        when(visitRepository.findAll()).thenReturn(new HashSet<>());

        Set<Visit> result = service.findAll();

        verify(visitRepository, atLeastOnce()).findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    @DisplayName("Find by ID Test")
    @Test
    void findById() {
        when(visitRepository.findById(anyLong())).thenReturn(Optional.of(mock(Visit.class)));

        Visit result = service.findById(anyLong());

        verify(visitRepository).findById(anyLong());

        assertNotNull(result);
    }

    @DisplayName("Save Test")
    @Test
    void save() {
        String testValidation = "Saved";

        Visit savedVisit = new Visit();
        savedVisit.setDescription(testValidation);

        when(visitRepository.save(savedVisit)).thenReturn(savedVisit);

        Visit resultSaved = service.save(savedVisit);

        verify(visitRepository).save(any(Visit.class));

        assertNotNull(resultSaved);
        assertEquals(testValidation, resultSaved.getDescription());
    }

    @DisplayName("Delete Test")
    @Test
    void delete() {
        service.delete(any(Visit.class));
        verify(visitRepository, atLeastOnce()).delete(any());
    }

    @DisplayName("Delete by ID Test")
    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(visitRepository).deleteById(any());
    }
}