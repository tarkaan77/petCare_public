<script>
  import axios from "axios";
  import { jwt_token} from "../../store";
  
  const api_root = window.location.origin;

  export let params = {};
  let id;
    let accommodation = {
        id: null,
        name: null
    };
  let ratings = [];

  $: {
    id = params.id;
    getAccommodation();
    getRatings();
  }

  function getAccommodation() {
    var config = {
      method: "get",
      url: api_root + "/api/accommodation/" + id,
      headers: {Authorization: "Bearer " + $jwt_token},
    };

    axios(config)
      .then(function (response) {
        accommodation = response.data;
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  function getRatings() {
    var config = {
      method: "get",
      url: api_root + "/api/accommodation/" + id + "/ratings",
      headers: {Authorization: "Bearer " + $jwt_token},
    };

    axios(config)
      .then(function (response) {
        ratings = response.data;
      })
      .catch(function (error) {
        console.log(error);
      });
  }
</script>

<body>
    <h2 id="name">Ratings from {accommodation.name} </h2>
    <h2 id="idFromAcc">(ID: {id})</h2>
    <div>
        {#each ratings as rating (rating.id)}
            <div class="rating-box">
                <div class="rating-value">{rating.ratingValue.toFixed(1)}</div>
                <div>{rating.comment}</div>
            </div>
        {/each}
    </div>
</body>

<style>
    .rating-box {
        border: 1px solid #ccc;
        padding: 20px;
        margin-bottom: 20px;
        border-radius: 10px;
        background-color: #f9f9f9;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }
    .rating-value {
        font-size: 20px;
        font-weight: bold;
    }

    #name {
        margin-bottom: irem;
    }

    #idFromAcc {
        font-size: 20px;
        margin-bottom: 20px;
    }
    
</style>
