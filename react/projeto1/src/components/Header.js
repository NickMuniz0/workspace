
import { Link } from 'react-router-dom';
import style from './Header.module.css';
export function Header(){
    return (
    <>
     <header className={style.siteHeader}>
        <h1 className={style.logo}>Aplicação de Filmes</h1>
        <nav className={style.mainNav} aria-label="Main navigation">
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/movies">Movies</Link></li>
            </ul>
        </nav>
    </header>
    </>
    )
}