<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { isAuthenticated, user, jwt_token } from "../../store";

    const api_root = window.location.origin;

    export let params = {};

    let ratingValue = "";
    let comment = "";
    let userId = params.id;

    let bookingId = params.bookingId



    function createRating() {
        var data = {
            accommodationId: params.accommodationId,
            ratingValue: ratingValue,
            comment: comment,
        };

        var config = {
            method: "post",
            url: api_root + "/api/service/rating",
            headers: {
                Authorization: "Bearer " + $jwt_token,
                "Content-Type": "application/json",
            },
            data: data,
        };

        axios(config)
            .then(function (response) {
                alert("Rating successfully created");
                // Set the booking status to DONE
                changeStatus(bookingId, "DONE");
            })
            .catch(function (error) {
                alert("Could not create rating");
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
                push(`/profile/${userId}`);
            })
            .catch(function (error) {
                alert("Could not update booking status");
                console.log(error);
            });
    }
</script>

<body>
    <div class="create-rating-container">
        <h2>Create a Rating</h2>
        <h4>Booking ID: {params.id}</h4>
        <h4 class="accommodationId">
            Accomodation ID: {params.accommodationId}
        </h4>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Rating Value:</label>
            <input
                class="form-control"
                type="number"
                bind:value={ratingValue}
                min="1"
                max="5"
                step="1"
            />
        </div>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Comment:</label>
            <textarea class="form-control" bind:value={comment} />
        </div>
        <button class="btn btn-success" on:click={createRating}>Submit</button>
    </div>
</body>

<style>
    .create-rating-container {
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

    .accommodationId {
        padding-bottom: 20px;
    }
</style>
