package ch.zli.m223.service;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Booking;
import ch.zli.m223.model.Space;
import ch.zli.m223.model.SpaceType;
import ch.zli.m223.model.UserType;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class SetUpService {

  @Inject
  EntityManager entityManager;

  // these Types are needed on Startup in every environment
  @Transactional
  void setUpGeneralData(@Observes StartupEvent event) {

    var adminType = new UserType();
    adminType.setTitle("Admin");
    entityManager.persist(adminType);

    var mitgliedType = new UserType();
    mitgliedType.setTitle("User");
    entityManager.persist(mitgliedType);

    var tableType = new SpaceType();
    tableType.setTitle("single space");
    entityManager.persist(tableType);

    var tableType2 = new SpaceType();
    tableType2.setTitle("group space");
    entityManager.persist(tableType2);
  }

  // these are test data objects, only needed in dev environment
  @IfBuildProfile("dev")
  @Transactional
  void generateTestData(@Observes StartupEvent event) {
    var adminType = new UserType();
    adminType.setTitle("Admin");
    entityManager.persist(adminType);

    var mitgliedType = new UserType();
    mitgliedType.setTitle("User");
    entityManager.persist(mitgliedType);

    var tableType = new SpaceType();
    tableType.setTitle("single space");
    entityManager.persist(tableType);

    var tableType2 = new SpaceType();
    tableType2.setTitle("group space");
    entityManager.persist(tableType2);

    var space = new Space();
    space.setSpaceType(tableType);
    space.setTitle("Single Space");
    space.setPrice(Float.valueOf("150"));
    entityManager.persist(space);

    var space2 = new Space();
    space2.setSpaceType(tableType2);
    space2.setTitle("Group Space");
    space2.setPrice(Float.valueOf("250"));
    entityManager.persist(space2);

    var admin = new ApplicationUser();
    admin.setFirstname("Admin");
    admin.setSurname("Admin");
    admin.setUserType(adminType);
    admin.setEmail("mail@mail.ch");
    admin.setPassword("hfid9Ebfd//q.V");
    entityManager.persist(admin);

    var user = new ApplicationUser();
    user.setFirstname("User");
    user.setSurname("User");
    user.setUserType(mitgliedType);
    user.setEmail("rammses@radus.ch");
    user.setPassword("hvsdfv286&//q.V");
    entityManager.persist(user);

    Booking booking = new Booking();
    booking.setUser(user);
    booking.setStartDate(LocalDateTime.now().minusHours(4));
    booking.setEndDate(LocalDateTime.now().plusHours(4));
    booking.setPrice(space.getPrice());
    booking.setSpace(space);
    booking.setState(null);
    entityManager.persist(booking);

    Booking booking2 = new Booking();
    booking2.setUser(user);
    booking2.setStartDate(LocalDateTime.now().minusHours(4));
    booking2.setEndDate(LocalDateTime.now().plusHours(4));
    booking2.setPrice(space2.getPrice());
    booking2.setSpace(space2);
    booking2.setState(null);
    entityManager.persist(booking2);
  }

}
