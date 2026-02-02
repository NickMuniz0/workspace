import { DeepPartial } from "typeorm";
import { Itask } from "../interfaces/task.interface";

export interface ITaskRepositories {
    findAll(userId: number): Promise<Itask[]>;
    findById(userId:number, id: number): Promise<Itask | null>;
    add(payload: DeepPartial<Itask>): Promise<Itask>;
    updateById(id:number, payload: DeepPartial<Itask>);
}