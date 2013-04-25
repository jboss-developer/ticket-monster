package org.jboss.jdf.example.ticketmonster.rest;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * <p>
 * This exception is thrown by RESTful services. As a subclass of {@link WebApplicationException},
 * it is translated automatically into a {@link Response}.
 * </p>
 * <p>
 * We mark it as {@link ApplicationException} because it is part of the application logic. Also,
 * we want the container to roll back automatically when it is thrown.
 * </p>
 * @author Marius Bogoevici
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are still developing this app, and want the JVM to
 * generate the serialVersionUID for us. When we put this app into production, we'll generate and embed the serialVersionUID
 */
@SuppressWarnings("serial")
@ApplicationException(inherited = true, rollback = true)
public class RestServiceException extends WebApplicationException {

    public RestServiceException() {
    }

    public RestServiceException(Response response) {
        super(response);
    }

    public RestServiceException(int status) {
        super(status);
    }

    public RestServiceException(Response.Status status) {
        super(status);
    }

    public RestServiceException(Throwable cause) {
        super(cause);
    }

    public RestServiceException(Throwable cause, Response response) {
        super(cause, response);
    }

    public RestServiceException(Throwable cause, int status) {
        super(cause, status);
    }

    public RestServiceException(Throwable cause, Response.Status status) {
        super(cause, status);
    }
}
