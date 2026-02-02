import { IProject } from "./project.interface";
import { IUser } from "./user.interface";

export interface Itask {
    id: number;
    name: string;
    status: 'pending' | 'in-progress' | 'completed' | undefined;
    project: IProject;
    user: IUser;
}