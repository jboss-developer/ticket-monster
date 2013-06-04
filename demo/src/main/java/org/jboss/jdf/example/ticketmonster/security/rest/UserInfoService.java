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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.security.AuthorizationManager;
import org.picketlink.Identity;
import org.picketlink.idm.model.User;

/**
 *
 */
@Named
@Path ("/userinfo")
@RequestScoped
public class UserInfoService {

    @Inject
    private Identity identity;
    
    @Inject
    private AuthorizationManager authorizationManager;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response info() {
        Map<String, Object> data = new HashMap<String, Object>();

        User user = (User) this.identity.getAgent();

        data.put("user", user);
        data.put("administrator", this.authorizationManager.isAdmin());
        
        return Response.ok().entity(data).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    
}