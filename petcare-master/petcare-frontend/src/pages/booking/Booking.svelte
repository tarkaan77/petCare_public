<script>
    import axios from "axios";
    import { jwt_token, user } from "../../store";
    import { push } from "svelte-spa-router";

    const api_root = window.location.origin;

    let startDate = "";
    let endDate = "";
    let selectedPet = "";
    let accommodationId = "";

    let pets = [];
    let accommodation = {}; // to store the accommodation details

    let userId = "";

    let today = new Date().toISOString().substr(0, 10);

    export let params = {};

    $: {
        if ($user && $user.email && $jwt_token) {
            fetchUserId($user.email).then((id) => {
                userId = id;
                accommodationId = params.id;
                getAccommodation(); 
                getPets(); 
            });
        }
    }

    function fetchUserId(email) {
        const config = {
            method: "get",
            url: `/api/person/email/${email}`,
            headers: {
                Authorization: "Bearer " + $jwt_token,
            },
        };

        return axios(config)
            .then((response) => {
                const person = response.data;
                return person.id;
            })
            .catch((error) => {
                console.error("Failed to fetch person by email", error);
            });
    }

    function getAccommodation() {
        var config = {
            method: "get",
            url: api_root + "/api/accommodation/" + accommodationId,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then((response) => {
                accommodation = response.data;
            })
            .catch((error) => {
                console.log(error);
            });
    }

    function getPets() {
        var config = {
            method: "get",
            url: api_root + "/api/person/petowner/" + userId + "/pets",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then((response) => {
                pets = response.data;
            })
            .catch((error) => {
                console.log(error);
            });
    }

    function bookAccommodation() {
        var bookingData = {
            accommodationId,
            personId: userId,
            startDate,
            endDate,
            petId: selectedPet,
        };

        var config = {
            method: "post",
            url: api_root + "/api/service/booking",
            headers: { Authorization: "Bearer " + $jwt_token },
            data: bookingData,
        };

        axios(config)
            .then(function (response) {
                alert("Booking successful!");
                push(`/profile/${userId}`);
            })
            .catch(function (error) {
                console.log(error);
                alert("Booking failed. Please try again later.");
            });
    }
</script>

<body>
    <div class="booking-container">
        <h2>Create new booking in <br>{accommodation.name}</h2>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Start Date:</label>
            <input
                class="form-control"
                type="date"
                bind:value={startDate}
                min={today}
            />
        </div>

        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>End Date:</label>
            <input
                class="form-control"
                type="date"
                bind:value={endDate}
                min={today}
            />
        </div>

        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Select your pet:</label>
            <select class="form-control" bind:value={selectedPet}>
                {#each pets as pet (pet.id)}
                    <option value={pet.id}>{pet.name}</option>
                {/each}
            </select>
        </div>

        <button class="btn btn-success" on:click={bookAccommodation}
            >Book Now</button
        >
    </div>
</body>

<style>
    .booking-container {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
        margin-top: 50px;
        background-color: #fff;
    }

    .form-group {
        margin-bottom: 15px;
    }

    .form-control {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 5px;
    }

    .btn {
        padding: 10px 20px;
        font-size: 16px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        margin-top: 20px;
        display: block;
        width: 100%;
    }

    .btn-success {
        background-color: #28a745;
    }

    .btn-success:hover {
        background-color: #218838;
    }
</style>
