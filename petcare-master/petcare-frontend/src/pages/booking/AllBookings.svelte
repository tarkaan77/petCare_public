<script>
    import axios from "axios";
    import { push } from "svelte-spa-router";
    import { querystring } from "svelte-spa-router";
    import { jwt_token } from "../../store";

    const api_root = window.location.origin;

    let currentPage;
    let nrOfPages = 0;
    let defaultPageSize = 4;

    let status;

    let bookings = [];

    $: {
        let searchParams = new URLSearchParams($querystring);
        if (searchParams.has("page")) {
            currentPage = searchParams.get("page");
        } else {
            currentPage = "1";
        }
        getBookings();
    }

    function getBookings() {
        let query =
            "?pageSize=" + defaultPageSize + "&pageNumber=" + currentPage;

        if (status) {
            query += "&status=" + status;
        }

        var config = {
            method: "get",
            url: api_root + "/api/service/bookings" + query,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                bookings = response.data.content;

                nrOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                alert("Could not get bookings");
                console.log(error);
            });
    }

    function cancelBooking(bookingId) {
        var config = {
            method: "delete",
            url: api_root + "/api/service/booking/" + bookingId,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                alert("Booking successfully cancelled by admin");
                // refresh bookings after delete
                getBookings();
            })
            .catch(function (error) {
                alert("Could not cancel booking");
                console.log(error);
            });
    }

    function formatDate(date) {
        return new Date(date).toLocaleDateString("de-DE");
    }
</script>

<body>
    <h1>All Bookings</h1>

    <div class="filtering">
        <div class="row my-3">
            <div class="col-auto">
                <label for="" class="col-form-label">Status: </label>
            </div>
            <div class="col-3">
                <select
                    bind:value={status}
                    class="form-select"
                    id="typefilter"
                    type="text"
                >
                    <option value="ALL" />
                    <option value="PENDING">PENDING</option>
                    <option value="IN_PROGRESS">IN PROGRESS</option>
                    <option value="AWAITING_FOR_FEEDBACK"
                        >AWAITING FOR FEEDBACK</option
                    >
                    <option value="DONE">DONE</option>
                </select>
            </div>
            <div class="col-3">
                <a
                    class="btn btn-info-filter"
                    href={"#/manage-all-bookings?page=1&status=" + status}
                    role="button">Apply</a
                >
            </div>
        </div>
    </div>

    {#each bookings as booking}
        <div class="booking">
            <div class="booking-details">
                <div class="booking-id"><b>Booking ID:</b> {booking.id}</div>
                <div class="booking-accommodation-id">
                    <b>Accommodation ID:</b>
                    {booking.accommodationId}
                </div>
                <div class="booking-person-id">
                    <b>Person ID:</b>
                    {booking.personId}
                </div>
                <div class="booking-pet-id"><b>Pet ID:</b> {booking.petId}</div>
                <div class="booking-pet-name">
                    <b>Pet Name:</b>
                    {booking.petName}
                </div>
                <div class="booking-start-date">
                    <b>Start Date:</b>
                    {formatDate(booking.startDate)}
                </div>
                <div class="booking-end-date">
                    <b>End Date:</b>
                    {formatDate(booking.endDate)}
                </div>
                <div class="booking-status">
                    <b>Booking Status:</b>
                    {booking.status}
                </div>
            </div>
            <button
                class="btn btn-danger"
                on:click={() => cancelBooking(booking.id)}
            >
                Cancel Booking
            </button>
        </div>
    {/each}

    <div class="pagination">
        <nav class="pagination-nav">
            {#each Array(nrOfPages) as _, i}
                <li class="page-item">
                    <a
                        class="page-link"
                        class:active={currentPage == i + 1}
                        href={"#/manage-all-bookings?page=" + (i + 1)}
                        >{i + 1}
                    </a>
                </li>
            {/each}
        </nav>
    </div>
</body>

<style>
    .booking {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.4);
        border-radius: 10px;
    }

    .booking-details {
        flex-grow: 1;
        margin-left: 20px;
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

    h1 {
        margin-bottom: 20px;
    }

    .filtering {
        margin-bottom: 20px;
    }

    .btn-info-filter {
        background-color: brown;
        color: white;
    }
</style>
