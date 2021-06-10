package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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