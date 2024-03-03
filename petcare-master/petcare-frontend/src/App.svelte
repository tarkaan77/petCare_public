<script>
	import Router from "svelte-spa-router";
	import routes from "./routes";
	import { isAuthenticated, user } from "./store";
	import auth from "./auth.service";
	import axios from "axios";
	import { jwt_token } from "./store";

	let userId = "";

	$: {
		if ($user && $user.email && $jwt_token) {
			fetchUserId($user.email).then((id) => {
				userId = id;
			});
		}
	}

	async function fetchUserId(email) {
		const config = {
			method: "get",
			url: `/api/person/email/${email}`,
			headers: {
				Authorization: "Bearer " + $jwt_token,
			},
		};

		try {
			const response = await axios(config);
			const person = response.data;
			return person.id;
		} catch (error) {
			console.error("Failed to fetch person by email", error);
		}
	}
</script>

<div id="app">
	<nav class="navbar navbar-expand-lg bg-light">
		<div class="container-fluid">
			<a class="navbar-brand" href="#/">
				<img src="/logo.png" alt="logo" height="50">
			</a>
			<button
				class="navbar-toggler"
				type="button"
				data-bs-toggle="collapse"
				data-bs-target="#navbarNav"
				aria-controls="navbarNav"
				aria-expanded="false"
				aria-label="Toggle navigation"
			>
				<span class="navbar-toggler-icon" />
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					{#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("admin")}
						<li class="nav-item">
							<a
								class="nav-link"
								aria-current="page"
								href="#/accommodations/create"
								>Add new accommodation</a
							>
						</li>
					{/if}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/accommodations">Accommodations</a
						>
					</li>
					{#if $isAuthenticated && !$user.user_roles.includes("admin")}
						<li class="nav-item">
							<a
								class="nav-link"
								aria-current="page"
								href="#/profile/{userId}">My Profile</a
							>
						</li>
					{/if}
					{#if $isAuthenticated && $user.user_roles.includes("admin")}
						<li class="nav-item">
							<a
								class="nav-link"
								aria-current="page"
								href="#/manage-all-bookings">Bookings</a
							>
						</li>
					{/if}
				</ul>
				<div class="d-flex">
					{#if $isAuthenticated}
						<span class="navbar-text me-2">
							{$user.name}
						</span>
						<button
							type="button"
							class="btn btn-primary"
							on:click={auth.logout}>Log Out</button
						>
					{:else}
						<button
							type="button"
							class="btn btn-primary"
							on:click={auth.loginWithPopup}>Log In</button
						>
					{/if}
				</div>
			</div>
		</div>
	</nav>

	<div class="container">
		<Router {routes} />
	</div>
</div>

<style>
	.btn-primary {
		background-color: brown;
		color: white;
		border-color: lightgreen;
	}

	.nav-item {
		margin-right: 15px;
		margin-left: 15px;
	}
</style>
