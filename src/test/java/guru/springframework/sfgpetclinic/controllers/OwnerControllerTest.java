package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";
    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";

    @Mock
    private OwnerService service;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private OwnerController ownerController;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor; //another way of declaring argument captor

    @Test
    void processFindFormWildcardString() {
        //given
        Owner owner = new Owner(1L, "Igor", "Fraga");
        List<Owner> ownerList = new ArrayList<>();
        ownerList.add(owner);

        //one way of declaring ArgumentCaptor
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class); //This is able to capture the argument that is passed
        given(service.findAllByLastNameLike(captor.capture())).willReturn(ownerList);

         //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%Fraga%").isEqualToIgnoringCase(captor.getValue());

    }

    @Test
    void processFindFormWildcardStringAnnotation() {
        //given
        Owner owner = new Owner(1L, "Igor", "Fraga");
        List<Owner> ownerList = new ArrayList<>();
        ownerList.add(owner);

        //now we're getting the captor from the annotated instance variable
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture())).willReturn(ownerList);

        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%Fraga%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());

    }

    @Test
    void processCreationFormSuccess() {
        //given
        Owner savedOwner = new Owner(5L, "Igor", "Fraga");
        given(service.save(any(Owner.class))).willReturn(savedOwner);
        given(bindingResult.hasErrors()).willReturn(false);

        //when
        String result = ownerController.processCreationForm(savedOwner, bindingResult);

        //then
        assertEquals(REDIRECT_OWNERS_5, result);
    }

    @Test
    void processCreationFormError() {
        //given
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String result = ownerController.processCreationForm(mock(Owner.class), bindingResult);

        //then
        assertEquals(OWNERS_CREATE_OR_UPDATE_OWNER_FORM, result);
    }
}