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

package org.jboss.jdf.example.ticketmonster.security.rest;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Group;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.SimpleUser;
import org.picketlink.idm.model.User;

/**
 *
 */
@Path("/registration")
@Stateless
public class RegistrationService {

    private static final String MESSAGE_RESPONSE_PARAMETER = "message";

    @Inject
    private IdentityManager identityManager;

    @Inject
    private LoginService loginService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            response.put(MESSAGE_RESPONSE_PARAMETER, "Password mismatch.");
        } else {
            try {
                // if there is no user with the provided e-mail, perform registration
                if (this.identityManager.getUser(request.getEmail()) == null) {
                    performRegistration(request);

                    // if the registration was successful, we perform a silent authentication.
                    return performSilentAuthentication(request);
                } else {
                    response.put(MESSAGE_RESPONSE_PARAMETER, "This username is already in use. Try another one.");
                }
            } catch (IdentityManagementException ime) {
                response.put(MESSAGE_RESPONSE_PARAMETER, "Oops ! Registration failed, try it later.");
            }
        }
        
        return Response.ok().entity(response).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * <p>Performs a registration using the data provided by {@link RegistrationRequest}.</p>
     * <p>The roles and groups in this method were previously created by the
     * {@link org.jboss.jdf.example.ticketmonster.security.IdentityManagementInitializer} during startup.</p>
     * @param request
     */
    private void performRegistration(RegistrationRequest request) {
        User newUser = new SimpleUser(request.getEmail());

        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        this.identityManager.add(newUser);
        
        Password password = new Password(request.getPassword());

        this.identityManager.updateCredential(newUser, password);

        Role userRole = this.identityManager.getRole("User");

        this.identityManager.grantRole(newUser, userRole);
        
        Group userGroup = this.identityManager.getGroup("Users");
        
        this.identityManager.addToGroup(newUser, userGroup);
    }
    
    private Response performSilentAuthentication(RegistrationRequest request) {
        DefaultLoginCredentials authenticationRequest = new DefaultLoginCredentials();

        authenticationRequest.setUserId(request.getEmail());
        authenticationRequest.setPassword(request.getPassword());

        return this.loginService.login(authenticationRequest);
    }
}