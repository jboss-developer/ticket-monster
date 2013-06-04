/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jdf.example.ticketmonster.security;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Group;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.SimpleGroup;
import org.picketlink.idm.model.SimpleRole;
import org.picketlink.idm.model.SimpleUser;
import org.picketlink.idm.model.User;

/**
 * <p>
 * Performs some initialization during the startup in order to populate the underlying identity stores with some data.
 * </p>
 * 
 * @author Pedro Silva
 * 
 */
@Singleton
@Startup
public class IdentityManagementInitializer {

    @Inject
    private IdentityManager identityManager;

    @PostConstruct
    public void initialize() {
        User admin = new SimpleUser("admin@ticketmonster.org");

        admin.setFirstName("Almight");
        admin.setLastName("Administrator");
        
        // let's store the admin user
        this.identityManager.add(admin);

        Password password = new Password("letmein!");

        // updates the admin password
        this.identityManager.updateCredential(admin, password);

        Role adminRole = new SimpleRole("Administrator");

        // stores the admin role
        this.identityManager.add(adminRole);

        Group adminGroup = new SimpleGroup("Administrators");

        // stores the admin group
        this.identityManager.add(adminGroup);
        
        // grants to the admin user the admin role
        this.identityManager.grantRole(admin, adminRole);
        
        // add the admin user to the admin group
        this.identityManager.addToGroup(admin, adminGroup);
        
        Role userRole = new SimpleRole("User");
        
        this.identityManager.add(userRole);
        
        Group usersGroup = new SimpleGroup("Users");
        
        this.identityManager.add(usersGroup);
    }

}
