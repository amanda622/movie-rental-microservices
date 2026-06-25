<template>
  <div>
    <!-- Header Section -->
    <header class="bg-dark text-light py-3">
      <div class="container">
        <h1 class="display-4">Movie Rentals</h1>
        <p class="lead">Rent the latest movies for an amazing entertainment experience!</p>
      </div>
    </header>

    <!-- Movie Categories Section -->
    <section class="py-5">
      <div class="container">
        <h2 class="h4 mb-4">Browse by Category</h2>
        <div class="row">
         
          
          <!-- Add more category cards as needed -->
        </div>
      </div>
    </section>

    <!-- Featured Movies Section -->
    <section class="bg-light py-5">
      <div class="container">
        <h2 class="h4 mb-4">Featured Movies</h2>
        <div class="row">
          <!-- Add your featured movie cards here -->
          <MovieList></MovieList>
          <RentalComponent></RentalComponent>
          
          <!-- Add more movie cards as needed -->
        </div>
      </div>
    </section>

    <!-- Footer Section -->
    <footer class="bg-dark text-light py-3 mt-5">
      <div class="container">
        <p class="mb-0 text-center">&copy; 2023 Movie Rentals. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script>
// eslint-disable-next-line
import GenreComponent from './components/GenreComponent.vue';
import MovieList from './components/MovieList.vue';
import RentalComponent from "@/components/RentalComponent.vue";

export default {
  name: 'App',
  components: {
    // eslint-disable-next-line
    GenreComponent,
    MovieList,
    RentalComponent
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
