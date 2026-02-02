import { DeepPartial } from "typeorm";
import { IProject } from "../interfaces/project.interface";

export interface IProjectsRepositories {
    findAll(userId: number): Promise<IProject[]>;
    findById(userId: number, id: number): Promise<IProject | null>;
    add(payload: DeepPartial<IProject>): Promise<IProject>;
}