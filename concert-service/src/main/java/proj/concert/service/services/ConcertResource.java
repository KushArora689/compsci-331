package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;


@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    EntityManager em = PersistenceManager.instance().createEntityManager();

    private final static Map<ConcertInfoSubscriptionDTO, AsyncResponse> subscriptions = new HashMap<>();


    // return a single concert based on its ID
    @GET
    @Path("/concerts/{id}")
    public Response getConcert(@PathParam("id") long id) {

        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;

        try {
            tx.begin();
            Concert concert = em.find(Concert.class, id);
            tx.commit();

            if (concert == null) {
                builder = Response.status(404);
            } else {
                ConcertDTO concertdto = ConcertMapper.toDTO(concert);
                builder = Response.ok(concertdto);
            }

        } finally {
            em.close();
        }
        return builder.build();

    }

    @GET
    @Path("/concerts")
    public Response retrieveAllConcerts() {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<ConcertDTO> concertsDTO = new ArrayList<>();
        try {
            tx.begin();
            List<Concert> concerts = em.createQuery("SELECT cs FROM Concert cs", Concert.class).getResultList();


            for (Concert concert : concerts) {
                concertsDTO.add(ConcertMapper.toDTO(concert));
            }
            tx.commit();
        } finally {
            em.close();
        }
        return Response.ok(concertsDTO).build();


    }


    @GET
    @Path("/performers/{id}")
    public Response getPerformer(@PathParam("id") long id) {

        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;
        try {

            tx.begin();
            Performer performer = em.find(Performer.class, id);
            tx.commit();

            if (performer == null) {
                builder = Response.status(404);
            } else {
                PerformerDTO performerDTO = PerformerMapper.toDTO(performer);
                builder = Response.ok(performerDTO);
            }

        } finally {
            em.close();
        }
        return builder.build();
    }

    @GET
    @Path("/performers")
    public Response retrieveAllPerformers() {

        EntityTransaction tx = em.getTransaction();
        List<PerformerDTO> PerformerDTOs = new ArrayList<>();
        try {
            tx.begin();
            List<Performer> Performers = em.createQuery("SELECT p FROM Performer p", Performer.class).getResultList();


            for (Performer performer : Performers) {
                PerformerDTOs.add(PerformerMapper.toDTO(performer));
            }
            tx.commit();
        } finally {
            em.close();
        }
        return Response.ok(PerformerDTOs).build();


    }
