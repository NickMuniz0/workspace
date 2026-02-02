import { IProject } from "../interfaces/project.interface";
import { Task } from "./task";
import { User } from "./user";

export class Project implements IProject {
    id: number;
    name: string;
    description: string;
    user: User;
    tasks: Task[];
}