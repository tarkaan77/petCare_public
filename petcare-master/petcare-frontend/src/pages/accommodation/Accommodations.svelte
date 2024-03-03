<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { querystring } from "svelte-spa-router";
    //import { jwt_token} from "../../store";

    const api_root = window.location.origin;

    let currentPage;
    let nrOfPages = 0;
    let defaultPageSize = 4;

    let maxPrice;
    let minCapacity;

    let accommodations = [];

    $: {
        let searchParams = new URLSearchParams($querystring);
        if (searchParams.has("page")) {
            currentPage = searchParams.get("page");
        } else {
            currentPage = "1";
        }
        getAccommodations();
    }

    function getAccommodations() {
        let query =
            "?pageSize=" + defaultPageSize + " &pageNumber=" + currentPage;

        if (maxPrice) {
            query += "&price=" + maxPrice;
        }

        if (minCapacity) {
            query += "&capacity=" + minCapacity;
        }

        var config = {
            method: "get",
            url: api_root + "/api/accommodation" + query,
            headers: {},
        };

        axios(config)
            .then(function (response) {
                accommodations = response.data.content;

                nrOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                alert("Could not get accommodations");
                console.log(error);
            });
    }
</script>

<body>
    <h1>All Accommodations</h1>

    <div class="filtering">
        <div class="row my-3">
            <div class="col-auto">
                <label for="" class="col-form-label">Max. Price: </label>
            </div>
            <div class="col-3">
                <input
                    class="form-control"
                    type="number"
                    placeholder="max"
                    bind:value={maxPrice}
                    step="5"
                />
            </div>
            <div class="col-auto">
                <label for="" class="col-form-label">Min. Capacity: </label>
            </div>
            <div class="col-3">
                <input
                    class="form-control"
                    type="number"
                    placeholder="min"
                    bind:value={minCapacity}
                />
            </div>
            <div class="col-3">
                <a
                    class="btn btn-info-filter"
                    href={"#/accommodations?page=1&price=" +
                        maxPrice +
                        "&capacity=" +
                        minCapacity}
                    role="button">Apply</a
                >
            </div>
        </div>
    </div>

    {#each accommodations as acc}
        <div class="accommodation">
            {#if acc.imageUrls && acc.imageUrls[0]}
                <div
                    class="accommodation-image"
                    style="background-image: url('{acc.imageUrls[0]}');"
                />
            {:else}
                <div class="accommodation-image">No Image</div>
            {/if}
            <div class="accommodation-details">
                <div class="accommodation-name">{acc.name}</div>
                <div class="accommodation-address">{acc.address}</div>
                <div class="accommodation-capacity">
                    Capacity: {acc.capacity}
                </div>
                <div class="accommodation-price">
                    Price per day: {acc.price} CHF
                </div>
            </div>
            <button
                class="btn btn-info"
                on:click={() => push(`/accommodations/${acc.id}`)}
                >Details</button
            >
        </div>
    {/each}

    <div class="pagination">
        <nav class="pagination-nav">
            {#each Array(nrOfPages) as _, i}
                <li class="page-item">
                    <a
                        class="page-link"
                        class:active={currentPage == i + 1}
                        href={"#/accommodations?page=" + (i + 1)}
                        >{i + 1}
                    </a>
                </li>
            {/each}
        </nav>
    </div>
</body>

<style>
    .accommodation {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.4);
        border-radius: 10px;
    }

    .accommodation-image {
        flex: 0 0 150px;
        height: 150px;
        background-size: cover;
        background-position: center center;
        border-radius: 5px;
    }

    .accommodation-details {
        flex-grow: 1;
        margin-left: 20px;
    }

    .accommodation-name {
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 10px;
    }

    .accommodation-address,
    .accommodation-capacity,
    .accommodation-price {
        font-size: 16px;
        margin-bottom: 10px;
    }

    .pagination {
        bottom: 0;
        left: 0;
        right: 0;
        display: flex;
        justify-content: center;
        padding-bottom: 15px;
    }

    .pagination-nav {
        display: flex;
        list-style-type: none;
        padding: 0;
        margin: 0;
    }

    .page-link.active {
        color: white;
        background-color: brown;
    }

    .btn-info-filter {
        background-color: brown;
        color: white;
    }

    .filtering {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 20px;
    }

    .row {
        flex-wrap: wrap;
        width: 100%;
    }

    .btn-info {
        color: #fff;
        background-color: brown;
        border-color: brown;
    }
</style>
