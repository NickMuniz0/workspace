import { Itask } from "../interfaces/task.interface";
import { Project } from "./project";
import { User } from "./user";

export class Task implements Itask {
    id: number;
    name: string;
    status: "pending" | "in-progress" | "completed" | undefined;
    project: Project;
    user: User;

}