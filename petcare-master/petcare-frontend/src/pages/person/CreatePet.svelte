<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { jwt_token} from "../../store";

    const api_root = window.location.origin;

    export let params = {};
    let id;
    let pet = {
        name: "",
        species: "",
        gender: "",
        size: "",
        description: "",
        birthdate: "",
    };

    let genders = ["MALE", "FEMALE"];
    let sizes = ["SMALL", "MEDIUM", "LARGE"];
    let species = ["CAT", "DOG", "RABBIT", "FISH", "BIRD", "REPTILE", "OTHER"];

    $: {
        id = params.id;
    }

    function createPet() {
        var config = {
            method: "post",
            url: api_root + "/api/pet",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + $jwt_token
            },
            data: pet,
        };

        axios(config)
            .then(function (response) {
                assignPetToOwner(response.data.id);
                alert("Pet created");
            })
            .catch(function (error) {
                console.log(error);
                alert("Could not create new pet");
            });
    }

    function assignPetToOwner(petId) {
        var config = {
            method: "put",
            url:
                api_root + "/api/person/petowner/" + id + "/assignpet/" + petId,
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + $jwt_token
            },
        };

        axios(config)
            .then(function (response) {
                push("/profile/" + id + "/pets");
            })
            .catch(function (error) {
                console.log(error);
            });
    }
</script>

<body>
    <div class="create-pet-container">
        <h2>Create a new pet</h2>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Name:</label>
            <input class="form-control" bind:value={pet.name} type="text" placeholder="Pet name" />
        </div>
            <!-- svelte-ignore a11y-label-has-associated-control -->
        <div class="form-group">
            <label>Species:</label>
            <select class="form-control" bind:value={pet.species}>
                <option disabled selected value=""> -- select an option -- </option>
                {#each species as specie}
                    <option>{specie}</option>
                {/each}
            </select>
        </div>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Gender:</label>
            <select class="form-control" bind:value={pet.gender}>
                <option disabled selected value=""> -- select an option -- </option>
                {#each genders as gender}
                    <option>{gender}</option>
                {/each}
            </select>
        </div>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Size:</label>
            <select class="form-control" bind:value={pet.size}>
                <option disabled selected value=""> -- select an option -- </option>
                {#each sizes as size}
                    <option>{size}</option>
                {/each}
            </select>
        </div>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Birthdate:</label>
            <input
                class="form-control"
                bind:value={pet.birthdate}
                type="date"
                placeholder="YYYY-MM-DD"
            />
        </div>
        <div class="form-group">
            <!-- svelte-ignore a11y-label-has-associated-control -->
            <label>Description:</label>
            <textarea class="form-control" bind:value={pet.description} placeholder="Pet description" />
        </div>
        <button class="btn btn-success" on:click={createPet}>Create Pet</button>
    </div>
</body>

<style>
    .create-pet-container {
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
        background-color: #007BFF;
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