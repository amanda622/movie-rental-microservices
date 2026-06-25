<template>
  <div>
    <h2 class="h4 mb-4">Movie Genres</h2>
    <div class="row">
      <div v-for="genre in genres" :key="genre.id" class="col-md-4 mb-4">
        <div class="card">
          <!-- You can add images or icons for each genre if available -->
          <div class="card-body">
            <h5 class="card-title">{{ genre }}</h5>
            <p class="card-text">{{ genre.description }}</p>
            <router-link :to="{ name: 'moviesByGenre', params: { genreId: genre.id }}">View Movies</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'GenreList',
  data() {
    return {
      genres: [],
    };
  },
  mounted() {
    // Fetch genres from the API when the component is mounted
    this.fetchGenres();
  },
  methods: {
    async fetchGenres() {
      // Make an API call to get the list of genres
      const response = await fetch("http://localhost:8081/movies/get-genres")
      console.log(response)
      this.genres = await response.json()
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
