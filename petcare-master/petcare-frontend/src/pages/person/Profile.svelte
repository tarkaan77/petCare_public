<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { isAuthenticated, user, jwt_token } from "../../store";

    const api_root = window.location.origin;

    export let params = {};
    let id;

    let bookings = [];

    $: {
        id = params.id;
        getPersonAndBookings();
    }

    let person = {
        id: null,
        name: null,
        address: null,
        email: null,
        phoneNumber: null,
        associationName: null,
        pets: null,
        personType: null,
    };

    let petNames = [];

    function getPersonAndBookings() {
        getPerson().then(() => {
            if (
                $user &&
                $user.user_roles &&
                ($user.user_roles.includes("petOwner") ||
                    person.personType === "petOwner")
            ) {
                getBookings();
            }
        });
    }

    function getPerson() {
        var config = {
            method: "get",
            url: api_root + "/api/person/" + id,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        return axios(config)
            .then(function (response) {
                person = response.data;

                // Verify if the person's type is defined
                if (person.personType === "NOT_SET") {
                    alert("Please finish first the setup from your profile");
                    push(`/profile/${id}/setup`);
                }
            })
            .catch(function (error) {
                alert("Could not get person");
                console.log(error);
            });
    }

    function getBookings() {
        var config = {
            method: "get",
            url: api_root + "/api/service/booking/person/" + id,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                bookings = response.data;
                bookings.forEach((booking) => {
                    getPetName(booking.petId);
                });
            })
            .catch(function (error) {
                alert("Could not get bookings");
                console.log(error);
            });
    }

    function formatDate(isoString) {
        const date = new Date(isoString);
        return date.toLocaleDateString("de-DE");
    }

    function getPetName(petId) {
        var config = {
            method: "get",
            url: api_root + "/api/pet/" + petId,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                const pet = response.data;
                petNames.push({ id: pet.id, name: pet.name });

                // Update bookings here
                bookings = bookings.map((booking) => {
                    if (booking.petId === pet.id) {
                        return {
                            ...booking,
                            petName: pet.name,
                        };
                    }
                    return booking;
                });
            })
            .catch(function (error) {
                alert("Could not get pet name. Maybe it was deleted or you should try again later.");
                console.log(error);
            });
    }

    function changeStatus(bookingId, newStatus) {
        var config = {
            method: "put",
            url: api_root + "/api/service/booking/" + bookingId + "/status",
            headers: {
                Authorization: "Bearer " + $jwt_token,
                "Content-Type": "application/json",
            },
            data: newStatus,
        };

        axios(config)
            .then(function (response) {
                alert("Booking status updated");
                getBookings();
            })
            .catch(function (error) {
                alert("Could not update booking status");
                console.log(error);
            });
    }

    function cancelBooking(bookingId) {
        var config = {
            method: "delete",
            url: api_root + "/api/service/booking/" + bookingId,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                alert("Booking cancelled");
                getBookings();
            })
            .catch(function (error) {
                alert("Could not cancel booking");
                console.log(error);
            });
    }
</script>

