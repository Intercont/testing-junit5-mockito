package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock //mocks the repository
    SpecialtyRepository specialtyRepository;

    @InjectMocks //creates an instance of that service and injects the mocks inside of it
    SpecialitySDJpaService specialitySDJpaService;

    @Test
    void testDeleteById() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, times(2)).deleteById(1L); //validates if the repo has been called twice
    }

    @Test
    void testDeleteByIdAtLeastOnce() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atLeastOnce()).deleteById(1L); //validates if the repo has been called at least Once
    }

    @Test
    void testDeleteByIdAtMost() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atMost(7)).deleteById(1L); //validates if the repo has been called at most 5 times
    }

    @Test
    void testDeleteByIdNever() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atLeastOnce()).deleteById(1L);
        verify(specialtyRepository, never()).deleteById(2L); //validates if the repo has NEVER been called with 2L id
    }

    @Test
    void testDelete() {
        specialitySDJpaService.delete(new Speciality());
    }

    @Test
    void findByIdTest() {
        Speciality speciality = new Speciality();

        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(speciality));

        Speciality foundSpeciality = specialitySDJpaService.findById(1L);

        assertThat(foundSpeciality).isNotNull();

        verify(specialtyRepository).findById(anyLong());
    }

    @Test
    void testDeleteByObject() {
        Speciality speciality = new Speciality();
        specialitySDJpaService.delete(speciality);
        verify(specialtyRepository).delete(any(Speciality.class));
    }

    @Test
    void testDeleteDoThrow() {
        doThrow(new RuntimeException("kabum")).when(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class, () -> specialitySDJpaService.delete(any(Speciality.class)));
        verify(specialtyRepository).delete(any());
    }

    @Test
    void testDeleteBDDThrow() {
        //given
        willThrow(new RuntimeException("kabum invertido")).given(specialtyRepository).delete(any());
//        when
        assertThrows(RuntimeException.class, () -> specialitySDJpaService.delete(any(Speciality.class)));
//        then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testFindByIdBDDThrows() {
        //given
        given(specialtyRepository.findById(anyLong())).willThrow(new RuntimeException("kabum"));
        //when
        assertThrows(RuntimeException.class, () -> specialitySDJpaService.findById(anyLong()));
        //then
        then(specialtyRepository).should().findById(anyLong());
    }
}