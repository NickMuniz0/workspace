import axios from "axios"

export const moviesApi = axios.create({
    baseURL: "https://api.themoviedb.org/3/",
    headers:{
        Authorization:'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5ZmY4ZDkzN2Q1YjczZDQyNDM4OGViMTI4YjkwODRjZCIsIm5iZiI6MTc2MjMwMjMxNi4xNTUsInN1YiI6IjY5MGE5OTZjZDQwY2U2NDA2Yjc4YTBhZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.8-wT-xX_oJq17qtiuEJBIlWs_G3gNPnqEJRnuJ7N_M0',
        accept:'application/json'
    
    }

})