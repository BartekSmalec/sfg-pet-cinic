package guru.springframework.sfgpetclinic.bootstrap;

import guru.springframework.sfgpetclinic.model.*;
import guru.springframework.sfgpetclinic.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialitysService specialitiesService;
    private final VisitService visitService;

    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService, SpecialitysService specialitiesService, VisitService visitService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialitiesService = specialitiesService;
        this.visitService = visitService;
    }

    @Override
    public void run(String... args) throws Exception {
        int count = petTypeService.findAll().size();
        if (count == 0) {
            loadData();
        }
    }

    private void loadData() {
        PetType dog = new PetType();
        dog.setName("Dog");
        PetType savedDog = petTypeService.save(dog);

        PetType cat = new PetType();
        cat.setName("Cat");
        PetType savedCat = petTypeService.save(cat);

        Speciality radiology = new Speciality();
        radiology.setDescription("Radiology");

        Speciality surgery = new Speciality();
        surgery.setDescription("Surgery");

        Speciality dentistry = new Speciality();
        dentistry.setDescription("Dentistry");

        Speciality savedRadiology = specialitiesService.save(radiology);
        Speciality savedSurgery = specialitiesService.save(surgery);
        Speciality savedDentistry = specialitiesService.save(dentistry);

        Owner owner1 = new Owner();
        owner1.setFirstName("Michael");
        owner1.setLastName("Weston");
        owner1.setAddress("123 Bcickerel");
        owner1.setCity("Miami");
        owner1.setTelephone("12345667");


        Owner.builder().city("test").address("test").build();

        Pet mikesPet = new Pet();
        mikesPet.setPetType(savedDog);
        mikesPet.setOwner(owner1);
        mikesPet.setBirthDate(LocalDate.now());
        mikesPet.setName("Rosco");
        owner1.getPets().add(mikesPet);

        ownerService.save(owner1);

        Owner owner2 = new Owner();
        owner2.setFirstName("Fiona");
        owner2.setLastName("Glenn");
        owner2.setAddress("123 Bcickerel");
        owner2.setCity("Miami");
        owner2.setTelephone("12345667");


        Pet fionaCat = new Pet();
        fionaCat.setName("Just Cat");
        fionaCat.setOwner(owner2);
        fionaCat.setBirthDate(LocalDate.now());
        fionaCat.setPetType(savedCat);
        owner2.getPets().add(fionaCat);

        ownerService.save(owner2);

        System.out.println("Loaded Owners");

        Visit catVisit = new Visit();
        catVisit.setPet(fionaCat);
        catVisit.setDescription("Snezzy Kitty");
        catVisit.setDate(LocalDate.now());

        visitService.save(catVisit);

        Vet vet1 = new Vet();
        vet1.setFirstName("Sam");
        vet1.setLastName("Axe");
        vet1.getSpecialitySet().add(savedRadiology);
        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jessica");
        vet2.setLastName("Porter");
        vet2.getSpecialitySet().add(savedSurgery);
        vetService.save(vet2);

        System.out.println("Loaded vets");
    }
}
