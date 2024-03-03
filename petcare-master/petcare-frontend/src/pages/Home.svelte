<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";

    const api_root = window.location.origin;

    let topAccommodations = [];

    $: {
        getTopAccommodations();
    }

    function getTopAccommodations() {
        var config = {
            method: "get",
            url: api_root + "/api/accommodation/top",
        };

        axios(config)
            .then(function (response) {
                topAccommodations = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }
</script>

<body>
    <header>
        <h1>Welcome to PetCare! 🐾</h1>
        
        <h2>Best rated Accommodations for your pet</h2>
        <div id="top-accommodations">
            {#each topAccommodations as acc}
                <div class="accommodation">
                    {#if acc.imageUrls && acc.imageUrls[0]}
                        <!-- svelte-ignore a11y-img-redundant-alt -->
                        <img src={acc.imageUrls[0]} alt="Accommodation image" />
                    {:else}
                        <div class="accommodation-image">No Image</div>
                    {/if}
                    <h4>{acc.name}</h4>
                    <p>⭐ {acc.rating}</p>
                    <p>{acc.address}</p>
                    <button class="btn btn-info" on:click={() => push(`/accommodations/${acc.id}`)}>Details</button>
                </div>
            {/each}
        </div>
        <button class = "btn btn-success" on:click={() => push("/accommodations")}>Browse all our accommodations</button>
    </header>
    <div>
        <h2>Become an Accommodator</h2>
        <p>
            If you want to become an Accommodator, please send an email to
            <b>testadmin@outlook.com</b> and provide your contact details. We will
            get in touch with you shortly.
        </p>
    </div>
    <div>
        <h2>About PetCare</h2>
        <p>
            PetCare is a platform that connects pet owners with pet services
            such as accommodations and pet lovers. It was created by an idea
            from a student during an semester project at the ZHAW University.
            The idea was born while the student's girlfriend was looking for a
            place to stay for her dog while she was on vacation. Then he
            realized that there is no platform that connects pet owners with pet
            services and it was not the first time that he heard about this
            problem. Our goal is toprovide a reliable solution for pet care and
            pet hotels. Every accommodation is checked by us before it is
            published on our platform. Pet animal activists can also create an
            account and check the accommodations spontaneously. If an
            accommodation does not meet our requirements, it will be removed
            from our platform.
        </p>
    </div>
</body>

<style>
    #top-accommodations {
        display: flex;
        justify-content: space-between;
        margin: 20px 0;
    }

    .accommodation {
        border: 1px solid brown;
        padding: 10px;
        flex-basis: calc(33% - 20px);
        box-sizing: border-box;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.4);
        border-radius: 10px;
        background-color: lightgreen;
        display: flex;
        flex-direction: column;
    }

    .accommodation img {
        width: 100%;
        height: 200px;
        object-fit: cover;
    }

    .accommodation-image {
        width: 100%;
        height: 200px;
        display: flex;
        justify-content: center;
        align-items: center;
        font-weight: bold;
        color: #aaa;
        background-color: #eee;
        border-radius: 5px;
    }

    .btn-info {
        color: #fff;
        background-color: brown;
        border-color: brown;
        margin-top: auto;
    }

    .btn-success {
        width: 100%;
        margin: 20px 0;
        display: block;
    }


</style>