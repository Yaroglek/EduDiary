package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);
        admin.setUsername("admin1");
    }

    @Test
    void create_shouldSaveAdmin() {
        when(userRepository.save(admin)).thenReturn(admin);

        Admin saved = adminService.create(admin);

        assertNotNull(saved);
        assertEquals("admin1", saved.getUsername());
        verify(userRepository, times(1)).save(admin);
    }

    @Test
    void create_saveNull() {
        assertThrows(IllegalArgumentException.class, () -> adminService.create(null));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnAdmin() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        Admin found = adminService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.getById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_shouldInvokeRepository() {
        adminService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}