<body>
    <div class="container">
        <div class="details">
            <div class="box">
                <h2>Welcome {person.name}!</h2>
            </div>
            <div class="box">
                <h3>Personal information</h3>
                {#if person.personType === "PET_OWNER"}
                    <h4><b>Pet owner</b></h4>
                {:else if person.personType === "ANIMAL_RIGHTS_ACTIVIST"}
                    <h4><b>Animal Rights Acitivist</b></h4>
                    <h4>{person.associationName}</h4>
                {/if}
                <h4>📍 {person.address}</h4>
                <h4>📧 {person.email}</h4>
                <h4>📞 {person.phoneNumber}</h4>
            </div>

            {#if person.personType === "PET_OWNER"}
                <div class="box pets">
                    <h3>Registred pets</h3>
                    {#if person.pets && person.pets.length > 0}
                        <ul>
                            {#each person.pets as pet (pet.id)}
                                <li>{pet.species}: {pet.name}</li>
                            {/each}
                        </ul>
                        <button
                            class="btn btn-info"
                            on:click={() => push("/profile/" + id + "/pets")}
                            >All pets detailed</button
                        >
                    {:else}
                        <p>No registred pets yet.</p>
                    {/if}
                    <button
                        class="btn btn-info"
                        on:click={() => push("/profile/" + id + "/pets/create")}
                        >Add New Pet</button
                    >
                </div>
            {/if}
        </div>
        <div class="right-side">
            <div class="profile-picture">
                <!-- svelte-ignore a11y-img-redundant-alt -->
                <img src={$user.picture} alt="Profile picture" />
            </div>
            <div class="box bookings">
                {#if (person.personType === "PET_OWNER") || $user.user_roles.includes("petOwner")}
                <h3>Your bookings</h3>
                {:else}
                <h3>Information for Animal Rights Activists</h3>
                {/if}
                {#if (bookings.length > 0 && person.personType === "PET_OWNER") || $user.user_roles.includes("petOwner")}
                    <table>
                        <thead>
                            <tr>
                                <th>From</th>
                                <th>To</th>
                                <th>Booking ID</th>
                                <th>Accommodation</th>
                                <th>Pet</th>
                                <th>Status</th>
                                <th>Action</th>
                                <th>Cancel</th>
                            </tr>
                        </thead>
                        <tbody>
                            {#each bookings as booking (booking.id)}
                                <tr>
                                    <td>{formatDate(booking.startDate)}</td>
                                    <td>{formatDate(booking.endDate)}</td>
                                    <td>{booking.id}</td>
                                    <td>
                                        <a
                                            href={`/#/accommodations/${booking.accommodationId}`}
                                        >
                                            {booking.accommodationId}
                                        </a></td
                                    >
                                    <td>{booking.petName}</td>
                                    <td>{booking.status}</td>
                                    <td class>
                                        {#if booking.status === "PENDING"}
                                            <button
                                                class="btn btn-warning"
                                                on:click={() =>
                                                    changeStatus(
                                                        booking.id,
                                                        "IN_PROGRESS"
                                                    )}>Check-in</button
                                            >
                                        {:else if booking.status === "IN_PROGRESS"}
                                            <button
                                                class="btn btn-warning"
                                                on:click={() =>
                                                    changeStatus(
                                                        booking.id,
                                                        "AWAITING_FOR_FEEDBACK"
                                                    )}>Check-out</button
                                            >
                                        {:else if booking.status === "AWAITING_FOR_FEEDBACK"}
                                            <button
                                                class="btn btn-warning"
                                                on:click={() =>
                                                    push(
                                                        `/profile/${id}/bookings/rate/${booking.id}/accommodation/${booking.accommodationId}`
                                                    )}>Rate</button
                                            >
                                        {/if}
                                    </td>
                                    <td>
                                        {#if booking.status !== "DONE" && booking.status !== "AWAITING_FOR_FEEDBACK"}
                                            <button
                                                class="btn btn-danger"
                                                on:click={() =>
                                                    cancelBooking(booking.id)}
                                                >X</button
                                            >
                                        {/if}
                                    </td>
                                </tr>
                            {/each}
                        </tbody>
                    </table>
                    <div class="note">
                        <b
                            ><div class="note">
                                <b>Note: </b> <br />The booking process goes
                                through different stages. When a booking is
                                first made, the status is set to "PENDING". You can
                                then proceed with the "Check-in" which changes the
                                status to "IN_PROGRESS". After your stay, you
                                can "Check-out" your booking, and the status
                                will change to "AWAITING_FOR_FEEDBACK". Lastly,
                                once your stay is completed and checked-out, the
                                booking status will become "DONE", and you will
                                no longer be able to cancel the booking. Please
                                make sure to not forget to bring your mobile
                                phone with you when you check-in your pet. We
                                will give you then the informations to the
                                airtag to track your pet.
                            </div>
                        </b>
                    </div>
                {:else if person.personType === "PET_OWNER"}
                    <p>No bookings yet.</p>
                {:else}
                    <p>
                        As Animal Rights Activist you can browse our
                        accommodations and check the address to visit the
                        accommodations. <br />No bookings are possible for your
                        person type.
                    </p>
                {/if}
            </div>
        </div>
    </div>
</body>

<style>
    h4 {
        font-size: 18px;
    }

    .container {
        display: flex;
        justify-content: space-between;
        padding: 20px;
        font-family: Arial, sans-serif;
        background-color: #f8f9fa;
    }

    .details {
        width: 45%;
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .box {
        padding: 20px;
        background-color: #ffffff;
        border: 1px solid #e9ecef;
        border-radius: 5px;
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.05);
        height: auto;
        width: 100%;
    }

    .profile-picture {
        width: 45%;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .profile-picture img {
        max-width: 100%;
        height: auto;
        border-radius: 50%;
        margin-bottom: 40px;
    }

    .bookings table {
        width: 100%;
        border-collapse: collapse;
    }

    .bookings th,
    .bookings td {
        border: 1px solid #dddddd;
        padding: 8px;
        text-align: left;
    }

    .right-side {
        width: 60%;
        display: flex;
        flex-direction: column;
        margin-left: 20px;
    }

    .bookings {
        overflow-x: auto;
    }

    .note {
        font-size: 14px;
        color: #6c757d;
        margin: 10px 0;
        color: red;
    }
</style>
