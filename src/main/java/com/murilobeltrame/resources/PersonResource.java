package com.murilobeltrame.resources;

import com.murilobeltrame.models.Person;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
    @GET
    public List<Person> list() {
        return Person.listAll();
    }

    @GET
    @Path("/{name}")
    public Person get(String name) {
        var foundPerson = Person.findByName(name);
        if (foundPerson == null) throw new NotFoundException();
        return foundPerson;
    }

    @POST
    @Transactional
    public Response create(Person person) {
        person.persist();
        return Response.created(URI.create("/people/"+person.name)).entity(person)
                .build();
    }

    @PUT
    @Path("/{name}")
    @Transactional
    public Response update(String name, Person person) {
        if (name != person.name) throw new BadRequestException();
        var foundPerson = Person.findByName(name);
        if (foundPerson == null) throw new NotFoundException();
        foundPerson.name = person.name;
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{name}")
    @Transactional
    public Response delete(String name) {
        var foundPerson = Person.findByName(name);
        if (foundPerson == null) throw new NotFoundException();
        foundPerson.delete();
        return Response.noContent().build();
    }
}
