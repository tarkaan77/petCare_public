<script>
    import axios from "axios";
    import { jwt_token, user } from "../../store";

    const api_root = window.location.origin;

    export let params = {};
    let id;

    $: {
        id = params.id;
    }   

    let petOwner = {
        name: "",
        address: "",
        phoneNumber: "",
    };

    function handleInput(event) {
        const { name, value } = event.target;
        petOwner[name] = value;
    }

    function submitPetOwnerForm() {
        const config = {
            method: "put",
            url: api_root + "/api/person/" + id + "/update",
            data: petOwner,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                console.log(response.data);
                alert("Pet Owner profile created successfully! Please sign out and wait till the admin assigned you the role.");
            })
            .catch(function (error) {
                console.error(error);
            });
    }
</script>

<body>
    <div class="setup-container">
        <h1 class="setup-title">Setup as Pet Owner</h1>
        <form class="setup-form" on:submit|preventDefault={submitPetOwnerForm}>
            <label for="name">Name:</label>
            <input
                type="text"
                id="name"
                name="name"
                bind:value={petOwner.name}
            />

            <label for="address">Address:</label>
            <input
                type="text"
                id="address"
                name="address"
                bind:value={petOwner.address}
            />

            <label for="phoneNumber">Phone Number:</label>
            <input
                type="tel"
                id="phoneNumber"
                name="phoneNumber"
                bind:value={petOwner.phoneNumber}
            />

            <div class="note">
                Note: You will be able to add pets to your profile later on the
                "My Profile" page.
            </div>

            <button class="btn btn-success">Submit</button>
        </form>
    </div>
</body>

<style>
    .setup-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        background-color: #f8f9fa;
        font-family: Arial, sans-serif;
    }

    .setup-title {
        font-size: 40px;
        font-weight: bold;
        margin-bottom: 20px;
    }

    .btn {
        padding: 10px 20px;
        font-size: 16px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .btn-success {
        background-color: #28a745;
    }

    .btn-success:hover {
        background-color: #218838;
    }

    .setup-form {
        display: flex;
        flex-direction: column;
        gap: 10px;
        width: 300px;
    }

    .note {
        font-size: 14px;
        color: #6c757d;
        margin: 10px 0;
    }
</style>
