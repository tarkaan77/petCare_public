import Home from "./pages/Home.svelte";

import Accommodations from "./pages/accommodation/Accommodations.svelte";
import AccommodationDetails from "./pages/accommodation/AccommodationDetails.svelte";
import CreateAccommodation from "./pages/accommodation/CreateAccommodation.svelte";
import AccommodationRating from "./pages/accommodation/AccommodationRating.svelte";

import Profile from "./pages/person/Profile.svelte";
import PetsByProfile from "./pages/person/PetsByProfile.svelte";
import CreatePet from "./pages/person/CreatePet.svelte";
import SetupProfile from "./pages/person/SetupProfile.svelte";
import SetupProfilePetOwner from "./pages/person/SetupProfilePetOwner.svelte";
import SetupProfileAnimalRightsActivist from "./pages/person/SetupProfileAnimalRightsActivist.svelte";

import Booking from "./pages/booking/Booking.svelte";
import CreateRatingForBooking from "./pages/booking/CreateRatingForBooking.svelte";
import AllBookings from "./pages/booking/AllBookings.svelte";

export default {
    '/': Home,
    '/home': Home,

    '/accommodations': Accommodations,
    '/accommodations/create': CreateAccommodation,
    '/accommodations/:id': AccommodationDetails,
    '/accommodations/:id/ratings': AccommodationRating,
    '/accommodations/create': CreateAccommodation,

    '/accommodations/:id/booking': Booking,
    '/profile/:id/bookings/rate/:bookingId/accommodation/:accommodationId': CreateRatingForBooking,
    '/manage-all-bookings': AllBookings,

    '/profile/:id': Profile,
    '/profile/:id/pets': PetsByProfile,
    '/profile/:id/pets/create': CreatePet,
    '/profile/:id/setup': SetupProfile,
    '/profile/:id/setup/pet-owner': SetupProfilePetOwner,
    '/profile/:id/setup/animal-rights-activist': SetupProfileAnimalRightsActivist,

}