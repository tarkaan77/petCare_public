<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { jwt_token } from "../../store";

    const api_root = window.location.origin;

    export let params = {};

    let id;

    $: {
        id = params.id;
    }

    function setupAsPetOwner() {
        setPersonType("PetOwner").then(() => {
            push("/profile/" + id + "/setup/pet-owner");
        });
    }

    function setupAsAnimalRightsActivist() {
        setPersonType("AnimalRightsActivist").then(() => {
            push("/profile/" + id + "/setup/animal-rights-activist");
        });
    }

    function setPersonType(type) {
        const config = {
            method: "put",
            url: api_root + "/api/person/" + id + "/setPersonType/" + type,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        return axios(config)
            .then(function (response) {
                console.log(response.data);
            })
            .catch(function (error) {
                console.error(error);
            });
    }
</script>

<body>
    <div class="setup-container">
        <h2 class="setup-title">Please finish the setup for your profile:</h2>
        <div class="setup-button-group">
            <button class="btn btn-success" on:click={setupAsPetOwner}
                >Setup as Pet Owner</button
            >
            <button
                class="btn btn-success"
                on:click={setupAsAnimalRightsActivist}
                >Setup as Animal Rights Activist</button
            >
        </div>
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
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 20px;
    }

    .setup-button-group {
        display: flex;
        gap: 20px;
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
</style>
