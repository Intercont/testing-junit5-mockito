package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private Model model;

    @InjectMocks
    private OwnerController ownerController;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor; //another way of declaring argument captor

    @BeforeEach
    void setUp() {
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation -> {

                    List<Owner> ownersList = new ArrayList<>();

                    String name = invocation.getArgument(0);

                    //for each test scenario, I add a condition to be covered
                    switch (name) {
                        case "%Fraga%":
                            ownersList.add(new Owner(1L, "Igor", "Fraga"));
                            return ownersList;
                        case "%DontFindMe%":
                            return ownersList;
                        case "%FindManyMore%":
                            ownersList.add(new Owner(1L, "Igor", "Fraga"));
                            ownersList.add(new Owner(2L, "Lebre", "Veloz"));
                            return ownersList;
                        default:
                            throw new RuntimeException("Invalid Argument");
                    }
                });
    }

    @Test
    void processFindFormWildcardStringAnnotation() {
        //given
        Owner owner = new Owner(1L, "Igor", "Fraga");

        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%Fraga%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);

    }

    @Test
    void processFindFormWildcardNotFound() {
        //given
        Owner owner = new Owner(1L, "Igor", "DontFindMe");

        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);

    }

    @Test
    void processFindFormWildcardFoundMany() {
        //given
        Owner owner = new Owner(1L, "Igor", "FindManyMore");
        InOrder inOrder = Mockito.inOrder(service, model); //specify the elements to validate the order of calling

        //when
        String viewName = ownerController.processFindForm(owner, bindingResult, model);

        //then
        assertThat("%FindManyMore%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);

        //inOrder Asserts
        inOrder.verify(service).findAllByLastNameLike(anyString()); //1st that should be called - the order is given by how you list the verifications
        inOrder.verify(model).addAttribute(anyString(), anyList());//2nd - after the service, the model should be called anytime

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