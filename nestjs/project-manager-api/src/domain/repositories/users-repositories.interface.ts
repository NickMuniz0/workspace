import { DeepPartial } from "typeorm";
import { IUser } from "../interfaces/user.interface";

export interface IUsersRepositories {
    findAll(userId: number): Promise<IUser[]>;
    findById(id: number): Promise<IUser| null>;
    add(payload: DeepPartial<IUser>): Promise<IUser>;
    updateById(id:number, payload: DeepPartial<IUser>);
    findByEmail(email: string): Promise<IUser | null>;
}