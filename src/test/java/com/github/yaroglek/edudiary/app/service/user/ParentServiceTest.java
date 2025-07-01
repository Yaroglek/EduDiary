package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ParentService parentService;

    private Parent parent;

    @BeforeEach
    void setUp() {
        parent = new Parent();
        parent.setId(1L);
        parent.setUsername("parent1");
        parent.setPassword("password");
    }

    @Test
    void create_shouldSaveParent() {
        when(userRepository.save(parent)).thenReturn(parent);
        when(passwordEncoder.encode(parent.getPassword())).thenReturn("password");


        Parent saved = parentService.create(parent);

        assertNotNull(saved);
        assertEquals("parent1", saved.getUsername());
        verify(userRepository, times(1)).save(parent);
    }

    @Test
    void create_saveNull() {
        assertThrows(IllegalArgumentException.class, () -> parentService.create(null));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnParent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(parent));

        Parent found = parentService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> parentService.getById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_shouldInvokeRepository() {
        parentService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
