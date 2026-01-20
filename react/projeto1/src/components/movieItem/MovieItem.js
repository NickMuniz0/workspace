import styles from "./MovieItem.module.css"
import { Link } from "react-router-dom"
export function MovieItem({ movie }){
    return(
        <div className={styles.movie}>
            {movie.poster_path &&
            (<img src={`https://image.tmdb.org/t/p/w200/${movie.poster_path}`} alt=""></img>)
            }
            <h3>{movie.title}</h3> 
            <p><Link className={styles.detalhesBtn} to={`/movies/${movie.id}`}>Ver Detalhes</Link></p>

            

        </div>
    )
}