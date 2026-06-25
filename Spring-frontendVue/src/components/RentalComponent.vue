<template>
  <div>
    <h2>All rentals</h2>
    <button @click="showAllRentals">Show All Rentals</button>
    <ul>
      <li v-for="rental in rentals" :key="rental.id">
        {{ rental.movieTitle }} - {{ rental.customerId }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rentals: [],
    };
  },
  mounted() {
    // this.fetchAllRentals(); // Commented out to fetch rentals on button click otherwise list of rentals would be visible all the time
  },
  methods: {
    async showAllRentals() {
      await this.fetchAllRentals();
    },
    async fetchAllRentals() {
      try {
        const response = await fetch("http://localhost:8082/rental/getAllRentals");
        if (response.ok) {
          const rentals = await response.json();

          // Fetch movie titles for each rental and include customerId in rental object
          const fetchTitlePromises = rentals.map(async (rental) => {
            rental.movieTitle = await this.getMovieTitle(rental.movieId);
          });

          await Promise.all(fetchTitlePromises);

          this.rentals = rentals;
        } else {
          console.error("Failed to fetch rentals:", response.statusText);
        }
      } catch (error) {
        console.error("Error fetching rentals:", error);
      }
    },

    async getMovieTitle(id) {
      try {
        const response = await fetch("http://localhost:8081/movies/get-movie-by-id/" + id);
        if (response.ok) {
          const movie = await response.json();
          return movie.movieTitle;
        } else {
          console.error("Failed to fetch movie title:", response.statusText);
          return ""; // Return a default value or handle the error as needed
        }
      } catch (error) {
        console.error("Error fetching movie title:", error);
        return ""; // Return a default value or handle the error as needed
      }
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here if needed */
</style>