//summaries
    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        EntityTransaction tx = em.getTransaction();
        List<ConcertSummaryDTO> concertSummaryDTOs;
        try {
            tx.begin();
            List<Concert> summaries = em.createQuery("SELECT cs FROM Concert cs", Concert.class).getResultList();
            tx.commit();

            concertSummaryDTOs = new ArrayList<>();
            for (Concert summary : summaries) {
                concertSummaryDTOs.add(ConcertSummaryMapper.toDTO(summary));
            }


        } finally {
            em.close();
        }
        return Response.ok(concertSummaryDTOs).build();
    }

    // Logs the user in, depending on the username and password it returns the status code, 200 if successfull
    @POST
    @Path("/login")
    public Response login(UserDTO uDTO, @CookieParam("c") Cookie c) {

        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;
        try {
            tx.begin();
            List<User> usss = em.createQuery("SELECT u FROM User u WHERE u.username = :username and u.password = :password", User.class)
                    .setParameter("username", uDTO.getUsername())
                    .setParameter("password", uDTO.getPassword()).getResultList();
            //LOGGER.info("Attempting login for user: " + uDTO.getUsername() + " with password: " + uDTO.getPassword());
            if (usss.isEmpty()) {
                builder = Response.status(Response.Status.UNAUTHORIZED);


            } else {
                User user = usss.get(0);
                UserDTO loggedInUser = UserMapper.toDTO(user);
                NewCookie cookie;
                if (user.getUId() == null) {
                    cookie = makeCookie(null);
                    user.setUId(cookie.getValue());
                    em.merge(user);
                } else {
                    cookie = NewCookie.valueOf(user.getUId());
                }
                tx.commit();
                builder = Response.ok(loggedInUser).cookie(cookie);
            }
        } catch (NoResultException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } finally {
            em.close();
        }

        return builder.build();
    }

    // Gets a single booking for a user given the user id
    @GET
    @Path("/bookings/{id}")
    public Response getSingleBookingForUser(@PathParam("id") Long id, @CookieParam("auth") Cookie auth) {
        EntityTransaction tx = em.getTransaction();
        if (auth != null) {
            try {
                tx.begin();
                TypedQuery<User> uQ = em.createQuery("select u from User u where u.UId = :uuid", User.class).setParameter("uuid", auth.getValue());
                User u1 = uQ.getSingleResult();
                Booking bkg = em.find(Booking.class, id);
                tx.commit();
                if (bkg == null || !Objects.equals(u1.getId(), bkg.getUserId())) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                } else {
                    BookingDTO bookingDTO = BookingMapper.toDTO(bkg);
                    return Response.ok(bookingDTO).build();
                }
            } catch (NoResultException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } finally {
                em.close();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    // function that actually makes the booking
    @POST
    @Path("/bookings")
    public Response makeBooking(BookingRequestDTO bok, @CookieParam("auth") Cookie auth) {
        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;
        if (auth == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            tx.begin();
            BookingRequest request = BookingRequestMapper.toDomainModel(bok);
            List<Concert> concerts = em.createQuery("SELECT cs FROM Concert cs where cs.id = :id", Concert.class)
                    .setParameter("id", request.getConcertId()).getResultList();
            Concert concert = null;
            for (Concert t : concerts) {
                for (LocalDateTime d : t.getDates()) {
                    if (d.equals(bok.getDate())) {
                        concert = t;
                        break;
                    }

                }
            }
            if (concert == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            TypedQuery<User> userQuery = em
                    .createQuery("select u from User u where u.UId = :uuid", User.class)
                    .setParameter("uuid", auth.getValue());
            User user = userQuery.getSingleResult();
            Set<Seat> seatsToBook = new HashSet<>();

            for (String i : request.getSeatLabels()) {
                List<Seat> seats = em.createQuery("select s from Seat s where s.label = :label and s.date = :date", Seat.class)
                        .setParameter("label", i)
                        .setParameter("date", request.getDate()).getResultList();
                if (seats.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                for (Seat s : seats) {
                    if (s.getLabel().equals(i) && !s.getIsBooked()) {
                        s.setIsBooked(true);
                        s.setBookingStatus(BookingStatus.Booked);
                        seatsToBook.add(s);
                        em.merge(s);
                    } else {
                        //LOGGER.debug("Seat " + label + " is already booked.");
                        return Response.status(Response.Status.FORBIDDEN).build();
                    }
                }
            }

            Booking booking = new Booking(
                    request.getConcertId(),
                    request.getDate(),
                    seatsToBook
            );
            booking.setUserId(user.getId());
            user.addBooking(booking);
            em.persist(booking);
            em.merge(user);

            List<Seat> remainingSeats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = :isBooked", Seat.class)
                    .setParameter("date", booking.getDate())
                    .setParameter("isBooked", false)
                    .getResultList();
            if (subscriptions.size() != 0) {
                notification(booking.getConcertId(), remainingSeats.size());
            }
            tx.commit();

            builder = Response
                    .created(URI.create("/concert-service/bookings/" + booking.getBookingId()))
                    .entity(BookingMapper.toDTO(booking));


        } catch (NoResultException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } finally {
            em.close();
        }
        return builder.build();

    }


    //gets all the bookings for the user
    @GET
    @Path("/bookings")
    public Response getAllBookingsForUser(@CookieParam("auth") Cookie auth) {
        EntityTransaction tx = em.getTransaction();
        ResponseBuilder bd;
        if (auth != null) {
            try {
                tx.begin();
                TypedQuery<User> uQ = em
                        .createQuery("select u from User u where u.UId = :uuid", User.class)
                        .setParameter("uuid", auth.getValue());
                User u = uQ.getSingleResult();
                TypedQuery<Booking> bQ = em.createQuery("select b from Booking b where b.userId = :userId", Booking.class).setParameter("userId", u.getId());
                List<Booking> bkgs = bQ.getResultList();
                List<BookingDTO> bookingDTOs = new ArrayList<>();
                for (Booking x : bkgs) {
                    bookingDTOs.add(BookingMapper.toDTO(x));
                }
                tx.commit();
                bd = Response.ok(bookingDTOs);
            } catch (NoResultException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } finally {
                em.close();
            }
        } else {
            bd = Response.status(Response.Status.UNAUTHORIZED);
        }
        return bd.build();
    }

    // get seats according to date
    @GET
    @Path("/seats/{date}")
    public Response getSeatsForDate(@PathParam("date") LocalDateTimeParam date,
                                    @QueryParam("status") BookingStatus status,
                                    @CookieParam("auth") Cookie auth) {

        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;

        try {
            tx.begin();
            List<Seat> seats = em.createQuery("select s from Seat s where s.date = :date", Seat.class).setParameter("date", date.getLocalDateTime()).getResultList();

            List<SeatDTO> seatDTOs = new ArrayList<>();
            tx.commit();
            for (Seat s : seats) {
                if (status == BookingStatus.Booked && s.getIsBooked()) {
                    seatDTOs.add(SeatMapper.toDTO(s));
                } else if (status == BookingStatus.Unbooked && !s.getIsBooked()) {
                    seatDTOs.add(SeatMapper.toDTO(s));
                } else if (status == BookingStatus.Any) {
                    seatDTOs.add(SeatMapper.toDTO(s));
                }
            }

            builder = Response.ok(seatDTOs);
        } finally {
            em.close();
        }

        return builder.build();
    }


    // recieves notfications about concrets
    @GET
    public void notification(long concertId, long remainingSeats) {
        synchronized (subscriptions) {

            for (Map.Entry<ConcertInfoSubscriptionDTO, AsyncResponse> subscription : subscriptions.entrySet()) {
                ConcertInfoSubscriptionDTO subdto = subscription.getKey();
                AsyncResponse resp = subscription.getValue();
                if (subdto.getConcertId() == concertId && remainingSeats < subdto.getPercentageBooked()) { resp.resume(remainingSeats); }
            }

        }
    }


    // subscribes to the receiveing notfications about concert
    @POST
    @Path("/subscribe/concertInfo")
    public void subscription(ConcertInfoSubscriptionDTO subscription, @Suspended AsyncResponse resp, @CookieParam("auth") Cookie ck) {

        EntityTransaction tx = em.getTransaction();

        if (ck == null) {
            resp.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        try {
            tx.begin();
            Concert ct = em.find(Concert.class, subscription.getConcertId());
            tx.commit();
            if (ct == null || !ct.getDates().contains(subscription.getDate())) {
                resp.resume(Response.status(Response.Status.BAD_REQUEST).build());
                return;
            }

        } finally { em.close(); }

        subscriptions.put(subscription, resp);
    }


    //    generates a new cookie
    private static NewCookie makeCookie(Cookie c) {
        NewCookie yummyCookie = null;
        if (c == null) {
            yummyCookie = new NewCookie("auth", UUID.randomUUID().toString());
        }
        return yummyCookie;
    }


}
