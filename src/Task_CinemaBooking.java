import java.util.*;

/*
 =======================================================================
  TASK: Cinema Seat Booking  -  a HashMap with your own class as the key
 =======================================================================

 A cinema hall has rows A to E, with seats 1 to 6 in each row.
 You will store the bookings in a HashMap:

     key   = Seat     (a class you finish below, for example B5)
     value = String   (the name of the customer who booked it)

 STEP 1  Complete Parts A to G in main(). Each part says what to print.
 STEP 2  Run the program BEFORE writing equals() and hashCode().
         Some results will be wrong. That is expected - write down which ones.
 STEP 3  Write equals() and hashCode() in the Seat class.
 STEP 4  Run it again and compare with the EXPECTED OUTPUT below.
 STEP 5  Answer the questions at the bottom of this file.

 RULES
   - Every time you use a seat, create a NEW object: new Seat('B', 5).
     A real program works the same way: when a customer types "B5",
     the program makes a new Seat object for it.
   - Do not change printChart().

 EXPECTED OUTPUT (after STEP 3)
 The order inside { } may be different in your output. HashMap has no order.

   Bookings:           {A1=Ali, C3=Mona, A2=Sara, B5=Omar}
   Who booked C3:      Mona
   Is A2 taken:        true
   Is E6 taken:        false
   Youssef -> B5:      refused, taken by Omar
   After Ali moved:    {C3=Mona, A2=Sara, B5=Omar, D2=Ali}
   Hana -> E1:         refused, seat is broken
   Seats booked:       4

        1  2  3  4  5  6
   A    .  X  .  .  .  .
   B    .  .  .  .  X  .
   C    .  .  X  .  .  .
   D    .  X  .  .  .  .
   E    #  #  .  .  .  .
*/

class Task_CinemaBooking {

    // A seat in the cinema hall, for example B5 = row B, seat number 5.
    // static so that main() can create Seat objects directly.
    static class Seat {
        final char row;     // 'A' to 'E'
        final int number;   // 1 to 6

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        // TODO 1: override equals()
        //   Two Seat objects are the same seat when the row AND the number are the same.
        //   The parameter must be of type Object, not Seat. Write @Override above it.
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;                       // same object
            if (o == null || getClass() != o.getClass()) return false; // null or other type
            Seat other = (Seat) o;                            // safe cast
            return row == other.row && number == other.number;
        }

