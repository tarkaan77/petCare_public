<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { jwt_token, isAuthenticated, user } from "../../store";

    const api_root = window.location.origin;

    let accommodation = {
        name: "",
        address: "",
        email: "",
        phoneNumber: "",
        iban: "",
        capacity: "",
        price: "",
        imageUrls: "",
    };

    function createAccommodation() {
        // comma seprated urls for multiple images
        accommodation.imageUrls = accommodation.imageUrls
            .split(",")
            .map((url) => url.trim());

        var config = {
            method: "post",
            url: api_root + "/api/accommodation",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + $jwt_token,
            },
            data: accommodation,
        };

        axios(config)
            .then(function (response) {
                alert("Accommodation created");
                push("/accommodations");
            })
            .catch(function (error) {
                console.log(error);
                alert("Could not create new accommodation");
            });
    }
</script>

<body>
    {#if $isAuthenticated && $user.user_roles.includes("admin")}
        <div class="create-accommodation-container">
            <h2>Create a new accommodation</h2>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>Name:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.name}
                    type="text"
                    placeholder="Enter name"
                />
            </div>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>Address:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.address}
                    type="text"
                    placeholder="Enter address"
                />
            </div>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>Email:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.email}
                    type="email"
                    placeholder="Enter email"
                />
            </div>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>Phone Number:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.phoneNumber}
                    type="text"
                    placeholder="Enter phone number"
                />
            </div>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>IBAN:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.iban}
                    type="text"
                    placeholder="Enter IBAN"
                />
            </div>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>Capacity:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.capacity}
                    type="number"
                    placeholder="Enter capacity"
                />
            </div>
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <div class="form-group">
                <label>Price per day:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.price}
                    type="number"
                    step="5"
                    placeholder="Enter price"
                />
            </div>
            <div class="form-group">
                <!-- svelte-ignore a11y-label-has-associated-control -->
                <label>Image URLs:</label>
                <input
                    class="form-control"
                    bind:value={accommodation.imageUrls}
                    type="text"
                    placeholder="Enter image URLs, separated by commas"
                />
                <small class="form-text text-muted"
                    >Enter image URLs, separated by commas.</small
                >
            </div>
            <button class="btn btn-success" on:click={createAccommodation}
                >Create Accommodation</button
            >
        </div>
    {/if}
</body>

<style>
    .create-accommodation-container {
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
