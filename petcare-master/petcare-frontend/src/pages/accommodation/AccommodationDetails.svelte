<script>
  import axios from "axios";
  import { get } from "svelte/store";
  import Map from "../Map.svelte";
  import { jwt_token, isAuthenticated, user } from "../../store";
  import { push } from "svelte-spa-router";

  const api_root = window.location.origin;

  export let params = {};
  let id;

  $: {
    id = params.id;
    getAccommodation();
    getCountOfRatings();
  }

  let accommodation = {
    id: null,
    name: null,
    address: null,
    email: null,
    phoneNumber: null,
    iban: null,
    capacity: null,
    price: null,
    rating: null,
    imageUrls: null,
  };

  let ratingCount = 0;
  let activeImageIndex = 0;

  function getAccommodation() {
    var config = {
      method: "get",
      url: api_root + "/api/accommodation/" + id,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        accommodation = response.data;
      })
      .catch(function (error) {
        alert(
          "Could not get accommodation. Please sign in to see the details. If you are already logged in the accommodation is not available anymore."
        );
        push("/home");
        console.log(error);
      });
  }

  function getCountOfRatings() {
    var config = {
      method: "get",
      url: api_root + "/api/accommodation/" + id + "/ratings/count",
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        ratingCount = response.data;
      })
      .catch(function (error) {
        alert("Could not get ratings. Please sign in to see the details. If you are already logged in the accommodation is not available anymore.");
        console.log(error);
      });
  }

  function nextImage() {
    if (
      accommodation &&
      accommodation.imageUrls &&
      activeImageIndex < accommodation.imageUrls.length - 1
    ) {
      activeImageIndex++;
    } else {
      activeImageIndex = 0; // back to first image
    }
  }

  function prevImage() {
    if (activeImageIndex > 0) {
      activeImageIndex--;
    } else if (accommodation && accommodation.imageUrls) {
      activeImageIndex = accommodation.imageUrls.length - 1; // change to last image when at beginning
    }
  }

  function deleteAccommodation() {
    var config = {
      method: "delete",
      url: api_root + "/api/accommodation/" + id,
      headers: { Authorization: "Bearer " + $jwt_token },
    };

    axios(config)
      .then(function (response) {
        alert("Accommodation successfully deleted.");
        push("/accommodations");
      })
      .catch(function (error) {
        alert("Could not delete accommodation. Please try again.");
        console.log(error);
      });
  }
</script>

<body>
  <div class="container">
    <div class="details">
      <div class="box">
        <h2>{accommodation.name}</h2>
        <h4>
          ⭐ {accommodation.rating} (<a
            href={`/#/accommodations/${accommodation.id}/ratings`}
            >{ratingCount} ratings</a
          >)
        </h4>
      </div>

      <div class="box">
        <h3>Contact Information</h3>
        <h4>📍 {accommodation.address}</h4>
        <h4>📧 {accommodation.email}</h4>
        <h4>📞 {accommodation.phoneNumber}</h4>
      </div>

      <div class="box map">
        <h3>Find us on the map</h3>
        <Map address={accommodation.address} />
      </div>
    </div>

    <div class="images">
      {#if accommodation && accommodation.imageUrls}
        <div class="image-container">
          <button class="arrow arrow-left" on:click={prevImage}>‹</button>
          <!-- svelte-ignore a11y-img-redundant-alt -->
          <img
            id="accImage"
            src={accommodation.imageUrls[activeImageIndex]}
            alt="missing picture"
          />
          <button class="arrow arrow-right" on:click={nextImage}>›</button>
        </div>
      {/if}
      {#if $isAuthenticated && $user.user_roles.includes("admin")}
        <button class="btn btn-danger" on:click={deleteAccommodation}
          >Delete Accommodation</button
        >
      {:else if $isAuthenticated && $user.user_roles.includes("petOwner")}
        <button class="btn btn-success">
          <a href={`/#/accommodations/${accommodation.id}/booking`}>Book Now</a>
        </button>
      {/if}
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
  }

  .map {
    padding-top: 30px;
  }

  .images {
    width: 45%;
    position: relative;
    height: 360px;
  }

  #accImage {
    width: 100%;
    height: 100%;
  }

  .image-container {
    position: relative;
    height: 100%;
  }

  .arrow {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    border: none;
    font-size: 2em;
    padding: 5px 10px;
    cursor: pointer;
  }

  .arrow-left {
    left: 10px;
  }

  .arrow-right {
    right: 10px;
  }

  .btn {
    padding: 10px 20px;
    font-size: 16px;
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

  .btn-success a {
    color: white;
    text-decoration: none;
  }
</style>
