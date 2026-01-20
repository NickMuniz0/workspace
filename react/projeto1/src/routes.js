import { Home } from "./views/Home";
import { Movies } from "./views/movies/Movies";
import { createBrowserRouter } from 'react-router-dom';

import { RootLayout } from "./views/RootLayout";
import { MovieDetail } from "./views/movieDetail/MovieDetail";
export const routes = createBrowserRouter([
    {
        path:"/",
        element: <RootLayout/>,
        children: [
            {
                path:"/",
                element: <Home/>
            },
            {
                path:"/movies",
                element: <Movies/>
            },
            {
                path:"/movies/:id",
                element: <MovieDetail/>
            }
        ]
    }

   
])