<template>
  <div>
    <h2 class="h4 mb-4">Movie Genres</h2>
    <div class="row">
      <div v-for="genre in genres" :key="genre.id" class="col-md-4 mb-4">
        <div class="card">
          <!-- You can add images or icons for each genre if available -->
          <div class="card-body">
            <h5 class="card-title">{{ genre.toUpperCase() }}</h5>
            <p class="card-text">{{ genre.description }}</p>
            <button @click="fetchMoviesByGenre(genre)"> Select</button>

          </div>
        </div>
      </div>
    </div>
  </div>
  <div>
    <div class="row">
      <div v-for="movie in movies" :key="movie.id" class="col-md-4 mb-4">
        <div class="card">
          <!-- You can add images or icons for each genre if available -->
          <div class="card-body">
            <h5 class="card-title">{{ movie.movieTitle }}</h5>
            <p class="card-text">{{ movie.description }}</p>


            <button @click="rentMovie(movie)"> Rent movie</button>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>

export default {
  name: 'MovieList',
  data() {
    return {
      movies: [],
      genres: [],
    };
  },
  mounted() {
    // Fetch genres from the API when the component is mounted
    this.fetchMovies();
    this.fetchGenres();
  },
  methods: {
    async rentedMovies() {
      const response = await fetch("http://localhost:8082/rental/getAllRentals")
      return await response.json()
    },
    getMovieIdsFromMovieArray(movies) {
      var movieIds = []
      movies.forEach((element) => {   // Picks out the movie id from the movie and adds the id to an id array
        movieIds.push(element.movieId)
      })

      return movieIds
    },

    async removeRentedMoviesFromList(rawMovies) {
      console.log("Removing rented movies from list")
      let rentedMovieIds = await this.getMovieIdsFromMovieArray(await this.rentedMovies()) // Gets the ids of the rented movies
      let availableMovies = []

      for (let i = 0; i < rawMovies.length; i++) {

        // Rawmovies is all the movies for a given selection (including already rented movies)
        // Removes the movies that are already rented by checking if the id exists in the rentedMovieIds
        if (!((rentedMovieIds).includes(rawMovies[i].id))) {
          availableMovies.push(rawMovies[i])
        }
      }
      this.movies = availableMovies
    },


    async fetchMovies() {
      console.log("Fetching all movies")
      // Make an API call to get the list of movies
      const response = await fetch("http://localhost:8081/movies/get-all-movies")

      let rawMovies = await response.json()

      this.removeRentedMoviesFromList(rawMovies)  // We need to remove the already rented movies, otherwise a customer could hire the same movie more than once
    },

    async fetchMoviesByGenre(genre) {
      console.log("Fetching movies in genre: " + genre)
      const response = await fetch("http://localhost:8081/movies/get-movies-by-genre/" + genre)

      let rawMovies = await response.json()
      this.removeRentedMoviesFromList(rawMovies)  // See above
    },

    async fetchGenres() {
      console.log("Fetching genres")
      // Make an API call to get the list of genres
      const response = await fetch("http://localhost:8081/movies/get-genres")
      this.genres = await response.json()
    },

    async rentMovie(movie) {

      let customerId = prompt("Please enter your customer id!");
      console.log(customerId)
      const onlyContainsNumbers = (str) => /^\d+$/.test(str);
      if ((onlyContainsNumbers(customerId))) {
        const postData = {
          customerId: customerId,
          movieId: movie.id,
          rentalCost: 75,
          rentalDate: currentDate
        };
        var currentDate = new Date();

        console.log(this.customerid + " want to hire " + movie.id)
        // eslint-disable-next-line
        const requestOptions = {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify(postData)
        };
        const response = await fetch("http://localhost:8082/rental/create-rental", requestOptions)
        const data = await response.json()
        this.postId = data.id

        await this.fetchMovies();
        alert("You have rented :" + movie.movieTitle)
      } else{
        alert("Invalid input. Use number to enter customerId")
      }
    }
  },
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