        // TODO 2: override hashCode()
        //   Equal seats must return the same number.
        //   Use the same fields you used in equals().
        @Override
        public int hashCode() {
            return 31 * row + number;   // same fields as equals()
        }

    }

    public static void main(String[] args) {

        // Key = Seat, Value = name of the customer who booked it
        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        // Part A: book these seats using put()
        //   A1 -> Ali     A2 -> Sara     B5 -> Omar     C3 -> Mona
        // Print:  Bookings:           <the map>
        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");
        System.out.println("Bookings:           " + bookings);

        // Part B: who booked seat C3?  Use get().
        // Print:  Who booked C3:      <name>
        System.out.println("Who booked C3:      " + bookings.get(new Seat('C', 3)));

        // Part C: are seats A2 and E6 taken?  Use containsKey().
        // Print:  Is A2 taken:        <true or false>
        //         Is E6 taken:        <true or false>
        System.out.println("Is A2 taken:        " + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        " + bookings.containsKey(new Seat('E', 6)));

        // Part D: Youssef wants seat B5. Use putIfAbsent(), so a booked seat is not replaced.
        //   putIfAbsent() returns null if the seat was free,
        //   otherwise it returns the name that is already there.
        // Print:  Youssef -> B5:      booked
        //    or:  Youssef -> B5:      refused, taken by <name>
        String owner = bookings.putIfAbsent(new Seat('B', 5), "Youssef");
        if (owner == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by " + owner);
        }

        // Part E: Ali moves from seat A1 to seat D2.  Use remove() and put().
        // Print:  After Ali moved:    <the map>
        String name = bookings.remove(new Seat('A', 1));   // returns "Ali" (or null if not found)
        bookings.put(new Seat('D', 2), "Ali");
        System.out.println("After Ali moved:    " + bookings);

        // Part F: seats E1 and E2 are broken. Add them to the HashSet "broken".
        //   Hana wants seat E1. Book it only if it is not broken.  Use contains().
        // Print:  Hana -> E1:         booked
        //    or:  Hana -> E1:         refused, seat is broken
        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));
        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        // Part G: how many seats are booked?  Use size().
        // Print:  Seats booked:       <number>
        System.out.println("Seats booked:       " + bookings.size());

        // GIVEN - prints the seating chart
        System.out.println();
        printChart(bookings, broken);
    }

    // GIVEN - do not change.  Prints the hall:  X = booked   # = broken   . = free
    static void printChart(HashMap<Seat, String> bookings, HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
 STEP 2 NOTES - what was wrong BEFORE equals()/hashCode()
   Without them, Seat uses Object's version: two objects are "equal" only if they are
   the very same object in memory. Every new Seat(...) is a different object, so:
     - Who booked C3      -> null          (get() can't find the key)
     - Is A2 taken        -> false         (containsKey() fails)
     - Youssef -> B5      -> "booked"      (putIfAbsent thinks B5 is free -> double booking!)
     - After Ali moved    -> A1 is still there (remove() finds nothing) and Ali appears twice
     - Hana -> E1         -> "booked"      (broken.contains() fails)
     - Seats booked       -> 6 or more instead of 4
     - The chart          -> every seat is shown as "." (nothing is ever found)

 QUESTIONS - write your answers here

 Q1.Why did we have to override hashcode() and equals()?
   A1. HashMap/HashSet find a key in two steps:
       1) hashCode() decides which bucket to look in,
       2) equals() decides which key inside that bucket is the real match.
       By default both compare MEMORY ADDRESSES, so new Seat('B',5) is never "the same"
       as another new Seat('B',5), even though they mean the same seat.
       We override equals() to say "same row + same number = same seat", and hashCode()
       so equal seats give the same hash (and land in the same bucket).
       Rule: if a.equals(b) is true, then a.hashCode() == b.hashCode() must be true.

 Q2. Delete your hashCode() but keep equals(), and run again.
     What happens? Why?
   A2. The results are wrong again (get returns null, containsKey is false, double booking...).
       Reason: without our hashCode(), Seat uses Object.hashCode(), which depends on the
       object's identity. Two equal seats get different hash codes, so HashMap looks in a
       different bucket and never even calls equals(). (It might work by pure luck if two
       hashes happen to land in the same bucket, but you can't rely on that.)
       Lesson: equals() and hashCode() must always be overridden together.

 Q3. Why are row and number final? What could go wrong if the number
     of a booked seat could be changed after it was put in the map?
   A3. The hash code is calculated from row and number, and the HashMap stores the entry in
       the bucket matching the hash AT THE TIME of put(). If the number changed later, the
       hash would change but the entry would stay in the OLD bucket. Then get()/containsKey()
       would look in the NEW bucket and fail to find it - the entry becomes "lost" inside
       the map (it still counts in size() and can't be removed), which is a memory leak
       and a source of bugs. final makes the key immutable, so its hash never changes.

 Q4. Change equals(Object o) to equals(Seat o). What happens?
     Then also remove @Override and run again. What happens now?
   A4. With @Override still there: COMPILE ERROR - "method does not override or implement
       a method from a supertype", because equals(Seat) is not the same signature as
       equals(Object). That's exactly why @Override is useful.
       After removing @Override: it compiles, but equals(Seat) is only an OVERLOAD (a
       different method), not an override. HashMap internally calls equals(Object), which
       is still Object's identity-based version, so the program gives the WRONG results
       again - silently, with no error. (Same wrong output as in STEP 2.)

 BONUS. Replace the whole Seat class with this one line:
            record Seat(char row, int number) {}
        Run it. What happens, and why?
   A.  It works and the logic/results are correct, because a record automatically generates
       equals(), hashCode() and toString() from its components (row, number), and its
       fields are automatically private final. The only visible difference is the printing:
       the generated toString() gives Seat[row=A, number=1] instead of A1, so the map prints
       like {Seat[row=A, number=2]=Sara, ...}. To get "A1" back, override toString() inside
       the record:  record Seat(char row, int number) {
                        public String toString() { return "" + row + number; } }
       (Needs Java 16 or newer.)
*/
