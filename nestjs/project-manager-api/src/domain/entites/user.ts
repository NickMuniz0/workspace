import { IUser } from "../interfaces/user.interface";
import { Project } from "./project";
import { Task } from "./task";

export class User implements IUser {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    projects: Project[];    
    tasks: Task[];
}



