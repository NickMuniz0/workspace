
import { Link } from 'react-router-dom';
export function Header(){
    return (
    <>
        <h1>Aplicacao de Filmes</h1>
        <ul><Link to="/">Home</Link></ul>
        <ul><Link to="/movies">Movies</Link></ul>

    </>
    )
}