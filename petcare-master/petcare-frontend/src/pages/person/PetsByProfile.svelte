<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { jwt_token } from "../../store";

    const api_root = window.location.origin;

    export let params = {};
    let id;
    let petOwner = {
        id: null,
        name: null,
    };
    let pets = [];

    $: {
        id = params.id;
        getPetOwner();
        getPets();
    }

    function getPetOwner() {
        var config = {
            method: "get",
            url: api_root + "/api/person/" + id,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                petOwner = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function getPets() {
        var config = {
            method: "get",
            url: api_root + "/api/person/petowner/" + id + "/pets",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                pets = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function calculateAge(birthdate) {
        let birthYear = new Date(birthdate).getFullYear();
        let currentYear = new Date().getFullYear();
        return currentYear - birthYear;
    }

    function deletePet(petId) {
        var config = {
            method: "delete",
            url:
                api_root +
                "/api/person/petowner/" +
                petOwner.id +
                "/deletepet/" +
                petId,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                // rm deleted pet from the list
                pets = pets.filter((pet) => pet.id !== petId);
                alert("Pet deleted successfully")
            })
            .catch(function (error) {
                alert("Could not delete pet");
                console.log(error);
            });
    }
</script>

<body>
    <h2>Pets of {petOwner.name}</h2>
    <button class="btn btn-info" on:click={() => push("/profile/" + id)}
        >Back to profile</button
    >
    <div>
        {#each pets as pet (pet.id)}
            <div class="pet-box">
                <h3>{pet.name}</h3>
                <ul class="pet-details">
                    <li>Species: {pet.species}</li>
                    <li>Gender: {pet.gender}</li>
                    <li>Size: {pet.size}</li>
                    <li>Age: {calculateAge(pet.birthdate)} years</li>
                    <li>Description: {pet.description}</li>
                    <button
                        class="btn btn-danger"
                        on:click={() => deletePet(pet.id)}>Delete</button
                    >
                </ul>
            </div>
        {/each}
    </div>
</body>

<style>
    .pet-box {
        border: 1px solid #ccc;
        padding: 20px;
        margin-bottom: 20px;
        border-radius: 10px;
        background-color: #f9f9f9;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }
    .pet-details {
        font-size: 20px;
        font-weight: bold;
    }

    .btn {
        margin-bottom: 20px;
    }

    h3 {
        font-weight: bold;
        color: brown;
    }

    .btn-danger{
        margin-bottom: 10px;
        margin-top: 20px;
        font-size: 20px;
    }
</style>
