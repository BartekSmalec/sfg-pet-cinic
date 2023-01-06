package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    private final OwnerService ownerService;
    private final PetTypeService petTypeService;
    private final PetService petService;

    public PetController(OwnerService ownerService, PetTypeService petTypeService, PetService petService) {

        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
        this.petService = petService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, Model model) {
        Pet pet = new Pet();
        owner.getPets().add(pet);
        pet.setOwner(owner);
        model.addAttribute("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @Transactional
    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.getPets().add(pet);
        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {

            pet.setOwner(owner);
            petService.save(pet);

            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable Long petId, Model model) {
        model.addAttribute("pet", petService.findById(petId));
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }


    @PostMapping("/pets/{petId}/edit")
    public String processUpdatePetForm(@ModelAttribute("owner") Owner owner,
                                       @Valid @ModelAttribute("pet") Pet pet,
                                       @PathVariable String petId, BindingResult bindingResult,
                                       Model model) {
        // validate the input data
        if (StringUtils.hasLength(pet.getName())) {
            Pet foundPet = owner.getPet(pet.getName());
            if (foundPet != null && !foundPet.getId().equals(Long.valueOf(petId))) {
                bindingResult.rejectValue("name", "duplicate", "already used for other pet for this owner");
            }
        }
        if (!StringUtils.hasLength(pet.getName())) {
            bindingResult.rejectValue("name", "null", "name of pet cannot be empty");
        }
        pet.setOwner(owner);
        if (bindingResult.hasErrors()) {
            model.addAttribute("pet", pet);
            return "pets/createOrUpdatePet";
        }

        Pet foundPet = petService.findById(Long.valueOf(petId));
        foundPet.setOwner(owner);
        foundPet.setPetType(pet.getPetType());
        foundPet.setName(pet.getName());
        foundPet.setBirthDate(pet.getBirthDate());
        petService.save(foundPet);
        return "redirect:/owners/" + owner.getId();
    }
}
