package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceBDDTest {

    @Mock //mocks the repository
    SpecialtyRepository specialtyRepository;

    @InjectMocks //creates an instance of that service and injects the mocks inside of it
    SpecialitySDJpaService specialitySDJpaService;

    @Test
    void testDeleteByObject() {
        //GIVEN
        Speciality speciality = new Speciality();
        //WHEN
        specialitySDJpaService.delete(speciality);
        //THEN
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void testDeleteById() {
        //GIVEN - none (could be the long ids but for the sake of brevity, I'll let then directy in the parameters below

        //WHEN
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //THEN
        then(specialtyRepository).should(times(2)).deleteById(1L);
    }

    @Test
    void testDeleteByIdAtLeastOnce() {
        //GIVEN - none (could be the long ids but for the sake of brevity, I'll let then directy in the parameters below

        //WHEN
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //THEN
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
    }

    @Test
    void testDeleteByIdAtMost() {
        //GIVEN - none (could be the long ids but for the sake of brevity, I'll let then directy in the parameters below

        //WHEN
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //THEN
        then(specialtyRepository).should(atMost(7)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNever() {
        //GIVEN - none (could be the long ids but for the sake of brevity, I'll let then directy in the parameters below

        //WHEN
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //THEN
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
        then(specialtyRepository).should(never()).deleteById(2L); //validates if the repo has NEVER been called with 2L id
    }

    @Test
    void testDelete() {
        //when
        specialitySDJpaService.delete(new Speciality());

        //then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void findByIdTest() {
        //BDD APROACH TEST STRUCTURE: GIVEN - WHEN - THEN

        //GIVEN
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality)); //same as above but in a BDD Style

        //WHEN
        Speciality foundSpeciality = specialitySDJpaService.findById(1L);

        //THEN
        assertThat(foundSpeciality).isNotNull();
        then(specialtyRepository).should().findById(anyLong()); //same as above in the BDD Style
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }
}