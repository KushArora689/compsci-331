## Team Contributions 

- Kushagra Arora (ka894)
  - Initial Team Discussion
  - Participated in domain model discussion, helped with ERD
  - User, Seat, Booking Domain classes
  - User, Seat Mappers
  - Cookie Auth
  - Implemented Get Single Booking
  - Implemented Get All Bookings with Users
  - Helped with make bookings
  - Implemented Login
    

- Norbu Law (nlaw679)
  - Initial Team Discussion
  - Participated in domain model discussion
  - Performer, ConcertSummary Domain classes
  - Performer, ConcertSummary Mappers
  - Implemented retrieve single concert
  - Implemented retrieve all concerts
  - Implemented retrieve single performer and all performers
  - Implemented retrieve concert summaries
  - Implemented making single and multiple bookings
  - Implemented subscription and notification method
  - Created Organisation.md
 
- Namar Dhaliwal (ndha984)
  - Initial Team Discussion
  - Fixed all DTOs
  - Created Skeletons for all Domain classes
  - Finished Implementation of Concert
  - Participated in domain model discussion, created ERD
  - Booking, BookingRequest, ConcertInfoSubscription, Concert Domain classes
  - Booking, BookingRequest, Concert, ConcertInfoNotification, ConcertInfoSubscription Mappers
  - Implemented retrieve single concert
  - Implemented get booked seats, unbooked seats and all seats
  - Wrote Organisation.md

## Domain Model 

![image](https://github.com/CS331-2024/project-project-38/assets/127709110/01538019-311c-4543-9933-e563cae0cb25)

- `Booking`: A confirmed booking made by a logged in user for some specific concert.
- `BookingRequest`: Represents users request to book 1 ~ n seats at some concert.
- `Concert`: A non-booked out concert that users can book.
- `ConcertInfoNotification`: Represents notifications sent to users about the no. of seats for a concert.
- `ConcertInfoSubscription`: A users subscription to receive the above notifications.
- `ConcertSummary`: Gives a simplified view of concert information.
- `Performer`: Represnts performer(s) that will be part of some concert(s).
- `Seat`: A seat at a concert users can book.
- `User`: An authenticated person interacting with the web service.

In accordance with the scenario we know that many performers can perform at many concerts, thus there is a many-to-many relationship between `Performer` and `Concert`, this is not ideal to map so we broke it into a one to many association from both classes into a join table `ConcertPerformer`. `Booking` has a one to many relationship with `Seat` as one booking of concert holds 120 seats (according to venue) and as logically a `User` can make 0 or more bookings there is a one to many relationship between `User` and `Booking`. 

## Concurrency Control Strategy 

For our concurrency control strategy we used pessimistic write which makes it so all other transactions are blocked from reading or writing to the locked entity except to the one user that first accessed the entity until the lock is released. Additionally, using the entityTransaction tx object for managing database operations in our web service ensures our transactions follow ACID properties and gives options to resolve errors that may occur from things like double bookings. 



