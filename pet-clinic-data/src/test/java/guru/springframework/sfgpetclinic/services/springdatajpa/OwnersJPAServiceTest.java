package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class OwnersJPAServiceTest {

    private final String lastName = "Smith";
    Owner returnOwner;

    @Mock
    OwnerRepository ownerRepository;
    @Mock
    PetRepository repository;
    @Mock
    PetTypeRepository petTypeRepository;
    @InjectMocks
    OwnersJPAService service;

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().id(1L).lastName(lastName).build();
    }

    @Test
    void findAll() {
        Set<Owner> returnOwnerSet = new HashSet<>();
        returnOwnerSet.add(Owner.builder().id(1L).build());
        returnOwnerSet.add(Owner.builder().id(2L).build());

        Mockito.when(ownerRepository.findAll()).thenReturn(returnOwnerSet);

        Set<Owner> owners = service.findAll();

        assertNotNull(owners);
        assertEquals(2, owners.size());
    }

    @Test
    void findById() {
        Mockito.when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(returnOwner));
        Owner owner = service.findById(1L);
        assertNotNull(owner);
    }
    @Test
    void findByIdNotFound() {
        Mockito.when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Owner owner = service.findById(1L);
        Assertions.assertNull(owner);
    }

    @Test
    void save() {
        Owner ownerToSave = Owner.builder().id(1L).build();
        Mockito.when(ownerRepository.save(any())).thenReturn(returnOwner);
        Owner savedOwner = service.save(ownerToSave);
        assertNotNull(savedOwner);

        Mockito.verify(ownerRepository).save(ownerToSave);
    }

    @Test
    void delete() {
        service.delete(returnOwner);
        Mockito.verify(ownerRepository, Mockito.times(1)).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        Mockito.verify(ownerRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    void findByLastName() {
        Owner returnedOwner = Owner.builder().id(1L).lastName(lastName).build();
        Mockito.when(ownerRepository.findByLastName(any())).thenReturn(returnedOwner);
        Owner owner = service.findByLastName(lastName);
        assertEquals(lastName, owner.getLastName());
        Mockito.verify(ownerRepository, Mockito.times(1)).findByLastName(any());
    }
}