package com.alkemy.wallet.service;

import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.model.ERoles;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.impl.RolServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class IRolServiceTest {
    private List<Role> roleList = new ArrayList<>();
    private IRolService iRolService;

    private IRoleRepository iRoleRepository;

    @BeforeEach
    public void setUp(){
        Role role1 = new Role(1L, ERoles.ROLE_ADMIN,"description role1",
                new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()));

        Role role2 = new Role(2L, ERoles.ROLE_USER,"description role2",
                new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()));
        roleList.add(role1);
        roleList.add(role2);

        iRolService = new RolServiceImpl();
        iRoleRepository = Mockito.mock(IRoleRepository.class);
    }

    @Test
    public void testGetById(){
        Role role = roleList.get(0);
        Mockito.when(iRoleRepository.getReferenceById(1L)).thenReturn(role);
        Assertions.assertEquals(role.getId(),1L);
        Assertions.assertEquals(role.getName(),ERoles.ROLE_ADMIN);
        Assertions.assertEquals(role.getDescription(),"description role1");
    }
    @Test
    public void testGetByIdNull(){
        Role role = roleList.get(0);
        int number = -2;
        Mockito.when(iRoleRepository.getReferenceById((long)number)).thenReturn(role);
        Assertions.assertNotEquals(role.getId(),number);
    }

    @Test
    public void testGetAll(){
        Mockito.when(iRoleRepository.findAll()).thenReturn(roleList);
        Assertions.assertEquals(roleList.size(), 2);
    }
}